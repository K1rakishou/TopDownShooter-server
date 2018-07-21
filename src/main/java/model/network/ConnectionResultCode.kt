package model.network

/**
 * WARNING! DO NOT CHANGE THESE BLINDLY, IT CAN BREAK EVERYTHING
 * */
enum class ConnectionResultCode(val result: Int) {
	//NoResult is an empty result it won't be sent to the client
	NoResultCode(-1),
	Connected(0),
	CouldNotConnect(1),
	OutdatedProtocol(2)
}