package model

import io.vertx.core.buffer.Buffer

data class Quaternion(
	var x: Float,
	var y: Float,
	var z: Float,
	var w: Float
) {
	companion object {
		const val SIZE = 16

		fun fromBuffer(buffer: Buffer, offset: Int): Quaternion? {
			try {
				val x = buffer.getFloat(offset)
				val y = buffer.getFloat(offset + 4)
				val z = buffer.getFloat(offset + 8)
				val w = buffer.getFloat(offset + 12)

				return Quaternion(x, y, z, w)
			} catch (error: Throwable) {
				error.printStackTrace()
				return null
			}
		}
	}
}