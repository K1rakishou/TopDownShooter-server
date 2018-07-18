package model

import io.vertx.core.buffer.Buffer


sealed class Response {
	class Ok(val data: Buffer) : Response()
	class Error(val error: ErrorCode) : Response()
}