package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import model.network.PacketType
import model.response.BaseResponse
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseHandler {
	abstract suspend fun handle(socket: NetSocket, playerIp: String, input: Buffer, packetType: PacketType, offset: AtomicInteger): BaseResponse
}