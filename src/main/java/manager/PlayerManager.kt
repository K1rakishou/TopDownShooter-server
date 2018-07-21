package manager

import io.vertx.core.net.NetSocket
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import model.Player
import model.response.BaseResponse
import java.util.concurrent.atomic.AtomicInteger

class PlayerManager {
	private val mutex = Mutex()
	private val playerIdPool = AtomicInteger()
	private val playersList = mutableMapOf<String, Player>()

	suspend fun addPlayer(netSocket: NetSocket, remotePlayerId: String, playerName: String): Boolean {
		return mutex.withLock {
			if (playersList.containsKey(remotePlayerId)) {
				return@withLock false
			}

			playersList[remotePlayerId] = Player(netSocket, remotePlayerId, playerIdPool.getAndIncrement(), playerName)
			return@withLock true
		}
	}

	suspend fun removePlayer(player: Player) {
		mutex.withLock {
			playersList.remove(player.remotePlayerId)
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

	suspend fun getPlayerId(playerIp: String): Int? {
		return mutex.withLock { playersList[playerIp]?.palyerId }
	}
}