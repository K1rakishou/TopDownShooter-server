package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import manager.PlayerManager
import model.network.ConnectionResult
import model.network.PacketType
import model.response.BaseResponse
import model.response.ConnectionResponse
import java.util.concurrent.atomic.AtomicInteger

class ConnectionHandler(
	private val playerManager: PlayerManager
) : BaseHandler() {

	fun handle(socket: NetSocket, input: Buffer, packetType: PacketType, offset: AtomicInteger): BaseResponse {
		val length = input.getIntLE(offset.getAndAdd(4))
		val playerName = input.getString(offset.get(), offset.get() + length)
			.also { offset.addAndGet(length) }

		val playerIp = socket.remoteAddress().host()
		playerManager.addPlayer(socket, playerIp, playerName)

		return ConnectionResponse(ConnectionResult.Connected, packetType)
	}
}