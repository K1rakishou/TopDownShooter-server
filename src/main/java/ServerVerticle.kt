
import handlers.ConnectionHandler
import handlers.PacketHandler
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.experimental.launch
import model.Response

class ServerVerticle : CoroutineVerticle() {
	private val connectionHandler = ConnectionHandler()
	private val packetHandler = PacketHandler(connectionHandler)

	override suspend fun start() {
		val server = vertx.createNetServer()

		server.connectHandler { socket ->
			socket.handler { buffer ->
				launch {
					handleData(buffer, socket)
				}
			}
		}.listen(14887)
	}

	private fun handleData(buffer: Buffer, socket: NetSocket) {
		val response = packetHandler.handle(buffer)

		when (response) {
			is Response.Ok -> {
				socket.write(response.data)
			}

			is Response.Error -> {
				println("An error has occurred: ${response.error.value}")
				socket.close()
			}
		}
	}
}