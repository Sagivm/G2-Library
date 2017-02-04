package control;

import java.io.IOException;
import java.util.ArrayList;

import boundry.ClientUI;
import entity.Login;
import entity.Message;
import entity.ScreensInfo;
import entity.User;
import entity.Worker;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * HomepageLibrarianController is the controller after user logged in.
 * this the main menu of the librarian, here he manages the actions in system.
 * librarian got advanced premissions.
 * @author nire
 */
public class HomepageLibrarianController implements ScreensIF {

	
	/**
	 * page gets the screen to load in the content pane.
	 */
	private static String page = null;
	
	/**
	 * the main content frame.
	 */
	@FXML private AnchorPane content;
	
	/**
	 * save the connected librarian
	 */
	private static Worker connectedLibrarian;
	
	/**
	 * The user full name will shown in this label.
	 */
	@FXML private Label usernameLabel;


	
	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#backButtonPressed(javafx.event.ActionEvent)
	 */
	@Override
	public void backButtonPressed(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#pressedCloseMenu(javafx.event.ActionEvent)
	 */
	@Override
	public void pressedCloseMenu(ActionEvent event) throws IOException {
		try{
		logout();
		Platform.exit();
		System.exit(0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	
	 /**
	 * Choose which screen to load in the content pane.
	 */
	@FXML
    public void initialize() {
		if (page != null)
		 {
			try {
				loadPage(page);
				page=null;
			} catch (IOException e) {
				e.printStackTrace();
			} 
		 }
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(ClientUI.testMode=false)
				usernameLabel.setText("Logged As: " + connectedLibrarian.getFirstname() + " " + connectedLibrarian.getLastname());
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#actionOnError(enums.ActionType, java.lang.String)
	 */
	@Override
	public void actionOnError(ActionType type, String errorCode) {
		// TODO Auto-generated method stub
		
	}
	
	/** Handler when pressed "Logout". this function log out the current librarian.
	 * @param event gets the ActionEvent when the function called.
	 * @throws IOException IO exception.
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
	
	
	/** This function log out the current librarian from the server.
	 * @throws IOException IO exception.
	 */
	public void logout() throws IOException
	{
		try{
			ClientController clientCtrl = new ClientController();
			if (clientCtrl.clientConnectionController == null)
				clientCtrl.clientConnectionController = new ClientConnectionController(clientCtrl.IP_ADDRESS,clientCtrl.DEFAULT_PORT);
			Login login = new Login(connectedLibrarian.getId(),connectedLibrarian.getPassword());
			Message message = prepareLogout(ActionType.LOGOUT,login);
			clientCtrl.clientConnectionController.sendToServer(message);
				  //itai
				  Platform.runLater(new Runnable() {
						@Override
						public void run() {
								HomepageLibrarianRecv recv_logout = new HomepageLibrarianRecv();
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
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Send log out message to the server.
	 * @param type The action type of the message that will pass to the server.
	 * @param login The parameter that will pass to the server.
	 * @return The message that will pass to the server.
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
	
	/** Return the connected worker.
	 * @return the connected librarian.
	 */
	public static Worker getConnectedlibrarian()
	{
		return connectedLibrarian;
	}
	
	/**Setter of the connected librarian.
	 * @param connectedUser Set the connected librarian.
	 */
	public void setConnectedlibrarian(Worker connectedlibrarian)
	{
		this.connectedLibrarian = connectedlibrarian;
	}
	
	
	/**
	 * When pressed, load book management screen. 
	 * @param event The event when button pressed.
	 * @throws IOException IO exception.
	 */
	@FXML
	public void bookManagmentButtonPressed(ActionEvent event) throws IOException {
		Platform.runLater(() -> {
		try {
			while(content.getChildren().size()>0)
				content.getChildren().remove(0);
			Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.HOMEPAGE_BOOK_MANAGEMENT));
			content.getChildren().add(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		});
	}
	
	
	/** Handler when pressed "Pending Registration requests". this function open the Pending Registration requests form.
	 * @param event Gets the ActionEvent when the function called.
	 * @throws IOException IO exception.
	 */
	@FXML
	public void pendingRegistrationButtonPressed(ActionEvent event) throws IOException {
		Platform.runLater(() -> {
		try {
			while(content.getChildren().size()>0)
				content.getChildren().remove(0);
			Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.HOMEPAGE_PENDING_REGISTRATION_SCREEN));
			content.getChildren().add(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		});
	}
	
	/** Handler when pressed "Pending Reviews requests". this function open the Pending Reviews requests form.
	 * @param event The event when button pressed.
	 * @throws IOException IO exception.
	 */
	@FXML
	public void pendingReviewsButtonPressed(ActionEvent event) throws IOException {
		try {
			if(content.getChildren().size()>0)
				content.getChildren().remove(0);
			Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.PENDING_REVIEWS_SCREEN));
			content.getChildren().add(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Handler when pressed "search book". this function open the search book
	 * form.
	 * @param event Gets the ActionEvent when the function called.
	 * @throws IOException IO exception.
	 */
	@FXML
	public void searchBookButtonPressed(ActionEvent event) throws IOException {
		try {
			if(content.getChildren().size()>0)
				content.getChildren().remove(0);
			Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.SEARCH_BOOK_SCREEN));
			content.getChildren().add(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//
	@FXML
	public void userMngButtonPressed(ActionEvent event) throws IOException {
		loadPage(ScreensInfo.SEARCH_USER_SCREEN);
	}
	
	@FXML
	public void searchWorkerButtonPressed(ActionEvent event) throws IOException {
		loadPage(ScreensInfo.SEARCH_WORKER_SCREEN);
	}
	
	/**
	 * when pressed, load pending account requests list screen.
	 * @param event The event when button pressed.
	 * @throws IOException IO exception.
	 */
	@FXML
	public void pendingAccountButtonPressed(ActionEvent event) throws IOException {
		loadPage(ScreensInfo.PENDING_ACCOUNT_SCREEN);
	}
	//
	/**
	 * this method load the page to the content AnchorPane.
	 * @param screenPath The path of the next screen.
	 * @throws IOException IO exception.
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
	

	
	
	/** Getter for the main AnchorPane. All the screens will shown in this AnchorPane.
	 * @return The AnchorPane of the screen.
	 */
	public AnchorPane getContent()
	{
		return content;
	}
	
	/** Setter for page.
	 * @param page The page that load in the AnchorPane.
	 */
	public static void setPage(String pageToLoad)
	{
		page = pageToLoad;
	}
	
	/** Getter for page.
	 * @return The page that load in the AnchorPane.
	 */
	public String getPage()
	{
		return page;
	}

}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class HomepageLibrarianRecv extends Thread{
	
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