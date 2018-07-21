package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import model.network.PacketType
import model.network.ProtocolVersion
import model.response.BaseResponse
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseHandler {
	abstract suspend fun handle(protocolVersion: ProtocolVersion,
								socket: NetSocket,
								remotePlayerId: String,
								input: Buffer,
								offset: AtomicInteger): BaseResponse
}