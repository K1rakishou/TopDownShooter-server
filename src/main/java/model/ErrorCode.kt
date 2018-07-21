package model

enum class ErrorCode(val value: Int) {
	UnknownError(-1),
	Ok(0),
	BadMagicNumber(1),
	CorruptedData(2),

	//Lobby
	LobbyIsFull(3),
	LobbyDoesNotExist(4)


}