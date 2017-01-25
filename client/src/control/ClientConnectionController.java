package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import boundry.ClientUI;
import entity.Author;
import entity.Domain;
import entity.FileEvent;
import entity.GeneralMessages;
import entity.Replay;
import entity.ScreensInfo;
import entity.User;
import entity.Worker;
import enums.ActionType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ocsf.client.AbstractClient;

//TODO: Auto-generated Javadoc
/**
 * ClientConnectionController create instance to client, and presave into
 * ClientController with static variable for communication later with the GUI
 * and clients.
 * 
 * @author nire
 */
public class ClientConnectionController extends AbstractClient{

	/**
	 * The main
	 */
	private static ClientUI clientMain = null;
	
	
	public static boolean canContinue = false;
	
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
	 *  File variable
	 */
	private File dstFile = null;
	
	
	/**
	 * File outputstream variable
	 */
	private FileOutputStream fileOutputStream = null;
	
	/**
	 * ClientConnectionController constructor initialize the hostname, and then
	 * the port for establish connection to server. it also open new connection
	 * physically for it.
	 * @param host - Gets The hostname/ip for connection.
	 * @param port - Gets The port for establish connection
	 */
	public ClientConnectionController(String host, int port) {
		super(host, port);
		try {
			openConnection();
		} catch (IOException e) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(GeneralMessages.SERVER_OFFLINE);
			alert.showAndWait();
			Platform.exit();
			System.exit(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ocsf.client.AbstractClient#handleMessageFromServer(java.lang.Object)
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
		Replay replay = (Replay) msg;
		actionToPerform(replay);
	}

	/**
	 * This function gets the replay from server and decrypt to action by his
	 * information.
	 * 
	 * @param replay
	 *            - Gets the replay with the data.
	 */
	public void actionToPerform(Replay replay)  {
		ActionType transmitType = replay.getTransmitType();
		ActionType type = replay.getType();
		FileEvent fileEvent = replay.getFileEvent();
		boolean success = replay.getSucess();
		
		switch (type) {
		
		case FILE: {
			downloadFile(fileEvent);
			DownloadFileRecv.canContinue = true;
		}
		break;
		case GET_MESSAGES: {
			
			ArrayList<String> list = new ArrayList<String>();
			list=replay.getElementsList();
			MessagesController.messagesResult = list;
			MessagesRecv.canContinue = true;
		}
		break;
		
		case REGISTER: {
			if (success == true) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						ScreenController screenController = new ScreenController();
						try {
							actionToDisplay(ActionType.CONTINUE, GeneralMessages.PENDING_FOR_LIBRARIAN);
							screenController.replaceSceneContent(ScreensInfo.CLIENT_SCREEN, ScreensInfo.CLIENT_TITLE);
						} catch (Exception e) {
							// COMPELETE
							e.printStackTrace();
						}
					}
				});
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						ScreenController screenController = new ScreenController();
						try {
							actionToDisplay(ActionType.CONTINUE, GeneralMessages.USER_ALREADY_EXISTS);
							screenController.replaceSceneContent(ScreensInfo.CLIENT_SCREEN, ScreensInfo.CLIENT_TITLE);
						} catch (Exception e) {
							// COMPELETE
							e.printStackTrace();
						}
					}
				});
			}
			
			RegisterRecv.canContinue = true;
			break;
		}
		case LOGIN: {
			if (success == true) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						ScreenController screenController = new ScreenController();
						try {
							int action = replay.getAction();
							actionToDisplay(ActionType.CONTINUE, GeneralMessages.USER_LOGGED_IN_SUCESSFULLY);
							if (action == 1) {
								User user = new User(replay.getElementsList().get(1).toString(),
										replay.getElementsList().get(2).toString(),
										replay.getElementsList().get(0).toString(),
										replay.getElementsList().get(3).toString(),
										replay.getElementsList().get(4).toString(),
										replay.getElementsList().get(5).toString());
								HomepageUserController userPage = new HomepageUserController();
								userPage.setConnectedUser(user);
								screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_USER_SCREEN,
										ScreensInfo.HOMEPAGE_USER_TITLE);
							} else if (action == 2) {
								Worker worker = new Worker(replay.getElementsList().get(1).toString(),
										replay.getElementsList().get(2).toString(),
										replay.getElementsList().get(0).toString(),
										replay.getElementsList().get(3).toString(),
										replay.getElementsList().get(4).toString(),
										replay.getElementsList().get(5).toString(),
										replay.getElementsList().get(6).toString());
								screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_LIBRARIAN_SCREEN,
										ScreensInfo.HOMEPAGE_LIBRARIAN_TITLE);
								HomepageLibrarianController liberianPage = new HomepageLibrarianController();
								liberianPage.setConnectedlibrarian(worker);
							} else if (action == 3) {
								Worker worker = new Worker(replay.getElementsList().get(1).toString(),
										replay.getElementsList().get(2).toString(),
										replay.getElementsList().get(0).toString(),
										replay.getElementsList().get(3).toString(),
										replay.getElementsList().get(4).toString(),
										replay.getElementsList().get(5).toString(),
										replay.getElementsList().get(6).toString());
								screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_MANAGER_SCREEN,
										ScreensInfo.HOMEPAGE_MANAGER_TITLE);
								HomepageManagerController managerPage = new HomepageManagerController();
								managerPage.setConnectedManager(worker);
							}
							if (clientMain == null)
								clientMain = new ClientUI();
							clientMain.setIsConnected(true);
							if (action == 1)
								clientMain.setTypeOfUser("User");
							else if (action == 2)
								clientMain.setTypeOfUser("Librarian");
							else if (action == 3)
								clientMain.setTypeOfUser("Manager");
							centerWindow(screenController);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						ScreenController screenController = new ScreenController();
						try {
								actionToDisplay(ActionType.CONTINUE, replay.getGnrlMsg().toString());

						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				});
			}
			
			ClientRecv.canContinue = true;
			break;
		}
		case LOGOUT: {
			if (success == true) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						ScreenController screenController = new ScreenController();
						try {
							screenController.replaceSceneContent(ScreensInfo.CLIENT_SCREEN, ScreensInfo.CLIENT_TITLE);
							centerWindow(screenController);
							if (clientMain == null)
								clientMain = new ClientUI();
							clientMain.setIsConnected(false);
							clientMain.setTypeOfUser(null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			
			HomepageLibrarianRecv.canContinue = true;
			HomepageManagerRecv.canContinue = true;
			HomepageUserRecv.canContinue = true;
			LogoutRecv.canContinue = true;
			break;
		}
		case ACCOUNTTYPEREQ: {
			if (success == true) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						ScreenController screenController = new ScreenController();
						try {
							actionToDisplay(ActionType.CONTINUE, GeneralMessages.PENDING_FOR_LIBRARIAN);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			break;
		}

		case GET_PENDING_USERS: {
			PendingRegistrationController.pendingUsersList = replay.getElementsList();
			PendingRegistrationRecv.canContinue = true;
			break;
		}
		
		case SEARCH_USER:{
			ArrayList<String> list = new ArrayList<String>();
			list=replay.getElementsList();
			SearchUserResultsController.userResult = list;
			SearchUserRecv.canContinue=true;
			break;
		}
		
		case SEARCH_WORKER:{
			ArrayList<String> list = new ArrayList<String>();
			list=replay.getElementsList();
			SearchWorkerResultsController.workerResult = list;
			SearchWorkerResultsRecv.canContinue = true;
			break;
		}
		
		case GET_BOOK_AUTHORS:{
			ArrayList<Author> list = new ArrayList<Author>();
			for (int i = 0; i < replay.getElementsList().size(); i++) {
				String tmp[] = new String[3];
				tmp = replay.getElementsList().get(i).split("\\^");
				Author author = new Author(tmp[1], tmp[2], tmp[0]);
				list.add(author);
			}

			BookManagementController.selectedAuthorsString = list;
			BookManagermentRecv.canContinue = true;
			
			break;
		}
		
		case SEARCH_BOOK_AND:{
		    
			ArrayList<String> list = new ArrayList<String>();
			list=replay.getElementsList();
			SearchBookResultsController.resultList = list;
			SearchBookResultsRecv.canContinue = true;
			break;
		}
		
		case SEARCH_BOOK_OR:{

			ArrayList<String> list = new ArrayList<String>();
			list=replay.getElementsList();
			SearchBookResultsController.resultList = list;
			SearchBookResultsRecv.canContinue = true;
			break;
		}
		
		case EDIT_USER_LIBRARIAN:{
			if (success == true) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (success == true)
							actionToDisplay(ActionType.CONTINUE, GeneralMessages.OPERATION_SUCCEEDED);
						else
							actionToDisplay(ActionType.CONTINUE, GeneralMessages.OPERATION_FAILED);
							
						//System.out.println("test");
						UserPageController.searchedUserPage.setFirstName(EditUserLibrarianController.userDetailsToChange.getFirstName());
						UserPageController.searchedUserPage.setLastName(EditUserLibrarianController.userDetailsToChange.getLastName());
						SearchUserController.updateSearchUserResults=2;
						if(ClientUI.getTypeOfUser()=="Librarian")
                    	{
                        	if (librarianMain == null)
                        		librarianMain = new HomepageLibrarianController();
                        	librarianMain.setPage(ScreensInfo.USER_PAGE_SCREEN);
                    	}						
						
						ScreenController screenController  = new ScreenController();
						try{
							
                			if(ClientUI.getTypeOfUser()=="Librarian")
                				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_LIBRARIAN_SCREEN,ScreensInfo.HOMEPAGE_LIBRARIAN_TITLE);						

                			
                		} 
                		catch (Exception e) {
        					e.printStackTrace();
        				}  
					}
				});
			} 

			break;
		}
		
		case EDIT_USER_MANAGER:{
			if (success == true) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (success == true)
							actionToDisplay(ActionType.CONTINUE, GeneralMessages.OPERATION_SUCCEEDED);
						else
							actionToDisplay(ActionType.CONTINUE, GeneralMessages.OPERATION_FAILED);
							
						if(ClientUI.getTypeOfUser()=="Manager")
                    	{
                        	if (managerMain == null)
                        		managerMain = new HomepageManagerController();
                        	if(UserPageController.searchedUserPage.getIsBlocked().equals("YES"))
                        		UserPageController.searchedUserPage.setIsBlocked("NO");
                        	else if(UserPageController.searchedUserPage.getIsBlocked().equals("NO"))
                        		UserPageController.searchedUserPage.setIsBlocked("YES");
                        	SearchUserController.updateSearchUserResults=1;
                        	managerMain.setPage(ScreensInfo.USER_PAGE_SCREEN);
                    	}
						
						
						ScreenController screenController  = new ScreenController();
						try{
							
                			if(ClientUI.getTypeOfUser()=="Manager")
                				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_MANAGER_SCREEN,ScreensInfo.HOMEPAGE_MANAGER_TITLE);
                			
                		} 
                		catch (Exception e) {
        					e.printStackTrace();
        				}  
					}
				});
			} 

			break;
		}
		

		case GET_AUTHORS: {
			ArrayList<Author> list = new ArrayList<Author>();
			for (int i = 0; i < replay.getElementsList().size(); i++) {
				String tmp[] = new String[3];
				tmp = replay.getElementsList().get(i).split("\\^");
				Author author = new Author(tmp[1], tmp[2], tmp[0]);
				list.add(author);
			}
			SearchBookController.authorList = list;
			BookManagementController.authorList = list;
			SearchBookAuthorsRecv.canContinue = true;
			BookManagermentRecv.canContinue =true;
			break;
		}
		
		case GET_NUMBER_BOOK_AT_DOMAIN: {
			BookManagementController.countBookByDomain = Integer.parseInt(replay.getElementsList().get(0));
			BookManagermentRecv.canContinue =true;
			break;
		}
		
		case GET_NUMBER_BOOK_OF_AUTHOR: {
			BookManagementController.countBookOfUser = Integer.parseInt(replay.getElementsList().get(0));
			BookManagermentRecv.canContinue = true;
			break;
		}
		
		
		case GET_NUMBER_BOOK_AT_SUBJECT: {
			BookManagementController.countBookBySubject = Integer.parseInt(replay.getElementsList().get(0));
			BookManagermentRecv.canContinue = true;
			break;
		}

		case GET_DOMAINS: {
			SearchBookController.domainList = replay.getElementsList();
			SearchBookDomainsRecv.canContinue = true;
			BookManagermentRecv.canContinue = true;
			break;
		}
		
		case GET_DOMAINS_WITH_ID: {
			BookManagementController.domainsList = replay.getElementsList();
			BookManagermentRecv.canContinue=true;
			break;
		}
		
		case GET_SUBJECTS_INFO: {
			BookManagementController.subjectsList = replay.getElementsList();
			BookManagermentRecv.canContinue=true;
			break;
		}
		
		case GET_SUBJECTS: {
			BookManagementController.subjectList = replay.getElementsList();
			BookManagementController.subjectListOfBook = replay.getElementsList();
			BookManagermentRecv.canContinue=true;
			break;
		}
		
		case GET_BOOK_SUBJETCS: {
			BookManagementController.selectedSubjectString = replay.getElementsList();
			BookManagermentRecv.canContinue=true;
			break;
		}

		case PENDING_REVIEWS: {
			PendingReviewsController.pendingReviewList = replay.getElementsList();
			PendingReviewsRecv.canContinue = true;
			//PendingReviewsController.pendingReviewList = replay.getElementsList();
			break;
		}
		case USEREPORT: {
			if (success == true)
				UserReportController.data = replay.getElementsList();
			else
				UserReportController.data = null;
			break;
		}
		case GETDOMAINSSPECIFIC: {
			BookPopularityReportController.domainsdata = replay.getElementsList();
			break;
		}
		case POPULARITYREPORT: {
			if (success == true)
				BookPopularityReportController.data = replay.getElementsList();
			else
				BookPopularityReportController.data = null;
			break;
		}
		
		case UPDATE_REVIEW_STATUS: {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(ClientUI.getTypeOfUser()=="Librarian")
						HomepageLibrarianController.setPage(ScreensInfo.PENDING_REVIEWS_SCREEN);
					else
						HomepageManagerController.setPage(ScreensInfo.PENDING_REVIEWS_SCREEN);
					ScreenController screenController = new ScreenController();
					try{
						if (success == true)
						{
							actionToDisplay(ActionType.CONTINUE,GeneralMessages.OPERATION_SUCCEEDED);
							if(ClientUI.getTypeOfUser()=="Librarian")
								screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_LIBRARIAN_SCREEN,ScreensInfo.HOMEPAGE_LIBRARIAN_TITLE);
							else
								screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_MANAGER_SCREEN,ScreensInfo.HOMEPAGE_MANAGER_TITLE);
						}
						else
						{
							actionToDisplay(ActionType.CONTINUE,GeneralMessages.OPERATION_FAILED);		
						}
					}
		    		catch (Exception e) {
						e.printStackTrace();
					} 
				}
			});
			break;
		}
		case BOOKREPORT: {
			BookReportController.data=replay.getElementsList();
			break;

		}
		case BOOK_REVIEWS: {
			BookReviewsController.data=replay.getElementsList();
			BookReviewsRecv.canContinue = true;
			break;
		}
		
		case GET_BOOK_LANGUAGE: {
			BookManagementController.editBookLanguage=replay.getElementsList().get(0);
			BookManagermentRecv.canContinue = true;
			//System.out.println(replay.getElementsList().get(0));
			break;	
		}
		
		case GET_BOOK_TABLE_OF_CONTENT: {
			BookManagementController.editBookTableOfContant=replay.getElementsList().get(0);
			//System.out.println(replay.getElementsList().get(0));
			BookManagermentRecv.canContinue = true;
			break;	
		}
		
		case WRITE_REVIEW: {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					
					String currUsername;
					int flag=0;
					if (clientMain.getTypeOfUser().equals("User")) {
						currUsername = HomepageUserController.getConnectedUser().getId();
						flag=1;
					} else if (clientMain.getTypeOfUser().equals("Librarian")) {
						currUsername = HomepageLibrarianController.getConnectedlibrarian().getId();
						flag=2;
					} else {
						currUsername = HomepageManagerController.getConnectedManager().getId();
						flag=3;
					}
					
					if (currUsername.equals(replay.getElementsList().get(0)))
					{
						HomepageUserController.setPage(null);
						ScreenController screenController = new ScreenController();
						try{
							if (success == true)
							{
								actionToDisplay(ActionType.CONTINUE,GeneralMessages.OPERATION_SUCCEEDED);
								screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_USER_SCREEN,ScreensInfo.HOMEPAGE_USER_TITLE);
							}
							else
							{
								actionToDisplay(ActionType.CONTINUE,GeneralMessages.OPERATION_FAILED);	
							}
						}
			    		catch (Exception e) {
							e.printStackTrace();
						}
					} else
					{
						if (flag == 2)
						actionToDisplay(ActionType.CONTINUE,GeneralMessages.YOU_GOT_NEW_REVIEWS);
					}
				}
			});
			break;

		}
		
		case GET_BOOK_LIST: {
			BookManagementController.BooksList = replay.getElementsList();
			BookManagermentRecv.canContinue = true;
			break;
		}
		
		case GET_PENDING_ACCOUNTS: {
			PendingAccountTypeController.pendingAccountList = replay.getElementsList();
			PendingAccountTypeRecv.canContinue = true;
			break;
		}
		
		case CHECK_WRITE_REVIEW: {
			BookPageController.canWrite = replay.getSucess();
			CheckWriteReviewRecv.canContinue = true;
			break;
		}
		
		case GET_BOOK_IMG: {
			if(success)
				BookPageController.img = replay.getElementsList();
			else
				BookPageController.img = null;
			BookImgRecv.canContinue = true;
			break;
		}
		case GET_TOTAL_PRICE: {
			if(success)
				BookPopularityReportController.priceList=replay.getElementsList();
			else
				BookPopularityReportController.priceList = null;
			break;
		}
		
		case GET_BUY_STATUS: {
				BookPageController.buyStatus = replay.getGnrlMsg();
				GetBuyStatusRecv.canContinue = true;
			break;
		}
		
		case BUY_BOOK: {
			PaymentController.success = success;
			PaymentRecv.canContinue = true;
		break;
		}
		
		case BUY_BOOK_SUBS: {
			BookPageController.success = success;
			BuyBookRecv.canContinue = true;
		break;
		}
		
		case BUY_SUBSCRIPTION: {
			PaymentController.success = success;
			PaymentRecv.canContinue = true;
		break;
		}
		
		case CHECK_ACCOUNT_TYPE: {
			if(success)
			{
				HomepageUserController.success = true;
				HomepageUserController.setSubscription(replay.getElementsList());
			}
			else
				HomepageUserController.success = false;
			
			HomepageUserRecv.canContinue = true;
		break;
		}	
		case MY_BOOKS: {
			if (success == true)
				MyBooksController.data = replay.getElementsList();
			else
				MyBooksController.data = null;
			break;
		}
		}

	}

	/**
	 * This function choose what to display the user.
	 * @param type - Gets the type of action after display.
	 * @param message - Gets the message to display in popup.
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

	/**
	 * Set the window in the center of the screen.
	 * @param screenController - Gets the screencontroller
	 */
	public void centerWindow(ScreenController screenController) {
		Stage primaryStage = screenController.getStage();
		ScreenController.setStage(primaryStage);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.show();
		primaryStage.setX(primaryScreenBounds.getMaxX() / 2.0 - primaryStage.getWidth() / 2.0);
		primaryStage.setY(primaryScreenBounds.getMaxY() / 2.0 - primaryStage.getHeight() / 2.0);
	}

	/**
	 * The function decode the file and download into the user's computer.
	 * @param fileEvent - Gets the file.
	 */
	public void downloadFile(FileEvent fileEvent) {
		try {
		String outputFile = fileEvent.getDestinationDirectory() + fileEvent.getFilename();
		if (!new File(fileEvent.getDestinationDirectory()).exists()) {
		new File(fileEvent.getDestinationDirectory()).mkdirs();
		}
		dstFile = new File(outputFile);
		fileOutputStream = new FileOutputStream(dstFile);
		fileOutputStream.write(fileEvent.getFileData());
		fileOutputStream.flush();
		fileOutputStream.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
}
