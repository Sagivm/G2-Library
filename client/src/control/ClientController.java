package control;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import entity.GeneralMessages;
import entity.Validate;
import entity.Login;
import entity.Message;
import entity.Register;
import entity.ScreensInfo;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.fxml.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * ClientController called by the GUI, create this instance and then manage "menu"
 * of events because this controlled registered as handler of all triggers.
 * @author nire
 */
public class ClientController implements ScreensIF {
	/**
	 * Default port for connection.
	 */
	final public static int DEFAULT_PORT = 1337;
	
	/**
	 *  Default hostname for connection.
	 */
	final public static String hostname = "localhost";
	
	
	/**
	 * The IP Address of localhost.
	 */
	final public static String hostnameAddress = "127.0.0.1";
	
	/**
	 *  The IP Address of the server
	 */
	public static String IP_ADDRESS;
	
	/**
	 *  The connection between GUI and the client.
	 */
	static ClientConnectionController clientConnectionController=null;
	
	
	/**
	 * Password field for password.
	 */
	@FXML private PasswordField passField;
	
	
	/**
	 * Text field for username.
	 */
	@FXML private TextField userField;
	
	
	/**
	 * Text field for server ip.
	 */
	@FXML private TextField ServerField;
	
	
	/**
	 * Button for connect action.
	 */
	@FXML private Button connectButton;
	
	
	/**
	 * Button for registration action.
	 */
	@FXML private Button registerButton;
	
	
	

	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#pressedCloseMenu(javafx.event.ActionEvent)
	 */
	public void pressedCloseMenu(ActionEvent event)
	{
		Platform.exit();
		System.exit(0);
	}
	
	
	/** Handler when press "help" in menu.
	 * @param event - Gets the ActionEvent when the function called.
	 */
	@FXML
	public void pressedHelpMenu(ActionEvent event)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Library");
		alert.setHeaderText(null);
		alert.setContentText(GeneralMessages.ABOUT_US);
		alert.showAndWait();//
	}
	
	/** Handler when press "connect" the GUI. this function loads the connect menu.
	 * @param event - gets the ActionEvent when the function called.
	 */
	public void connectButtonPressed(ActionEvent event) throws ConnectException
	{
		IP_ADDRESS = ServerField.getText();
		String username = userField.getText();
		String password = passField.getText();
		
		if (username.equals("") || password.equals("") || IP_ADDRESS.equals(""))
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.EMPTY_FIELDS);
			return;
		}
		
		if (IP_ADDRESS.equals("localhost") || IP_ADDRESS.equals("LOCALHOST"))
			IP_ADDRESS=hostnameAddress;
		
		if (Validate.IPValidate(IP_ADDRESS) == false
		&& !username.equals("") && !password.equals(""))
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.WRONG_IP);
			return;
		}
		
		if (Validate.usernameValidate(username) == false)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.MUST_INCLUDE_ONLY_DIGITS);
			return;
		}
		
		if (password.length() < 5)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.PASSWORD_TOO_SHORT);
			return;
		}
		
		if (clientConnectionController == null)
		clientConnectionController =
		new ClientConnectionController(IP_ADDRESS,DEFAULT_PORT);
		
		
		Login login = new Login(username,password);
		Message message = prepareLogin(ActionType.LOGIN,login);
		
		try {
			clientConnectionController.sendToServer(message);
		} catch (IOException e1) {
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}	
		
			  //itai
			  Platform.runLater(new Runnable() {
					@Override
					public void run() {
							ClientRecv recv_login = new ClientRecv();
							recv_login.start();
							synchronized (recv_login) {
								try{
									recv_login.wait();
								}catch(InterruptedException e){
									e.printStackTrace();
								}
							}
					}});
	return;
	}

	public Message prepareLogin(ActionType type, Login login)
	{
		Message message = new Message();
		message.setType(type);
		ArrayList <String> elementsList = new ArrayList<String>();
		elementsList.add(0,login.getUsername());
		elementsList.add(1,login.getPassword());
		message.setElementsList(elementsList);
		return message;
	}


	/** Handler when press "register". this function forward the guest to
	 * registration form.
	 * @param event - gets the ActionEvent when the function called.
	 */
	public void registerButtonPressed(ActionEvent event) throws IOException
	{
		String IP_ADDRESS = ServerField.getText();
		String username = userField.getText();
		String password = passField.getText();
		
		if (IP_ADDRESS.equals("localhost") || IP_ADDRESS.equals("LOCALHOST"))
			IP_ADDRESS=hostnameAddress;
		
		if (!username.equals("") || !password.equals("")
			|| Validate.IPValidate(IP_ADDRESS) == false	)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.EMPTY_FIELDS_REGISTER);
			return;
		}
		
		if (clientConnectionController == null)
		clientConnectionController =
		new ClientConnectionController(IP_ADDRESS,DEFAULT_PORT);
		
		ScreenController screenController = new ScreenController();
        try {
			screenController.replaceSceneContent(ScreensInfo.REGISTRATION_SCREEN,ScreensInfo.REGISTRATION_TITLE);
			Stage primaryStage = screenController.getStage();
			ScreenController.setStage(primaryStage);
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			primaryStage.show();
			primaryStage.setX(primaryScreenBounds.getMaxX()/2.0 - primaryStage.getWidth()/2.0);
			primaryStage.setY(primaryScreenBounds.getMaxY()/2.0 - primaryStage.getHeight()/2.0);
			 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#backButtonPressed(javafx.event.ActionEvent)
	 */
	@Override
	public void backButtonPressed(ActionEvent event)
	{
		
	}


	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#actionOnError(enums.ActionType, java.lang.String)
	 */
	@Override
	public void actionOnError(ActionType type, String errorCode) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(errorCode);
		alert.showAndWait();
		if (type == ActionType.TERMINATE)
		{
			Platform.exit();
			System.exit(1);
		}
		if (type == ActionType.CONTINUE)
			return;
	}

}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class ClientRecv extends Thread{
	
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
