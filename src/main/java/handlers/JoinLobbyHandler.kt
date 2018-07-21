package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import manager.LobbyManager
import model.ErrorCode
import model.network.PacketType
import model.response.BaseResponse
import model.response.JoinLobbyResponse
import model.response.PlayerJoinedLobbyResponse
import java.util.concurrent.atomic.AtomicInteger

class JoinLobbyHandler(
	private val lobbyManager: LobbyManager
) : BaseHandler() {

	override suspend fun handle(socket: NetSocket, playerIp: String, input: Buffer, packetType: PacketType, offset: AtomicInteger): BaseResponse {
		val lobbyId = input.getLongLE(offset.getAndAdd(LONG))
		val result = lobbyManager.joinLobby(lobbyId, playerIp)

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

		lobbyManager.broadcast(lobbyId, playerIp, PlayerJoinedLobbyResponse.create(result.playerId))

		return JoinLobbyResponse.success()
	}
}