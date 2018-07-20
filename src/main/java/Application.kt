
import handlers.ConnectionHandler
import handlers.PacketHandler
import io.vertx.core.Vertx
import manager.PlayerManager
import service.GameLobby

fun main(args: Array<String>) {
	val playerManager = PlayerManager()
	val gameLobby = GameLobby(playerManager)
	val connectionHandler = ConnectionHandler(playerManager)
	val packetHandler = PacketHandler(connectionHandler)

	Vertx.vertx().deployVerticle(ServerVerticle(packetHandler)) { ar ->
		if (ar.succeeded()) {
			println("Application started")
		} else {
			println("Could not start application")
			ar.cause().printStackTrace()
		}
	}
}