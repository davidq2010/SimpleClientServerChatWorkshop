package chat.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chat.common.ChatMessage;

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
	private String clientUsername;

	// Streams to communicate with client
	private ObjectOutputStream toClient;
	private ObjectInputStream fromClient;
	
	private Server parentServer;
	
	public ClientHandler(Socket connWithClient, int handlerID, Server parentServer) 
			throws IOException {
		this.connWithClient = connWithClient;
		this.handlerID = handlerID;
		this.parentServer = parentServer;
		
		// Output stream first
		try {
			toClient = new ObjectOutputStream(new BufferedOutputStream(connWithClient.getOutputStream()));
			toClient.flush();
		} catch (IOException e) {
			String message = "ERROR: Could not create output stream to client.";
			closeResources();
			throw new IOException(message, e);
		}
		
		try {
			fromClient = new ObjectInputStream(new BufferedInputStream(connWithClient.getInputStream()));
		} catch (IOException e) {
			String message = "ERROR: Could not create input stream from client.";
			closeResources();
			throw new IOException(message, e);
		}
		
		System.out.println("Successfully created communication streams with client.");
		
		try {
			clientUsername = (String) fromClient.readObject();
		} catch (ClassNotFoundException e) {
			String message = "ERROR: String is not a class???";
			closeResources();
			throw new RuntimeException(message, e);
		} catch (IOException e) {
			String message = "ERROR: Could not retrieve username from client.";
			closeResources();
			throw new IOException(message, e);
		}
		
		System.out.println("Successfully retrieved username from client.");
	}

	@Override
	public void run() {
		loop: while (!Thread.interrupted()) {
			ChatMessage chatMessage;
			try {
				chatMessage = (ChatMessage) fromClient.readObject();
			} catch (ClassNotFoundException e) {
				String message = "ERROR: ChatMessage is not a class???";
				closeResources();
				throw new RuntimeException(message, e);
			} catch (IOException e) {
				String message = "ERROR: Could not read chat message from client " + clientUsername;
				closeResources();
				System.err.println(message);
				return;
			}
			
			switch(chatMessage.getType()) {
			case LOGOUT:
				break loop;
			
			case MESSAGE:
				System.out.println(clientUsername + ": " + chatMessage.getMessage());
				parentServer.broadCast( clientUsername + ": " + chatMessage.getMessage() );
				break;
			}
		}
		closeResources();
	}
	
	public void writeMessageToClient(String message) throws IOException {
			toClient.writeObject(message);
			toClient.flush();
	}

	public String getClientUsername() {
		return clientUsername;
	}
	
	public int getHandlerID() {
		return handlerID;
	}
	
	public void closeResources() {
		try {
			if (connWithClient != null)
				connWithClient.close();
			if (fromClient != null)
				fromClient.close();
			if (toClient != null)
				toClient.close();
		} catch (IOException e) {
			String message = "WARNING: ClientHandler " + handlerID + " failed to close resources.";
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		parentServer.removeHandlerByID(handlerID);
	}
}
