package handlers

import extensions.getPlayerRemoteId
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import model.Constants
import model.Constants.INT_SIZE
import model.Constants.LONG_SIZE
import model.ErrorCode
import model.Response
import model.network.PacketType
import model.network.ProtocolVersion
import java.util.concurrent.atomic.AtomicInteger

class MainPacketHandler(
	private val connectionHandler: ConnectionHandler,
	private val createLobbyHandler: CreateLobbyHandler,
	private val joinLobbyHandler: JoinLobbyHandler
) {
	suspend fun handle(socket: NetSocket, input: Buffer): Response {
		val remotePlayerId = socket.remoteAddress().getPlayerRemoteId()
		val offset = AtomicInteger(0)
		val magicNumber = input.getIntLE(offset.getAndAdd(INT_SIZE))

		if (magicNumber != Constants.MAGIC_NUMBER) {
			return Response.Error(ErrorCode.BadMagicNumber)
		}

		val packetType = PacketType.from(input.getIntLE(offset.getAndAdd(INT_SIZE)))
		val receiverId = input.getLongLE(offset.getAndAdd(LONG_SIZE))
		val protocolVersion = ProtocolVersion.fromValue(input.getIntLE(offset.getAndAdd(INT_SIZE)))

		println("packetType = $packetType")

		val response = when (packetType) {
			PacketType.Connect -> connectionHandler.handle(protocolVersion, socket, remotePlayerId, input, offset)
			PacketType.Disconnect -> TODO()
			PacketType.CreateLobby -> createLobbyHandler.handle(protocolVersion, socket, remotePlayerId, input, offset)
			PacketType.JoinLobby -> joinLobbyHandler.handle(protocolVersion, socket, remotePlayerId, input, offset)
			else -> throw IllegalArgumentException("Unknown packetType ${packetType}")
		}

		return Response.Ok(receiverId, response.toBuffer())
	}

}