package chat.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class TextInputPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextArea textInputArea;
	private InputMap inputMap;
	private ActionMap actionMap;
	private JButton sendButton;
	
	private TextInputListener textInputListener;
	
	public TextInputPanel() {		
		textInputArea = new JTextArea();
		sendButton = new JButton("Send");
		
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessagePerformed();
			}
		});
		
		initializeInputMapKeyStrokes();
		
		actionMap.put("text-submit", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessagePerformed();
			}
		});
		
		// Border of this panel
		Border innerBorder = BorderFactory.createTitledBorder("Your Message:");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);	// Some space around the edges
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		setLayout(new BorderLayout());
		
		// Puts JTextArea in a scrolling panel
		add(new JScrollPane(textInputArea), BorderLayout.CENTER);
		add(sendButton, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void sendMessagePerformed() {
		String message = textInputArea.getText();
		TextInputEvent ev = new TextInputEvent(this, message);
		
		if(textInputListener != null) {
			textInputListener.textInputEventOccurred(ev);
		}
		
		textInputArea.setText(null);
	}
	
	private void initializeInputMapKeyStrokes() {
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		
		inputMap = textInputArea.getInputMap();
		
		inputMap.put(enter, "text-submit");
		inputMap.put(shiftEnter, "insert-break");
		
		actionMap = textInputArea.getActionMap();
	}
	
	public void setTextInputListener(TextInputListener listener) {
		textInputListener = listener;
	}
}
