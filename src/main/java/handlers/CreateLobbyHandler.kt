package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import manager.LobbyManager
import model.ErrorCode
import model.network.PacketType
import model.response.BaseResponse
import model.response.CreateLobbyResponse
import java.util.concurrent.atomic.AtomicInteger

class CreateLobbyHandler(
	private val lobbyManager: LobbyManager
) : BaseHandler() {

	override suspend fun handle(socket: NetSocket, playerIp: String, input: Buffer, packetType: PacketType, offset: AtomicInteger): BaseResponse {
		val lobbyId = lobbyManager.createLobby()

		if (lobbyManager.joinLobby(lobbyId, playerIp) !is LobbyManager.JoinLobbyResult.Joined) {
			return CreateLobbyResponse.error(ErrorCode.UnknownError)
		}

		return CreateLobbyResponse.success(lobbyId)
	}
}