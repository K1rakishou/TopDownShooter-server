package model

enum class PacketId(val id: Int) {
	Connect(0),
	Disconnect(1);

	companion object {
		fun from(value: Int): PacketId {
			return when(value) {
				0 -> PacketId.Connect
				1 -> PacketId.Disconnect
				else -> throw IllegalArgumentException("Unknown packetId ${value}")
			}
		}
	}
}