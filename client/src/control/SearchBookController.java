package control;

import java.awt.List;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import boundry.ClientUI;
import entity.Author;
import entity.Book;
import entity.Domain;
import entity.GeneralMessages;
import entity.Language;
import entity.Message;
import entity.Register;
import entity.Review;
import entity.ScreensInfo;
import entity.Validate;
import enums.ActionType;
import interfaces.ScreensIF;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import unittests.SearchBookControllerTest;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;

/** SearchBookController. Responsible to enable users search books in DB.
 * @author itain
 */


public class SearchBookController implements ScreensIF{

	/**
	 * shows image of books 
	 */
	@FXML private ImageView booksImageView;
	
	/**
	 * enables user enter title to search 
	 */
	@FXML public TextField titleTextField;
	
	/**
	 * show authors from DB 
	 */
	@FXML public ListView<String> authorListView;
	
	/**
	 * show available languages 
	 */
	@FXML public ComboBox<String> languageComboBox;
	
	/**
	 * enables user enter summary to search 
	 */
	@FXML public TextArea summaryTextArea;
	
	/**
	 * enables user enter table of contents to search 
	 */
	@FXML public TextArea tocTextArea;
	
	/**
	 * shows domains from DB 
	 */
	@FXML public ListView<String> domainListView;
	
	/**
	 * enables user enter summary to search 
	 */
	@FXML public TextArea keywTextArea;
	
	/**
	 * starts books search
	 */
	@FXML public Button searchButton;
	
	/**
	 * clears all fields 
	 */
	@FXML public Button clearButton;
	
	/**
	 * makes an AND search with users data 
	 */
	@FXML public RadioButton andRadioButton;
	
	/**
	 * makes an OR search with users data 
	 */
	@FXML public RadioButton orRadioButton;
	
	/**
	 * make radio buttons connected so user can choose only one 
	 */
	@FXML public ToggleGroup searchGroup;
	
	/**
	 * information question mark for multiple choice 
	 */
	@FXML private ImageView authorImage;
	
	/**
	 * information question mark for multiple choice 
	 */
	@FXML private ImageView domainImage;
	
	/**
	 * information question mark for right separating key words 
	 */
	@FXML private ImageView keyWordsImage;
	

	/**
	 * holds available domains from DB  
	 */
	public static ArrayList<String> domainList;

	/**
	 * holds available authors from DB  
	 */
	public static ArrayList<Author> authorList;
	
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
	 * flag, if true downloads data from DB.  
	 */
	int goToServer_flag=1;
	

	
	/** initializing data when page comes up
	 * @author itain
	 */
	@FXML
	public void initialize() {
		Image booksImagePath = new Image("/img/books.png");
		booksImageView.setImage(booksImagePath);
		ArrayList<String> elementList = new ArrayList<String>();
		Message message = new Message(ActionType.GET_AUTHORS, elementList);
		Message message2= new Message(ActionType.GET_DOMAINS, domainList);
		
		if(goToServer_flag==1)
		{
    		
    		try {
    			ClientController.clientConnectionController.sendToServer(message);
				ClientController.clientConnectionController.sendToServer(message2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}

		goToServer_flag=0;

		
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				SearchBookAuthorsRecv recv_authors = new SearchBookAuthorsRecv();
				recv_authors.start();
				synchronized (recv_authors) {
					try{
						recv_authors.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					/* authors */
					ArrayList<String> names=new ArrayList<String>();
					authorListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					for(int i=0 ; i< authorList.size();i++)
						names.add(i, "("+authorList.get(i).getId()+")"+"\t"+authorList.get(i).getFirstname()+" "+authorList.get(i).getLastname());
					ObservableList<String> items = FXCollections.observableArrayList(names);
					authorListView.setItems(items);	
				}	

				SearchBookDomainsRecv recv_domains = new SearchBookDomainsRecv();
				recv_domains.start();
				synchronized (recv_domains) {
					try{
						recv_domains.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
					/* domains */
					domainListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					ObservableList<String> items2 = FXCollections.observableArrayList(domainList);
					domainListView.setItems(items2);
				}

				
				
				//question mark images
				Image questionMarkImage= new Image("/img/questionMark.png");
				authorImage.setImage(questionMarkImage);
				domainImage.setImage(questionMarkImage);
				keyWordsImage.setImage(questionMarkImage);

				

				ObservableList<String> lanaguageOptions = FXCollections.observableArrayList("","Hebrew", "English", "Russian");
				languageComboBox.getItems().addAll(lanaguageOptions);
				languageComboBox.getSelectionModel().select(0);
				
				
				try{

				} catch (Exception e) {
					e.printStackTrace();		
					}

			}
			
		});



		

	}
	
	

	
	/** When search button is pressed a search is made.
	 * @author itain
	 * @param event The event when button pressed.
	 * @throws IOException IO exception.
	 */
	@FXML
	public void searchButtonPressed(ActionEvent event) throws IOException {
		int i;

        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try{
				String title=titleTextField.getText().trim();
				
		        ObservableList<String> selectedAuthors =  authorListView.getSelectionModel().getSelectedItems();
				ArrayList<String> authorList= new ArrayList<String>();
		        for(String s : selectedAuthors)
		        	authorList.add(s);
		        
		        
		        String language=languageComboBox.getSelectionModel().getSelectedItem().toString();
		        
		        String summary=summaryTextArea.getText().trim();
		        
		        String toc=tocTextArea.getText().trim();
		        
		        ObservableList<String> selectedDomains =  domainListView.getSelectionModel().getSelectedItems();
				ArrayList<String> domainList= new ArrayList<String>();
		        for(String s : selectedDomains)
		        	domainList.add(s);
		        
		        String keyWords=keywTextArea.getText().trim();
				if (title.equals("") && authorList.isEmpty() &&	language.equals("") && summary.equals("") && toc.equals("") && domainList.isEmpty() && keyWords.equals(""))
				{
					 if(ClientUI.testMode == false)
	                        actionOnError(ActionType.CONTINUE,GeneralMessages.EMPTY_FIELDS);
	                    if(ClientUI.testMode == true)
	                    {
	                        SearchBookControllerTest.emptyFieldsFlag=true;
	                        SearchBookControllerTest.getEmptyFieldAnswerFlag=1;
	                    }
					return;
				}
				
				if (title.contains("^") ||  summary.contains("^") || toc.contains("^") || keyWords.contains("^"))
				{
					if(ClientUI.testMode == false)
                        actionOnError(ActionType.CONTINUE,GeneralMessages.ILLEGAL_CHARACTER);
                    if(ClientUI.testMode == true)
                    {
                        SearchBookControllerTest.SpecialCharacterFlag=true;
                        SearchBookControllerTest.getSpecialCharacterAnswerFlag=1;
                    }
					return;
				}
				
				Book newSearch = new Book(0, title, language, summary, toc,keyWords,"", authorList, domainList, new ArrayList<String>());
				
				String selectedToggle=searchGroup.getSelectedToggle().toString();
				
				
				/* get type of search - AND / OR */
				if(selectedToggle.contains("AND"))
				{
					Message message = prepareSerachBook(ActionType.SEARCH_BOOK_AND,newSearch);
					try {
						if(ClientUI.testMode == true)
							SearchBookControllerTest.clientCC.sendToServer(message);
						else
							ClientController.clientConnectionController.sendToServer(message);
					
					} catch (IOException e) {
								
						actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
					}
				}
				else if(selectedToggle.contains("OR"))
				{
					Message message = prepareSerachBook(ActionType.SEARCH_BOOK_OR,newSearch);
					try {
						if(ClientUI.testMode == true)
							SearchBookControllerTest.clientCC.sendToServer(message);
						else
							ClientController.clientConnectionController.sendToServer(message);
					
					} catch (IOException e) {
								
						actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
					}
				}
				} catch (Exception e) {
					e.printStackTrace();		
					}
				
				
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
                	managerMain.setPage(ScreensInfo.SEARCH_BOOK_RESULTS_SCREEN);
            	}
            	else if(ClientUI.getTypeOfUser()=="User")
            	{
                	if (userMain == null)
                		userMain = new HomepageUserController();
                	userMain.setPage(ScreensInfo.SEARCH_BOOK_RESULTS_SCREEN);
            	}
        		
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
		});
		
	}
	
	
	/** When listview question mark is pressed the function shows message for multiple choice.
	 * @author itain
	 */
	@FXML
	public void msgMultipleChoice() throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(null);
		alert.setContentText("For multiple choice, use ctrl+right click");
		alert.showAndWait();			
		return;
		
	}
	
	
	/** When question mark of Key Words is pressed the function shows message for right separation.
	 * @author itain
	 */
	@FXML
	public void msgKeyWords() throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(null);
		alert.setContentText("Seperate key words using ','");
		alert.showAndWait();			
		return;
		
	}
	
	/** When clear button is pressed the function clears all fields.
	 * @param event The event when button pressed.
	 * @throws IOException IO exception.
	 */
	@FXML
	public void clearButtonPressed(ActionEvent event) throws IOException {
		try {
			titleTextField.clear();
			authorListView.getItems().clear();
			languageComboBox.getItems().clear();
			summaryTextArea.setText(null);
			tocTextArea.setText(null);
			domainListView.getItems().clear();
			keywTextArea.setText(null);
			andRadioButton.setSelected(true);
			orRadioButton.setSelected(false);
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void pressedCloseMenu(ActionEvent event) {
		// TODO Auto-generated method stub
	/*	Platform.exit();
		System.exit(0);*/
	}

	
	/**
	 * This function gets message and perform the task by the error type.
	 * @author itain
	 * @param type Gets error type.
	 * @param errorCode Gets error message.
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
	

	@Override
	public void backButtonPressed(ActionEvent event){
	}
	
	
	
	/** This function prepare message that will be sent to the server with arraylist,
	 * and the action.
	 * @author itain
	 * @param type Gets the type of the action
	 * @param searchBook Gets the class with the search book information.
	 * @return message that will be sent to server.
	 */
	public Message prepareSerachBook(ActionType type, Book searchBook)
	{
		int i;
		Message message = new Message();
		message.setType(type);
		ArrayList <String> elementsList = new ArrayList<String>();
		
		elementsList.add(searchBook.getTitle()); //title
		elementsList.add(searchBook.getLanguage()); //language
		elementsList.add(searchBook.getSummary()); //summary
		elementsList.add(searchBook.getTableOfContent()); //table of contents
		elementsList.add(searchBook.getKeywords()); //keywords
		
		elementsList.add(Integer.toString(searchBook.getAuthors().size())); //authors number
		for(i=0;i<searchBook.getAuthors().size();i++) //authors
			elementsList.add(searchBook.getAuthors().get(i));

		elementsList.add(Integer.toString(searchBook.getDomains().size())); //domains number
		for(i=0;i<searchBook.getDomains().size();i++) //domains
			elementsList.add(searchBook.getDomains().get(i));
		

		message.setElementsList(elementsList);
		
		return message;
	}




}

/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class SearchBookAuthorsRecv extends Thread{
	
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

/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class SearchBookDomainsRecv extends Thread{
	
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