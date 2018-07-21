package model.network

enum class ProtocolVersion(val version: Int) {
	V1(1);

	companion object {
		fun fromValue(value: Int): ProtocolVersion {
			return when (value) {
				1 -> V1
				else -> throw IllegalArgumentException("Unknown protocol version ${value}")
			}
		}
	}
}