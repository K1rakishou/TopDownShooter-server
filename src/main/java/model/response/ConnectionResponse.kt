package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.network.ConnectionResultCode
import model.network.ResponseType

class ConnectionResponse private constructor(
	private val errorCode: ErrorCode,
	private val connectionResultCode: ConnectionResultCode
) : BaseResponse(ResponseType.Connect, errorCode) {

	override fun toBuffer(): Buffer {
		val buffer = super.toBuffer()

		if (errorCode == ErrorCode.Ok) {
			buffer.appendIntLE(connectionResultCode.result)
		}

		return buffer
	}

	companion object {
		fun success(connectionResultCode: ConnectionResultCode): ConnectionResponse {
			return ConnectionResponse(ErrorCode.Ok, connectionResultCode)
		}

		fun error(errorCode: ErrorCode): ConnectionResponse {
			return ConnectionResponse(errorCode, ConnectionResultCode.NoResultCode)
		}
	}
}