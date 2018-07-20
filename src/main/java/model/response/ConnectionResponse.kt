package model.response

import io.vertx.core.buffer.Buffer
import model.network.ConnectionResult
import model.network.PacketType

class ConnectionResponse(
	private val connectionResult: ConnectionResult,
	packetType: PacketType
) : BaseResponse(packetType) {

	override fun toBuffer(): Buffer {
		val buffer = super.toBuffer()
		buffer.appendIntLE(connectionResult.result)
		return buffer
	}
}