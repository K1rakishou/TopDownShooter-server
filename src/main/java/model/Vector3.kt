package model

import io.vertx.core.buffer.Buffer

data class Vector3(
	val x: Float,
	val y: Float,
	val z: Float
) {

	companion object {
		const val SIZE = 12

		fun fromBuffer(buffer: Buffer, offset: Int): Vector3? {
			try {
				val x = buffer.getFloat(offset)
				val y = buffer.getFloat(offset + 4)
				val z = buffer.getFloat(offset + 8)

				return Vector3(x, y, z)
			} catch (error: Throwable) {
				error.printStackTrace()
				return null
			}
		}
	}
}