package chat.common;

import java.io.Serializable;

public class ChatMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MessageType type;
	private String message;

	public ChatMessage(MessageType type, String message) {
		this.type = type;
		this.message = message;
	}
	
	// Overload constructor
	public ChatMessage(MessageType type) {
//		this.type = type;
//		this.message = null;
		
		this(type, null);
	}

	public MessageType getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	
}
