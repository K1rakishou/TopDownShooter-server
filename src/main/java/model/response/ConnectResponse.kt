package model.response

import io.netty.buffer.ByteBufAllocator
import io.vertx.core.buffer.Buffer
import model.ConnectionResult
import model.ErrorCode
import model.PacketId

class ConnectResponse(
	val connectionResult: ConnectionResult,
	entityId: Long,
	packetId: PacketId,
	errorCode: ErrorCode
) : BaseResponse(entityId, packetId, errorCode) {

	override fun toBuffer(): Buffer {
		val byteBuffer = ByteBufAllocator.DEFAULT.buffer()

		byteBuffer.writeLong(entityId)
		byteBuffer.writeInt(packetId.id)
		byteBuffer.writeInt(connectionResult.result)
		byteBuffer.writeInt(errorCode.value)

		return Buffer.buffer(byteBuffer)
	}
}