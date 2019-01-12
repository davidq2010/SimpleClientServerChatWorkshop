package chat.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientChatMainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private TextInputPanel textInputPanel;
	private ReadTextPanel readTextPanel;
	
	public ClientChatMainFrame(Client client) {
		super("ClientChatGUI");
		
		client.setClientChatMainFrame(this);
		client.run();
		
		setLayout(new BorderLayout());	// BorderLayout is a visual layout manager

		textInputPanel = new TextInputPanel();
		readTextPanel = new ReadTextPanel();
		
		textInputPanel.setTextInputListener(new TextInputListener() {
			
			@Override
			public void textInputEventOccurred(TextInputEvent e) {
				String message = e.getMessage();
				
				// TODO: Send message to client
				client.sendMessage(message);
			}
		});
		
		add(readTextPanel, BorderLayout.CENTER);
		add(textInputPanel, BorderLayout.SOUTH);
		
		setSize(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);		
	}
	
	public void writeTextToReadPanel(String message) {
		readTextPanel.appendText(message + "\n");
	}
	
	public void sendWarningMessage(String warningMessage) {
		JOptionPane.showMessageDialog(this, warningMessage);
	}
}
