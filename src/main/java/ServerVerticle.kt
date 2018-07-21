
import handlers.MainPacketHandler
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.experimental.launch
import model.Constants
import model.ErrorCode
import model.Response

class ServerVerticle(
	private val mainPacketHandler: MainPacketHandler
) : CoroutineVerticle() {
	private val HEADER_SIZE = 8
	private val RECEIVER_ID_SIZE = 8

	override suspend fun start() {
		val server = vertx.createNetServer()

		server.connectHandler { socket ->
			socket.handler { buffer ->
				launch { handleData(buffer, socket) }
			}.closeHandler {
				//TODO disconnect player
			}.exceptionHandler {
				//TODO disconnect player
			}
		}.exceptionHandler { error ->
			error.printStackTrace()
		}.listen(14887)
	}

	private suspend fun handleData(buffer: Buffer, socket: NetSocket) {
		println("new request from ${socket.remoteAddress().host()}")

		val response = try {
			println("RECEIVING <<< ${byteArrayToHex(buffer.bytes)}")
			mainPacketHandler.handle(socket, buffer)
		} catch (error: Throwable) {
			error.printStackTrace()
			ErrorCode.UnknownError
		}

		when (response) {
			is Response.Ok -> {
				println("SENDING >>> ${byteArrayToHex(response.data.bytes)}")
				sendResponse(response, socket)
			}

			is Response.Error -> {
				println("An error has occurred: ${response.error.value}")
				socket.close()
			}
		}
	}

	private fun sendResponse(response: Response.Ok, socket: NetSocket) {
		val dataLen = response.data.length()
		val outBuffer = Buffer.buffer(dataLen + HEADER_SIZE)

		outBuffer.appendIntLE(Constants.MAGIC_NUMBER)
		outBuffer.appendIntLE(dataLen + RECEIVER_ID_SIZE)
		outBuffer.appendLongLE(response.receiverId)
		outBuffer.appendBuffer(response.data)

		socket.write(outBuffer)
	}

	fun byteArrayToHex(a: ByteArray): String {
		val sb = StringBuilder(a.size * 2)
		for (b in a) {
			sb.append(String.format("%02x ", b))
		}
		return sb.toString()
	}
}