package model

import io.vertx.core.net.NetSocket

class PlayerInLobby(
	val socket: NetSocket,
	val remotePlayerId: String,
	var isReady: Boolean
) {

	override fun equals(other: Any?): Boolean {
		if (other == null || other !is Player) {
			return false
		}

		return remotePlayerId == other.remotePlayerId
	}

	override fun hashCode(): Int {
		return 31 * remotePlayerId.hashCode()
	}
}