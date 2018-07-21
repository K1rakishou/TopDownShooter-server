package model.entity

import math.Quaternion
import math.Vector3

class Player(
	name: String,
	position: Vector3,
	val rotation: Quaternion
) : NetworkEntity(name, position) {
}