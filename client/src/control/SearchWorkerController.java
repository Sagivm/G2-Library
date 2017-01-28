package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import boundry.ClientUI;
import entity.GeneralMessages;
import entity.Message;
import entity.Register;
import entity.ScreensInfo;
import entity.User;
import entity.Validate;
import entity.Worker;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/** SearchUserController. Responsible to enable a librarian or a manager to search users.
 * @author itain
 */
public class SearchWorkerController implements ScreensIF{

	/**
	 * shows workers image
	 */
	@FXML private ImageView workersImageView;
	
	/**
	 * gets username to search
	 */
	@FXML private TextField idTextField;
	
	/**
	 * gets first name of user to search
	 */
	@FXML private TextField fNameTextField;
	
	/**
	 * gets last name of user to search
	 */
	@FXML private TextField lNameTextField;
	
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
		Image workersImagePath = new Image("/img/workers.png");
		workersImageView.setImage(workersImagePath);
	}

	
	/** This function called when the worker press on search. the function tests the
	 * integrity of the fields, and send the message to the server.
	 * @author itain
	 * @param event The event when button pressed.
	 */
	@FXML
	public void searchButtonPressed(ActionEvent event)
	{
		
		String id = idTextField.getText();
		String firstName = fNameTextField.getText();
		String lastName = lNameTextField.getText();
		/*
		if (id.equals("") && firstName.equals("") && lastName.equals(""))
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.EMPTY_FIELDS);
			return;
		}
		*/
		if (Validate.usernameValidateNumbresOnly(id) == false)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.MUST_INCLUDE_ONLY_DIGITS_VER2);
			return;
		}
		
		if (Validate.nameValidateCharactersOnly(firstName) == false || Validate.nameValidateCharactersOnly(lastName) == false)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.MUST_INCLUDE_ONLY_CHARACTERS);
			return;
		}
		

		
		Worker worker = new Worker();
		worker.setId(id);
		worker.setFirstname(firstName);
		worker.setLastname(lastName);
		
		Message message = prepareWorkerSearch(ActionType.SEARCH_WORKER,worker);
		
		try {
			ClientController.clientConnectionController.sendToServer(message);
		} catch (IOException e) {
					
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		
		
    	if(ClientUI.getTypeOfUser()=="Librarian")
    	{
        	if (librarianMain == null)
        		librarianMain = new HomepageLibrarianController();
        	librarianMain.setPage(ScreensInfo.SEARCH_WORKER_RESULTS_SCREEN);
    	}
    	else if(ClientUI.getTypeOfUser()=="Manager")
    	{
        	if (managerMain == null)
        		managerMain = new HomepageManagerController();
        	managerMain.setPage(ScreensInfo.SEARCH_WORKER_RESULTS_SCREEN);
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
	
	/** When clear button pressed the function clear all fields.
	 * @author itain
	 * @param event The event when button pressed.
	 */
	@FXML
	public void clearButtonPressed(ActionEvent event)
	{
		idTextField.setText("");
		fNameTextField.setText("");
		lNameTextField.setText("");
	}
	
	/** This function prepare message that will be send to the server with arraylist,
	 * and the action.
	 * @param type Gets the type of the action
	 * @param user Gets the class with the user information.
	 * @return message that will send to server.
	 */
	public Message prepareWorkerSearch(ActionType type, Worker worker)
	{
		Message message = new Message();
		message.setType(type);
		ArrayList <String> elementsList = new ArrayList<String>();
		elementsList.add(worker.getId());
		elementsList.add(worker.getFirstname());
		elementsList.add(worker.getLastname());
		message.setElementsList(elementsList);
		return message;
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
	 * @param type Gets error type.
	 * @param errorCode Gets error message.
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


