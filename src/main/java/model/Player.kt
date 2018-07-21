package model

import io.vertx.core.net.NetSocket
import math.Vector3

class Player(
	val socket: NetSocket,
	val playerIp: String,
	val palyerId: Int,
	val playerName: String
) : NetworkEntity(playerName, Vector3.zero()) {

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