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
			closeResources();
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		while(true) {
			System.out.println("Waiting for connections on port " + port + "...");
			
			Socket connWithClient;
			int handlerID = 0;
			
			try {
				connWithClient = serverSocket.accept();	// accept() waits until connection established
				System.out.println("Connection established!");
				
			} catch (IOException e) {	// Don't want to shut server down
				System.err.println("WARNING: Could not establish connection with client attempting to join.");
				continue;
			}
			
			System.out.println("Creating new client handler for new client...");
			ClientHandler handler = new ClientHandler(connWithClient, handlerID, this);
			
			System.out.println(handler.getClientUsername() + " has joined.");
			handlers.add(handler);
			handlerID++;
			
			Thread thread = new Thread(handler);
			threads.add(thread);
			thread.start();
			System.out.println("ClientHandler was created and successfully started on new thread.");
			
			System.out.println("Number of Handlers: " + handlers.size());
			System.out.println("Number of Threads: " + threads.size());
		}
	}
	
	synchronized void removeHandlerByID(int id) {
		for(int i = 0; i < handlers.size(); i++) {
			if(id == handlers.get(i).getHandlerID()) {
				
				handlers.remove(i);
				threads.get(i).interrupt();
				threads.remove(i);
				System.out.println("Client " + id + " has been disconnected.");
				
				break;
			}
		}
		System.out.println("Number of Handlers: " + handlers.size());
		System.out.println("Number of Threads: " + threads.size());
	}
	
	private void closeResources() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			} 
		} catch (IOException e) {
			String message = "WARNING: Error closing server socket.";
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		System.out.println("Shutting down threads...");
		for (Thread thread : threads) { 
			thread.interrupt();
		}
	}
}
