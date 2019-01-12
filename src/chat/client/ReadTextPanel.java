package chat.client;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ReadTextPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextArea readTextArea;
	
	public ReadTextPanel() {
		readTextArea = new JTextArea();
		readTextArea.setEnabled(false);
		readTextArea.setDisabledTextColor(Color.black);
		
		setLayout(new BorderLayout());
		
		// Puts JTextArea in a scrolling panel
		add(new JScrollPane(readTextArea), BorderLayout.CENTER);
	}

	public void appendText(String text) {
		readTextArea.append(text);
	}
}
