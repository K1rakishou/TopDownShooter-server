package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import manager.PlayerManager
import model.Constants.INT_SIZE
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
		}
	}

	private suspend fun handle_V1(
		socket: NetSocket,
		remotePlayerId: String,
		input: Buffer,
		offset: AtomicInteger
	): BaseResponse {
		val length = input.getIntLE(offset.getAndAdd(INT_SIZE))
		val playerName = input.getString(offset.get(), offset.get() + length)
			.also { offset.addAndGet(length) }

		if (!playerManager.addPlayer(socket, remotePlayerId, playerName)) {
			return ConnectionResponse.error(ErrorCode.UnknownError)
		}

		return ConnectionResponse.success(ConnectionResultCode.Connected)
	}
}