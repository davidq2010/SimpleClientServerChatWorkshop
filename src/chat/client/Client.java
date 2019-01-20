package chat.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import chat.common.ChatMessage;
import chat.common.MessageType;

public class Client {
	
	private Socket connWithServer;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Thread listener;
	
	public Client(String host, int port, String userName) { 
		try {
			this.connWithServer = new Socket(host,port);
		} catch (UnknownHostException e) {
			String message = "ERROR: Unknown host " + host;
			closeResources();
			throw new RuntimeException(message, e.fillInStackTrace());
		} catch (IOException e) {
			String message = "ERROR: Could not bind socket to port " + port;
			closeResources();
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		System.out.println("CONNECTION SUCCESSFUL");
		
		try {
			toServer = new ObjectOutputStream(new BufferedOutputStream(connWithServer.getOutputStream()));
			toServer.flush();
		} catch (IOException e) {
			String message = "ERROR: Could not create output stream to server.";
			closeResources();
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		try {
			fromServer = new ObjectInputStream(new BufferedInputStream(connWithServer.getInputStream()));
		} catch (IOException e) {
			String message = "ERROR: Could not create input stream from server.";
			closeResources();
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		System.out.println("Communication streams with server created successfully!");
		
		try {
			toServer.writeObject(userName);
			toServer.flush();
		} catch (IOException e) {
			String message = "ERROR: Could not send username to server.";
			closeResources();
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		System.out.println("You are logged in as " + userName);
	}
	
	public void start() {
		listener = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (!Thread.interrupted()) {
					try {
						String message = (String) fromServer.readObject();
						System.out.println(message);
						System.out.print("> ");
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
	}
	
	public void sendMessage(String message) throws IOException {	// Let ClientMain handle
		if (message.equalsIgnoreCase("LOGOUT")) {
			toServer.writeObject(new ChatMessage(MessageType.LOGOUT));
		}
		else {
			toServer.writeObject(new ChatMessage(MessageType.MESSAGE, message));
		}
		toServer.flush();
	}
	
	public void closeResources() {
		try {
			if ( connWithServer != null )
				connWithServer.close();
			if ( fromServer != null )
				fromServer.close();
			if ( toServer != null )
				toServer.close();
			if ( listener != null )
				listener.interrupt();
		} catch (Exception e) {
			System.err.println("ERROR: Failed to close all resources.");
		}
	}
}
