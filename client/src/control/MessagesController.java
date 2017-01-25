package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import control.BookManagementController.PropertyBook;
import control.PendingRegistrationController.pendingUser;
import control.PendingReviewsController.pendingReview;
import entity.GeneralMessages;
import entity.Message;
import entity.SearchUserResult;
import entity.User;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This class handles the messages that recived from the system to the user.
 * @author nire
 * 
 */
public class MessagesController implements ScreensIF
{
	/**
	 * attribute for the table view.
	 */
	@FXML
    private TableView<Messages> table;
    
	/**
	 * attribute for the date colmun.
	 */
	@FXML
    private TableColumn dateCol;
	
	/**
	 * attribute for the time colmun.
	 */
	@FXML 
	private TableColumn timeCol;
	
	/**
	 * attribute for the msg colmun.
	 */
	@FXML
	private TableColumn msgCol;
	
	/**
	 * attribute for data that will connect rows to this.
	 */
	private ObservableList<Messages> data = FXCollections.observableArrayList();

	/**
	 * static array to update data.
	 */
	public static ArrayList<String> messagesResult;
	
	
	/**
	 * initialize function that initialize the class (run first).
	 */
	@FXML
	private void initialize(){
		ArrayList<String> elementsList = new ArrayList<String>();
		User user = HomepageUserController.getConnectedUser();
		String username = user.getId();
		
		elementsList.add(username);
		Message message = new Message(ActionType.GET_MESSAGES,elementsList);
		
		
		try {
			ClientController.clientConnectionController.sendToServer(message);
		} catch (IOException e) {	
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		
		  //itai
		  Platform.runLater(new Runnable() {
				@Override
				public void run() {
					MessagesRecv recv_getMessages = new MessagesRecv();
					recv_getMessages.start();
						synchronized (recv_getMessages) {
							try{
								recv_getMessages.wait();
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
				}});
		
		Platform.runLater(() -> {

			table.setEditable(true);
			
			dateCol.setCellValueFactory(
	                new PropertyValueFactory<pendingUser, String>("date"));

			timeCol.setCellValueFactory(
	                new PropertyValueFactory<pendingUser, String>("time"));

			msgCol.setCellValueFactory(
	                new PropertyValueFactory<pendingUser, String>("msg"));
			
			
				for(int i=0;i<messagesResult.size();i++)
				{
					String[] tmp=new String[5];
					tmp = messagesResult.get(i).split("\\^");
					data.add(new Messages(tmp[0],tmp[1],tmp[2]));
					table.setItems(data);
				}
		});
		
		
		
	}
	

	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#backButtonPressed(javafx.event.ActionEvent)
	 */
	@Override
	public void backButtonPressed(ActionEvent event) {
		
	}

	
	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#pressedCloseMenu(javafx.event.ActionEvent)
	 */
	@Override
	public void pressedCloseMenu(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		
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
	
	
	/**
	 * Inner class for messsages that will be added as a row.
	 * @author nire
	 */
	public static class Messages {

	    /**
	     * SimpleString for date
	     */
	    private final SimpleStringProperty date;
	    
	    /**
	     * SimpleString for time
	     */
	    private final SimpleStringProperty time;
	    
	    /**
	     * SimpleString for msg
	     */
	    private final SimpleStringProperty msg;

	    /**
	     * Constructor for create messages that will be added to the message screen rows.
	     * @param date - Gets date.
	     * @param time - Gets time.
	     * @param msg - Gets msg.
	     */
	    private Messages(String date, String time, String msg) {
	    	this.date = new SimpleStringProperty(date);
	        this.time = new SimpleStringProperty(time);
	        this.msg = new SimpleStringProperty(msg);       
	    	
	    }


		/**
		 * Getter for date.
		 * @return the username
		 */
		public String getDate() {
	        return date.get();
	    }
		
		/**
		 * Getter for time.
		 * @return the time.
		 */
	    public String getTime() {
	        return time.get();
	    }
	    
		/**
		 * Getter for msg.
		 * @return the the msg.
		 */
	    public String getMsg() {
	        return msg.get();
	    }
	    
	    
	    /** Setter for date.
	     * @param date - Gets the date.
	     */
	    public void setDate(String date) {
	    	this.date.set(date);
	    }
	    
	    
	    /**
	     * Setter for time.
	     * @param time - Gets the time.
	     */
	    public void setTime(String time) {
	    	this.time.set(time);
	    }
	    
	    /**
	     * Setter for msg.
	     * @param time - Gets the msg.
	     */
	    public void setMsg(String msg) {
	    	this.msg.set(msg);
	    }

	}
	
}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class MessagesRecv extends Thread{
	
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
