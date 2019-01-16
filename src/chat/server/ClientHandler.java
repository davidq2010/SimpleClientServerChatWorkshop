package chat.server;

import java.net.Socket;

/**
 * ClientHandler represents the server-side endpoint of the connection with a client
 * @author davidqin
 *
 * Get messages from client
 * Send messages to client
 */
public class ClientHandler implements Runnable {

	private Socket connWithClient;
	private int handlerID;
	
	public ClientHandler(Socket connWithClient, int handlerID) {
		this.connWithClient = connWithClient;
		this.handlerID = handlerID;
	}

	@Override
	public void run() {

	}

}
