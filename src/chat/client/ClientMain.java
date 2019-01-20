package chat.client;

import java.io.IOException;
import java.util.Scanner;

// Client Main
public class ClientMain {
	public static void main(String[] args) {
			Client client = new Client("localhost", 1234, "Bob");
			client.start();
			
			Scanner userInput = new Scanner(System.in);
			
			while(true) {
				System.out.println("> ");
				String message = userInput.nextLine();
				
				try {
					client.sendMessage(message);
				} catch (IOException e) {
					System.err.println("ERROR: Could not send message");
				}
				
				if(message.equalsIgnoreCase("LOGOUT"))
					break;
			}
			
			client.closeResources();
			userInput.close();
	}
}