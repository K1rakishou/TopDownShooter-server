package model.network

enum class PacketType(val id: Int) {
	Connect(0),
	Disconnect(1);

	companion object {
		fun from(value: Int): PacketType {
			return when(value) {
				0 -> Connect
				1 -> Disconnect
				else -> throw IllegalArgumentException("Unknown packetId ${value}")
			}
		}
	}
}