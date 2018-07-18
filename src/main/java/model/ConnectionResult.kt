package model

enum class ConnectionResult(val result: Int) {
	Connected(0),
	CouldNotConnect(1),
	ServerIsFull(2)
}