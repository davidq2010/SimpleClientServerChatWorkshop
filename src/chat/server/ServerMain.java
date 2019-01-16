package chat.server;

public class ServerMain {
	public static void main(String[] args) {
		Server server = new Server(1234);
		
		try {
			server.start();
		} catch(RuntimeException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
	}
}
