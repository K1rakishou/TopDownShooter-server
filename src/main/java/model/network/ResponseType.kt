package model.network

/**
 * WARNING! DO NOT CHANGE THESE BLINDLY, IT CAN BREAK EVERYTHING
 * */
enum class ResponseType(val id: Int) {
	Connect(0),
	Disconnect(1),
	CreateLobby(2),
	JoinLobby(3),
	PlayerJoinedLobby(4);

	companion object {
		fun from(value: Int): ResponseType {
			return when(value) {
				0 -> Connect
				1 -> Disconnect
				2 -> CreateLobby
				3 -> JoinLobby
				4 -> PlayerJoinedLobby
				else -> throw IllegalArgumentException("Unknown packetId ${value}")
			}
		}
	}
}