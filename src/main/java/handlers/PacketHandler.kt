package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import model.Constants
import model.ErrorCode
import model.Response
import model.network.PacketType
import java.util.concurrent.atomic.AtomicInteger

class PacketHandler(
	private val connectionHandler: ConnectionHandler
) {
	fun handle(socket: NetSocket, input: Buffer): Response {
		val offset = AtomicInteger(0)
		val magicNumber = input.getIntLE(offset.getAndAdd(4))
		if (magicNumber != Constants.MAGIC_NUMBER) {
			return Response.Error(ErrorCode.BadMagicNumber)
		}

		val packetType = PacketType.from(input.getIntLE(offset.getAndAdd(4)))
		val receiverId = input.getLongLE(offset.getAndAdd(8))

		val response = when (packetType) {
			PacketType.Connect -> connectionHandler.handle(socket, input, packetType, offset)
			PacketType.Disconnect -> TODO()
		}

		return Response.Ok(receiverId, response.toBuffer())
	}

}