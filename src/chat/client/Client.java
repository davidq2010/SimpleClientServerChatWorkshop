package chat.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	public Client(String host, int port, String userName) throws RuntimeException { 
		try {
			Socket connWithServer = new Socket(host,port);
		} catch (UnknownHostException e) {
			String message = "ERROR: Unknown host " + host;
			throw new RuntimeException(message, e.fillInStackTrace());
		} catch (IOException e) {
			String message = "ERROR: Could not bind socket to port " + port;
			throw new RuntimeException(message, e.fillInStackTrace());
		}
	}
	
}
