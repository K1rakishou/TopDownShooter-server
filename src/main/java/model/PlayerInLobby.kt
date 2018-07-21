package model

import io.vertx.core.net.NetSocket

class PlayerInLobby(
	val socket: NetSocket,
	val playerIp: String,
	var isReady: Boolean
) {

	override fun equals(other: Any?): Boolean {
		if (other == null || other !is Player) {
			return false
		}

		return playerIp == other.playerIp
	}

	override fun hashCode(): Int {
		return 31 * playerIp.hashCode()
	}
}