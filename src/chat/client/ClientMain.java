package chat.client;

// Client Main
public class ClientMain {
	public static void main(String[] args) {
		Client client = new Client("192.168.1.7", 1234, "Dave");
		client.run();
	}
}