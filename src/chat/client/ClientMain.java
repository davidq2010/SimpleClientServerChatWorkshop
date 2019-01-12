package chat.client;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class ClientMain {
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		//Client client = new Client("192.168.1.3", 1234, "Dave");
		//client.run();
		
		// Should run swing application in new thread to be safe	
		SwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				new ClientLoginMainFrame();		
				//new ClientChatMainFrame();
			}
		});		
	}
}