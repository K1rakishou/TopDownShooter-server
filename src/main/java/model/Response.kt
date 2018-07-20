package model

import io.vertx.core.buffer.Buffer


sealed class Response {
	class Ok(val receiverId: Long,
			 val data: Buffer) : Response()
	class Error(val error: ErrorCode) : Response()
}