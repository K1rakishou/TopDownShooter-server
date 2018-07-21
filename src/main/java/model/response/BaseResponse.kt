package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.network.PacketType

abstract class BaseResponse(
	private val packetType: PacketType,
	private val errorCode: ErrorCode
) {

	open fun toBuffer(): Buffer {
		val buffer = Buffer.buffer()
		buffer.appendIntLE(packetType.id)
		buffer.appendIntLE(errorCode.value)
		return buffer
	}
}