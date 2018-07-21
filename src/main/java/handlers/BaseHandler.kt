package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import model.network.PacketType
import model.response.BaseResponse
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseHandler {
	protected val INT = 4
	protected val LONG = 8

	abstract suspend fun handle(socket: NetSocket, playerIp: String, input: Buffer, packetType: PacketType, offset: AtomicInteger): BaseResponse
}