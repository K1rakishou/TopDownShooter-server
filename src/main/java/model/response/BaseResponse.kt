package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.network.ResponseType

abstract class BaseResponse(
	private val responseType: ResponseType,
	private val errorCode: ErrorCode
) {

	open fun toBuffer(): Buffer {
		val buffer = Buffer.buffer()
		buffer.appendIntLE(responseType.id)
		buffer.appendIntLE(errorCode.value)
		return buffer
	}
}