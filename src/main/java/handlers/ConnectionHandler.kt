package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import manager.PlayerManager
import model.ErrorCode
import model.network.ConnectionResultCode
import model.network.ProtocolVersion
import model.response.BaseResponse
import model.response.ConnectionResponse
import java.util.concurrent.atomic.AtomicInteger

class ConnectionHandler(
	private val playerManager: PlayerManager
) : BaseHandler() {

	override suspend fun handle(
		protocolVersion: ProtocolVersion,
		socket: NetSocket,
		remotePlayerId: String,
		input: Buffer,
		offset: AtomicInteger
	): BaseResponse {
		return when (protocolVersion) {
			ProtocolVersion.V1 -> handle_V1(socket, remotePlayerId, input, offset)
			else -> throw IllegalArgumentException("Unknown protocol version $protocolVersion")
		}
	}

	private suspend fun handle_V1(
		socket: NetSocket,
		remotePlayerId: String,
		input: Buffer,
		offset: AtomicInteger
	): BaseResponse {
		if (!playerManager.addPlayer(socket, remotePlayerId)) {
			println("Could not add a player to player manager")
			return ConnectionResponse.error(ErrorCode.UnknownError)
		}

		return ConnectionResponse.success(ConnectionResultCode.Connected)
	}
}