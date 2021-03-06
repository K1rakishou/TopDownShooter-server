package model.network

/**
 * WARNING! DO NOT CHANGE THESE BLINDLY, IT CAN BREAK EVERYTHING
 * */
enum class PacketType(val id: Int) {
	Connect(0),
	Disconnect(1),
	CreateLobby(2),
	JoinLobby(3);

	companion object {
		fun from(value: Int): PacketType {
			return when(value) {
				0 -> Connect
				1 -> Disconnect
				2 -> CreateLobby
				3 -> JoinLobby
				else -> throw IllegalArgumentException("Unknown packetId ${value}")
			}
		}
	}
}