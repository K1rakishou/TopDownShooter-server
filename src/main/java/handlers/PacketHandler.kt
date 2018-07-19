package handlers

import io.vertx.core.buffer.Buffer
import model.ErrorCode
import model.PacketType
import model.Response
import java.util.concurrent.atomic.AtomicInteger

class PacketHandler(
	private val connectionHandler: ConnectionHandler
) {
	private val MAGIC_NUMBER = 0x11223344

	fun handle(input: Buffer): Response {
		val offset = AtomicInteger(0)
		val magicNumber = input.getIntLE(offset.getAndAdd(4))
		if (magicNumber != MAGIC_NUMBER) {
			return Response.Error(ErrorCode.BadMagicNumber)
		}

		val packetType = PacketType.from(input.getIntLE(offset.getAndAdd(4)))
		val response = when (packetType) {
			PacketType.Connect -> connectionHandler.handle(input, packetType, offset)
			PacketType.Disconnect -> TODO()
		}

		return Response.Ok(response.toBuffer())
	}

}