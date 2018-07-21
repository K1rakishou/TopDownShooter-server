package manager

import io.vertx.core.net.NetSocket
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import model.Player
import model.response.BaseResponse

class PlayerManager {
	private val mutex = Mutex()
	private val playersList = mutableMapOf<String, Player>()

	suspend fun addPlayer(netSocket: NetSocket, playerIp: String, playerName: String): Boolean {
		return mutex.withLock {
			if (playersList.containsKey(playerIp)) {
				return@withLock false
			}

			playersList[playerIp] = Player(netSocket, playerIp, playerName)
			return@withLock true
		}
	}

	suspend fun removePlayer(player: Player) {
		mutex.withLock {
			playersList.remove(player.playerIp)
			player.socket.close()
		}
	}

	/**
	 * This should be called when player suddenly loses connection.
	 * At this point socket should already be closed
	 * */
	suspend fun removePlayer(playerIp: String) {
		mutex.withLock { playersList.remove(playerIp) }
	}

	suspend fun broadcastTo(playerIpList: List<String>, baseResponse: BaseResponse) {
		val playersToBroadcast = mutex.withLock {
			return@withLock playerIpList.mapNotNull { playersList[it] }
		}

		launch {
			for (player in playersToBroadcast) {
				player.socket.write(baseResponse.toBuffer())
			}
		}
	}
}