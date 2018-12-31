package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	private int port;
	private boolean serverOn;
	private ArrayList<ClientHandler> handlers;
	private ArrayList<Thread> threads;
	private	ServerSocket serverSocket;

	
	public Server(int port) {
		this.port = port;
		handlers = new ArrayList<>();
		threads = new ArrayList<>();
	}
	
	public void start() {
		serverOn = true;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("ERROR: Could not bind server socket to port " + port + ".");
			return;
		}
		
		int handlerID = 0;
		
		System.out.println("Starting server");
		while ( serverOn ) {
			System.out.println("Waiting for connections on port " + port + "...");
			Socket connWithClient;
			try {
				connWithClient = serverSocket.accept();
			} catch (IOException e) {
				System.err.println("WARNING: Could not establish connection with client attempting join.");
				continue;
			}
			
			System.out.println("Creating client handler for new client...");
			ClientHandler handler = new ClientHandler(connWithClient, handlerID, this);
			System.out.println(handler.getClientUsername() + " has joined.");
			handlers.add(handler);
			handlerID++;
			
			Thread thread = new Thread(handler);
			threads.add(thread);
			thread.start();
			System.out.println("Client Handler created and successfully started on new thread.");
			
			System.out.println("Number of Handlers: " + handlers.size());
			System.out.println("Number of Threads: " + threads.size());
		}
	}
	
	synchronized void removeHandlerByID(int id) {
		for ( int i = 0; i < handlers.size(); i++ ) {
			if ( id == handlers.get(i).getHandlerID() ) {
				String disconnUser = handlers.get(i).getClientUsername();
				handlers.remove(i);
				threads.get(i).interrupt();
				threads.remove(i);
				System.out.println("Client " + id + " has disconnected.");
				broadCast(disconnUser + " has left the chat.");
				break;
			}
		}
		System.out.println("Number of Handlers: " + handlers.size());
		System.out.println("Number of Threads: " + threads.size());
	}
	
	synchronized public void broadCast(String message) {
		for ( int i = handlers.size() - 1; i >= 0; i-- ) {
			handlers.get(i).writeMessageToClient(message);
		}
	}
	
	void closeResources() {
		try {
			if ( serverSocket != null )
				serverSocket.close();
		} catch (IOException e) {
			System.err.println("WARNING: Error closing server socket.");
		}
		
		System.out.println("Shutting down threads...");
		for ( Thread thread : threads ) 
			thread.interrupt();
	}
}
