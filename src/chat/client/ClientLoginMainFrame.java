package chat.client;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientLoginMainFrame extends JFrame implements LoginListener {
	
	private static final long serialVersionUID = 1L;
	
	private LoginPanel loginPanel;
	
	private boolean loginWorked;
	private String errorMessage;
	
	public ClientLoginMainFrame() {
		super("ClientLoginGUI");	// Call JFrame constructor
		
		setLayout(new BorderLayout());	// BorderLayout is a visual layout manager

		loginPanel = new LoginPanel();
		
		loginPanel.setLoginListener(this);
		
		add(loginPanel, BorderLayout.CENTER);
		
		setSize(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);		
	}
	
	public void setClientLoginWorked(boolean loginWorked) {
		this.loginWorked = loginWorked;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public void loginEventOccurred(LoginEvent e) {
		String userName = e.getUserName();
		String host = e.getHost();
		int port = e.getPort();
		
		System.out.println("UserName: " + userName);
		System.out.println("Host: " + host);
		System.out.println("Port: " + port);
		
		// Instantiate client
		Client client = new Client(host, port, userName, this);
		
		if(loginWorked) {
			new ClientChatMainFrame(client);
			dispose();
		}
		else {
			JOptionPane.showMessageDialog(this, errorMessage);
		}
	}
}
