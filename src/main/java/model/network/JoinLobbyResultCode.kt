package model.network

/**
 * WARNING! DO NOT CHANGE THESE BLINDLY, IT CAN BREAK EVERYTHING
 * */
enum class JoinLobbyResultCode(val result: Int) {
	NoResultCode(-1),
	Joined(0),
	LobbyIsFullCode(1)
}