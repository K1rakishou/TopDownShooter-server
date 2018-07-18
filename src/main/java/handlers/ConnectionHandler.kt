package handlers

import io.vertx.core.buffer.Buffer
import model.ConnectionResult
import model.ErrorCode
import model.PacketId
import model.response.BaseResponse
import model.response.ConnectResponse
import java.util.concurrent.atomic.AtomicInteger

class ConnectionHandler : BaseHandler() {

	fun handle(input: Buffer, packetId: PacketId, offset: AtomicInteger): BaseResponse {
		val entityId = input.getLong(offset.getAndAdd(8))

		return ConnectResponse(ConnectionResult.Connected, entityId, packetId, ErrorCode.Ok)
	}
}