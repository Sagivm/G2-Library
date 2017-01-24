package control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.javafx.collections.SetAdapterChange;

import boundry.ClientUI;
import entity.GeneralMessages;
import entity.Message;
import entity.ScreensInfo;
import entity.SearchBookResult;
import entity.SearchUserResult;
import entity.User;
import enums.ActionType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * Makes a popularity report given a specific user The class will make a book
 * list of the books that the user purchased including for each book- id ,title
 * ,language ,date of purchase and the price at the acquisition date
 * 
 * @author sagivm
 */
public class UserReportController implements Initializable {

	/**
	 * Main table in the Popularity report screen
	 */

	@FXML
	private TableView<Purchase> table;
	/**
	 * Data returned from Db will be inserted here comes in 6 strings divided by
	 */
	public static ArrayList<String> data;
	/**
	 * Specific user that
	 */
	private static SearchUserResult selectedUser;
	/**
	 * Book id column in the table
	 */
	@FXML
	private TableColumn<Purchase, String> bookIdColumn;
	/**
	 * Title column in the table
	 */
	@FXML
	private TableColumn<Purchase, String> titleColumn;
	/**
	 * Author column in the table
	 */
	@FXML
	private TableColumn<Purchase, String> authorColumn;

	/**
	 * Language column in the table
	 */
	@FXML
	private TableColumn<Purchase, String> languageColumn;
	/**
	 * Date column in the table
	 */
	@FXML
	private TableColumn<Purchase, String> dateColumn;
	/**
	 * Price column in the table
	 */
	@FXML
	private TableColumn<Purchase, String> priceColumn;
	/**
	 * ArrayList containing the data in Purchase form
	 */
	private ArrayList<Purchase> list;
	/**
	 * List of items that will be displayed in the table
	 */
	private ObservableList<Purchase> items = FXCollections.observableArrayList();
	
	
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
	

	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		this.selectedUser = UserPageController.searchedUserPage;
		

		initializeTable();
	
		
		table.setRowFactory( tv -> {
		    TableRow<Purchase> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) )
		        {
		        	Purchase rowData = row.getItem();
		            System.out.println(rowData.getId());
		            
		            BookPageController.searchedBookPage = new SearchBookResult(
		            		rowData.getId(),rowData.getTitle(),rowData.getLanguage(),
		            		"","","",rowData.getAuthor(),"","",rowData.getPrice());
                	BookPageController.previousPage = "UserReport";
      				if(ClientUI.getTypeOfUser()=="Librarian")
                	{
                    	if (librarianMain == null)
                    		librarianMain = new HomepageLibrarianController();
                    	librarianMain.setPage(ScreensInfo.BOOK_PAGE_SCREEN);
                	}
                	else if(ClientUI.getTypeOfUser()=="Manager")
                	{
                    	if (managerMain == null)
                    		managerMain = new HomepageManagerController();
                    	managerMain.setPage(ScreensInfo.BOOK_PAGE_SCREEN);
                	}
                	else if(ClientUI.getTypeOfUser()=="User")
                	{
                    	if (userMain == null)
                    		userMain = new HomepageUserController();
                    	userMain.setPage(ScreensInfo.BOOK_PAGE_SCREEN);
                	}
            		
            		ScreenController screenController = new ScreenController();
            		try{
            			if(ClientUI.getTypeOfUser()=="Librarian")
            			{
            				screenController.replaceSceneContent(ScreensInfo.HOMEPAGE_LIBRARIAN_SCREEN,ScreensInfo.HOMEPAGE_LIBRARIAN_TITLE);						
            			}
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
		    return row ;
		});
	}

	/**
	 * Given a specific user ,request from the DB a list with the books that the
	 * user bought including for each book- id ,title ,language ,date of
	 * purchase and the price at the acquisition date. The function proceeds
	 * further to receiver for analyzing data and displaying
	 */
	private void initializeTable() {
		ArrayList<String> elementsList = new ArrayList<String>();
		elementsList.add(selectedUser.getUsername());
		Message message = new Message(ActionType.USEREPORT, elementsList);
		try {
			ClientController.clientConnectionController.sendToServer(message);

		} catch (IOException e) {

			// actionToDisplay("Warning",ActionType.CONTINUE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		receiver();

	}

	/**
	 * Analyzes Data given from DB and displays it the table. If the User didn't
	 * bought any books displays a message.
	 */
	private void receiver() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				try {
					if (data != null) {
						table.setEditable(true);
						arrangelist();

						mergeDuplicates();
						items = FXCollections.observableArrayList();
						for (int i = 0; i < list.size(); i++) {
							items.add(list.get(i));
						}

						bookIdColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("id"));
						titleColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("title"));
						authorColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("author"));
						languageColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("language"));
						dateColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("date"));
						priceColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("price"));

						table.setVisible(true);
						table.setItems(items);
						table.setVisible(true);

					} else {
						// actionToDisplay("Info", ActionType.CONTINUE,"No
						// purchase data for this user");
					}

					// }

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * gets the specific user.
	 * 
	 * @return specific user
	 */
	public static SearchUserResult getSelectedUser() {
		return selectedUser;
	}

	/**
	 * Sets the user.
	 * 
	 * @param selectedUser
	 *            a specific user that the list will be based upon
	 */
	public static void setSelectedUser(SearchUserResult selectedUser) {
		UserReportController.selectedUser = selectedUser;

	}

	/**
	 * Transfer the list from the DB to ArrayList<Purchase>
	 */
	private void arrangelist() {
		list = new ArrayList<Purchase>();
		String datasplit[] = new String[6];
		for (int i = 0; i < data.size(); i++) {
			datasplit = data.get(i).split("\\^");
			Purchase p = new Purchase(datasplit);
			list.add(p);
		}

	}

	/**
	 * Merge books in the list that have more than one author
	 */
	private void mergeDuplicates() {
		boolean dup[] = new boolean[list.size()];
		for (int i = 0; i < dup.length; i++)
			dup[i] = false;
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(i).getId().equals(list.get(j).getId()) && i != j) {
					list.get(i).setAuthor(list.get(i).getAuthor() + " & " + list.get(j).getAuthor());
					list.remove(j);

				}
			}
		}
	}

	/**
	 * Creates a label displaying the current user
	 */

	/**
	 * Data class for analyzing DB data and displaying it
	 * @author sagivm
	 */
	public class Purchase {
		/**
		 * Book's id
		 */
		private String id;
		/**
		 * Book's title
		 */
		private String title;
		/**
		 * Book's author
		 */
		private String author;
		/**
		 * Book's language
		 */
		private String language;
		/**
		 * User date
		 */
		private String date;
		/**
		 * Book's price at the acquisition date
		 */
		private String price;

		/**
		 * constructor
		 * 
		 * @param split
		 *            String array containing the 6 attributes of Purchase in
		 *            the order id,title,author,language,date,price
		 */
		public Purchase(String split[]) {
			this.id = new String(split[0]);
			this.title = new String(split[1]);
			this.author = new String(split[2]);
			this.language = new String(split[3]);
			this.date = new String(split[4]);
			this.price = new String(split[5]);
		}

		/**
		 * Getter
		 * @return book's Id
		 */
		public String getId() {
			return id;
		}

		/**
		 * Setter
		 * sets bookId
		 * @param id of the book
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * Getter
		 * @return book's title
		 */

		public String getTitle() {
			return title;
		}
		/**
		 * Setter
		 * sets book's title
		 * @param book's title
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		/**
		 * Getter
		 * @return book's author
		 */

		public String getAuthor() {
			return author;
		}

		/**
		 * Setter
		 * sets book's authors
		 * @param book's authors
		 */
		public void setAuthor(String author) {
			this.author = author;
		}
		/**
		 * Getter
		 * @return book's language
		 */

		public String getLanguage() {
			return language;
		}
		/**
		 * Setter
		 * sets book's language
		 * @param book's language
		 */
		public void setLanguage(String language) {
			this.language = language;
		}
		/**
		 * Getter
		 * @return the date that the book was purchased
		 */

		public String getDate() {
			return date;
		}
		/**
		 * Setter
		 * sets book's date of purchase
		 * @param book's date of purchase
		 */

		public void setDate(String date) {
			this.date = date;
		}
		/**
		 * Getter
		 * @return the price of the book at the acquisition date
		 */

		public String getPrice() {
			return price;
		}
		/**
		 * Setter
		 * sets the price of the book at the acquisition date
		 * @param book's price at the acquisition date
		 */
		public void setPrice(String price) {
			this.price = price;
		}

	}

}
