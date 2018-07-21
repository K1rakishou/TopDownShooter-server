package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.network.ResponseType

class CreateLobbyResponse private constructor(
	private val errorCode: ErrorCode,
	private val lobbyId: Long
) : BaseResponse(ResponseType.CreateLobby, errorCode) {

	override fun toBuffer(): Buffer {
		val buffer = super.toBuffer()

		if (errorCode == ErrorCode.Ok) {
			buffer.appendLongLE(lobbyId)
		}

		return buffer
	}

	companion object {
		fun success(lobbyId: Long): CreateLobbyResponse {
			return CreateLobbyResponse(ErrorCode.Ok, lobbyId)
		}

		fun error(errorCode: ErrorCode): CreateLobbyResponse {
			return CreateLobbyResponse(errorCode, -1L)
		}
	}
}