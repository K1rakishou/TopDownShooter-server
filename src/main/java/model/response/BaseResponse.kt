package model.response

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.PacketId

abstract class BaseResponse(
	val entityId: Long,
	val packetId: PacketId,
	val errorCode: ErrorCode
) {
	abstract fun toBuffer(): Buffer
}