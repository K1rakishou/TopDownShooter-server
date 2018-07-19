package model

enum class PacketType(val id: Int) {
	Connect(0),
	Disconnect(1);

	companion object {
		fun from(value: Int): PacketType {
			return when(value) {
				0 -> PacketType.Connect
				1 -> PacketType.Disconnect
				else -> throw IllegalArgumentException("Unknown packetId ${value}")
			}
		}
	}
}