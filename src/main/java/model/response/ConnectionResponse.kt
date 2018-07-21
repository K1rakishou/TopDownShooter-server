package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.network.ConnectionResult
import model.network.PacketType

class ConnectionResponse private constructor(
	packetType: PacketType,
	private val errorCode: ErrorCode,
	private val connectionResult: ConnectionResult
) : BaseResponse(packetType, errorCode) {

	override fun toBuffer(): Buffer {
		val buffer = super.toBuffer()

		if (errorCode == ErrorCode.Ok) {
			buffer.appendIntLE(connectionResult.result)
		}

		return buffer
	}

	companion object {
		fun success(connectionResult: ConnectionResult): ConnectionResponse {
			return ConnectionResponse(PacketType.Connect, ErrorCode.Ok, connectionResult)
		}

		fun error(errorCode: ErrorCode): ConnectionResponse {
			return ConnectionResponse(PacketType.Connect, errorCode, ConnectionResult.NoResult)
		}
	}
}