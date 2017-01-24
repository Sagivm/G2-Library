package control;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import boundry.ClientUI;
import entity.GeneralMessages;
import entity.Message;
import entity.ScreensInfo;
import entity.SearchBookResult;
import entity.User;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;



/** BookPageController. Responsible to show a specific book page.
 * @author itain
 */


public class BookPageController implements ScreensIF
{

	/**
	 * lable which contains book's name 
	 */
	@FXML private Label bookLable;
	
	/**
	 * lable which contains book's authors names 
	 */
	@FXML private Label authorsLable;
	
	/**
	 * lable which contains book's language 
	 */
	@FXML private Label languageLable;
	
	/**
	 * lable which contains book's domains 
	 */
	@FXML private Label domainsLable;
	
	/**
	 * lable which contains book's subjects 
	 */
	@FXML private Label subjectsLable;
	
	/**
	 * text area which contains book's key words 
	 */
	@FXML private TextArea keyWordsTextArea;
	
	/**
	 * text area which contains book's table of contents.
	 */
	@FXML private TextArea tocTextArea;
	
	/**
	 * text area which contains book's summary. 
	 */
	@FXML private TextArea summaryTextArea;
	
	/**
	 * lable which contains book's price 
	 */
	@FXML private Label priceLable;
	
	
	/**
	 * tab pane which contains book's details 
	 */
	@FXML private TabPane bookTabPane;
	
	/**
	 * tab for book's reviews page
	 */
	@FXML private Tab readReviewsTab;
	
	/**
	 * tab for writing a review page
	 */
	@FXML private Tab writeReviewTab;
	
	/**
	 * tab for book's report
	 */
	@FXML private Tab bookReportTab;
	
	/**
	 * tab which contains book's popularity report
	 */
	@FXML private Tab popularityReportTab;
	
	/**
	 * anchor pane which contains book's reviews page
	 */
	@FXML private AnchorPane readReviewContent;
	
	/**
	 * anchor pane which show book's writing a reviews page
	 */
	@FXML private AnchorPane writeReviewContent;
	
	/**
	 * anchor pane which show book's report page
	 */
	@FXML private AnchorPane bookReportContent;
	
	/**
	 * anchor pane which show book's popularity report page
	 */
	@FXML private AnchorPane popularityContent;
	
	/**
	 * image view which show book's image
	 */
	@FXML private ImageView imgBookImg;
	
	@FXML private Label lblBought;
	@FXML private Button btnPurchase;
	@FXML private Button btnDownload;
	
	/**
	 * button which takes user to last page
	 */
	@FXML private Button backButton;
	
	/**
	 * get the book's image from DB.
	 */
	public static ArrayList<String> img;
	
	/**
	 * States which buttons to show.
	 * 0: Can't allow to buy this book.
	 * 1: The user already buy this book.
	 * 2: The user allow to buy this book. User account per book.
	 * 3: The user allow to buy this book. The user is subscribed.
	 */
	public static String buyStatus="0";
	
	/**
	 * Checks if the user can write a review for this book.
	 */
	public static boolean canWrite = false;
	
	/**
	 * contains book's data to show on page
	 */
	public static SearchBookResult searchedBookPage;
	
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
	
	/**
	 * Get answer from DB if success.
	 */
	public static boolean success = false;
	
	/**
	 * Load book image from the path.
	 */
	private Image bookImage;
	
	/**
	 * Which screen load this screen.
	 */
	public static String previousPage = "";
	
	
	/** initializing data when page comes up
	 * @author itain
	 */
	@FXML
	public void initialize() 
	{
		int i;
		try{
			lblBought.setVisible(false);
			btnPurchase.setVisible(false);
			btnDownload.setVisible(false);
			btnPurchase.setDisable(true);
			btnDownload.setDisable(true);
			
			
			ArrayList<String> bookSn = new ArrayList<>();
			bookSn.add(searchedBookPage.getBookSn());
			Message message = prepareGetFromSQL(ActionType.GET_BOOK_IMG,bookSn);
			try {
				ClientController.clientConnectionController.sendToServer(message);
			} catch (IOException e) {	
				actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
			}
				
			Service<Void> service = new Service<Void>() {
		        @Override
		        protected Task<Void> createTask() {
		            return new Task<Void>() {           
		                @Override
		                protected Void call() throws Exception {                
		                    final CountDownLatch latch = new CountDownLatch(1);
		                    Platform.runLater(new Runnable() {                          
		                        @Override
		                        public void run() {
		                        	BookImgRecv recv = new BookImgRecv();
		                	        recv.start();
		                			synchronized(recv)
		                			{
		                				try {
		                					recv.wait();
		                				} catch (InterruptedException e1) {
		                					e1.printStackTrace();
		                				}
			                			
			                		    //show picture
			                        	if(img.get(0)!=null)
			                        	{
			                        		try {
				                		    String data = img.get(0).toString();
				                		    //String base64EncodedImage = data.split(",")[1];
				                		    String base64EncodedImage = data;
				                		    byte[] imageInBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64EncodedImage);
				                		    BufferedImage imgbuf;
			                				imgbuf = ImageIO.read(new ByteArrayInputStream(imageInBytes));
			                				Image image = SwingFXUtils.toFXImage(imgbuf, null);
			                				
			                				imgBookImg.setImage(image);
				                			} catch (Exception e1) {
				                				//e1.printStackTrace();
				                        		bookImage = new Image("/img/Books/no photo.png");
				                        		imgBookImg.setImage(bookImage);	
				                			}
			                        	}
			                        	else
			                        	{
			                        		bookImage = new Image("/img/Books/no photo.png");
			                        		imgBookImg.setImage(bookImage);	
			                        	}
			                		    //end show picture
		                        	}
								}
		                        });
		                     latch.await();                      
		                     return null;
		                   }
		                };
		            }
		        };
		        service.start();
			
			
			BookReviewsController bookReview = new BookReviewsController();
			bookReview.book = searchedBookPage;
			loadReviews();
			loadPopReport();
			loadBookReport();
			
			if(ClientUI.getTypeOfUser()=="User")
			{
				bookTabPane.getTabs().remove(4); //remove Book popularity report tab
				bookTabPane.getTabs().remove(3); //remove Book report tab
				
				User user = HomepageUserController.getConnectedUser();
				ArrayList<String> bookUser = new ArrayList<>();
				bookUser.add(user.getId());
				bookUser.add(searchedBookPage.getBookSn());
				
				message = prepareGetFromSQL(ActionType.GET_BUY_STATUS,bookUser);
				try {
					ClientController.clientConnectionController.sendToServer(message);
				} catch (IOException e) {	
					actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
				}
				service = new Service<Void>() {
			        @Override
			        protected Task<Void> createTask() {
			            return new Task<Void>() {           
			                @Override
			                protected Void call() throws Exception {                
			                    final CountDownLatch latch = new CountDownLatch(1);
			                    Platform.runLater(new Runnable() {                          
			                        @Override
			                        public void run() {
			                        	GetBuyStatusRecv recv = new GetBuyStatusRecv();
			                	        recv.start();
			                			synchronized(recv)
			                			{
			                				try {
			                					recv.wait();
			                				} catch (InterruptedException e1) {
			                					e1.printStackTrace();
			                				}
			                			
			                        		if(buyStatus.equals("1"))
			                        		{
			                        			lblBought.setVisible(true);
			                        			btnPurchase.setVisible(false);
			                        			btnPurchase.setDisable(true);
			                        			btnDownload.setVisible(true);
			                        			btnDownload.setDisable(false);
			                        		}
			                        		else if(buyStatus.equals("2") || buyStatus.equals("3"))
			                        		{
			                        			lblBought.setVisible(false);
			                        			btnPurchase.setVisible(true);
			                        			btnPurchase.setDisable(false);
			                        			btnDownload.setVisible(false);
			                        			btnDownload.setDisable(true);
			                        		}
			                        	}
									}
			                        });
			                     latch.await();                      
			                     return null;
			                   }
			                };
			            }
			        };
			        service.start();
				
				message = prepareGetFromSQL(ActionType.CHECK_WRITE_REVIEW,bookUser);
				try {
					ClientController.clientConnectionController.sendToServer(message);
				} catch (IOException e) {	
					actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
				}
				service = new Service<Void>() {
			        @Override
			        protected Task<Void> createTask() {
			            return new Task<Void>() {           
			                @Override
			                protected Void call() throws Exception {                
			                    final CountDownLatch latch = new CountDownLatch(1);
			                    Platform.runLater(new Runnable() {                          
			                        @Override
			                        public void run() {
			                        	CheckWriteReviewRecv recv = new CheckWriteReviewRecv();
			                	        recv.start();
			                			synchronized(recv)
			                			{
			                				try {
			                					recv.wait();
			                				} catch (InterruptedException e1) {
			                					e1.printStackTrace();
			                				}
			                			
				                        	if(canWrite)
				                        	{
				                        		WriteReviewController bookReview = new WriteReviewController();
				                        		bookReview.book = searchedBookPage;
				                    			try{
				                    				loadWriteReview();
				                    			} 
				                    			catch (Exception e) {
				                    				e.printStackTrace();
				                    			}
				                        	}
				                        	else
				                        	{
				                        		bookTabPane.getTabs().remove(2); // remove write review tab
				                        	}
			                        	}
									}
			                        });
			                     latch.await();                      
			                     return null;
			                   }
			                };
			            }
			        };
			        service.start();
				
			}
			else if(ClientUI.getTypeOfUser()=="Librarian")
			{
				bookTabPane.getTabs().remove(4); //remove Book popularity report tab
				bookTabPane.getTabs().remove(3); //remove Book report tab
				bookTabPane.getTabs().remove(2); // remove write review tab				
			}
			else
			{
				bookTabPane.getTabs().remove(2); // remove write review tab
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		bookLable.setText(searchedBookPage.getBookTitle());
		languageLable.setText(searchedBookPage.getBookLanguage());
		summaryTextArea.setText(searchedBookPage.getBookSummary());
		summaryTextArea.setEditable(false);
		summaryTextArea.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ; -fx-background-insets: 0;-fx-background-color: transparent, white, transparent, white;");
		summaryTextArea.setMaxWidth(500);
		summaryTextArea.setWrapText(true);
		tocTextArea.setText(searchedBookPage.getBookToc());
		tocTextArea.setEditable(false);
		tocTextArea.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ; -fx-background-insets: 0;-fx-background-color: transparent, white, transparent, white;");
		tocTextArea.setMaxWidth(500);
		tocTextArea.setWrapText(true);
		keyWordsTextArea.setText(searchedBookPage.getBookKeywords());
		keyWordsTextArea.setEditable(false);
		keyWordsTextArea.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ; -fx-background-insets: 0;-fx-background-color: transparent, white, transparent, white;");
		keyWordsTextArea.setMaxWidth(500);
		keyWordsTextArea.setWrapText(true);

		
		authorsLable.setText(searchedBookPage.getBookAuthors());

		subjectsLable.setText(searchedBookPage.getBookSubjects());
		domainsLable.setText(searchedBookPage.getBookDomains());

		priceLable.setText(searchedBookPage.getBookPrice()+ " \u20AA");
	}
	
	private void loadBookReport() {
		try {
			if(bookReportContent.getChildren().size()>0)
				bookReportContent.getChildren().remove(0);
			Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.BOOK_REPORT)); // SAGIV - CHANGE HERE USER REPORT SCREEN
			bookReportContent.getChildren().add(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadPopReport() {
		try {
			if(popularityContent.getChildren().size()>0)
				popularityContent.getChildren().remove(0);
			Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.BOOK_POPULARITY_REPORT)); // SAGIV - CHANGE HERE USER REPORT SCREEN
			popularityContent.getChildren().add(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Create's a message for get data from DB.
	 * @param type
	 * @param elementList
	 * @return
	 */
	public Message prepareGetFromSQL(ActionType type, ArrayList<String> elementList)
	{
		Message message = new Message();
		message.setType(type);
		message.setElementsList(elementList);
		return message;
	}
	

	/**
	 * Responsible to load the book's reviews screen.
	 * @throws IOException
	 */
	@FXML
	public void loadReviews() throws IOException {
				try {
					if(readReviewContent.getChildren().size()>0)
						readReviewContent.getChildren().remove(0);
					Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.BOOK_REVIEWS_SCREEN));
					readReviewContent.getChildren().add(root);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	
	/**
	 * Responsible to load the book's write review scrren.
	 * @throws IOException
	 */
	@FXML
	public void loadWriteReview() throws IOException {
				try {
					if(writeReviewContent.getChildren().size()>0)
						writeReviewContent.getChildren().remove(0);
					Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.WRITE_REVIEW_SCREEN));
					writeReviewContent.getChildren().add(root);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	
	
	/**
	 * This button responsible for purchasing the book.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void btnPurchasePressed(ActionEvent event) throws IOException{    
		try{
			boolean ans = yesNoDialog("Are you sure you want to buy this book?");
			if(ans == true)
			{
				/*
				 * Action list:
				 * 1: PerBook - buy book. - call to ExternalPaymentController.
				 * 2: Monthly - buy book. - call to PaymentController.
				 * 3: Yearly - buy book. - call to PaymentController.
				 * ---------------------
				 * Status list:
				 * 1: already bought this book.
				 * 2: user PerBook account type and allowed to buy this book.
				 * 3: user Subscribed account type and allowed to buy this book.
				 */
				if(buyStatus.equals("2")) //PerBook
				{
					ScreenController screenController = new ScreenController();
			        try {
			        	ExternalPaymentController extPayment = new ExternalPaymentController();
			        	extPayment.setProduct("Book - " + searchedBookPage.getBookTitle());
			        	extPayment.setPrice(searchedBookPage.getBookPrice());
			        	extPayment.setAction(1);	//buy book PerBook
			        	/*synch*/
			        	//extPayment.searchedBookPage = searchedBookPage;
			        	PaymentController.searchedBookPage = searchedBookPage;
			        	        			        	
						screenController.replaceSceneContent(ScreensInfo.EXTERNAL_PAYMENT_SCREEN,ScreensInfo.EXTERNAL_PAYMENT_TITLE);
						Stage primaryStage = screenController.getStage();
						ScreenController.setStage(primaryStage);
						Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
						primaryStage.show();
						primaryStage.setX(primaryScreenBounds.getMaxX()/2.0 - primaryStage.getWidth()/2.0);
						primaryStage.setY(primaryScreenBounds.getMaxY()/2.0 - primaryStage.getHeight()/2.0);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(buyStatus.equals("3"))
				{
					ArrayList<String> buyBook = new ArrayList<>();
					User user = HomepageUserController.getConnectedUser();
					buyBook.add(user.getId());
					buyBook.add(searchedBookPage.getBookSn());
					buyBook.add(searchedBookPage.getBookPrice());
					buyBook.add("3");	//buy book Subscribed
					
					Message message = prepareGetFromSQL(ActionType.BUY_BOOK,buyBook);
					try {
						ClientController.clientConnectionController.sendToServer(message);
					} catch (IOException e) {	
						actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
					}
					
					Service<Void> service = new Service<Void>() {
				        @Override
				        protected Task<Void> createTask() {
				            return new Task<Void>() {           
				                @Override
				                protected Void call() throws Exception {                
				                    final CountDownLatch latch = new CountDownLatch(1);
				                    Platform.runLater(new Runnable() {                          
				                        @Override
				                        public void run() { 	
				                        	BuyBookRecv recv = new BuyBookRecv();
				                	        recv.start();
				                			synchronized(recv)
				                			{
				                				try {
				                					recv.wait();
				                				} catch (InterruptedException e1) {
				                					e1.printStackTrace();
				                				}
					                        	if (userMain == null)
					                        		userMain = new HomepageUserController();
					                        	if(success == true)
					                        	{
					                        		userMain.setPage(ScreensInfo.BOOK_PAGE_SCREEN);
					                        	}
					                        	else
					                        	{
					                    			/*try {
					                    				TimeUnit.SECONDS.sleep(1);
					                    			} catch (InterruptedException e1) {
					                    				e1.printStackTrace();
					                    			}*/
					                    			userMain.setPage(ScreensInfo.BOOK_PAGE_SCREEN);
					                        	}
					                        	BookPageController bookPage = new BookPageController();
					                        	bookPage.searchedBookPage = searchedBookPage;
					                    		ScreenController screenController = new ScreenController();
					                        	
					                    		try{
					                    			screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_USER_SCREEN,ScreensInfo.HOMEPAGE_USER_TITLE);	
					                    		} 
					                    		catch (Exception e) {
					                    			System.out.println(e);
					                    			e.printStackTrace();
					                    		}
				                        	}	
										}
				                        });
				                     latch.await();                      
				                     return null;
				                   }
				                };
				            }
				        };
				        service.start();
					
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This buuton opens pop-up with 3 options of formats to download the book.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void btnDownloadPressed(ActionEvent event) throws IOException{    
		try{
			
			ArrayList<String> choices = new ArrayList<String>();
			choices.add("pdf");
			choices.add("docx");
			choices.add("bf2");

			ChoiceDialog<String> dialog = new ChoiceDialog<>("pdf", choices);
			dialog.setTitle(null);
			dialog.setHeaderText("Format choose:");
			dialog.setContentText("Format:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			String format = result.get();
		
			final Label labelSelectedDirectory = new Label();
			DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = 
                    directoryChooser.showDialog(ScreenController.getStage());
             
            if(selectedDirectory == null){
                labelSelectedDirectory.setText("No Directory selected");
            }else{
                labelSelectedDirectory.setText(selectedDirectory.getAbsolutePath());
            }
            char ot1='\\';
            char ot2='/';
            String path = labelSelectedDirectory.getText().replace(ot1,ot2);
            ArrayList <String> elementsList = new ArrayList<String>();         
            elementsList.add(path);         
            elementsList.add(searchedBookPage.getBookSn());        
            elementsList.add(format);
            Message message = new Message(ActionType.FILE,elementsList);
            actionToDisplay(ActionType.CONTINUE,"File successfully saved");
    		try {
    			ClientController.clientConnectionController.sendToServer(message);
    		} catch (IOException e) {	
            
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	} 
		finally 
		{
		
		}
	}

	
	/**
	 * An alert message that poped-up when the user click on "Purchase".
	 * @param message
	 * @return
	 */
	public boolean yesNoDialog(String message)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION, 
		            message,ButtonType.OK, ButtonType.CANCEL);
		alert.setTitle("Buy Book Dialog");
		alert.setHeaderText(null);
		alert.setContentText(message);
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK)
			return true;
		return false;
	}
	
	/** When pressed, takes user to search book result page.
	 * @author itain
	 * @param event - Gets event.
	 */
	@FXML
	public void backButtonPressed(ActionEvent event)
	{
		if(ClientUI.getTypeOfUser()=="Librarian")
    	{
        	if (librarianMain == null)
        		librarianMain = new HomepageLibrarianController();
        	librarianMain.setPage(ScreensInfo.SEARCH_BOOK_RESULTS_SCREEN);
    	}
    	else if(ClientUI.getTypeOfUser()=="Manager")
    	{
        	if (managerMain == null)
        		managerMain = new HomepageManagerController();
        	if(previousPage.equals("UserReport"))
        		managerMain.setPage(ScreensInfo.USER_REPORT);
        	else
        		managerMain.setPage(ScreensInfo.SEARCH_BOOK_RESULTS_SCREEN);
    	}
    	else if(ClientUI.getTypeOfUser()=="User")
    	{
        	if (userMain == null)
        		userMain = new HomepageUserController();
        	if(previousPage.equals("UserReport"))
        		userMain.setPage(ScreensInfo.USER_REPORT);
        	else
        		userMain.setPage(ScreensInfo.SEARCH_BOOK_RESULTS_SCREEN);
    	}
		SearchBookResultsRecv.canContinue = true;
		previousPage = "";
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

	@Override
	public void pressedCloseMenu(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionOnError(ActionType type, String errorCode) {
		// TODO Auto-generated method stub
	}
	
	/** shows an alert to screen
	 * @author itain
	 * @param type - Gets type of action.
	 * @param message - Gets message to display.
	 */
	public void actionToDisplay(ActionType type, String message) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		if (type == ActionType.TERMINATE) {
			Platform.exit();
			System.exit(1);
		}
		if (type == ActionType.CONTINUE)
			return;
	}

}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class BookPageRecv extends Thread{
	
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

/**This class makes sure the information from the server was received successfully.
 * @author ork
 *
 */
class GetBuyStatusRecv extends Thread{
	
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

/**This class makes sure the information from the server was received successfully.
 * @author ork
 *
 */
class CheckWriteReviewRecv extends Thread{
	
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

/**This class makes sure the information from the server was received successfully.
 * @author ork
 *
 */
class BuyBookRecv extends Thread{
	
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

/**This class makes sure the information from the server was received successfully.
 * @author ork
 *
 */
class BookImgRecv extends Thread{
	
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
