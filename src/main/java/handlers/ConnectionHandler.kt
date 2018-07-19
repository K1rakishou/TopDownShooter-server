package handlers

import io.vertx.core.buffer.Buffer
import model.ConnectionResult
import model.ErrorCode
import model.PacketType
import model.response.BaseResponse
import model.response.ConnectResponse
import java.util.concurrent.atomic.AtomicInteger

class ConnectionHandler : BaseHandler() {

	fun handle(input: Buffer, packetType: PacketType, offset: AtomicInteger): BaseResponse {
		val length = input.getIntLE(offset.getAndAdd(4))
		val playerName = input.getString(offset.get(), offset.get() + length)
			.also { offset.addAndGet(length) }

		println("playerName = ${playerName}")

		return ConnectResponse(ConnectionResult.Connected, 0, packetType, ErrorCode.Ok)
	}
}