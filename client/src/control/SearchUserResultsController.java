package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.sun.javafx.scene.control.skin.TableHeaderRow;

import boundry.ClientUI;
import entity.AccountType;
import entity.Book;
import entity.Message;
import entity.Review;
import entity.ScreensInfo;
import entity.SearchBookResult;
import entity.SearchUserResult;
import entity.User;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;



/** SearchUserResultsController. Responsible to show user search results and do actions on them.
 * @author itain
 */
public class SearchUserResultsController implements ScreensIF{
	
	/**
	 * shows results table  
	 */
	@FXML private TableView resultsTable;
	
	/**
	 * shows usernames  
	 */
	@FXML private TableColumn usernameCol;
	
	/**
	 * shows first names
	 */
	@FXML private TableColumn fNameCol;
	
	/**
	 * shows last names
	 */
	@FXML private TableColumn lNameCol;
	
	/**
	 * shows accounts types
	 */
	@FXML private TableColumn accountTypeCol;
	
	/**
	 * shows accounts statuses  
	 */
	@FXML private TableColumn accountStatusCol;
	
	/**
	 * Column for button that leads to user page  
	 */
	@FXML private TableColumn userPageCol;
	
	/**
	 * block status of user 
	 */
	@FXML private TableColumn isBlockedCol;
	
	/**
	 * Back button to users search page
	 */
	@FXML private Button backButton;
	
	/**
	 * image for entering user page button
	 */
	private final Image enterImage = new Image("/img/enter.png");
	
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
	 * holds users data from worker's search
	 */
	public static ArrayList<String> userResult;
	
	/**
	 * holds users data from worker's search in order to show on the results table
	 */
	private ObservableList<SearchUserResult> data = FXCollections.observableArrayList();
			
	
	/** initializing data when page comes up
	 * @author itain
	 */
	@FXML
	private void initialize(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				SearchUserResultsRecv recv_searchUser = new SearchUserResultsRecv();
				recv_searchUser.start();
				synchronized (recv_searchUser) {
					try{
						recv_searchUser.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
					if(SearchUserController.updateSearchUserResults==1) //updateBlockStatus
					{
						for(int i=0;i<userResult.size();i++)
						{
							String[] tmp=new String[9];
							tmp = userResult.get(i).split("\\^");
							
							if(tmp[0].equals(UserPageController.searchedUserPage.getUsername()))
							{
								userResult.remove(i);
								String isBlocked="0";
								if(UserPageController.searchedUserPage.getIsBlocked()=="YES")
									isBlocked="1";
								userResult.add(tmp[0]+"^"+tmp[1]+"^"+tmp[2]+"^"+tmp[3]+"^"+tmp[4]+"^"+isBlocked+"^"+tmp[6]+"^"+tmp[7]+"^"+tmp[8]);
								SearchUserController.updateSearchUserResults=0;
								break;
								
							}
						}
						
					}
					else if(SearchUserController.updateSearchUserResults==2) //edit User's full name
					{
						for(int i=0;i<userResult.size();i++)
						{
							String[] tmp=new String[9];
							tmp = userResult.get(i).split("\\^");
							if(tmp[0].equals(UserPageController.searchedUserPage.getUsername()))
							{
								userResult.remove(i);
								userResult.add(tmp[0]+"^"+UserPageController.searchedUserPage.getFirstName()+"^"+UserPageController.searchedUserPage.getLastName()+"^"+tmp[3]+"^"+tmp[4]+"^"+tmp[5]+"^"+tmp[6]+"^"+tmp[7]+"^"+tmp[8]);
								SearchUserController.updateSearchUserResults=0;
								break;
								
							}
						}
						
					}
					
					
			
							try {
								try{
									for(int i=0;i<userResult.size();i++)
									{
										String[] tmp=new String[9];
										tmp = userResult.get(i).split("\\^");
										String isBlocked="NO";
										if(Integer.parseInt(tmp[5])==1)
											isBlocked="YES";
										data.add(new SearchUserResult(tmp[0],tmp[1],tmp[2],tmp[3],tmp[4], isBlocked, tmp[6],tmp[7],tmp[8]));
									}
								}
								 catch (Exception e1) {
									e1.printStackTrace();
									
								}
													
								fNameCol.setSortType(TableColumn.SortType.ASCENDING);
								lNameCol.setSortType(TableColumn.SortType.ASCENDING);
								
								resultsTable.setEditable(false);
													
								usernameCol.setCellValueFactory(
						                new PropertyValueFactory<SearchUserResult, String>("username"));
								
								fNameCol.setCellValueFactory(
						                new PropertyValueFactory<SearchUserResult, String>("firstName"));
								
								lNameCol.setCellValueFactory(
						                new PropertyValueFactory<SearchUserResult, String>("lastName"));
					
								accountTypeCol.setCellValueFactory(
						                new PropertyValueFactory<SearchUserResult, String>("accountType"));
								
								accountStatusCol.setCellValueFactory(
						                new PropertyValueFactory<SearchUserResult, String>("accountStatus"));
								
								isBlockedCol.setCellValueFactory(
						                new PropertyValueFactory<SearchUserResult, String>("isBlocked"));
								
								
								
								userPageCol.setCellValueFactory(new Callback<CellDataFeatures<SearchUserResult, SearchUserResult>, ObservableValue<SearchUserResult>>() {
							          @Override public ObservableValue<SearchUserResult> call(CellDataFeatures<SearchUserResult, SearchUserResult> features) {
							              return new ReadOnlyObjectWrapper(features.getValue());
							          }
							        });
								
								userPageCol.setCellFactory(new Callback<TableColumn<SearchUserResult, SearchUserResult>, TableCell<SearchUserResult, SearchUserResult>>() {
							          @Override public TableCell<SearchUserResult, SearchUserResult> call(TableColumn<SearchUserResult, SearchUserResult> userPageCol) {
							            return new TableCell<SearchUserResult, SearchUserResult>() {
							              final ImageView buttonGraphic = new ImageView();
							              final Button button = new Button(); {
							                button.setGraphic(buttonGraphic);
							                button.setPrefWidth(5);
							              }
							            
							              @Override public void updateItem(final SearchUserResult userRes, boolean empty) {
							                  super.updateItem(userRes, empty);
							                  if (userRes != null) {
							                    buttonGraphic.setImage(enterImage);
							                    setGraphic(button);
							                    button.setOnAction(new EventHandler<ActionEvent>() {
							                      @Override public void handle(ActionEvent event) {
							                    	  UserPageController.searchedUserPage = userRes;
							                    	  //UserPageRecv = true;
			
							                    	  
							          				if(ClientUI.getTypeOfUser()=="Librarian")
							                    	{
							          					
							                        	if (librarianMain == null)
							                        		librarianMain = new HomepageLibrarianController();
							                        	librarianMain.setPage(ScreensInfo.USER_PAGE_SCREEN);
							                    	}
							                    	else if(ClientUI.getTypeOfUser()=="Manager")
							                    	{
							                    		   
							                        	if (managerMain == null)
							                        		managerMain = new HomepageManagerController();
							                        	managerMain.setPage(ScreensInfo.USER_PAGE_SCREEN);
							                    	}
			
							                		
							                		ScreenController screenController = new ScreenController();
							                		try{
							                			if(ClientUI.getTypeOfUser()=="Librarian")
							                			{
							                				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_LIBRARIAN_SCREEN,ScreensInfo.HOMEPAGE_LIBRARIAN_TITLE);						
							                			}
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
								
								
								usernameCol.setStyle( "-fx-alignment: CENTER;");
								fNameCol.setStyle( "-fx-alignment: CENTER;");
								lNameCol.setStyle( "-fx-alignment: CENTER;");
								accountTypeCol.setStyle( "-fx-alignment: CENTER;");
								accountStatusCol.setStyle( "-fx-alignment: CENTER;");
								isBlockedCol.setStyle( "-fx-alignment: CENTER;");
								userPageCol.setStyle( "-fx-alignment: CENTER;");
								
								resultsTable.setItems(data);
								resultsTable.getSortOrder().add(fNameCol);
								resultsTable.getSortOrder().add(lNameCol);
								
							}		catch (Exception e) {
								e.printStackTrace();
							}  
				}
			}});
		System.out.println("test2");
			

		
	}
	
	/** When pressed, takes worker to workers search page.
	 * @author itain
	 * @param event - Gets event.
	 */
	public void backButtonPressed(ActionEvent event) {
		if(ClientUI.getTypeOfUser()=="Librarian")
    	{
        	if (librarianMain == null)
        		librarianMain = new HomepageLibrarianController();
        	librarianMain.setPage(ScreensInfo.SEARCH_USER_SCREEN);
    	}
    	else if(ClientUI.getTypeOfUser()=="Manager")
    	{
        	if (managerMain == null)
        		managerMain = new HomepageManagerController();
        	managerMain.setPage(ScreensInfo.SEARCH_USER_SCREEN);
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

	@Override
	public void pressedCloseMenu(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionOnError(ActionType type, String errorCode) {
		// TODO Auto-generated method stub
		
	}
	
	

}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class SearchUserResultsRecv extends Thread{
	
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


