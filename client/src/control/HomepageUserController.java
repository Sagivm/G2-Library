package control;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.net.URL;
//import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import entity.Author;
import entity.Book;
import entity.GeneralMessages;
import entity.Login;
import entity.Message;
import entity.ScreensInfo;
import entity.User;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * HomepageUserController is the controller after user logged in. this the main menu
 * of the user, here he manages the actions in system.
 * 
 * @author ork
 */
public class HomepageUserController implements ScreensIF {

	/**
	 * page gets the screen to load in the content pane.
	 */
	private static String page = null;
	
	/**
	 * the main content frame
	 */
	@FXML private AnchorPane content;
	
	/**
	 * Button for subscription payment.
	 */
	@FXML private Button btnPayForSubscription;
	
	/**
	 * The user full name will shown in this label.
	 */
	@FXML private Label usernameLabel;

	/**
	 * The user subscription.
	 */
	@FXML private Label lblSubscribed;
	
	/**
	 * The subscription expiration date.
	 */
	@FXML private Label lblExpDate;
	
	/**
	 * The subscription credits.
	 */
	@FXML private Label lblCredits;
	
	/**
	 * save the connected user
	 */
	private static User connectedUser;
	
	/**
	 * saves if the user is subscribed or not.
	 */
	public static boolean isSubscribed=false;
	
	/**
	 * saves the subscription expiration date.
	 */
	public static String expDate = "";
	
	/**
	 * saves the credits.
	 */
	public static boolean success = false;
	
	/**
	 * saves the subscription info.
	 */
	private static ArrayList<String> subscription;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.ScreensIF#backButtonPressed(javafx.event.ActionEvent)
	 */
	@Override
	public void backButtonPressed(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.ScreensIF#pressedCloseMenu(javafx.event.ActionEvent)
	 */
	@Override
	public void pressedCloseMenu(ActionEvent event) throws IOException{
		try{
			logout();
			Platform.exit();
			System.exit(0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.ScreensIF#actionOnError(enums.ActionType,
	 * java.lang.String)
	 */
	@Override
	public void actionOnError(ActionType type, String errorCode) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(errorCode);
		alert.showAndWait();
		if (type == ActionType.TERMINATE) {
			Platform.exit();
			System.exit(1);
		}
		if (type == ActionType.CONTINUE)
			return;
	}

	/**
	 * Handler when pressed "search book". this function open the search book
	 * form.
	 * 
	 * @param event - gets the ActionEvent when the function called.
	 * @throws IOException
	 */
	@FXML
	public void searchBookButtonPressed(ActionEvent event) throws IOException {
		loadPage(ScreensInfo.SEARCH_BOOK_SCREEN);
	}

	/**
	 * Handler when pressed "set Account Type". this function open the account
	 * type request form.
	 * 
	 * @param event
	 *            - gets the ActionEvent when the function called.
	 * @throws IOException
	 */
	@FXML
	public void settingsButtonPressed(ActionEvent event) throws IOException {
		loadPage(ScreensInfo.HOMEPAGE_SET_ACCOUNT_TYPE_SCREEN);
	}	

	/** Handler when pressed "Logout". this function log out the current user.
	 * @param event - gets the ActionEvent when the function called.
	 * @throws IOException
	 */
	@FXML
	public void logoutButtonPressed(ActionEvent event) throws IOException{    
		try{
			logout();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@FXML
	public void messageButtonPressed(ActionEvent event) {    
		try {
			loadPage("/boundry/MessagesUI.fxml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/** This function log out the current user from the server.
	 * @throws IOException
	 */
	public void logout() throws IOException
	{
		try{
			ClientController clientCtrl = new ClientController();
			if (clientCtrl.clientConnectionController == null)
				clientCtrl.clientConnectionController = new ClientConnectionController(clientCtrl.IP_ADDRESS,clientCtrl.DEFAULT_PORT);
			Login login = new Login(connectedUser.getId(),connectedUser.getPassword());
			Message message = prepareLogout(ActionType.LOGOUT,login);
			clientCtrl.clientConnectionController.sendToServer(message);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		  //itai
		  Platform.runLater(new Runnable() {
				@Override
				public void run() {
						HomepageUserRecv recv_logout = new HomepageUserRecv();
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
	 * @param type
	 * @param login
	 * @return
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
     * Print the name of the user on the screen.
     */
    @FXML
    private void initialize() {
		if (page != null)
		 {
			try {
				loadPage(page);
				page=null;
			} catch (IOException e) {
				e.printStackTrace();
			} 
		 }
		btnPayForSubscription.setDisable(true);
		ArrayList<String> userId = new ArrayList<>();
		userId.add(connectedUser.getId());
		Message message = prepareGetFromSQL(ActionType.CHECK_ACCOUNT_TYPE,userId);
		try {
			ClientController.clientConnectionController.sendToServer(message);
		} catch (IOException e) {	
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
			  //itai
			  Platform.runLater(new Runnable() {
					@Override
					public void run() {
							HomepageUserRecv recv_checkAccountType = new HomepageUserRecv();
							recv_checkAccountType.start();
							synchronized (recv_checkAccountType) {
								try{
									recv_checkAccountType.wait();
								}catch(InterruptedException e){
									e.printStackTrace();
								}
							}
					}});
		
		
    	Platform.runLater(new Runnable() {
    		@Override
    		public void run() {
    			usernameLabel.setText("Logged As: " + connectedUser.getFirstname() + " " + connectedUser.getLastname());
    		
    			lblSubscribed.setVisible(false);
    			lblExpDate.setVisible(false);
    			lblCredits.setVisible(false);
    			
    			if(success == true)
    			{
    				if( subscription.get(1).equals("PendingPayment") & (subscription.get(0).equals("Monthly") || subscription.get(0).equals("Yearly") ))
    					btnPayForSubscription.setDisable(false);
    				else if(subscription.get(0).equals("Monthly") || subscription.get(0).equals("Yearly"))
    				{
    					lblExpDate.setText("Exp.Date: " + subscription.get(3));
    					lblCredits.setText("Credits: " + subscription.get(2) + " \u20AA");
    					
    	    			lblSubscribed.setVisible(true);
    	    			lblExpDate.setVisible(true);
    	    			lblCredits.setVisible(true);
    				}
    				else
    				{
    					lblSubscribed.setText("Unsubscribed account");
    					lblSubscribed.setVisible(true);
    				}
    			}
    			
    		}
    	});
    }
	
	/**
	 * @return the connected user.
	 */
	public static User getConnectedUser()
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

	/**
	 * This function choose what to display the user.
	 * 
	 * @param type
	 *            - Gets the type of action after display.
	 * @param message
	 *            - Gets the message to display in popup.
	 */
	public void actionToDisplay(ActionType type, String message) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		if (type == ActionType.TERMINATE) {
			Platform.exit();
			System.exit(1);
		}
		if (type == ActionType.CONTINUE)
			return;
	}
	
	/**
	 * this method load the page to the content AnchorPane.
	 * @param screenPath
	 * @throws IOException
	 */
	@FXML
	public void loadPage(String screenPath) throws IOException {
				try {
					if(content.getChildren().size()>0)
						content.getChildren().remove(0);
					Parent root = FXMLLoader.load(getClass().getResource(screenPath));
					content.getChildren().add(root);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	
	/** Setter for page.
	 * @param page
	 */
	public static void setPage(String pageToLoad)
	{
		page = pageToLoad;
	}
	
	/** Getter for page.
	 * @return
	 */
	public String getPage()
	{
		return page;
	}
//check
	public void myBooksButtonPressed()
	{
		try {
			loadPage(ScreensInfo.MY_BOOKS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * when pressed, load the external payment company screen.
	 * @throws Exception
	 */
	public void btnPayForSubscriptionPressed() throws Exception
	{	
		try 
		{
			ScreenController screenController = new ScreenController();
        	ExternalPaymentController extPayment = new ExternalPaymentController();
        	extPayment.setProduct(subscription.get(0).toString() + " Subscription");
        	if(subscription.get(0).toString().equals("Monthly"))
        	{
        		extPayment.setPrice("250");
        		extPayment.setAction(4);	//Buy monthly subscription.
        	}
        	else
        	{
        		extPayment.setPrice("2800");
        		extPayment.setAction(5);	//Buy yearly subscription.
        	}
        	//extPayment.searchedBookPage = searchedBookPage;
        	        			        	
			screenController.replaceSceneContent(ScreensInfo.EXTERNAL_PAYMENT_SCREEN,ScreensInfo.EXTERNAL_PAYMENT_TITLE);
			Stage primaryStage = screenController.getStage();
			ScreenController.setStage(primaryStage);
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			primaryStage.show();
			primaryStage.setX(primaryScreenBounds.getMaxX()/2.0 - primaryStage.getWidth()/2.0);
			primaryStage.setY(primaryScreenBounds.getMaxY()/2.0 - primaryStage.getHeight()/2.0);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Gets type of account.
	 * @param type
	 * @param elementList
	 * @return
	 */
	public Message prepareGetFromSQL(ActionType type,ArrayList<String> elementList)
	{
		Message message = new Message();
		message.setType(type);
		message.setElementsList(elementList);
		return message;
	}
	
	/**
	 * Setter of subscription info.
	 * @param subscription
	 */
	public static void setSubscription(ArrayList<String> subs)
	{
		subscription = subs;
	}
	

}

/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class HomepageUserRecv extends Thread{
	
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
