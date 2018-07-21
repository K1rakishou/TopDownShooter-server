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

	suspend fun join(remotePlayerId: String): Int? {
		var playerId: Int? = null

		mutex.withLock {
			assert(!playerReadyStateMap.containsKey(remotePlayerId))

			playersInLobby += remotePlayerId
			playerReadyStateMap[remotePlayerId] = false

			playerId = playerManager.getPlayerId(remotePlayerId)
		}

		return playerId
	}

	suspend fun quit(remotePlayerId: String) {
		mutex.withLock {
			playersInLobby.removeFirst { it == remotePlayerId }
			playerReadyStateMap.remove(remotePlayerId)
		}
	}

	suspend fun isFull(): Boolean {
		return mutex.withLock { playersInLobby.size >= MAX_PLAYERS }
	}

	suspend fun playersCount(): Int {
		return mutex.withLock { playersInLobby.size }
	}

	suspend fun playerSetReady(remotePlayerId: String, isReady: Boolean) {
		mutex.withLock {
			if (!playerReadyStateMap.containsKey(remotePlayerId)) {
				return@withLock
			}

			playerReadyStateMap[remotePlayerId] = isReady
			allReady.set(playerReadyStateMap.values.any { isReady -> !isReady })
		}
	}

	suspend fun areAllPlayersReady(): Boolean {
		return allReady.get()
	}

	suspend fun broadcast(currentPlayerRemoteId: String, baseResponse: BaseResponse) {
		val localPlayersInLobby = mutex.withLock {
			playersInLobby.filter { it != currentPlayerRemoteId }
		}

		playerManager.broadcastTo(localPlayersInLobby, baseResponse)
	}

	companion object {
		const val MAX_PLAYERS = 4
	}
}