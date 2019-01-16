package chat.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	
	private int port;
	private ServerSocket serverSocket;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void start() throws RuntimeException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			String message = "ERROR: Could not bind server socket to port " + port + ".";
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		while(true) {
			System.out.println("Waiting for connections on port " + port + "...");
			
			try {
				serverSocket.accept();	// accept() waits until connection established
				System.out.println("Connection established!");
				
			} catch (IOException e) {
				System.err.println("WARNING: Could not establish connection with client attempting to join.");
			}
		}
	}
}
