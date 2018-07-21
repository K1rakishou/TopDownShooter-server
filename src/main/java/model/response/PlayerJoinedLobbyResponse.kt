package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.network.ResponseType

class PlayerJoinedLobbyResponse private constructor(
	private val joinedPlayerId: Int
) : BaseResponse(ResponseType.PlayerJoinedLobby, ErrorCode.Ok) {

	override fun toBuffer(): Buffer {
		val buffer = super.toBuffer()
		buffer.appendIntLE(joinedPlayerId)

		return buffer
	}

	companion object {
		fun create(joinedPlayerId: Int): PlayerJoinedLobbyResponse {
			return PlayerJoinedLobbyResponse(joinedPlayerId)
		}
	}
}