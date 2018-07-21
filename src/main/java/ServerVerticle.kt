
import handlers.PacketHandler
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.experimental.launch
import model.Constants
import model.ErrorCode
import model.Response

class ServerVerticle(
	private val packetHandler: PacketHandler
) : CoroutineVerticle() {
	private val HEADER_SIZE = 8
	private val RECEIVER_ID_SIZE = 8

	override suspend fun start() {
		val server = vertx.createNetServer()

		server.connectHandler { socket ->
			socket.handler { buffer ->
				launch { handleData(buffer, socket) }
			}
		}.exceptionHandler { error ->
			error.printStackTrace()
		}.listen(14887)
	}

	private suspend fun handleData(buffer: Buffer, socket: NetSocket) {
		println("new request from ${socket.remoteAddress().host()}")

		val response = try {
			packetHandler.handle(socket, buffer)
		} catch (error: Throwable) {
			error.printStackTrace()
			ErrorCode.UnknownError
		}

		when (response) {
			is Response.Ok -> {
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
}