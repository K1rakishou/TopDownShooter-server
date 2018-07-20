package model.response

import io.vertx.core.buffer.Buffer
import model.network.PacketType

abstract class BaseResponse(
	private val packetType: PacketType
) {
	open fun toBuffer(): Buffer {
		val buffer = Buffer.buffer()
		buffer.appendIntLE(packetType.id)
		return buffer
	}
}