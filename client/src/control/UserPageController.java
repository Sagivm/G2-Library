package control;

import java.io.IOException;
import java.util.ArrayList;

import boundry.ClientUI;
import entity.GeneralMessages;
import entity.Message;
import entity.ScreensInfo;
import entity.SearchBookResult;
import entity.SearchUserResult;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class UserPageController implements ScreensIF{

	/**
	 * lable which contains the full name and his username (headline)
	 */
	@FXML private Label userLable;
	
	/**
	 * lable which contains user's username 
	 */
	@FXML private Label usernameLable;
	
	/**
	 * lable which contains user's password
	 */
	@FXML private Label passwordLable;
	
	/**
	 * lable which contains user's first name 
	 */
	@FXML private Label fNameLable;
	
	/**
	 * lable which contains user's last name 
	 */
	@FXML private Label lNameLable;
	
	/**
	 * check box which says whether user is blocked or not 
	 */
	@FXML private CheckBox blockedCheckBox;
	
	/**
	 * lable which contains user's account status
	 */
	@FXML private Label accountStatusLable;
	
	/**
	 * lable which contains user's account type
	 */
	@FXML private Label accountTypeLable;
	
	/**
	 * credits lable
	 */
	@FXML private Label creditsLable;
	
	/**
	 * lable which contains user's left credits 
	 */
	@FXML private Label crdtsLable;
	
	/**
	 * end of subscription date lable
	 */
	@FXML private Label endSubscroptionLable;
	
	/**
	 * lable which contains user's end of subscription date
	 */
	@FXML private Label eosTextLable;
	
	/**
	 * tab pane which contains users's details 
	 */
	@FXML private TabPane userTabPane;
	
	/**
	 * tab to show edit user for librarian
	 */
	@FXML private Tab editUserLibrarianTab;
	
	/**
	 * tab to show user's report
	 */
	@FXML private Tab userReportTab;
	
	/**
	 * anchor pane which contains edit user details page for librarian
	 */
	@FXML private AnchorPane editUserLibrarianContent;
	
	/**
	 * anchor pane which contains show user's report page for
	 */
	@FXML private AnchorPane userReportContent;
	
	/**
	 * image of user
	 */
	@FXML private ImageView userImageView;
	
	/**
	 * block user button for manager
	 */
	@FXML private Button blockButton;
	
	/**
	 * unblock user button for manager
	 */
	@FXML private Button unblockButton;
	

	
		/**
	 * holds user's information to display
	 */
	public static SearchUserResult searchedUserPage;
	
	
	/**
	 * static reference of user home page.
	 */
	private static HomepageUserController userMain;
	
	

	/**
	 * static reference of librarian home page.
	 */
	private static HomepageLibrarianController librarianMain;
	
	/**
	 * static reference of manager home page.
	 */
	private static HomepageManagerController managerMain;
	
	
	/** initializing data when page comes up
	 * @author itain
	 */
	@FXML
	public void initialize()
	{ 
		SearchUserController.updateSearchUserResults=0;
		unblockButton.setVisible(false);
		userLable.setText(searchedUserPage.getFirstName()+" "+searchedUserPage.getLastName()+ " ("+searchedUserPage.getUsername()+")");
		Image userImagePath = new Image("/img/user.png");
		userImageView.setImage(userImagePath);
		usernameLable.setText(searchedUserPage.getUsername());
		passwordLable.setText(searchedUserPage.getPassword());
		fNameLable.setText(searchedUserPage.getFirstName());
		lNameLable.setText(searchedUserPage.getLastName());
		if(searchedUserPage.getIsBlocked()=="YES")
		{
			blockedCheckBox.setSelected(true);
			blockButton.setVisible(false);
			unblockButton.setVisible(true);
		}
		blockedCheckBox.setDisable(true);
		accountStatusLable.setText(searchedUserPage.getAccountStatus());
		accountTypeLable.setText(searchedUserPage.getAccountType());
		creditsLable.setText(searchedUserPage.getCredits());
		endSubscroptionLable.setText(searchedUserPage.getEndSubscription());
		
		
		if(!searchedUserPage.getAccountType().equals("Yearly") && !searchedUserPage.getAccountType().equals("Monthly"))
		{
			creditsLable.setVisible(false);
			crdtsLable.setVisible(false);
			endSubscroptionLable.setVisible(false);
			eosTextLable.setVisible(false);
		}
		
		if(ClientUI.getTypeOfUser()=="Librarian")
		{
			unblockButton.setVisible(false);
			blockButton.setVisible(false);
			userTabPane.getTabs().remove(2); //remove user report tab
		}
		else if(ClientUI.getTypeOfUser()=="Manager")
			userTabPane.getTabs().remove(1); //remove edit user tab
			
		
		//BookReviewsController bookReview = new BookReviewsController();
		try {
			loadEditUserLibrarian();
			loadUserReport();  //SAGIV -add user report
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/** when page comes up, loads edit user page for librarian
	 * @author itain
	 */
	@FXML
	public void loadEditUserLibrarian() throws IOException {
				try {
					if(editUserLibrarianContent.getChildren().size()>0)
						editUserLibrarianContent.getChildren().remove(0);
					Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.USER_PAGE_LIBRARIAN_SCREEN));
					editUserLibrarianContent.getChildren().add(root);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

	
	
	/**
	 * /** when page comes up, loads user's report page
	 * @author itain
	 * @throws IOException
	 */
	@FXML
	public void loadUserReport() throws IOException {
				try {
					if(userReportContent.getChildren().size()>0)
						userReportContent.getChildren().remove(0);
					Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.USER_REPORT)); // SAGIV - CHANGE HERE USER REPORT SCREEN
					userReportContent.getChildren().add(root);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	
	
	/** When pressed, takes user to search users result page.
	 * @author itain
	 * @param event - Gets event.
	 */
	@FXML
	public void backButtonPressed(ActionEvent event)
	{
		
		if(ClientUI.getTypeOfUser()=="Librarian")
    	{
			SearchUserController.updateSearchUserResults=2;
        	if (librarianMain == null)
        		librarianMain = new HomepageLibrarianController();
        	librarianMain.setPage(ScreensInfo.SEARCH_USER_RESULTS_SCREEN);
    	}
    	else if(ClientUI.getTypeOfUser()=="Manager")
    	{
    		SearchUserController.updateSearchUserResults=1;
        	if (managerMain == null)
        		managerMain = new HomepageManagerController();
        	managerMain.setPage(ScreensInfo.SEARCH_USER_RESULTS_SCREEN);
    	}
		
		ScreenController screenController = new ScreenController();
		try{
			if(ClientUI.getTypeOfUser()=="Librarian")
				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_LIBRARIAN_SCREEN,ScreensInfo.HOMEPAGE_LIBRARIAN_TITLE);						
			else if(ClientUI.getTypeOfUser()=="Manager")
				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_MANAGER_SCREEN,ScreensInfo.HOMEPAGE_MANAGER_TITLE);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	
	

	@Override
	public void pressedCloseMenu(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionOnError(ActionType type, String errorCode) {
		// TODO Auto-generated method stub
		
	}
	
	/** When pressed, blocks user from system.
	 * @author itain
	 * @param event - Gets event.
	 */
	@FXML
	public void blockButtonPressed(ActionEvent event) 
	{		
		String isBlocked="1";
		SearchUserResult user = new SearchUserResult(UserPageController.searchedUserPage.getUsername(), UserPageController.searchedUserPage.getFirstName(), UserPageController.searchedUserPage.getLastName(), "", "", isBlocked, "", "", "");
		Message message = prepareEditUser(ActionType.EDIT_USER_MANAGER,user);
		
		try {
			ClientController.clientConnectionController.sendToServer(message);
		} catch (IOException e) {
					
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
	}
	
	/** When pressed, unblocks user from system.
	 * @author itain
	 * @param event - Gets event.
	 */
	@FXML
	public void unblockButtonPressed(ActionEvent event) 
	{		
		String isBlocked="0";
		SearchUserResult user = new SearchUserResult(UserPageController.searchedUserPage.getUsername(), UserPageController.searchedUserPage.getFirstName(), UserPageController.searchedUserPage.getLastName(), "", "", isBlocked, "", "", "");
		Message message = prepareEditUser(ActionType.EDIT_USER_MANAGER,user);
		
		try {
			ClientController.clientConnectionController.sendToServer(message);
		} catch (IOException e) {
					
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
	}
	
	
	/** This function prepare message that will be send to the server with arraylist,
	 * and the action.
	 * @author itain
	 * @param type - Gets the type of the action
	 * @param user - Gets the class with the user information.
	 * @return - message that will send to server.
	 */
	public Message prepareEditUser(ActionType type, SearchUserResult user)
	{
		Message message = new Message();
		message.setType(type);
		ArrayList <String> elementsList = new ArrayList<String>();
		elementsList.add(user.getUsername());
		elementsList.add(user.getFirstName());
		elementsList.add(user.getLastName());
		elementsList.add(user.getIsBlocked());
		message.setElementsList(elementsList);
		return message;
	}

}


