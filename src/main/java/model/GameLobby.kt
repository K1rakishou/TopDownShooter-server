package model

import extensions.removeFirst
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import manager.PlayerManager
import model.response.BaseResponse
import java.util.concurrent.atomic.AtomicBoolean

class GameLobby(
	private val playerManager: PlayerManager
) {
	private val mutex = Mutex()
	private val allReady = AtomicBoolean(false)
	private val playersInLobby = mutableListOf<String>()
	private val playerReadyStateMap = mutableMapOf<String, Boolean>()

	suspend fun join(playerIp: String): Boolean {
		mutex.withLock {
			assert(!playerReadyStateMap.containsKey(playerIp))

			if (playersInLobby.size >= MAX_PLAYERS) {
				return false
			}

			playersInLobby += playerIp
			playerReadyStateMap[playerIp] = false
		}

		return true
	}

	suspend fun quit(playerIp: String) {
		mutex.withLock {
			playersInLobby.removeFirst { it == playerIp }
			playerReadyStateMap.remove(playerIp)
		}
	}

	suspend fun playersCount(): Int {
		return mutex.withLock { playersInLobby.size }
	}

	suspend fun playerSetReady(playerIp: String, isReady: Boolean) {
		mutex.withLock {
			if (!playerReadyStateMap.containsKey(playerIp)) {
				return@withLock
			}

			playerReadyStateMap[playerIp] = isReady
			allReady.set(playerReadyStateMap.values.any { isReady -> !isReady })
		}
	}

	suspend fun areAllPlayersReady(): Boolean {
		return allReady.get()
	}

	suspend fun broadcast(currentPlayerIp: String, baseResponse: BaseResponse) {
		val localPlayersInLobby = mutex.withLock {
			playersInLobby.filter { it != currentPlayerIp }
		}

		playerManager.broadcastTo(localPlayersInLobby, baseResponse)
	}

	companion object {
		const val MAX_PLAYERS = 4
	}
}