package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.PacketType

abstract class BaseResponse(
	val entityId: Long,
	val packetType: PacketType,
	val errorCode: ErrorCode
) {
	abstract fun toBuffer(): Buffer
}