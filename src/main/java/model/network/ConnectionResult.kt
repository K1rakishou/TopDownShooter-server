package model.network

enum class ConnectionResult(val result: Int) {
	//NoResult is an empty result it won't be sent to the client
	NoResult(-1),
	Connected(0),
	CouldNotConnect(1),
	ServerIsFull(2),
	OutdatedProtocol(3)
}