package chat.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private Socket connWithServer;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	
	public Client(String host, int port, String userName) {
		try {
			connWithServer = new Socket(host, port);
		} catch (IOException e) {
			System.err.println("ERROR: Could not connect to " + host + " at port " + port + ".");
			return;
		}
		
		System.out.println("CONNECTION SUCCESSFUL");
		
		try {
			toServer = new ObjectOutputStream( new BufferedOutputStream(connWithServer.getOutputStream() ) );
			toServer.flush();
		} catch (IOException e) {
			System.err.println("ERROR: Could not create output stream to server.");
			return;
		}
				
		try {
			fromServer = new ObjectInputStream( new BufferedInputStream( connWithServer.getInputStream() ) );
		} catch (IOException e) {
			System.err.println("ERROR: Could not create input stream from server.");
			return;
		}
	
		try {
			toServer.writeObject(userName);
			toServer.flush();
		} catch (IOException e) {
			System.err.println("ERROR: Could not send username to server.");
			return;
		}
		
		System.out.println("You are logged in as " + userName);
	}
	
	public void run() {		
		Thread listener = new Thread(new Runnable() {
			public void run() {
				while ( !Thread.interrupted() ) {
					try {
						String message = (String) fromServer.readObject();
						System.out.println(message);
					} catch (ClassNotFoundException e) {
						// It's a String, won't throw exception
					} catch (IOException e) {
						// When ClientHandler closes socket
						System.err.println("WARNING: Listener thread closed.");
						break;
					}
				}
			}
		});
		
		listener.start();
		
		Scanner userInput = new Scanner(System.in);
		while ( true ) {
			String message = userInput.nextLine();
			
			if ( message.equalsIgnoreCase("LOGOUT") ) {
				try {
					toServer.writeObject( new ChatMessage(MessageType.LOGOUT) );
					toServer.flush();
				} catch (IOException e) {
					System.err.println("ERROR: Could not send Logout Message");
					break;
				}
				System.out.println("Logging out...");
				break;
			}
			else {
				try {
					toServer.writeObject( new ChatMessage(MessageType.MESSAGE, message));
					toServer.flush();
				} catch (IOException e) {
					System.err.println("WARNING: Could not send your message.");
				}
			}
		}
		
		listener.interrupt();
		userInput.close();
		closeResources();
	}
	
	void closeResources() {
		try {
			if ( connWithServer != null )
				connWithServer.close();
			if ( fromServer != null )
				fromServer.close();
			if ( toServer != null )
				toServer.close();
		} catch (Exception e) {
			System.err.println("ERROR: Failed to close all resources.");
		}
	}
}
