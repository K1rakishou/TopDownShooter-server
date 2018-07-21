
import handlers.ConnectionHandler
import handlers.CreateLobbyHandler
import handlers.PacketHandler
import io.vertx.core.Vertx
import manager.LobbyManager
import manager.PlayerManager

fun main(args: Array<String>) {
	val playerManager = PlayerManager()
	val lobbyManager = LobbyManager(playerManager)
	val connectionHandler = ConnectionHandler(playerManager)
	val createLobbyHandler = CreateLobbyHandler(lobbyManager)
	val packetHandler = PacketHandler(connectionHandler, createLobbyHandler)

	Vertx.vertx().deployVerticle(ServerVerticle(packetHandler)) { ar ->
		if (ar.succeeded()) {
			println("Application started")
		} else {
			println("Could not start application")
			ar.cause().printStackTrace()
		}
	}
}