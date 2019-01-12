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
	
	private ClientChatMainFrame chatMainFrame;
		
	public Client(String host, int port, String userName, ClientLoginMainFrame loginMainFrame) {
		try {
			connWithServer = new Socket(host, port);
		} catch (IOException e) {
			String errorMessage = "ERROR: Could not connect to " + host + " at port " + port + ".";
			if (loginMainFrame == null)
				System.err.println(errorMessage);
			else {
				loginMainFrame.setClientLoginWorked(false);
				loginMainFrame.setErrorMessage(errorMessage);
			}
			return;
		}
		
		System.out.println("CONNECTION SUCCESSFUL");
		
		try {
			toServer = new ObjectOutputStream( new BufferedOutputStream(connWithServer.getOutputStream() ) );
			toServer.flush();
		} catch (IOException e) {
			String errorMessage = "ERROR: Could not create output stream to server.";
			if (loginMainFrame == null)
				System.err.println(errorMessage);
			else {
				loginMainFrame.setClientLoginWorked(false);
				loginMainFrame.setErrorMessage(errorMessage);
			}
			return;
		}
				
		try {
			fromServer = new ObjectInputStream( new BufferedInputStream( connWithServer.getInputStream() ) );
		} catch (IOException e) {
			String errorMessage = "ERROR: Could not create input stream from server.";
			if (loginMainFrame == null)
				System.err.println(errorMessage);
			else {
				loginMainFrame.setClientLoginWorked(false);
				loginMainFrame.setErrorMessage(errorMessage);
			}
			return;
		}
	
		try {
			toServer.writeObject(userName);
			toServer.flush();
		} catch (IOException e) {
			String errorMessage = "ERROR: Could not send username to server.";
			if (loginMainFrame == null)
				System.err.println(errorMessage);
			else {
				loginMainFrame.setClientLoginWorked(false);
				loginMainFrame.setErrorMessage(errorMessage);
			}
			return;
		}
		
		loginMainFrame.setClientLoginWorked(true);
		System.out.println("You are logged in as " + userName);
	}
	
	public Client(String host, int port, String userName) {
		this(host, port, userName, null);
	}
	
	public void run() {		
		Thread listener = new Thread(new Runnable() {
			public void run() {
				while ( !Thread.interrupted() ) {
					try {
						String message = (String) fromServer.readObject();
						if ( chatMainFrame == null ) {
							System.out.println(message);
							System.out.print("> ");
						}
						else
							chatMainFrame.writeTextToReadPanel(message);
					} catch (ClassNotFoundException e) {
						// It's a String, won't throw exception
					} catch (IOException e) {
						// When ClientHandler closes socket
						String warningMessage = "WARNING: Listener thread closed.";
						if ( chatMainFrame == null )
							System.err.println(warningMessage);
						else 
							chatMainFrame.sendWarningMessage(warningMessage);
						break;
					}
				}
			}
		});
		
		listener.start();
		
		// If using GUI, the GUI will handle message-sending
		if ( chatMainFrame == null ) {
			Scanner userInput = new Scanner(System.in);
			while ( true ) {
				System.out.print("> ");
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
			userInput.close();
			listener.interrupt();
			closeResources();
		}
	}
	
	public void sendMessage(String message) {
		try {
			toServer.writeObject( new ChatMessage(MessageType.MESSAGE, message));
			toServer.flush();
		} catch (IOException e) {
			String warningMessage = "WARNING: Could not send your message.";
			if ( chatMainFrame == null )
				System.err.println(warningMessage);
			else {
				chatMainFrame.sendWarningMessage(warningMessage);
			}
		}
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
	
	public void setClientChatMainFrame(ClientChatMainFrame chatMainFrame) {
		this.chatMainFrame = chatMainFrame;
	}
}
