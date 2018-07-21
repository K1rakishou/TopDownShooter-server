package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.network.ResponseType

class JoinLobbyResponse private constructor(
	private val errorCode: ErrorCode
) : BaseResponse(ResponseType.JoinLobby, errorCode) {

	override fun toBuffer(): Buffer {
		return super.toBuffer()
	}

	companion object {
		fun success(): JoinLobbyResponse {
			return JoinLobbyResponse(ErrorCode.Ok)
		}

		fun error(errorCode: ErrorCode): JoinLobbyResponse {
			return JoinLobbyResponse(errorCode)
		}
	}
}