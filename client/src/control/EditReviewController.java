package control;

import java.io.IOException;
import java.util.ArrayList;

import boundry.ClientUI;
import entity.GeneralMessages;
import entity.Message;
import entity.Review;
import entity.ScreensInfo;
import entity.Validate;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;

/**
 * EditReviewController is the controller that responsible to show
 * a review and allows to approve or decline it.
 * @author ork
 *
 */
public class EditReviewController implements ScreensIF {
	
	
	/**
	 * This is the current review that shown on the screen.
	 */
	public static entity.Review editReview;
	
	/**
	 * The name of the book.
	 */
	@FXML
	private Label lblTitle;
	
	/**
	 * The username of the user that wrote the review.
	 */
	@FXML
	private Label lblUsername;
	
	/**
	 * The full name of the user that wrote the review.
	 */
	@FXML
	private Label lblFullName;
	
	/**
	 * The date of the review.
	 */
	@FXML
	private Label lblDate;
	
	/**
	 * The review content (Editable).
	 */
	@FXML
	private TextArea txtAreaContent;
	
	/**
	 * ArrayList with the response for the DB (What to do and if approved saves the new content).
	 */
	private ArrayList<String> reviewStatus = new ArrayList<>();
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.ScreensIF#backButtonPressed(javafx.event.ActionEvent)
	 */
	@Override
	public void backButtonPressed(ActionEvent event) {
		ScreenController screenController = new ScreenController();
		try{
			if(ClientUI.getTypeOfUser()=="Librarian")
			{
				HomepageLibrarianController.setPage(ScreensInfo.PENDING_REVIEWS_SCREEN);
				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_LIBRARIAN_SCREEN,ScreensInfo.HOMEPAGE_LIBRARIAN_TITLE);
			}
			else
			{
				HomepageManagerController.setPage(ScreensInfo.PENDING_REVIEWS_SCREEN);
				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_MANAGER_SCREEN,ScreensInfo.HOMEPAGE_MANAGER_TITLE);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.ScreensIF#pressedCloseMenu(javafx.event.ActionEvent)
	 */
	@Override
	public void pressedCloseMenu(ActionEvent event) throws IOException{

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
	 * Initialize values to the FX components from the DB.
	 */
	@FXML
    public void initialize() {
		try{

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				EditReviewRecv recv = new EditReviewRecv();
		        recv.start();
				synchronized(recv)
				{
					try {
						recv.wait();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					lblTitle.setText(editReview.getBookTitle());
					lblUsername.setText("User: " + editReview.getUsername());
					lblFullName.setText(editReview.getFirstName() + " " + editReview.getLastName());
					lblDate.setText("reviewed on " + editReview.getReviewDate());
					txtAreaContent.setText(editReview.getReviewContent());		
					txtAreaContent.setWrapText(true);
				}
			}
		});
	}
	
	/**
	 * Sends the action to the DB.
	 * @param status
	 * @param content
	 */
	public void actionPressed(String status,String content)
	{
		try{
			//System.out.print(editReview.getReviewID());
			reviewStatus.add(editReview.getReviewID());
			reviewStatus.add(Validate.fixText(content));
			reviewStatus.add(status);
			reviewStatus.add(editReview.getUsername()); 
			
			Message message = updateReviewStatus(ActionType.UPDATE_REVIEW_STATUS,reviewStatus);
			
			ClientController clientCtrl = new ClientController();
			if (clientCtrl.clientConnectionController == null)
				clientCtrl.clientConnectionController = new ClientConnectionController(clientCtrl.IP_ADDRESS,clientCtrl.DEFAULT_PORT);
			clientCtrl.clientConnectionController.sendToServer(message);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Move the action "declined" to actionPressed method.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void declineButtonPressed(ActionEvent event) throws IOException{    
		try{
			actionPressed("declined",editReview.getReviewContent());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Move the action "approved" to actionPressed method.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void aprroveButtonPressed(ActionEvent event) throws IOException{    
		try{
			if (txtAreaContent.getText().length() == 0)
			{
				actionOnError(ActionType.CONTINUE,GeneralMessages.EMPTY_REVIEW);
				return;
			}
			actionPressed("approved",txtAreaContent.getText());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create a message to the server with the review status ActionType.
	 * @param type
	 * @param reviewStatus
	 * @return
	 */
	public Message updateReviewStatus(ActionType type, ArrayList<String> reviewStatus)
	{
		Message message = new Message();
		message.setType(type);
		message.setElementsList(reviewStatus);
		return message;
	}

}

/**
 * This class makes sure the information from the server was received successfully.
 * @author ork
 */
class EditReviewRecv extends Thread{
    
	/**
	 * static Array list of all the pending reviews from the DB.
	 */
	//public static ArrayList <String> pendingReviewList;
	
	/**
	 * Get true after receiving values from DB.
	 */
	public static boolean canContinue = false;

    @Override
    public void run(){
        synchronized(this){
        	while(canContinue == false)
        		{
        			System.out.print("");
        		}
        	canContinue = false;
        	
            notify();
        }
    }
}
