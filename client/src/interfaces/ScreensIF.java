package interfaces;

import java.io.IOException;

/*import java.net.URL;
import java.util.ResourceBundle;*/

import enums.ActionType;
import javafx.event.ActionEvent;

/**
 * This interface include similar behavior: every screen got back button,
 * every screen got close button in every step. so we will impose that in the controllers.
 * @author nire
 *
 */
public interface ScreensIF {
	
	/**
	 * The function retrieve the user to the last screen.
	 * @param event - ActionEvent event
	 */
	public void backButtonPressed(ActionEvent event);
	
	/**
	 * The function close the program.
	 * @param event - ActionEvent event
	 */
	public void pressedCloseMenu(ActionEvent event) throws IOException;
	
	
	/**
	 * This function gets message and perform the task by the error type.
	 * @param type - Gets error type.
	 * @param errorCode - Gets error message.
	 */
	public void actionOnError(ActionType type, String errorCode);

/*	*//** This function initialize values when fxml controller is open.
	 * @param fxmlFileLocation
	 * @param resources
	 *//*
	public void initialize(URL fxmlFileLocation, ResourceBundle resources);*/
	
}
