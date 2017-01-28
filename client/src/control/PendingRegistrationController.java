package control;

import java.io.IOException;
import java.util.ArrayList;

import entity.GeneralMessages;
import entity.Message;
import entity.Register;
import enums.ActionType;
import img.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * PendingRegistrationController is the controller that shows a list of all the registration requests.
 * @author idanN
 */
public class PendingRegistrationController {
	
	
	/**
	 * TableView for showing the pending registration.
	 */
	@FXML
    private TableView<pendingUser> table;
    
	/**
	 * Column in the table that shows the user id.
	 */
	@FXML
    private TableColumn username;
	
	/**
	 * Column in the table that shows the user first name.
	 */
	@FXML
    private TableColumn firstName;
	
	/**
	 * Column in the table that shows the user last name.
	 */
	@FXML
    private TableColumn lastName;
	
	/**
	 * Column in the table that shows the accept button.
	 */
	@FXML
    private TableColumn<pendingUser, pendingUser> btnConfirm;
	
    /**
	 * Column in the table that shows the decline button.
	 */
	@FXML
    private TableColumn<pendingUser, pendingUser> btnDecline;
	
    /**
	 * Label that show the state of the last operation.
	 */
	@FXML
    private Label printAction;
	
	/**
	 * Label that show the amount of pending registration in DB.
	 */
	@FXML
    private Label CountLabel;
	
	/**
	 * static Array list of all the pending registration from the DB.
	 */
	public static ArrayList <String> pendingUsersList; //itai - V
	
	/**
	 * Integer that count the registration pending requests.
	 */
	public static int countUsers;
    
	/**
	 * this attribute insert the data (pending registration) to the table.
	 */
	private ObservableList<pendingUser> data = FXCollections.observableArrayList();;

	/**
	 * Image of the accept button.
	 */
	private final Image confirmImage = new Image(
    	      "/img/confirm.png"
    	    );
	
	/**
	 * Image of the decline button.
	 */
    private final Image declineImage = new Image(
    	      "/img/decline.png"
    	    );
	
	
    /**
	 * initialize data from the DB in the form on load.
	 */
	@FXML
	private void initialize(){
		Message message = prepareGetPendingUsers(ActionType.GET_PENDING_USERS);
		try {
			ClientController.clientConnectionController.sendToServer(message);
		} catch (IOException e) {	
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		
			  //itai
			  Platform.runLater(new Runnable() {
					@Override
					public void run() {
							PendingRegistrationRecv recv_getPendingUsers = new PendingRegistrationRecv();
							recv_getPendingUsers.start();
							synchronized (recv_getPendingUsers) {
								try{
									recv_getPendingUsers.wait();
								}catch(InterruptedException e){
									e.printStackTrace();
								}
							}
					}});
		
		Platform.runLater(() -> {
		countUsers=0;
		for(int i=0;i<pendingUsersList.size();i+=3){
			countUsers++;
			data.add(new pendingUser(pendingUsersList.get(i), pendingUsersList.get(i+1), pendingUsersList.get(i+2)));
		}
		
		CountLabel.setText("There are "+countUsers+" Users that wait for your reply");
		
        table.setEditable(true);
        
        username.setCellValueFactory(
                new PropertyValueFactory<pendingUser, String>("username"));

        firstName.setCellValueFactory(
                new PropertyValueFactory<pendingUser, String>("firstName"));

        lastName.setCellValueFactory(
                new PropertyValueFactory<pendingUser, String>("lastName"));


        username.setStyle( "-fx-alignment: CENTER;");
        firstName.setStyle( "-fx-alignment: CENTER;");
        lastName.setStyle( "-fx-alignment: CENTER;");
        btnConfirm.setStyle( "-fx-alignment: CENTER;");
        btnDecline.setStyle( "-fx-alignment: CENTER;");

        

        btnConfirm.setCellValueFactory(new Callback<CellDataFeatures<pendingUser, pendingUser>, ObservableValue<pendingUser>>() {
          @Override public ObservableValue<pendingUser> call(CellDataFeatures<pendingUser, pendingUser> features) {
              return new ReadOnlyObjectWrapper(features.getValue());
          }
        });

        btnConfirm.setCellFactory(new Callback<TableColumn<pendingUser, pendingUser>, TableCell<pendingUser, pendingUser>>() {
          @Override public TableCell<pendingUser, pendingUser> call(TableColumn<pendingUser, pendingUser> btnConfirm) {
            return new TableCell<pendingUser, pendingUser>() {
              final ImageView buttonGraphic = new ImageView();
              final Button button = new Button(); {
                button.setGraphic(buttonGraphic);
                button.setPrefWidth(40);
              }
              @Override public void updateItem(final pendingUser user, boolean empty) {
                super.updateItem(user, empty);
                if (user != null) {
                  buttonGraphic.setImage(confirmImage);
                  setGraphic(button);
                  button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent event) {
                    	Message message = prepareUpdatePendingUsers(ActionType.ACCEPT_PENDING_USERS,user.getUsername());
                		try {
                			ClientController.clientConnectionController.sendToServer(message);
                        	printAction.setText("User " + user.getFirstName().toLowerCase() + " has been confirm successfully");     
                        	table.getItems().remove(user);
                        	countUsers--;
                    		CountLabel.setText("There are "+countUsers+" Users that wait for your reply");
                		} catch (IOException e) {	
                			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
                		}
                    }
                  });
                } else {
                  setGraphic(null);
                }
              }
            };
          }
        });
        
        
        
        btnDecline.setCellValueFactory(new Callback<CellDataFeatures<pendingUser, pendingUser>, ObservableValue<pendingUser>>() {
            @Override public ObservableValue<pendingUser> call(CellDataFeatures<pendingUser, pendingUser> features) {
                return new ReadOnlyObjectWrapper(features.getValue());
            }
          });

        btnDecline.setCellFactory(new Callback<TableColumn<pendingUser, pendingUser>, TableCell<pendingUser, pendingUser>>() {
            @Override public TableCell<pendingUser, pendingUser> call(TableColumn<pendingUser, pendingUser> btnDecline) {
              return new TableCell<pendingUser, pendingUser>() {
                final ImageView buttonGraphic = new ImageView();
                final Button button = new Button(); {
                  button.setGraphic(buttonGraphic);
                  button.setPrefWidth(40);
                }
                @Override public void updateItem(final pendingUser user, boolean empty) {
                  super.updateItem(user, empty);
                  if (user != null) {
                    buttonGraphic.setImage(declineImage);
                    setGraphic(button);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                      @Override public void handle(ActionEvent event) {
                      	Message message = prepareUpdatePendingUsers(ActionType.DECLINE_PENDING_USERS,user.getUsername());
                  		try {
                  			ClientController.clientConnectionController.sendToServer(message);
                            printAction.setText("User " + user.getFirstName().toLowerCase() + " has been decline successfully");
                          	table.getItems().remove(user);
                          	countUsers--;
                    		CountLabel.setText("There are "+countUsers+" Users that wait for your reply");
                  		} catch (IOException e) {	
                  			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
                  		}
                      }
                    });
                  } else {
                    setGraphic(null);
                  }
                }
              };
            }
          });

        table.setItems(data);
        //table.getColumns().addAll(firstNameCol, lastNameCol, emailCol, btnCol);
		});
	
	}
	
	
	/** 
	 * Create a message to the server with the Pending Registration ActionType.
	 * @param type The action type of the message that will pass to the server.
	 * @return message The message that will pass to the server.
	 */
	public Message prepareGetPendingUsers(ActionType type)
	{
		Message message = new Message();
		message.setType(type);
		return message;
	}
	
	
	/** 
	 * Create a message to the server with the Pending Registration Username to update.
	 * @param type The action type of the message that will pass to the server.
	 * @param username The parameter that will pass to the server.
	 * @return message The message that will pass to the server.
	 */
	public Message prepareUpdatePendingUsers(ActionType type,String username)
	{
		Message message = new Message();
		ArrayList<String> elementsList = new ArrayList<String>();
		elementsList.add(username);
		message.setType(type);
		message.setElementsList(elementsList);
		return message;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.ScreensIF#actionOnError(enums.ActionType,
	 * java.lang.String)
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
	
	/**
	 * inner class that contains propery strings of pending user attributes
	 * @author idaN
	 *
	 */
	public static class pendingUser {

	    /**
	     * String Property that contains username of pending user.
	     */
	    private final SimpleStringProperty username;
	    
	    
	    /**
	     * String Property that contains first name of pending user.
	     */
	    private final SimpleStringProperty firstName;
	    
	    
	    /**
	     * String Property that contains last name of pending user.
	     */
	    private final SimpleStringProperty lastName;

	    /**
	     * pendingUser constructor store the data.
	     * @param username - Gets the username.
	     * @param firstName - Gets the firstName.
	     * @param lastName - Gets the lastName.
	     */
	    private pendingUser(String username, String firstName, String lastName) {
	        this.username = new SimpleStringProperty(username);
	        this.firstName = new SimpleStringProperty(firstName);
	        this.lastName = new SimpleStringProperty(lastName);
	    }

	    /**
	     * Getter for username.
	     * @return The username.
	     */
	    public String getUsername() {
	        return username.get();
	    }
	    
	    /**
	     * Getter for first name.
	     * @return The first name.
	     */
	    public String getFirstName() {
	        return firstName.get();
	    }
	    
	    /**
	     * Getter for last name.
	     * @return The last name.
	     */
	    public String getLastName() {
	        return lastName.get();
	    }

	    /**
	     * Setter for username
	     * @param username The username.
	     */
	    public void setUsername(String Username) {
	    	username.set(Username);
	    }
	    
	    /**
	     * Setter for first name
	     * @param fName The first name.
	     */
	    public void setFirstName(String fName) {
	    	firstName.set(fName);
	    }

	    /**
	     * Setter for last name
	     * @param lName The last name.
	     */
	    public void setLastName(String lName) {
	        lastName.set(lName);
	    }

	}

	
}



/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class PendingRegistrationRecv extends Thread{
	
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