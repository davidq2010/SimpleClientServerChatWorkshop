package chat.server;

public class ServerMain {
	public static void main(String[] args) {
		Server server = new Server(1234);
		server.start();
	}
}
