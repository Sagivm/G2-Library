package control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import boundry.ClientUI;
import entity.GeneralMessages;
import entity.Message;
import entity.Register;
import entity.ScreensInfo;
import enums.ActionType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;

//
/**
 * SetAccountTypeController is the controller that responsible to send the
 * Account Type request of the user to the DB.
 * 
 * @author sagivm
 */
public class SetAccountTypeController implements Initializable {

	/**
	 * List of Account Types
	 */
	@FXML
	private ComboBox settingList;

	/**
	 * static reference of user home page.
	 */
	private static HomepageUserController userMain;
	
	/**
	 * Initialize account type list with available account types for the user
	 */
	@FXML
	public void initializeSettingList() {
		settingList.setPromptText("Select sub.");
		switch (HomepageUserController.getConnectedUser().getAccountType()) {
		case Yearly: {
			settingList.getItems().addAll("Monthly sub.");
			break;
		}
		case Monthly: {
			settingList.getItems().addAll("Monthly sub.", "Yearly sub.");
			break;
		}
		case PerBook: {
			settingList.getItems().addAll("Monthly sub.", "Yearly sub.");
			break;
		}
		case Intrested: {
			settingList.getItems().addAll("Per book sub.", "Monthly sub.", "Yearly sub.");
			break;
		}
		default:{}
		}
		settingList.setPromptText("Select sub.");
	}

	/**
	 * Update's User information to pending and send a notification to librarian
	 * @param event The event when button pressed.
	 * @throws IOException IO exception.
	 */
	@FXML
	public void submitSettingButtonPressed(ActionEvent event) throws IOException {
		boolean valid = true;
		String choice = new String();
		if ((String) settingList.getSelectionModel().getSelectedItem() != null) {
			HomepageUserController userPage = new HomepageUserController();
			switch ((String) settingList.getValue()) {
			case "Per book sub.": {
				HomepageUserController.getConnectedUser().setAccountStatus("PendingPerBook");
				choice = "PendingPerBook";
				userPage.getConnectedUser().setAccountStatus("PendingPerBook");
				break;
			}
			case "Monthly sub.": {
				HomepageUserController.getConnectedUser().setAccountStatus("PendingMonthly");
				choice = "PendingMonthly";
				userPage.getConnectedUser().setAccountStatus("PendingMonthly");
				break;
			}
			case "Yearly sub.": {
				HomepageUserController.getConnectedUser().setAccountStatus("PendingYearly");
				choice = "PendingYearly";
				userPage.getConnectedUser().setAccountStatus("PendingYearly");
				break;
			}
			}
			ArrayList<String> elementsList = new ArrayList<String>();
			elementsList.add(0, HomepageUserController.getConnectedUser().getId());
			elementsList.add(1, choice);
			Message message = new Message(ActionType.ACCOUNTTYPEREQ, elementsList);
			try {
				ClientController.clientConnectionController.sendToServer(message);

			} catch (IOException e) {

				actionToDisplay("Warning", ActionType.CONTINUE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
			}
			if (userMain == null)
        		userMain = new HomepageUserController();
        	userMain.setPage(null);
        	
        	ScreenController screenController = new ScreenController();
    		try{
    			if(ClientUI.getTypeOfUser()=="Librarian")
    				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_LIBRARIAN_SCREEN,ScreensInfo.HOMEPAGE_LIBRARIAN_TITLE);						
    			else if(ClientUI.getTypeOfUser()=="Manager")
    				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_MANAGER_SCREEN,ScreensInfo.HOMEPAGE_MANAGER_TITLE);
    			else if(ClientUI.getTypeOfUser()=="User")
    				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_USER_SCREEN,ScreensInfo.HOMEPAGE_USER_TITLE);
    		} 
    		catch (Exception e) {
				e.printStackTrace();
			}
		} 
		else {
			actionToDisplay("Info", ActionType.CONTINUE, "Subscription must be selected");
		}

	}

	/**
	 * This function choose what to display the user.
	 * 
	 * @param type Defines if alert or info.
	 * @param actiontype Gets the type of action after display.
	 * @param message Gets the message to display in popup.
	 */
	public void actionToDisplay(String type, ActionType actiontype, String message) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(type);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		if (actiontype == ActionType.TERMINATE) {
			Platform.exit();
			System.exit(1);
		}
		if (actiontype == ActionType.CONTINUE)
			return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		initializeSettingList();

	}

}
