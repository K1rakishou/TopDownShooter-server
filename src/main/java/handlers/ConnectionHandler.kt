package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import manager.PlayerManager
import model.ErrorCode
import model.network.ConnectionResult
import model.network.PacketType
import model.response.BaseResponse
import model.response.ConnectionResponse
import java.util.concurrent.atomic.AtomicInteger

class ConnectionHandler(
	private val playerManager: PlayerManager
) : BaseHandler() {

	override suspend fun handle(socket: NetSocket, playerIp: String, input: Buffer, packetType: PacketType, offset: AtomicInteger): BaseResponse {
		val length = input.getIntLE(offset.getAndAdd(4))
		val playerName = input.getString(offset.get(), offset.get() + length)
			.also { offset.addAndGet(length) }

		if (!playerManager.addPlayer(socket, playerIp, playerName)) {
			return ConnectionResponse.error(ErrorCode.UnknownError)
		}

		return ConnectionResponse.success(ConnectionResult.Connected)
	}
}