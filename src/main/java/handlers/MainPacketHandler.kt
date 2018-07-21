package handlers

import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import model.Constants
import model.ErrorCode
import model.Response
import model.network.PacketType
import java.util.concurrent.atomic.AtomicInteger

class MainPacketHandler(
	private val connectionHandler: ConnectionHandler,
	private val createLobbyHandler: CreateLobbyHandler,
	private val joinLobbyHandler: JoinLobbyHandler
) {
	suspend fun handle(socket: NetSocket, input: Buffer): Response {
		val playerIp = socket.remoteAddress().host()
		val offset = AtomicInteger(0)
		val magicNumber = input.getIntLE(offset.getAndAdd(4))

		if (magicNumber != Constants.MAGIC_NUMBER) {
			return Response.Error(ErrorCode.BadMagicNumber)
		}

		val packetType = PacketType.from(input.getIntLE(offset.getAndAdd(4)))
		val receiverId = input.getLongLE(offset.getAndAdd(8))

		val response = when (packetType) {
			PacketType.Connect -> connectionHandler.handle(socket, playerIp, input, packetType, offset)
			PacketType.Disconnect -> TODO()
			PacketType.CreateLobby -> createLobbyHandler.handle(socket, playerIp, input, packetType, offset)
			PacketType.JoinLobby -> joinLobbyHandler.handle(socket, playerIp, input, packetType, offset)
		}

		return Response.Ok(receiverId, response.toBuffer())
	}

}