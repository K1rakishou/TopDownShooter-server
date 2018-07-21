package model

import io.vertx.core.net.NetSocket

class ClientInfo(
	val socket: NetSocket,
	val remotePlayerId: String,
	val playerId: Int) {

	override fun equals(other: Any?): Boolean {
		if (other == null || other !is ClientInfo) {
			return false
		}

		return remotePlayerId == other.remotePlayerId
	}

	override fun hashCode(): Int {
		return 31 * remotePlayerId.hashCode()
	}
}