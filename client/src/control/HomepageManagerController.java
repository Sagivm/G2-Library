package control;


import java.io.IOException;
import java.util.ArrayList;

import entity.Login;
import entity.Message;
import entity.ScreensInfo;
import entity.Worker;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
//
public class HomepageManagerController implements ScreensIF {
	
	/**
	 * page gets the screen to load in the content pane.
	 */
	private static String page = null;
	
	/**
	 * the main content frame.
	 */
	@FXML private AnchorPane content;
	
	/**
	 * save the connected manager
	 */
	private static Worker connectedManager;
	
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
				usernameLabel.setText("Logged As: " + connectedManager.getFirstname() + " " + connectedManager.getLastname());
			}
		});
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
	
	/** Handler when pressed "Logout". this function log out the current manager.
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
	
	
	/** This function log out the current manager from the server.
	 * @throws IOException
	 */
	public void logout() throws IOException
	{
		try{
			ClientController clientCtrl = new ClientController();
			if (clientCtrl.clientConnectionController == null)
				clientCtrl.clientConnectionController = new ClientConnectionController(clientCtrl.IP_ADDRESS,clientCtrl.DEFAULT_PORT);
			Login login = new Login(connectedManager.getId(),connectedManager.getPassword());
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
							HomepageManagerRecv recv_logout = new HomepageManagerRecv();
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

	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#actionOnError(enums.ActionType, java.lang.String)
	 */
	@Override
	public void actionOnError(ActionType type, String errorCode) {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Getter of the connected manager.
	 * @return
	 */
	public static Worker getConnectedManager()
	{
		return connectedManager;
	}
	
	/**Setter of the connected liberian.
	 * @param connectedUser - Set the connected liberian.
	 */
	public void setConnectedManager(Worker connectedManager)
	{
		this.connectedManager = connectedManager;
	}
	
	/** Handler when pressed "Pending Reviews requests". this function open the Pending Reviews requests form.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void pendingReviewsButtonPressed(ActionEvent event) throws IOException {
		loadPage(ScreensInfo.PENDING_REVIEWS_SCREEN);
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
		try {
			if(content.getChildren().size()>0)
				content.getChildren().remove(0);
			Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.SEARCH_BOOK_SCREEN));
			content.getChildren().add(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	@FXML
	public void blockUserButtonPressed(ActionEvent event) throws IOException {
		loadPage(ScreensInfo.SEARCH_USER_SCREEN);
	}
	
	@FXML
	public void searchWorkerButtonPressed(ActionEvent event) throws IOException {
		loadPage(ScreensInfo.SEARCH_WORKER_SCREEN);
	}
	
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
	
}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class HomepageManagerRecv extends Thread{
	
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
