package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import manager.LobbyManager
import model.ErrorCode
import model.network.ProtocolVersion
import model.response.BaseResponse
import model.response.CreateLobbyResponse
import java.util.concurrent.atomic.AtomicInteger

class CreateLobbyHandler(
	private val lobbyManager: LobbyManager
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
		val lobbyId = lobbyManager.createLobby()

		val joinLobbyResult = lobbyManager.joinLobby(lobbyId, remotePlayerId)
		if (joinLobbyResult !is LobbyManager.JoinLobbyResult.Joined) {
			println("joinLobbyResult = $joinLobbyResult")
			return CreateLobbyResponse.error(ErrorCode.UnknownError)
		}

		return CreateLobbyResponse.success(lobbyId)
	}
}