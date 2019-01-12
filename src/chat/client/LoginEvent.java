package chat.client;

import java.util.EventObject;

public class LoginEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private String userName;
	private int port;
	private String host;
	
	public LoginEvent(Object source) {
		super(source);
	}
	
	public LoginEvent(Object source, String userName, int port, String host) {
		super(source);
		
		this.userName = userName;
		this.port = port;
		this.host = host;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
