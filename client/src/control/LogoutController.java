package control;

import java.io.IOException;
import java.util.ArrayList;

import entity.Login;
import entity.Message;
import entity.User;
import enums.ActionType;
import javafx.application.Platform;

/**
 * LogoutController is the controller that responsible to log out the user.
 * @author ork
 */
public class LogoutController {
	
	
	/**
	 * save the connected user
	 */
	private static User connectedUser;
	
	/** This function log out the current user from the server.
	 * @throws IOException IO exception.
	 */
	public void logout() throws IOException
	{
		try{
			
			ClientController clientCtrl = new ClientController();
			//System.out.println("100");
			if (clientCtrl.clientConnectionController == null)
				clientCtrl.clientConnectionController = new ClientConnectionController(clientCtrl.IP_ADDRESS,clientCtrl.DEFAULT_PORT);
			//System.out.println("111");
			Login login = new Login(connectedUser.getId(),connectedUser.getPassword());
			//Login login = new Login("123456789","123456");
			//System.out.println("123");
			Message message = prepareLogout(ActionType.LOGOUT,login);
			//System.out.println("122");
			clientCtrl.clientConnectionController.sendToServer(message);
			//System.out.println("133");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		  //itai
		  Platform.runLater(new Runnable() {
				@Override
				public void run() {
						LogoutRecv recv_logout = new LogoutRecv();
						recv_logout.start();
						synchronized (recv_logout) {
							try{
								recv_logout.wait();
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
				}});
	}
	
	/** Send log out message to the server.
	 * @param type The action type of the message that will passed to the server.
	 * @param login The parameter that will passed to the server.
	 * @return The message that will passed to the server.
	 */
	public Message prepareLogout(ActionType type, Login login)
	{
		Message message = new Message();
		message.setType(type);
		ArrayList <String> elementsList = new ArrayList<String>();
		elementsList.add(0,login.getUsername());
		elementsList.add(1,login.getPassword());
		message.setElementsList(elementsList);
		return message;
	}
	
	/**
	 * @return the connected user.
	 */
	public User getConnectedUser()
	{
		return connectedUser;
	}
	
	/**Setter of the connected user.
	 * @param connectedUser - Set the connected user.
	 */
	public void setConnectedUser(User connectedUser)
	{
		this.connectedUser = connectedUser;
	}

}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class LogoutRecv extends Thread{
	
	/**
	 * Get true after receiving values from DB.
	 */
	public static boolean canContinue = false;
	
	@Override
	public void run() {
		synchronized (this) {
        	while(canContinue == false)
    		{
        		System.out.print("");
    		}
        	canContinue = false;
			notify();
		}
	}
	
}

