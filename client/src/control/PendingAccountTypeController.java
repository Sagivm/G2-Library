package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import entity.GeneralMessages;
import entity.Message;
import enums.ActionType;
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
 * PendingAccountTypeController is the controller that responsible to show
 *  the requested AccountType list.
 * @author ork
 */
public class PendingAccountTypeController {
	
	/**
	 * The table view that shows the pending account type requests.
	 */
	@FXML
    private TableView<pendingAccount> table;
    
	/**
	 * username column.
	 */
	@FXML
    private TableColumn username;
	
	/**
	 * fist name column.
	 */
	@FXML
    private TableColumn firstName;
	
	/**
	 * last name column.
	 */
	@FXML
    private TableColumn lastName;
	
	/**
	 * The user's current account type.
	 */
	@FXML
	private TableColumn currentAccType;
	
	/**
	 * The user's requested account type.
	 */
	@FXML
	private TableColumn requestedAccType;
	
	/**
	 * Click on this button call the method that responsible for confirming this request.
	 */
	@FXML
    private TableColumn<pendingAccount, pendingAccount> btnConfirm;
	
	/**
	 * Click on this button call the method that responsible for decline this request.
	 */
	@FXML
    private TableColumn<pendingAccount, pendingAccount> btnDecline;
    
	/**
	 * shows message about the action.
	 */
	@FXML
    private Label printAction;
	
	/**
	 * shows the count of the requests.
	 */
	@FXML
    private Label CountLabel;
    
    
	/**
	 * saves all the account requests list.
	 */
	public static ArrayList <String> pendingAccountList;
	
	/**
	 * the number of the requests.
	 */
	public static int countUsers;
    
	private ObservableList<pendingAccount> data = FXCollections.observableArrayList();;

	/**
	 * Image for the confirm button.
	 */
	private final Image confirmImage = new Image("/img/confirm.png");
	
    /**
     * Image for the decline button.
     */
    private final Image declineImage = new Image("/img/decline.png");
    
    
    
	/**
	 * Fill the table when the screen load.
	 */
	@FXML
	private void initialize(){
		Message message = prepareGetPendingAccunts(ActionType.GET_PENDING_ACCOUNTS);
		try {
			ClientController.clientConnectionController.sendToServer(message);
		} catch (IOException e) {	
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		
		Platform.runLater(() -> {
			PendingAccountTypeRecv recv = new PendingAccountTypeRecv();
	        recv.start();
	        synchronized(recv)
			{
				try {
					recv.wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				/*try {
					TimeUnit.MILLISECONDS.sleep(300);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}*/
				countUsers=0;
				for(int i=0;i<pendingAccountList.size();i+=5){
					countUsers++;
					data.add(new pendingAccount(pendingAccountList.get(i), pendingAccountList.get(i+1), pendingAccountList.get(i+2), pendingAccountList.get(i+3), pendingAccountList.get(i+4)));
				}
				
				CountLabel.setText("There are "+countUsers+" Users that wait for your reply");
				
		        table.setEditable(true);
		        
		        username.setCellValueFactory(
		                new PropertyValueFactory<pendingAccount, String>("username"));
		
		        firstName.setCellValueFactory(
		                new PropertyValueFactory<pendingAccount, String>("firstName"));
		
		        lastName.setCellValueFactory(
		                new PropertyValueFactory<pendingAccount, String>("lastName"));
		        
		        currentAccType.setCellValueFactory(
		                new PropertyValueFactory<pendingAccount, String>("currentAccType"));
		
		        requestedAccType.setCellValueFactory(
		                new PropertyValueFactory<pendingAccount, String>("requestedAccType"));
		
		
		        username.setStyle( "-fx-alignment: CENTER;");
		        firstName.setStyle( "-fx-alignment: CENTER;");
		        lastName.setStyle( "-fx-alignment: CENTER;");
		        currentAccType.setStyle( "-fx-alignment: CENTER;");
		        requestedAccType.setStyle( "-fx-alignment: CENTER;");
		        btnConfirm.setStyle( "-fx-alignment: CENTER;");
		        btnDecline.setStyle( "-fx-alignment: CENTER;");
		
		        btnConfirm.setCellValueFactory(new Callback<CellDataFeatures<pendingAccount, pendingAccount>, ObservableValue<pendingAccount>>() {
		          @Override public ObservableValue<pendingAccount> call(CellDataFeatures<pendingAccount, pendingAccount> features) {
		              return new ReadOnlyObjectWrapper(features.getValue());
		          }
		        });
		
		        btnConfirm.setCellFactory(new Callback<TableColumn<pendingAccount, pendingAccount>, TableCell<pendingAccount, pendingAccount>>() {
		          @Override public TableCell<pendingAccount, pendingAccount> call(TableColumn<pendingAccount, pendingAccount> btnConfirm) {
		            return new TableCell<pendingAccount, pendingAccount>() {
		              final ImageView buttonGraphic = new ImageView();
		              final Button button = new Button(); {
		                button.setGraphic(buttonGraphic);
		                button.setPrefWidth(40);
		              }
		              @Override public void updateItem(final pendingAccount user, boolean empty) {
		                super.updateItem(user, empty);
		                if (user != null) {
		                  buttonGraphic.setImage(confirmImage);
		                  setGraphic(button);
		                  button.setOnAction(new EventHandler<ActionEvent>() {
		                    @Override public void handle(ActionEvent event) {
		                    	//String newaccountType = user.getRequestedAccType().substring(7);
		                    	//System.out.println(newaccountType);
		                    	Message message = prepareUpdatePendingAccounts(ActionType.UPDATE_PENDING_ACCOUNT,user.getUsername(),"approve",user.getRequestedAccType());
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
		        
		        
		        
		        btnDecline.setCellValueFactory(new Callback<CellDataFeatures<pendingAccount, pendingAccount>, ObservableValue<pendingAccount>>() {
		            @Override public ObservableValue<pendingAccount> call(CellDataFeatures<pendingAccount, pendingAccount> features) {
		                return new ReadOnlyObjectWrapper(features.getValue());
		            }
		          });
		
		        btnDecline.setCellFactory(new Callback<TableColumn<pendingAccount, pendingAccount>, TableCell<pendingAccount, pendingAccount>>() {
		            @Override public TableCell<pendingAccount, pendingAccount> call(TableColumn<pendingAccount, pendingAccount> btnDecline) {
		              return new TableCell<pendingAccount, pendingAccount>() {
		                final ImageView buttonGraphic = new ImageView();
		                final Button button = new Button(); {
		                  button.setGraphic(buttonGraphic);
		                  button.setPrefWidth(40);
		                }
		                @Override public void updateItem(final pendingAccount user, boolean empty) {
		                  super.updateItem(user, empty);
		                  if (user != null) {
		                    buttonGraphic.setImage(declineImage);
		                    setGraphic(button);
		                    button.setOnAction(new EventHandler<ActionEvent>() {
		                      @Override public void handle(ActionEvent event) {
		                      	Message message = prepareUpdatePendingAccounts(ActionType.UPDATE_PENDING_ACCOUNT,user.getUsername(),"decline",user.getCurrentAccType());
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
			}
		});
	
	}
	
	
	/**
	 * Create's a message for get the account requests from DB.
	 * @param type
	 * @return
	 */
	public Message prepareGetPendingAccunts(ActionType type)
	{
		Message message = new Message();
		message.setType(type);
		return message;
	}
	
	
	/**
	 * Create's a message for update the DB with the new account type.
	 * @param type
	 * @param username
	 * @param action
	 * @param newAccountType
	 * @return
	 */
	public Message prepareUpdatePendingAccounts(ActionType type,String username,String action, String newAccountType)
	{
		Message message = new Message();
		ArrayList<String> elementsList = new ArrayList<String>();
		elementsList.add(username);
		elementsList.add(action);
		elementsList.add(newAccountType);
		message.setType(type);
		message.setElementsList(elementsList);
		return message;
	}
	
	
	/**
	 * Shows alert message on error.
	 * @param type
	 * @param errorCode
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
	 * Inner class for filling the table dynamically.
	 * @author or
	 *
	 */
	public static class pendingAccount {

	    private final SimpleStringProperty username;
	    private final SimpleStringProperty firstName;
	    private final SimpleStringProperty lastName;
	    private final SimpleStringProperty currentAccType;
	    private final SimpleStringProperty requestedAccType;

	    private pendingAccount(String username, String firstName, String lastName, String currentAccType, String requestedAccType) {
	        this.username = new SimpleStringProperty(username);
	        this.firstName = new SimpleStringProperty(firstName);
	        this.lastName = new SimpleStringProperty(lastName);
	        this.currentAccType = new SimpleStringProperty(currentAccType);
	        String newaccountType = requestedAccType.substring(7);
	        this.requestedAccType = new SimpleStringProperty(newaccountType);
	    }

	    /**
	     * Getter for username.
	     * @return
	     */
	    public String getUsername() {
	        return username.get();
	    }
	    
	    /**
	     * Getter for first name.
	     * @return
	     */
	    public String getFirstName() {
	        return firstName.get();
	    }
	    
	    /**
	     * Getter for last name.
	     * @return
	     */
	    public String getLastName() {
	        return lastName.get();
	    }
	    
	    /**
	     * Getter for current account type.
	     * @return
	     */
	    public String getCurrentAccType() {
	        return currentAccType.get();
	    }
	    
	    /**
	     * Getter for requests account type.
	     * @return
	     */
	    public String getRequestedAccType() {
	        return requestedAccType.get();
	    }

	    /**
	     * Setter for first name.
	     * @param fName
	     */
	    public void setFirstName(String fName) {
	    	firstName.set(fName);
	    }

	    /**
	     * Setter for last name.
	     * @param fName
	     */
	    public void setLastName(String fName) {
	        lastName.set(fName);
	    }
	    
	    /**
	     * Setter for current account type.
	     * @param currAccType
	     */
	    public void setCurrentAccType(String currAccType) {
	    	currentAccType.set(currAccType);
	    }

	    /**
	     * Setter requested account type.
	     * @param reqAccType
	     */
	    public void setRequestedAccType(String reqAccType) {
	    	requestedAccType.set(reqAccType);
	    }

	}

}


class PendingAccountTypeRecv extends Thread{
	
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
