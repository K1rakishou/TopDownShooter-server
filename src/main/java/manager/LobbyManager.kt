package manager

import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import model.GameLobby
import model.response.BaseResponse
import java.util.concurrent.atomic.AtomicLong

class LobbyManager(
	private val playerManager: PlayerManager
) {
	private val mutex = Mutex()
	private val lobbyIdPool = AtomicLong(0)
	private val activeLobbies = mutableMapOf<Long, GameLobby>()

	suspend fun createLobby(): Long {
		val lobbyId = lobbyIdPool.getAndIncrement()

		mutex.withLock {
			assert(!activeLobbies.containsKey(lobbyId))
			activeLobbies[lobbyId] = GameLobby(playerManager)
		}

		return lobbyId
	}

	suspend fun joinLobby(lobbyId: Long, remotePlayerId: String): JoinLobbyResult {
		return mutex.withLock {
			if (!activeLobbies.containsKey(lobbyId)) {
				return@withLock JoinLobbyResult.LobbyDoesNotExist()
			}

			if (activeLobbies[lobbyId]!!.isFull()) {
				return@withLock JoinLobbyResult.LobbyIsFull()
			}

			val playerId = activeLobbies[lobbyId]!!.join(remotePlayerId)
			return@withLock JoinLobbyResult.Joined(playerId)
		}
	}

	suspend fun quitLobby(lobbyId: Long, playerIp: String) {
		mutex.withLock {
			if (activeLobbies.containsKey(lobbyId)) {
				activeLobbies[lobbyId]?.quit(playerIp)
			}

			val playerCount = activeLobbies[lobbyId]?.playersCount() ?: -1
			if (playerCount == 0) {
				activeLobbies.remove(lobbyId)
			}
		}
	}

	suspend fun broadcast(lobbyId: Long, currentPlayerRemoteId: String, baseResponse: BaseResponse) {
		mutex.withLock {
			if (activeLobbies.containsKey(lobbyId)) {
				activeLobbies[lobbyId]!!.broadcast(currentPlayerRemoteId, baseResponse)
			}
		}
	}

	sealed class JoinLobbyResult {
		class Joined(val playerId: Int?) : JoinLobbyResult()
		class LobbyIsFull : JoinLobbyResult()
		class LobbyDoesNotExist : JoinLobbyResult()
	}
}