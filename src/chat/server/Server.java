package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	private int port;
	private ServerSocket serverSocket;
	private ArrayList<ClientHandler> handlers;
	private ArrayList<Thread> threads;
	
	public Server(int port) {
		this.port = port;
		this.handlers = new ArrayList<>();
		this.threads = new ArrayList<>();
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
			
			Socket connWithClient;
			int handlerID = 0;
			
			try {
				connWithClient = serverSocket.accept();	// accept() waits until connection established
				System.out.println("Connection established!");
				
			} catch (IOException e) {
				System.err.println("WARNING: Could not establish connection with client attempting to join.");
				continue;
			}
			
			System.out.println("Creating new client handler for new client...");
			ClientHandler handler = new ClientHandler(connWithClient, handlerID);
			System.out.println();
			handlers.add(handler);
			handlerID++;
			
			Thread thread = new Thread(handler);
			thread.start();
			System.out.println("ClientHandler was created and successfully started on new thread.");
		}
	}
}
