package manager

import io.vertx.core.net.NetSocket
import model.Player
import model.response.BaseResponse
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class PlayerManager {
	private val lock = ReentrantReadWriteLock()
	private val playersList = mutableMapOf<String, Player>()

	fun addPlayer(netSocket: NetSocket, playerIp: String, playerName: String): Boolean {
		return lock.write {
			if (playersList.containsKey(playerIp)) {
				return@write false
			}

			playersList[playerIp] = Player(netSocket, playerIp, playerName)
			return@write true
		}
	}

	fun removePlayer(player: Player) {
		lock.write {
			playersList.remove(player.playerIp)
			player.socket.close()
		}
	}

	fun broadcast(currentPlayer: Player, response: BaseResponse) {
		lock.read {
			playersList.forEach { playerIp, player ->
				if (playerIp != currentPlayer.playerIp) {
					player.socket.write(response.toBuffer())
				}
			}
		}
	}
}