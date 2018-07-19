package model.response

import io.netty.buffer.ByteBufAllocator
import io.vertx.core.buffer.Buffer
import model.ConnectionResult
import model.ErrorCode
import model.PacketType

class ConnectResponse(
	val connectionResult: ConnectionResult,
	entityId: Long,
	packetType: PacketType,
	errorCode: ErrorCode
) : BaseResponse(entityId, packetType, errorCode) {

	override fun toBuffer(): Buffer {
		val byteBuffer = ByteBufAllocator.DEFAULT.buffer()

		byteBuffer.writeLong(entityId)
		byteBuffer.writeInt(packetType.id)
		byteBuffer.writeInt(connectionResult.result)
		byteBuffer.writeInt(errorCode.value)

		return Buffer.buffer(byteBuffer)
	}
}