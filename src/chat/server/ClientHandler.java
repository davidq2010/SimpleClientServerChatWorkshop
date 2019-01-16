package chat.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	// Streams to communicate with client
	private ObjectOutputStream toClient;
	private ObjectInputStream fromClient;
	
	public ClientHandler(Socket connWithClient, int handlerID) {
		this.connWithClient = connWithClient;
		this.handlerID = handlerID;
		
		// Output stream first
		try {
			toClient = new ObjectOutputStream(new BufferedOutputStream(connWithClient.getOutputStream()));
			toClient.flush();
		} catch (IOException e) {
			String message = "ERROR: Could not create output stream to client.";
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		try {
			fromClient = new ObjectInputStream(new BufferedInputStream(connWithClient.getInputStream()));
		} catch (IOException e) {
			String message = "ERROR: Could not create input stream from client.";
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		System.out.println("Successfully created communication streams with client.");
	}

	@Override
	public void run() {

	}

}
