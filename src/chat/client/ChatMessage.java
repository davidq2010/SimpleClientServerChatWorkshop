package chat.client;

import java.io.Serializable;

public class ChatMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MessageType type;
	String message;
	
	public ChatMessage(MessageType type) {
		this(type, null);
	}
	
	public ChatMessage(MessageType type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public MessageType getMessageType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
}
