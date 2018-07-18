package model

enum class ErrorCode(val value: Int) {
	Ok(0),
	BadMagicNumber(1),
	CorruptedData(2)
}