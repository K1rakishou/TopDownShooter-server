package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import manager.LobbyManager
import model.Constants.LONG_SIZE
import model.ErrorCode
import model.network.ProtocolVersion
import model.response.BaseResponse
import model.response.JoinLobbyResponse
import model.response.PlayerJoinedLobbyResponse
import java.util.concurrent.atomic.AtomicInteger

class JoinLobbyHandler(
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
		}
	}

	private suspend fun handle_V1(
		socket: NetSocket,
		remotePlayerId: String,
		input: Buffer,
		offset: AtomicInteger
	): BaseResponse {
		val lobbyId = input.getLongLE(offset.getAndAdd(LONG_SIZE))
		val result = lobbyManager.joinLobby(lobbyId, remotePlayerId)

		when (result) {
			is LobbyManager.JoinLobbyResult.Joined -> {
				if (result.playerId == null) {
					return JoinLobbyResponse.error(ErrorCode.UnknownError)
				}
			}
			is LobbyManager.JoinLobbyResult.LobbyIsFull -> {
				return JoinLobbyResponse.error(ErrorCode.LobbyIsFull)
			}
			is LobbyManager.JoinLobbyResult.LobbyDoesNotExist -> {
				return JoinLobbyResponse.error(ErrorCode.LobbyDoesNotExist)
			}
		}

		lobbyManager.broadcast(lobbyId, remotePlayerId, PlayerJoinedLobbyResponse.create(result.playerId))

		return JoinLobbyResponse.success()
	}
}