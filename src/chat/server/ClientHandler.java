package chat.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chat.client.ChatMessage;

public class ClientHandler implements Runnable {

	private Socket connWithClient;
	private int handlerID;
	private Server parentServer;
	private ObjectInputStream fromClient;
	private ObjectOutputStream toClient;
	private String clientUsername;
	
	public ClientHandler(Socket connWithClient, int handlerID, Server parentServer) {
		this.connWithClient = connWithClient;
		this.handlerID = handlerID;
		this.parentServer = parentServer;
				
		// HAVE TO CREATE OUTPUT STREAM FIRST, OR IT DEADLOCKS
		// ALSO HAVE TO FLUSH THE OUTPUTSTREAM HEADER
		try {
			toClient = new ObjectOutputStream( new BufferedOutputStream( connWithClient.getOutputStream() ) );
			toClient.flush();
		} catch (IOException e) {
			System.err.println("ERROR: Could not create output stream to client.");
			return;
		}
		
		try {
			fromClient = new ObjectInputStream( new BufferedInputStream( connWithClient.getInputStream() ) );
		} catch (IOException e) {
			System.err.println("ERROR: Could not create input stream from client.");
			return;
		}
		
		System.out.println("Successfully created communication streams with client.");
		
		try {
			clientUsername = (String) fromClient.readObject();
		} catch (ClassNotFoundException e) {
			// String exists, so not possible to throw exception.
		} catch (IOException e) {
			System.err.println("ERROR: Could not retrieve username from client.");
			return;
		}
		
		System.out.println("Successfully retrieved username from client.");
	}

	@Override
	public void run() {
		loop: while ( !Thread.interrupted() ) {
			ChatMessage chatMessage;
			try {
				chatMessage = (ChatMessage) fromClient.readObject();
			} catch (ClassNotFoundException e) {
				// Shouldn't happen
				System.err.println("WTF??");
				e.printStackTrace();
				break;
			} catch (IOException e) {
				System.err.println("ERROR: Could not read chat message from client " + clientUsername);
				break;
			}
			
			switch ( chatMessage.getMessageType() ) {
			case LOGOUT:
				break loop;
			case MESSAGE:
				System.out.println( clientUsername + ": " + chatMessage.getMessage() );
				parentServer.broadCast( chatMessage.getMessage() );
				break;
			}
		}
		closeResources();
		parentServer.removeHandlerByID(handlerID);
	}
	
	public void writeMessageToClient(String message) {
		try {
			toClient.writeObject(message);
			toClient.flush();
		} catch (IOException e) {
			System.err.println("ERROR: Could not send message to " + clientUsername);
			System.err.println("Removing " + clientUsername + " from the chat.");
			closeResources();
			parentServer.removeHandlerByID(handlerID);
		}
	}
	
	public String getClientUsername() {
		return clientUsername;
	}
	
	public int getHandlerID() {
		return handlerID;
	}
	
	private void closeResources() {
		try {
			if ( connWithClient != null )
				connWithClient.close();
			if ( fromClient != null )
				fromClient.close();
			if ( toClient != null )
				toClient.close();
		} catch (IOException e) {
			System.err.println("WARNING: ClientHandler resources failed to close.");
		}
		
		// Remove myself from server's handler list
	}
}
