package chat.client;

// Client Main
public class ClientMain {
	public static void main(String[] args) {
		try {
			new Client("localhost", 1234, "David");
		} catch(RuntimeException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
	}
}