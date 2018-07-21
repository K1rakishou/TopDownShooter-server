package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.network.PacketType

class CreateLobbyResponse private constructor(
	packetType: PacketType,
	private val errorCode: ErrorCode,
	private val lobbyId: Long
) : BaseResponse(packetType, errorCode) {

	override fun toBuffer(): Buffer {
		val buffer = super.toBuffer()

		if (errorCode == ErrorCode.Ok) {
			buffer.appendLongLE(lobbyId)
		}

		return buffer
	}

	companion object {
		fun success(lobbyId: Long): CreateLobbyResponse {
			return CreateLobbyResponse(PacketType.CreateLobby, ErrorCode.Ok, lobbyId)
		}

		fun error(errorCode: ErrorCode): CreateLobbyResponse {
			return CreateLobbyResponse(PacketType.CreateLobby, errorCode, -1L)
		}
	}
}