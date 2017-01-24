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



/** SearchWorkerResultsController. Responsible to show worker search results
 * @author itain
 */
public class SearchWorkerResultsController implements ScreensIF{
	
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
	 * shows emails
	 */
	@FXML private TableColumn emailCol;
	
	/**
	 * shows jobs
	 */
	@FXML private TableColumn jobCol;
	
	/**
	 * shows depatments
	 */
	@FXML private TableColumn departmentCol;
	
	/**
	 * Back button to workers search page
	 */
	@FXML private Button backButton;
	

	/**
	 * static reference of librarian home page.
	 */
	private static HomepageLibrarianController librarianMain;
	
	/**
	 * static reference of manager home page.
	 */
	private static HomepageManagerController managerMain;
	
	/**
	 * holds workers data from worker's search
	 */
	public static ArrayList<String> workerResult;
	
	/**
	 * holds workers data from worker's search in order to show on the results table
	 */
	private ObservableList<SearchWorkerResult> data = FXCollections.observableArrayList();
			
	
	
	/** initializing data when page comes up
	 * @author itain
	 */
	@FXML
	private void initialize(){
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				SearchWorkerResultsRecv recv = new SearchWorkerResultsRecv();
				recv.start();
				synchronized (recv) {
					try{
						recv.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}

				try {
					try{
						for(int i=0;i<workerResult.size();i++)
						{
							String[] tmp=new String[6];
							tmp = workerResult.get(i).split("\\^");
							data.add(new SearchWorkerResult(tmp[0],tmp[1],tmp[2],tmp[3],tmp[4], tmp[5]));
						}
					}
					 catch (Exception e1) {
						 e1.printStackTrace();
					}
										
					fNameCol.setSortType(TableColumn.SortType.ASCENDING);
					lNameCol.setSortType(TableColumn.SortType.ASCENDING);
					
					resultsTable.setEditable(false);
										
					usernameCol.setCellValueFactory(
			                new PropertyValueFactory<SearchWorkerResult, String>("username"));
					
					fNameCol.setCellValueFactory(
			                new PropertyValueFactory<SearchWorkerResult, String>("firstName"));
					
					lNameCol.setCellValueFactory(
			                new PropertyValueFactory<SearchWorkerResult, String>("lastName"));
		
					emailCol.setCellValueFactory(
			                new PropertyValueFactory<SearchWorkerResult, String>("email"));
					
					departmentCol.setCellValueFactory(
			                new PropertyValueFactory<SearchWorkerResult, String>("department"));
					
					jobCol.setCellValueFactory(
			                new PropertyValueFactory<SearchWorkerResult, String>("job"));

					usernameCol.setStyle( "-fx-alignment: CENTER;");
					fNameCol.setStyle( "-fx-alignment: CENTER;");
					lNameCol.setStyle( "-fx-alignment: CENTER;");
					emailCol.setStyle( "-fx-alignment: CENTER;");
					departmentCol.setStyle( "-fx-alignment: CENTER;");
					jobCol.setStyle( "-fx-alignment: CENTER;");

					
					resultsTable.setItems(data);
					resultsTable.getSortOrder().add(fNameCol);
					resultsTable.getSortOrder().add(lNameCol);
					
				}		catch (Exception e) {
					e.printStackTrace();
				}  
			}
		});
		
	}
	
	
	
	
	public void backButtonPressed(ActionEvent event) {
		if(ClientUI.getTypeOfUser()=="Librarian")
    	{
        	if (librarianMain == null)
        		librarianMain = new HomepageLibrarianController();
        	librarianMain.setPage(ScreensInfo.SEARCH_WORKER_SCREEN);
    	}
    	else if(ClientUI.getTypeOfUser()=="Manager")
    	{
        	if (managerMain == null)
        		managerMain = new HomepageManagerController();
        	managerMain.setPage(ScreensInfo.SEARCH_WORKER_SCREEN);
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
	
	
	/**
	 * stores the search worker result details.
	 * @author itain
	 */
	public static class SearchWorkerResult {

	    private final SimpleStringProperty username;
	    private final SimpleStringProperty firstName;
	    private final SimpleStringProperty lastName;
	    private final SimpleStringProperty email;
	    private final SimpleStringProperty job;
	    private final SimpleStringProperty department;


	    public SearchWorkerResult(String username, String firstName, String lastName, String email, String job, String department) {
	        this.username = new SimpleStringProperty(username);
	        this.firstName = new SimpleStringProperty(firstName);
	        this.lastName = new SimpleStringProperty(lastName);
	        this.email = new SimpleStringProperty(email);
	        this.job = new SimpleStringProperty(job);
	        this.department = new SimpleStringProperty(department);
	    }

	    public String getUsername() {
	        return username.get();
	    }
	    
	    public String getFirstName() {
	        return firstName.get();
	    }
	    
	    public String getLastName() {
	        return lastName.get();
	    }
	    
	    public String getEmail() {
	        return email.get();
	    }
	    
	    public String getJob() {
	        return job.get();
	    }
	    
	    public String getDepartment() {
	        return department.get();
	    }
	    
	    
	    public void setFirstName(String fName) {
	    	firstName.set(fName);
	    }

	    public void setLastName(String lName) {
	        lastName.set(lName);
	    }
	    
	    public void setEmail(String mail) {
	    	email.set(mail);
	    }
	    
	    public void setJob(String jb) {
	    	job.set(jb);
	    }
	    
	    public void setDepartment(String dep) {
	    	department.set(dep);
	    }	    

	}
	
	
	
	

}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class SearchWorkerResultsRecv extends Thread{
	
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