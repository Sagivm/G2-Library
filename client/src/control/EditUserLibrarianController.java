package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import boundry.ClientUI;
import entity.GeneralMessages;
import entity.Message;
import entity.Register;
import entity.ScreensInfo;
import entity.SearchBookResult;
import entity.SearchUserResult;
import entity.User;
import entity.Validate;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;

/** EditUserLibrarianController. Responsible to enable a librarian to edit user first name and last name.
 * @author itain
 */
public class EditUserLibrarianController implements ScreensIF{
	
	/**
	 * holds first name of user, editable
	 */
	@FXML private TextField fNameTextField;
	
	/**
	 * holds last name of user, editable
	 */
	@FXML private TextField lNameTextField;
	
	/**
	 * confirm changes
	 */
	@FXML private Button submitButton;
	
	/**
	 * cancel changes
	 */
	@FXML private Button cancelButton;

	
	/**
	 * static reference for user's changes.
	 */
	public static SearchUserResult userDetailsToChange;
	
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
		fNameTextField.setText(UserPageController.searchedUserPage.getFirstName().trim());
		lNameTextField.setText(UserPageController.searchedUserPage.getLastName().trim());
		
		fNameTextField.setEditable(true);
		lNameTextField.setEditable(true);
	}
	
	
	
	/** When submit button is pressed, checks violation of fields and sends data to server.
	 * @author itain
	 * @param event Gets event.
	 */
	@FXML
	public void submitButtonPressed(ActionEvent event) 
	{
		if (fNameTextField.equals("") || lNameTextField.equals(""))
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.EMPTY_FIELDS);
			return;
		}
		
		if (Validate.nameValidateCharactersOnly(fNameTextField.getText()) == false)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.MUST_INCLUDE_ONLY_CHARACTERS);
			return;
		}
		
		if (Validate.nameValidateCharactersOnly(lNameTextField.getText()) == false)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.MUST_INCLUDE_ONLY_CHARACTERS);
			return;
		}
				
		SearchUserResult user = new SearchUserResult(UserPageController.searchedUserPage.getUsername(), fNameTextField.getText().trim(), lNameTextField.getText().trim(), "", "", "", "", "", "");
		Message message = prepareEditUser(ActionType.EDIT_USER_LIBRARIAN,user);
		userDetailsToChange=new SearchUserResult(user.getUsername(),user.getFirstName(),user.getLastName(),"","","","","","");
		try {
			ClientController.clientConnectionController.sendToServer(message);
		} catch (IOException e) {
					
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
	}
	
	
	/** When cancel button is pressed, delete changes and goes back to user's page details.
	 * @author itain
	 * @param event Gets event.
	 */
	public void cancelButtonPressed(ActionEvent event)
	{
		if(ClientUI.getTypeOfUser()=="Librarian")
    	{
        	if (librarianMain == null)
        		librarianMain = new HomepageLibrarianController();
        	librarianMain.setPage(ScreensInfo.USER_PAGE_SCREEN);
    	}
    	else if(ClientUI.getTypeOfUser()=="Manager")
    	{
        	if (managerMain == null)
        		managerMain = new HomepageManagerController();
        	managerMain.setPage(ScreensInfo.USER_PAGE_SCREEN);
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
	public void backButtonPressed(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressedCloseMenu(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This function gets message and perform the task by the error type.
	 * @author itain
	 * @param type Gets error type.
	 * @param errorCode Gets error message.
	 */
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
	
	/** This function prepare message that will be send to the server with arraylist,
	 * and the action.
	 * @author itain
	 * @param type Gets the type of the action
	 * @param user Gets the class with the user information.
	 * @return message that will send to server.
	 */
	public Message prepareEditUser(ActionType type, SearchUserResult user)
	{
		Message message = new Message();
		message.setType(type);
		ArrayList <String> elementsList = new ArrayList<String>();
		elementsList.add(user.getUsername());
		elementsList.add(user.getFirstName());
		elementsList.add(user.getLastName());
		message.setElementsList(elementsList);
		return message;
	}
	
	
	

}
