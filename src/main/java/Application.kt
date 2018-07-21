
import handlers.ConnectionHandler
import handlers.CreateLobbyHandler
import handlers.JoinLobbyHandler
import handlers.MainPacketHandler
import io.vertx.core.Vertx
import manager.LobbyManager
import manager.PlayerManager

fun main(args: Array<String>) {
	/**
	 * Managers
	 * */
	val playerManager = PlayerManager()
	val lobbyManager = LobbyManager(playerManager)

	/**
	 * Packet handlers
	 * */
	val connectionHandler = ConnectionHandler(playerManager)
	val createLobbyHandler = CreateLobbyHandler(lobbyManager)
	val joinLobbyHandler = JoinLobbyHandler(lobbyManager)

	/**
	 * Main packet handler
	 * */
	val packetHandler = MainPacketHandler(connectionHandler, createLobbyHandler, joinLobbyHandler)

	Vertx.vertx().deployVerticle(ServerVerticle(packetHandler)) { ar ->
		if (ar.succeeded()) {
			println("Server started")
		} else {
			println("Could not start server")
			ar.cause().printStackTrace()
		}
	}
}