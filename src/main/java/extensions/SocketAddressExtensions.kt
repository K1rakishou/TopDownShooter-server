package extensions

import io.vertx.core.net.SocketAddress


/**
 * For test purposes. Since I will have to create multiple instances of the client on my machine -
 * they all will have the same ip address (and my code heavily depends on it), so to avoid confusion I have to add the port
 * as well
 * */
fun SocketAddress.getPlayerRemoteId(): String {
	return "${this.host()}:${this.port()}"
}