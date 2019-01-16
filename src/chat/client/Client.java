package chat.client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private Socket connWithServer;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	
	public Client(String host, int port, String userName) { 
		try {
			this.connWithServer = new Socket(host,port);
		} catch (UnknownHostException e) {
			String message = "ERROR: Unknown host " + host;
			throw new RuntimeException(message, e.fillInStackTrace());
		} catch (IOException e) {
			String message = "ERROR: Could not bind socket to port " + port;
			throw new RuntimeException(message, e.fillInStackTrace());
		}
		
		System.out.println("CONNECTION SUCCESSFUL");
		
		try {
			toServer = new ObjectOutputStream(new BufferedOutputStream(connWithServer.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
