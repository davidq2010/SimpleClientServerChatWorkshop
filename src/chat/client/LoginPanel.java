package chat.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class LoginPanel extends JPanel implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	private JLabel userLabel;
	private JTextField userField;
	
	private JLabel portLabel;
	private JTextField portField;
	
	private JLabel hostLabel;
	private JTextField hostField;
	
	private LoginListener loginListener;
	
	private JButton enterButton;
	
	public LoginPanel() {
		Dimension dim = getPreferredSize();
		//dim.width = 250;
		setPreferredSize(dim);
		
		userLabel = new JLabel("User Name: ");
		portLabel = new JLabel("Port Number: ");
		hostLabel = new JLabel("Host IP: ");
		
		userField = new JTextField(10);
		portField = new JTextField(10);
		hostField = new JTextField(10);
		
		enterButton = new JButton("ENTER");
		
		// When button is pressed, this ActionListener will call actionPerformed
		enterButton.addActionListener(this);
		userField.addKeyListener(this);
		portField.addKeyListener(this);
		hostField.addKeyListener(this);
		
		Border innerBorder = BorderFactory.createTitledBorder("Login fields Person");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);	// Some space around the edges
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		gc.weightx = 1;
		gc.weighty = 1;
		
		// First Row
		gc.anchor = GridBagConstraints.LINE_END;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 0, 5);	// Indentation after userLabel
		add(userLabel, gc);
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 0;
		gc.insets = new Insets(0,0,0,0);
		add(userField, gc);
		
		// Second Row
		gc.anchor = GridBagConstraints.LINE_END;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.insets = new Insets(0, 0, 0, 5);	// Indentation after hostLabel
		add(hostLabel, gc);
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 1;
		gc.insets = new Insets(0,0,0,0);
		add(hostField, gc);
		
		// Third Row
		gc.anchor = GridBagConstraints.LINE_END;
		gc.gridx = 0;
		gc.gridy = 2;
		gc.insets = new Insets(0, 0, 0, 5);	// Indentation after portLabel
		add(portLabel, gc);
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 2;
		gc.insets = new Insets(0,0,0,0);
		add(portField, gc);
		
		// Fourth Row
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 1;
		gc.gridy = 3;
		gc.insets = new Insets(0,0,0,0);
		add(enterButton, gc);
	}
	
	public void setLoginListener(LoginListener listener) {
		loginListener = listener;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			enterActionPerformed();		
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		enterActionPerformed();		
	}
	
	public void enterActionPerformed() {
		String userName = userField.getText();
		int port;
		try {
			port = Integer.parseInt(portField.getText());
		} catch (NumberFormatException exception) {
			System.err.println("ERROR: Invalid port value; must be a number.");
			// TODO: Make a J-something pop up saying error!
			return;
		}
		String host = hostField.getText();
		
		LoginEvent ev = new LoginEvent(this, userName, port, host);
		
		// Implement loginListener interface in MainFrame
		if(loginListener != null) {
			loginListener.loginEventOccurred(ev);
		}
	}
}
