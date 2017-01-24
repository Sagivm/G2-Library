package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import boundry.ClientUI;
import entity.GeneralMessages;
import entity.Message;
import entity.Review;
import entity.ScreensInfo;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * PendingReviewsController is the controller that responsible to show
 *  the pending reviews and opening a specific review.
 * @author ork
 */
public class PendingReviewsController implements ScreensIF {

	/**
	 * TableView for showing the pending reviews.
	 */
	@FXML
    private TableView<pendingReview> table;
    
	/**
	 * Column in the table that shows the review id.
	 */
	@FXML
    private TableColumn idCol;
    
	/**
	 * Column in the table that shows the username of the review's writer.
	 */
	@FXML
    private TableColumn usernameCol;
	
	/**
	 * Column in the table that shows the first name of the review's writer.
	 */
	@FXML
    private TableColumn firstNameCol;
	
	/**
	 * Column in the table that shows the last name of the review's writer.
	 */
	@FXML
    private TableColumn lastNameCol;
	
	/**
	 * Column in the table that shows the book title.
	 */
	@FXML
    private TableColumn bookTitleCol;
	
	/**
	 * Column in the table that shows the date of the review.
	 */
	@FXML
    private TableColumn dateCol;
	
	/**
	 * Click on this button to open the review approve/decline form.
	 */
	@FXML
    private TableColumn<pendingReview, pendingReview> btnEdit;
	
	/**
	 * static Array list of all the pending reviews from the DB.
	 */
	public static ArrayList <String> pendingReviewList;
	
	/**
	 * static reference of librarian home page.
	 */
	private static HomepageLibrarianController librarianMain;
	
	/**
	 * static reference of manager home page.
	 */
	private static HomepageManagerController managerMain;
	
	private static EditReviewController editReview;
    
	/**
	 * this attribute insert the data (pending review) to the table.
	 */
	private ObservableList<pendingReview> data = FXCollections.observableArrayList();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.ScreensIF#backButtonPressed(javafx.event.ActionEvent)
	 */
	@Override
	public void backButtonPressed(ActionEvent event) {
		// TODO Auto-generated method stub

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
	 * initialize data from the DB in the form on load.
	 */
	@FXML
	private void initialize(){
		Message message = prepareGetPendingReviews(ActionType.PENDING_REVIEWS);
		try {
			ClientController.clientConnectionController.sendToServer(message);
		} catch (IOException e) {	
			actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		
		Platform.runLater(new Runnable() {
		@Override
		public void run() {
	        PendingReviewsRecv recv = new PendingReviewsRecv();
	        recv.start();
			synchronized(recv)
			{
				try {
					recv.wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				for(int i=0;i<pendingReviewList.size();i+=7){
					data.add(new pendingReview(pendingReviewList.get(i), pendingReviewList.get(i+1), pendingReviewList.get(i+2), pendingReviewList.get(i+3), pendingReviewList.get(i+4), pendingReviewList.get(i+5), pendingReviewList.get(i+6)));
				}
				
		        table.setEditable(true);
		        
		        //prevent change order               
	            table.getColumns().addListener(new ListChangeListener() {
	                public boolean suspended;
	
	                @Override
	                public void onChanged(Change change) {
	                    change.next();
	                    if (change.wasReplaced() && !suspended) {
	                        this.suspended = true;
	                        table.getColumns().setAll(idCol,usernameCol,firstNameCol,lastNameCol,bookTitleCol,dateCol,btnEdit);
	                        this.suspended = false;
	                    }
	                }
	            });
		        
		        idCol.setCellValueFactory(
		                new PropertyValueFactory<pendingReview, String>("reviewId"));
		        
		        usernameCol.setCellValueFactory(
		                new PropertyValueFactory<pendingReview, String>("username"));
		
		        firstNameCol.setCellValueFactory(
		                new PropertyValueFactory<pendingReview, String>("firstName"));
		
		        lastNameCol.setCellValueFactory(
		                new PropertyValueFactory<pendingReview, String>("lastName"));
		
		        bookTitleCol.setCellValueFactory(
		                new PropertyValueFactory<pendingReview, String>("bookTitle"));
		
		        dateCol.setCellValueFactory(
		                new PropertyValueFactory<pendingReview, String>("reviewDate"));
		/*
		        username.setStyle( "-fx-alignment: CENTER;");
		        firstName.setStyle( "-fx-alignment: CENTER;");
		        lastName.setStyle( "-fx-alignment: CENTER;");
		        btnConfirm.setStyle( "-fx-alignment: CENTER;");
		        btnDecline.setStyle( "-fx-alignment: CENTER;");*/
		
		        btnEdit.setStyle( "-fx-alignment: CENTER;");
	
		        btnEdit.setCellValueFactory(new Callback<CellDataFeatures<pendingReview, pendingReview>, ObservableValue<pendingReview>>() {
		          @Override public ObservableValue<pendingReview> call(CellDataFeatures<pendingReview, pendingReview> features) {
		              return new ReadOnlyObjectWrapper(features.getValue());
		          }
		        });
		
		        btnEdit.setCellFactory(new Callback<TableColumn<pendingReview, pendingReview>, TableCell<pendingReview, pendingReview>>() {
		          @Override public TableCell<pendingReview, pendingReview> call(TableColumn<pendingReview, pendingReview> btnEdit) {
		            return new TableCell<pendingReview, pendingReview>() {
		              final ImageView buttonGraphic = new ImageView();
		              final Button button = new Button(); {
		                button.setGraphic(buttonGraphic);
		                button.setText("Edit");
		                button.setPrefWidth(60);
		              }
		              @Override public void updateItem(final pendingReview review, boolean empty) {
		                super.updateItem(review, empty);
		                if (review != null) {
		                  //buttonGraphic.setImage(confirmImage);
		                  setGraphic(button);
		                  button.setOnAction(new EventHandler<ActionEvent>() {
		                    @Override public void handle(ActionEvent event) {
		                    	
		                    	if(ClientUI.getTypeOfUser()=="Librarian")
		                    	{
			                    	if (librarianMain == null)
			                    		librarianMain = new HomepageLibrarianController();
			                    	librarianMain.setPage(ScreensInfo.EDIT_REVIEW_SCREEN);
		                    	}
		                    	else if(ClientUI.getTypeOfUser()=="Manager")
		                    	{
			                    	if (managerMain == null)
			                    		managerMain = new HomepageManagerController();
			                    	managerMain.setPage(ScreensInfo.EDIT_REVIEW_SCREEN);
		                    	}
	                    		Review editReview = new Review(review.getReviewId(),review.getUsername(),review.getFirstName(),review.getLastName(),review.getBookTitle(),review.getReviewContent(),review.getReviewDate());
	                    		EditReviewController editReviewPage = new EditReviewController();
	                    		editReviewPage.editReview = editReview;
	                    		EditReviewRecv.canContinue = true;
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
			}
		});
	}
	
	/** Create a message to the server with the Pending Reviews ActionType.
	 * @param type
	 * @return
	 */
	public Message prepareGetPendingReviews(ActionType type)
	{
		Message message = new Message();
		message.setType(type);
		return message;
	}
	
	/**
	 * Inner class that responsible for filling the table.
	 * @author ork
	 *
	 */
	public static class pendingReview {

	    private final SimpleStringProperty reviewId;
	    private final SimpleStringProperty username;
	    private final SimpleStringProperty firstName;
	    private final SimpleStringProperty lastName;
	    private final SimpleStringProperty bookTitle;
	    private final SimpleStringProperty reviewContent;
	    private final SimpleStringProperty reviewDate;
	    
	    
		/**
		 * pendingReview constructor store the data.
		 * @param reviewId - Gets the reviewId.
		 * @param firstName - Gets the firstName.
		 * @param lastName - Gets lastName.
		 * @param bookTitle - Gets bookTitle.
		 * @param reviewContent - Gets reviewContent.
		 * @param reviewDate - Gets reviewDate.
		 */
	    private pendingReview(String reviewId, String username, String firstName,String lastName, String bookTitle,String reviewContent, String reviewDate) {
	    	this.reviewId = new SimpleStringProperty(reviewId);
	        this.username = new SimpleStringProperty(username);
	        this.firstName = new SimpleStringProperty(firstName);       
	    	this.lastName = new SimpleStringProperty(lastName);
	        this.bookTitle = new SimpleStringProperty(bookTitle);
	        this.reviewContent = new SimpleStringProperty(reviewContent);
	        this.reviewDate = new SimpleStringProperty(reviewDate);
	    }

	    /** Getter for reviewId.
	     * @return
	     */
	    public String getReviewId() {
	        return reviewId.get();
	    }
	    
	    /** Getter for username.
	     * @return
	     */
	    public String getUsername() {
	        return username.get();
	    }
	    
	    /** Getter for firstName.
	     * @return
	     */
	    public String getFirstName() {
	        return firstName.get();
	    }
	    
	    /** Getter for lastName.
	     * @return
	     */
	    public String getLastName() {
	        return lastName.get();
	    }
	    
	    /** Getter for bookTitle.
	     * @return
	     */
	    public String getBookTitle() {
	        return bookTitle.get();
	    }
	    
	    /** Getter for reviewContent.
	     * @return
	     */
	    public String getReviewContent() {
	        return reviewContent.get();
	    }
	    
	    /** Getter for reviewDate.
	     * @return
	     */
	    public String getReviewDate() {
	        return reviewDate.get();
	    }
	    
	    
	    /** Setter for reviewId.
	     * @param reviewId
	     */
	    public void setReviewId(String reviewId) {
	    	this.reviewId.set(reviewId);
	    }
	    
	    /** Setter for username.
	     * @param username
	     */
	    public void setUsername(String username) {
	    	this.username.set(username);
	    }
	    
	    /** Setter for firstName.
	     * @param firstName
	     */
	    public void setFistname(String firstName) {
	    	this.firstName.set(firstName);
	    }

	    /** Setter for lastName.
	     * @param lastName
	     */
	    public void setLastName(String lastName) {
	        this.lastName.set(lastName);
	    }
	    
	    /** Setter for bookTitle.
	     * @param bookTitle
	     */
	    public void setBookTitle(String bookTitle) {
	    	this.bookTitle.set(bookTitle);
	    }
	    
	    /** Setter for reviewContent.
	     * @param reviewContent
	     */
	    public void setReviewContent(String reviewContent) {
	        this.reviewContent.set(reviewContent);
	    }

	    /** Setter for reviewDate.
	     * @param reviewDate
	     */
	    public void setReviewDate(String reviewDate) {
	        this.reviewDate.set(reviewDate);
	    }
	}
	
}

class PendingReviewsRecv extends Thread{
    
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
		    	/*try {
					TimeUnit.MILLISECONDS.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}*/
        	//while(pendingReviewList==null)
        	while(canContinue == false)
        		{
        			System.out.print("");
        		}
        	canContinue = false;
            notify();
        }
    }
}
