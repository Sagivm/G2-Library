package control;

import javafx.event.ActionEvent;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import javax.swing.text.AbstractDocument.ElementEdit;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;


import entity.CurrentDate;
import entity.FileEvent;
import entity.GeneralMessages;
import entity.Login;
import entity.Message;
import entity.Registration;
import entity.Replay;
import enums.ActionType;
import javafx.application.Platform;
import javafx.fxml.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * ServerController is controller of the GUI window. the controller creates log
 * in files and open connection to server, after auth via database.
 * 
 * @author nire
 *
 */
/**
 * @author nire
 *
 */
public class ServerController extends AbstractServer {

	/**
	 * Default port
	 */
	private static int dPort = 1337;

	/**
	 * Log
	 */
	private Logger logger;

	/**
	 * Text area for log.
	 */
	@FXML
	private TextArea logField;

	
	/**
	 * Text field for user.
	 */
	@FXML
	private TextField userField;

	/**
	 * Password field for password.
	 */
	@FXML
	private PasswordField passField;

	/**
	 * Username label for username.
	 */
	@FXML
	private Text usernameLabel;

	/**
	 * Password label for password.
	 */
	@FXML
	private Text passLabel;

	/**
	 * Button for connect/disconnect.
	 */
	@FXML
	private Button connectButton;

	/**
	 * ArrayList for all of the connected users
	 */
	private static ArrayList<Login> connectedList = new ArrayList<Login>();
	

	/**
	 * Constructor to establish connection with server, and prepare log file.
	 */
	public ServerController() {
		super(dPort);
		
		logger = Logger.getLogger("ServerLog.log");
		FileHandler fh;
		try {
			fh = new FileHandler("ServerLogFile.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} //
		

	}

	/**
	 * Constructor to establish connection with server, and prepare log file.
	 * @param port - Gets the port.
	 */
	public ServerController(int port) {
		super(port);
		logger = Logger.getLogger("ServerLog.log");
		FileHandler fh;
		try {
			fh = new FileHandler("ServerLogFile.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * CloseApp close the application when function called by menu -> "close"
	 * @param event - Gets the event.
	 */
	@FXML
	public void CloseApp(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ocsf.server.AbstractServer#handleMessageFromClient(java.lang.Object,
	 * ocsf.server.ConnectionToClient)
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Message message = (Message) msg;
		CurrentDate date=new CurrentDate();
		try {
			Replay replay = actionToPerform(message);
			if (replay.getTransmitType().equals(ActionType.BROADCAST))
				sendToAllClients(replay);
			else 
				client.sendToClient(replay);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Given a message from client actionToPerform decide what to perform and
	 * the reply to return
	 * 
	 * @param - Gets the message.
	 * @return - the replay for the client.
	 */
	private Replay actionToPerform(Message message) throws IOException {

		ActionType type = message.getType();
		ArrayList<String> data = message.getElementsList();
		boolean sqlResult = false;
		Replay replay = null;
		int action = 0;
		switch (type) {
		
		case FILE: {
			String suffix = data.get(2);
			URL location = ServerController.class.getProtectionDomain().getCodeSource().getLocation();
			String destinationPath = data.get(0) + "/";
			String sourceFilePath = "C:/books/" + data.get(1) + "." + suffix;
			//String sourceFilePath = location.getFile().replaceAll("bin", "src") + "/books/" + data.get(1) + "." + suffix;
			System.out.println(sourceFilePath);
			replay = new Replay(ActionType.FILE, generateFile(destinationPath,sourceFilePath));
		}
		break;
		case GET_MESSAGES: {
			
			String username = data.get(0);
			
			ArrayList<String> elementsList = new ArrayList<String>();
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT date,time,msg FROM messages WHERE username =" + "'"+username+"'");
				while (rs.next())
				{
					elementsList.add(rs.getString(1) + "^" + rs.getString(2) + "^" + rs.getString(3));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}	
			replay = new Replay(ActionType.GET_MESSAGES, true, elementsList);
		}
		break;
		case REGISTER: {

			Registration registration = new Registration(Integer.parseInt(data.get(0)), data.get(1).toString(),
					data.get(2).toString(), data.get(3).toString());

			try {
				DatabaseController.addToDatabase(registration.PrepareAddStatement());
				sqlResult = true;
			} catch (SQLException e) {
				e.printStackTrace();
				if (e.getErrorCode() == 1062) { //// duplicate primary key
					System.out.println("username already exist");
				}
			}
			if (sqlResult == true)
				replay = new Replay(ActionType.REGISTER,true);
			else {
				replay = new Replay(ActionType.REGISTER,false);
				System.out.println(replay.getSucess());
			}
			writeToLog("Registration attempt");
			break;
		}

		case LOGIN: {
			try {
				boolean isConnected = false;
				ArrayList<String> elementsList = new ArrayList<String>();
				for (int i = 0; i < connectedList.size(); i++) {
					if (connectedList.get(i).getUsername().equals(data.get(0).toString())) {
						isConnected = true;
						break;
					}
				}
				if (!isConnected) {
					Login login = new Login(data.get(0).toString(), data.get(1).toString());
					Statement stmt = DatabaseController.connection.createStatement();
					ResultSet rs = stmt.executeQuery(login.PrepareSelectStatement(1));

					while (rs.next()) {
						if (rs.getString(4).equals(data.get(1).toString())) {
							
							System.out.println(rs.getString(1));
							
						
							ResultSet rs1 = DatabaseController.searchInDatabase("SELECT isBlocked FROM clients WHERE username='" + rs.getString(1) + "'");	
							
							while (rs1.next()) {
								if (rs1.getString(1).equals("1")) {
									replay = new Replay(ActionType.LOGIN, false, GeneralMessages.USER_BLOCKED);
									return replay;
								}
							}
							
							System.out.println("login succssefully");
							sqlResult = true;
							action = 1;
							elementsList.add(0, rs.getString(1)); // username
							elementsList.add(1, rs.getString(2)); // first name
							elementsList.add(2, rs.getString(3)); // last name
							elementsList.add(3, rs.getString(4)); // password
							elementsList.add(4, rs.getString(5)); // account
																	// type
							elementsList.add(5, rs.getString(6)); // account
																	// status
							break;
						}
					}
					if (!sqlResult) {
						rs = stmt.executeQuery(login.PrepareSelectStatement(2));
						while (rs.next()) {
							if (rs.getString(4).equals(data.get(1).toString())) {
								System.out.println("login succssefully");
								sqlResult = true;
								if (rs.getString(6).toString().equals("Manager"))
									action = 3;
								else
									action = 2;

								elementsList.add(0, rs.getString(1)); // username
								elementsList.add(1, rs.getString(2)); // first
																		// name
								elementsList.add(2, rs.getString(3)); // last
																		// name
								elementsList.add(3, rs.getString(4)); // password
								elementsList.add(4, rs.getString(5)); // email
								elementsList.add(5, rs.getString(6)); // job
								elementsList.add(6, rs.getString(7)); // department
								break;
							}
						}
					}
					if (sqlResult == true) {
						
						replay = new Replay(ActionType.LOGIN, true, action, elementsList);
						connectedList.add(login);
					} else {
						replay = new Replay(ActionType.LOGIN, false, GeneralMessages.USER_LOGGED_IN_FAILED);
						System.out.println(replay.getSucess());
						
					}
				} else {
					replay = new Replay(ActionType.LOGIN, false, GeneralMessages.USER_ALREADY_LOGGED_IN);
					System.out.println(replay.getSucess());
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("error");
			}

			writeToLog("Login attempt");
			break;
		}

		case LOGOUT: {
			boolean succes = false;
			for (int i = 0; i < connectedList.size(); i++) {
				if (connectedList.get(i).getUsername().equals(data.get(0).toString())) {
					connectedList.remove(i);
					succes = true;
					break;
				}
			}
			if (succes) {
				replay = new Replay(ActionType.LOGOUT, true);
			}
			break;
		}
		case ACCOUNTTYPEREQ: {
			try {
				DatabaseController
						.updateDatabase("UPDATE clients SET accountStatus=" + "'" + message.getElementsList().get(1)
								+ "'" + "  WHERE username=" + "'" + message.getElementsList().get(0) + "'");
				writeToLog(message.getElementsList().get(0) + " Changed accountStatus to"
						+ message.getElementsList().get(1));

				
				
				replay = new Replay(ActionType.ACCOUNTTYPEREQ, true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		}

		case GET_PENDING_USERS: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT username,firstName,lastName FROM clients WHERE accountType='RegisterPending'");
				while (rs.next()) {
					elementsList.add(rs.getString(1)); // username
					elementsList.add(rs.getString(2)); // first name
					elementsList.add(rs.getString(3)); // last name
				}
				replay = new Replay(ActionType.GET_PENDING_USERS, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		}
	
		case SEARCH_BOOK_AND: { // itai
			ArrayList<String> elementsList = new ArrayList<String>();
			elementsList=makeSearchBook();
			int[] filterResults=new int[elementsList.size()]; //books that will be shown in results
			int i,j, continue_index;
			ArrayList<String> newSearch=message.getElementsList();
			//initiate filterResults
			for(i=0;i<filterResults.length;i++)
				filterResults[i]=1;
			
			for(i=0;i<elementsList.size();i++)
			{
				String bookRow[] = new String[countItems(elementsList.get(i),"^")];
				bookRow = elementsList.get(i).split("\\^");
				
				if(!newSearch.get(0).isEmpty()) //title
					if(!bookRow[1].toLowerCase().contains(newSearch.get(0).toLowerCase())) //searched title doesn't appear
						filterResults[i]=0;
					
				if(!newSearch.get(1).equals("")) //language
					if(!bookRow[2].equals(newSearch.get(1))) //searched language wansn't found
						filterResults[i]=0;
				
				if(!newSearch.get(2).isEmpty()) //summary
					if(!bookRow[3].toLowerCase().contains(newSearch.get(2).toLowerCase())) //searched summary doesn't appear
						filterResults[i]=0;
				
				if(!newSearch.get(3).isEmpty()) //toc
					if(!bookRow[4].toLowerCase().contains(newSearch.get(3).toLowerCase())) //searched toc doesn't appear
						filterResults[i]=0;
				
				if(!newSearch.get(4).isEmpty()) //key words
				{
					int size=countItems(newSearch.get(4), ",");
					String[] keyWords = new String[size];
					keyWords = newSearch.get(4).split("\\,");
					for(j=0;j<size;j++)
					{
						if(!bookRow[5].toLowerCase().contains(keyWords[j].toLowerCase().trim())) //searched key words don't appear
							filterResults[i]=0;
					}
				}

				continue_index=6; //in case there are no authors on search
				
				//authors
				int authorsCount=Integer.parseInt(newSearch.get(5)); //newSearch
				int authorsInRow=Integer.parseInt(bookRow[6]); //bookRow
				int[] authorsFound=new int[authorsCount];
				if(authorsCount!=0) 
				{
					for(j=0;j<authorsCount;j++)
					{
						for(int k=0;k<authorsInRow;k++)
						{
							int beginName=newSearch.get(6+j).lastIndexOf(")")+1;
							String aName=newSearch.get(6+j).substring(beginName).trim();
							if(bookRow[7+k].equals(aName))
								authorsFound[j]++;
						}
					}
					continue_index=6+authorsCount;
					
					for(j=0;j<authorsFound.length;j++)
						if(authorsFound[j]==0)
							filterResults[i]=0;

				}

				//domains
				int domainsCount=Integer.parseInt(newSearch.get(continue_index));
				int domainsInRow=Integer.parseInt(bookRow[7+authorsInRow]);
				int[] domainsFound=new int[domainsCount];
				

				if(domainsCount!=0) 
				{
					for(j=0;j<domainsCount;j++)
					{
						for(int k=0;k<domainsInRow;k=k+2)
						{						
							if(bookRow[9+authorsInRow+k].equals(newSearch.get(continue_index+1+j)))
								domainsFound[j]++;
						}
					}
					for(j=0;j<domainsFound.length;j++)
						if(domainsFound[j]==0)
							filterResults[i]=0;
				}
			}
			
			
			j=0;
			for(i=0;i<elementsList.size();i=i+2)
			{
				elementsList.add(i, Integer.toString(filterResults[j]));
				j++;
			}
			
			
			ArrayList<String> res = new ArrayList<String>();
			
			for(i=0;i<elementsList.size();i=i+2)
			{
				if(Integer.parseInt(elementsList.get(i))==1)
					res.add(elementsList.get(i+1)); 
                   
			}
			
			//remove duplicates
			Set<String> hs = new HashSet<>();
			hs.addAll(res);
			res.clear();
			res.addAll(hs);
			
			for(i=0;i<res.size();i++)
			{
				 String bookId = res.get(i).substring(0, (res.get(i).indexOf("^")));
                 int searchCount=0;
                 try {
                     searchCount = CurrentDate.IncSearchBookDateRow(bookId);
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
                 writeToLog("Search counter of book ID " + "'" + bookId + "' was increased from '"
                      + searchCount + "' to '" + (searchCount+1) +"'");
			}
			
			
			replay = new Replay(ActionType.SEARCH_BOOK_AND, true, res);
			break;
		}

		case SEARCH_BOOK_OR: { // itai -need to fix
			
			ArrayList<String> elementsList = new ArrayList<String>();
			elementsList=makeSearchBook();
			//int[] filterResults=new int[elementsList.size()]; //books that will be shown in results
			int i,j, continue_index;
			ArrayList<String> newSearch=message.getElementsList();

			ArrayList<String> res=new ArrayList<String>();
			
						
			for(i=0;i<elementsList.size();i++)
			{
				String bookRow[] = new String[countItems(elementsList.get(i),"^")];
				bookRow = elementsList.get(i).split("\\^");
				
				if(!newSearch.get(0).isEmpty()) //title
					if(bookRow[1].toLowerCase().contains(newSearch.get(0).toLowerCase())) //searched title doesn't appear
						res.add(elementsList.get(i));					
				
					
				if(!newSearch.get(1).equals("")) //language
					if(bookRow[2].equals(newSearch.get(1))) //searched language wansn't found
						res.add(elementsList.get(i));

				if(!newSearch.get(2).isEmpty()) //summary
					if(bookRow[3].toLowerCase().contains(newSearch.get(2).toLowerCase())) //searched summary doesn't appear
						res.add(elementsList.get(i));
				
				if(!newSearch.get(3).isEmpty()) //toc
					if(bookRow[4].toLowerCase().contains(newSearch.get(3).toLowerCase())) //searched toc doesn't appear
						res.add(elementsList.get(i));
				
				if(!newSearch.get(4).isEmpty()) //key words
				{
					int size=countItems(newSearch.get(4), ",");
					String[] keyWords = new String[size];
					keyWords = newSearch.get(4).split("\\,");
					for(j=0;j<size;j++)
					{
						if(bookRow[5].toLowerCase().contains(keyWords[j].toLowerCase().trim())) //searched key words don't appear
							res.add(elementsList.get(i));
					}
				}
				
				continue_index=6; //in case there are no authors on search
				
				//authors
				int authorsCount=Integer.parseInt(newSearch.get(5)); //newSearch
				int authorsInRow=Integer.parseInt(bookRow[6]); //bookRow
				if(authorsCount!=0) 
				{
					for(j=0;j<authorsCount;j++)
					{
						for(int k=0;k<authorsInRow;k++)
						{
							int beginName=newSearch.get(6+j).lastIndexOf(")")+1;
							String aName=newSearch.get(6+j).substring(beginName).trim();
							if(bookRow[7+k].equals(aName))
								res.add(elementsList.get(i));
							
						}
					}
					continue_index=6+authorsCount;
				}
					
				//domains
				int domainsCount=Integer.parseInt(newSearch.get(continue_index));
				int domainsInRow=Integer.parseInt(bookRow[7+authorsInRow]);

				if(domainsCount!=0) 
				{
					for(j=0;j<domainsCount;j++)
					{
						for(int k=0;k<domainsInRow;k=k+2)
						{
							if(bookRow[9+authorsInRow+k].equals(newSearch.get(continue_index+1+j)))
								res.add(elementsList.get(i));
						}
					}

				}
	
			}

			Set<String> hs = new HashSet<>();
			hs.addAll(res);
			res.clear();
			res.addAll(hs);
			
			for(i=0;i<res.size();i++)
			{
				 String bookId = res.get(i).substring(0, (res.get(i).indexOf("^")));
                 int searchCount=0;
                 try {
                     searchCount = CurrentDate.IncSearchBookDateRow(bookId);
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
                 writeToLog("Search counter of book ID " + "'" + bookId + "' was increased from '"
                      + searchCount + "' to '" + (searchCount+1) +"'");
			}
			 
			replay = new Replay(ActionType.SEARCH_BOOK_OR, true, res);
			break;
		}
		
		case GET_DOMAINS_WITH_ID: {
			ArrayList<String> domainList = new ArrayList<String>();
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id,name FROM domains");
				while (rs.next()) {
					domainList.add(rs.getString(1));
					domainList.add(rs.getString(2));
				}
				replay = new Replay(ActionType.GET_DOMAINS_WITH_ID, true, domainList);
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			break;
		}

		case GET_SUBJECTS_INFO: {
			ArrayList<String> subjectsList = new ArrayList<String>();
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT subjects.id,subjects.name,subjects.domain,domains.name FROM subjects,domains WHERE subjects.domain=domains.id");
				while (rs.next()) {
					subjectsList.add(rs.getString(1));
					subjectsList.add(rs.getString(2));
					subjectsList.add(rs.getString(3));
					subjectsList.add(rs.getString(4));
				}
				replay = new Replay(ActionType.GET_SUBJECTS_INFO, true, subjectsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			break;
		}
		
		case SEARCH_WORKER: {
			try
			{
				ArrayList<String> elementsList = new ArrayList<String>();
				ArrayList<String> res = new ArrayList<String>();
				
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT username, firstName,lastName, email, job, department FROM workers");
				while (rs.next())
				{
					elementsList.add(rs.getString(1)); // username
					elementsList.add(rs.getString(2)); // first name
					elementsList.add(rs.getString(3)); // last name
					elementsList.add(rs.getString(4)); // email
					elementsList.add(rs.getString(5)); // job
					elementsList.add(rs.getString(6)); // department
				}
				
				
				int[] filterResult = new int[elementsList.size()/6];
				
				for(int i=0;i<filterResult.length;i++)
					filterResult[i]=1;


				for(int i=0;i<elementsList.size();i+=6)
				{
					if(!message.getElementsList().get(0).isEmpty())
						if(!elementsList.get(i).contains(message.getElementsList().get(0).trim()))
							filterResult[i/6]=0;
						

					if(!message.getElementsList().get(1).isEmpty())
						if(!elementsList.get(i+1).toLowerCase().trim().contains(message.getElementsList().get(1).toLowerCase().trim()))
							filterResult[i/6]=0;


					if(!message.getElementsList().get(2).isEmpty())
						if(!elementsList.get(i+2).toLowerCase().trim().contains(message.getElementsList().get(2).toLowerCase().trim()))
							filterResult[i/6]=0;

					
				}
				
				
				for(int j=0;j<elementsList.size();j+=6)
				{
					if(filterResult[j/6]==1)
						res.add(elementsList.get(j)+"^"+elementsList.get(j+1)+"^"+elementsList.get(j+2)+"^"+elementsList.get(j+3)+"^"+elementsList.get(j+4)+"^"+elementsList.get(j+5));
				}
				
				
				replay = new Replay(ActionType.SEARCH_WORKER, true, res);
				
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			break;
		}
		
		
		case SEARCH_USER: {
			try
			{
				ArrayList<String> elementsList = new ArrayList<String>();
				ArrayList<String> res = new ArrayList<String>();
				
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT username, firstName,lastName, accountType, accountStatus, isBlocked, password, credits, endSubscription FROM clients");
				while (rs.next())
				{
					elementsList.add(rs.getString(1)); // username
					elementsList.add(rs.getString(2)); // first name
					elementsList.add(rs.getString(3)); // last name
					elementsList.add(rs.getString(4)); // accountType
					elementsList.add(rs.getString(5)); // accountStatus
					elementsList.add(rs.getString(6)); // isBlocked
					elementsList.add(rs.getString(7)); // password
					elementsList.add(rs.getString(8)); // credits
					elementsList.add(rs.getString(9)); // endSubscription
				}
				
				
				int[] filterResult = new int[elementsList.size()/9];
				
				for(int i=0;i<filterResult.length;i++)
					filterResult[i]=1;


				for(int i=0;i<elementsList.size();i+=9)
				{
					if(!message.getElementsList().get(0).isEmpty())
						if(!elementsList.get(i).contains(message.getElementsList().get(0).trim()))
							filterResult[i/9]=0;
						

					if(!message.getElementsList().get(1).isEmpty())
						if(!elementsList.get(i+1).toLowerCase().trim().contains(message.getElementsList().get(1).toLowerCase().trim()))
							filterResult[i/9]=0;


					if(!message.getElementsList().get(2).isEmpty())
						if(!elementsList.get(i+2).toLowerCase().trim().contains(message.getElementsList().get(2).toLowerCase().trim()))
							filterResult[i/9]=0;
					
				}

				for(int j=0;j<elementsList.size();j+=9)
				{
					if(filterResult[j/9]==1)
						res.add(elementsList.get(j)+"^"+elementsList.get(j+1)+"^"+elementsList.get(j+2)+"^"+elementsList.get(j+3)+"^"+elementsList.get(j+4)+"^"+elementsList.get(j+5)+"^"+elementsList.get(j+6)+"^"+elementsList.get(j+7)+"^"+elementsList.get(j+8));
				}
						
				replay = new Replay(ActionType.SEARCH_USER, true, res);
				
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			break;
		}
		
		
		case EDIT_USER_LIBRARIAN: {
			try
			{
				DatabaseController.updateDatabase("UPDATE clients SET firstName=" + "'" + message.getElementsList().get(1)
						+ "'" + " ,lastName=" + "'" + message.getElementsList().get(2) +"'" +  " WHERE username=" + "'" + message.getElementsList().get(0) + "'");
				
				writeToLog("Name of username " + "'" + message.getElementsList().get(0) + "' was changed to '"
				+ message.getElementsList().get(1) + " " + message.getElementsList().get(2)+"'");
				replay = new Replay(ActionType.EDIT_USER_LIBRARIAN, true);
				
			} 
			catch (Exception e) {
				e.printStackTrace();		
			}
			break;
		}
		
		case EDIT_USER_MANAGER: {
			try
			{
				DatabaseController.updateDatabase("UPDATE clients SET isBlocked='" + message.getElementsList().get(3) + "' WHERE username=" + "'" + message.getElementsList().get(0) + "'");
				if(Integer.parseInt(message.getElementsList().get(3))==1)
					writeToLog("Username" + "'" + message.getElementsList().get(0) + "' is now blocked");
				else
					writeToLog("Username" + "'" + message.getElementsList().get(0) + "' block has been cancelled");
				replay = new Replay(ActionType.EDIT_USER_MANAGER, true);
				
			} 
			catch (Exception e) {
				e.printStackTrace();		
			}
			break;
		}

		case GET_AUTHORS: {
			ArrayList<String> elementsList = new ArrayList<String>();
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, firstName, lastName FROM authors;");
				while (rs.next()) {
					elementsList.add(rs.getString(1) + "^" + rs.getString(2) + "^" + rs.getString(3));
				}
				replay = new Replay(ActionType.GET_AUTHORS, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			break;
		}

		case GET_DOMAINS: {
			ArrayList<String> domainList = new ArrayList<String>();
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT name FROM domains;");
				while (rs.next()) {
					domainList.add(rs.getString(1));
				}
				replay = new Replay(ActionType.GET_DOMAINS, true, domainList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			break;
		}
		
		case GET_SUBJECTS: {
			ArrayList<String> subjectList = new ArrayList<String>();
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT subjects.name,subjects.id,subjects.domain,domains.id,domains.name FROM subjects,domains WHERE subjects.domain=domains.id");
				while (rs.next()) {
					subjectList.add("("+rs.getString(2)+") "+rs.getString(1)+" ("+rs.getString(5)+")");
				}
				replay = new Replay(ActionType.GET_SUBJECTS, true, subjectList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			break;
		}
		
		case GET_BOOK_SUBJETCS: {
			ArrayList<String> subjectList = new ArrayList<String>();
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String bookSn = data.get(0);
				ResultSet rs = stmt.executeQuery("SELECT DISTINCT subjects.name,subjects.id,subjects.domain,domains.id,domains.name FROM project.books,project.subjects,project.domains,project.book_subjects WHERE subjects.id=book_subjects.subjectId AND subjects.domain=domains.id AND book_subjects.bookId="+bookSn);
				while (rs.next()) {
					System.out.println(rs.getString(1));
					subjectList.add("("+rs.getString(2)+") "+rs.getString(1)+" ("+rs.getString(5)+")");
				}
				replay = new Replay(ActionType.GET_BOOK_SUBJETCS, true, subjectList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			break;
		}


		case ACCEPT_PENDING_USERS: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String username = data.get(0);
				stmt.executeUpdate("UPDATE clients SET accountType='Intrested' WHERE username=" + username);
		
				
				DateFormat currentTime = new SimpleDateFormat("HH:mm");
				DateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				
				String currTime = currentTime.format(date);
				String currDate = currentDate.format(date);
				String msg="Your account has been approved.";
				
				DatabaseController.addToDatabase("INSERT INTO messages (`username`, `date`, `time`, `msg`) VALUES('"+username+"', '"+currDate+"' , '"+currTime+"', '"+msg+"')");
		
				replay = new Replay(ActionType.ACCEPT_PENDING_USERS, true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case DECLINE_PENDING_USERS: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String username = data.get(0);
				stmt.executeUpdate("DELETE FROM clients WHERE username=" + username);
				replay = new Replay(ActionType.DECLINE_PENDING_USERS, true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

		case PENDING_REVIEWS: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT reviews.id, clients.username,clients.firstName,clients.lastName,books.title,reviews.content,reviews.date FROM reviews, clients, books WHERE clients.username = reviews.userId and books.sn = reviews.bookId and reviews.status = 'pending'");
				while (rs.next()) {
					elementsList.add(rs.getString(1)); // review id
					elementsList.add(rs.getString(2)); // username
					elementsList.add(rs.getString(3)); // first name
					elementsList.add(rs.getString(4)); // last name
					elementsList.add(rs.getString(5)); // book title
					elementsList.add(rs.getString(6)); // review content
					elementsList.add(rs.getString(7)); // review date
				}

				replay = new Replay(ActionType.PENDING_REVIEWS, true, elementsList);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case USEREPORT: {
			ArrayList<String> elementsList = new ArrayList<String>();
			try {
				ResultSet rs = DatabaseController.searchInDatabase(
						"Select books.sn,title,authors.firstName,authors.lastName,language,purchaseDate,bought_book.price "
						+ "FROM books,bought_book,authors,book_authors "
						+ "WHERE books.sn=bought_book.bookId and books.sn=book_authors.bookId and authors.id=book_authors.authorid and bought_book.userId="
								+ data.get(0) + ";");
				if (!rs.isBeforeFirst())
					replay = new Replay(ActionType.USEREPORT, false);// no data
				else {
					while (rs.next()) {
						elementsList.add(String.valueOf(rs.getInt(1)) + "^" + rs.getString(2) + "^" + rs.getString(3)
								+ " " + rs.getString(4) + "^" + rs.getString(5) + "^" +rs.getString(6) 
								+ "^" +String.valueOf(rs.getFloat(7)));
					}
					replay = new Replay(ActionType.USEREPORT, true, elementsList);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		}
		case MY_BOOKS: {
			ArrayList<String> elementsList = new ArrayList<String>();
			try {
				ResultSet rs = DatabaseController.searchInDatabase(
						"Select books.sn,title,authors.firstName,authors.lastName,language,purchaseDate,bought_book.price "
						+ "FROM books,bought_book,authors,book_authors "
						+ "WHERE books.sn=bought_book.bookId and books.sn=book_authors.bookId and authors.id=book_authors.authorid and bought_book.userId="
								+ data.get(0) + ";");
				if (!rs.isBeforeFirst())
					replay = new Replay(ActionType.MY_BOOKS, false);// no data
				else {
					while (rs.next()) {
						elementsList.add(String.valueOf(rs.getInt(1)) + "^" + rs.getString(2) + "^" + rs.getString(3)
								+ " " + rs.getString(4) + "^" + rs.getString(5) + "^" +rs.getString(6) 
								+ "^" +String.valueOf(rs.getFloat(7)));
					}
					replay = new Replay(ActionType.MY_BOOKS, true, elementsList);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		}
		case GETDOMAINSSPECIFIC: {
			ArrayList<String> elementsList = new ArrayList<String>();
			try {
				ResultSet rs = DatabaseController.searchInDatabase(
						"SELECT DISTINCT domains.name "
						+ "FROM books,subjects,book_subjects,domains "
						+ "WHERE subjects.domain=domains.id and subjects.id=book_subjects.subjectId and book_subjects.bookId="
								+ data.get(0) + ";");
				while (rs.next()) {
					elementsList.add(rs.getString(1));

				}
				replay = new Replay(ActionType.GETDOMAINSSPECIFIC, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case POPULARITYREPORT: {
			ArrayList<String> elementsList = new ArrayList<String>();
			try {
				ResultSet rs = DatabaseController.searchInDatabase(
						"SELECT DISTINCT (sn),title,authors.firstName,authors.lastName,language,domains.name "
						+ "FROM books,authors,book_authors,subjects,book_subjects,domains "
						+ "WHERE books.sn=book_authors.bookId and authors.Id=book_authors.authorId and  subjects.domain=domains.id and subjects.id=book_subjects.subjectId and book_subjects.bookId=books.sn;");
				if (!rs.isBeforeFirst())
					replay = new Replay(ActionType.POPULARITYREPORT, false);// no data
				else {
					while (rs.next()) {
						elementsList.add(String.valueOf(rs.getInt(1)) + "^" + rs.getString(2) + "^" + rs.getString(3)
								+ " " + rs.getString(4) + "^" + rs.getString(5) + "^" + rs.getString(6));
					}
			
					replay = new Replay(ActionType.POPULARITYREPORT, true, elementsList);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		}
		case BOOKREPORT: {
			ArrayList<String> elementsList = new ArrayList<String>();
			try {
				System.out.println("sagiv+"+data.get(0));
				ResultSet rs = DatabaseController
						.searchInDatabase("SELECT * FROM book_by_date WHERE bookId="
								+ Integer.valueOf(data.get(0)) + ";");
				System.out.println("melamed");
				while (rs.next()) {
					elementsList.add(
							rs.getString(2) + "^" + String.valueOf(rs.getInt(3)) + "^" + String.valueOf(rs.getInt(4)));
				}
				System.out.println(elementsList.get(0));
				replay = new Replay(ActionType.BOOKREPORT, true, elementsList);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		}
		case UPDATE_REVIEW_STATUS: {
			try {
				char quotationMarks = '"';  
				Statement stmt = DatabaseController.connection.createStatement();
				stmt.executeUpdate("UPDATE project.reviews SET reviews.status = '" + data.get(2) + "' , reviews.content = " + quotationMarks + data.get(1) + quotationMarks + " WHERE reviews.id =" + data.get(0));
				
				DateFormat currentTime = new SimpleDateFormat("HH:mm");
				DateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				
				String currTime = currentTime.format(date);
				String currDate = currentDate.format(date);
				String msg;
				
			
				if (data.get(2).equals("approved"))
				msg = "Review num " + data.get(0) + " has been approved";
				else msg = "Review num " + data.get(0) + " has been declined";
				String username = data.get(3);
				
				DatabaseController.addToDatabase("INSERT INTO messages (`username`, `date`, `time`, `msg`) VALUES('"+username+"', '"+currDate+"' , '"+currTime+"', '"+msg+"')");
					
				replay = new Replay(ActionType.UPDATE_REVIEW_STATUS, true);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case BOOK_REVIEWS: {
			try { 
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT clients.firstName,clients.lastName,bought_book.purchaseDate,reviews.date,reviews.content FROM project.reviews,project.clients,project.bought_book WHERE reviews.status='approved' and reviews.userId=clients.username and reviews.userId=bought_book.userId and bought_book.bookId = " + data.get(0) + " and reviews.bookId = " + data.get(0));
				
				if(rs == null)
				{
					elementsList.add("null"); // no results
				}
				else
				{
					while (rs.next()) {
						elementsList.add(rs.getString(1)); // first name
						elementsList.add(rs.getString(2)); // last name
						elementsList.add(rs.getString(3)); // purchase date
						elementsList.add(rs.getString(4)); // review date
						elementsList.add(rs.getString(5)); // content
					}
				}
				replay = new Replay(ActionType.BOOK_REVIEWS, true, elementsList);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case WRITE_REVIEW: {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();

			String sqlStmt = "INSERT INTO reviews (`userId`, `bookId`, `content`, `status`, `date`)"
					+ "VALUES ('"+data.get(0)+"','"+data.get(1)+"','"+data.get(2)+"','pending','"+dateFormat.format(date).toString()+"')";
	
			try {
				DatabaseController.addToDatabase(sqlStmt);
				sqlResult = true;
			} catch (SQLException e) {
				//System.out.println(sqlStmt);
				System.out.println(e);
				if (e.getErrorCode() == 1062) { //// duplicate primary key
					System.out.println("duplicate primary key - Write a review");
				}
			}
			if (sqlResult == true)
			{
				ArrayList<String> elementsList = new ArrayList<String>();
				elementsList.add(data.get(0));
				replay = new Replay(ActionType.WRITE_REVIEW,true,ActionType.BROADCAST,elementsList);
			}
			else {
				replay = new Replay(ActionType.WRITE_REVIEW, false);
				System.out.println(replay.getSucess());
			}
			break;
		}
		
		
		case GET_BOOK_LIST: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT books.sn,books.title,books.keywords,books.hide,authors.id,CONCAT(authors.firstName,' ',authors.lastName) as authorName, books.summary, books.image, books.price FROM books,book_authors,authors "
						+ "WHERE books.sn=book_authors.bookId AND book_authors.authorId=authors.id "
						+ "ORDER BY sn");
				while (rs.next()) {
					elementsList.add(rs.getString(1)); // sn
					elementsList.add(rs.getString(2)); // title
					elementsList.add(rs.getString(3)); // keywords
					elementsList.add(rs.getString(4)); // hide
					elementsList.add(rs.getString(5)); // author id
					elementsList.add(rs.getString(6)); // author name
					elementsList.add(rs.getString(7)); // book summary
					elementsList.add(rs.getString(8)); // book image
					elementsList.add(rs.getString(9)); // book price
				}
				replay = new Replay(ActionType.GET_BOOK_LIST, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		}
		
		case GET_NUMBER_BOOK_AT_DOMAIN: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				String domainId = data.get(0);
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT COUNT(DISTINCT book_subjects.bookId) FROM project.books,project.domains,project.book_subjects,project.subjects WHERE book_subjects.bookId=books.sn AND domains.id=subjects.domain AND book_subjects.subjectId=subjects.id AND domains.id="+domainId);
				while (rs.next()) {
					elementsList.add(rs.getString(1));
				}
						
				replay = new Replay(ActionType.GET_NUMBER_BOOK_AT_DOMAIN, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case GET_NUMBER_BOOK_AT_SUBJECT: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				String subjectId = data.get(0);
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT booksCount FROM subjects WHERE id="+subjectId);
				while (rs.next()) {
					elementsList.add(rs.getString(1));
				}
						
				replay = new Replay(ActionType.GET_NUMBER_BOOK_AT_SUBJECT, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		}
		
		
		case GET_NUMBER_BOOK_OF_AUTHOR: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				String subjectId = data.get(0);
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT booksCount FROM authors WHERE id="+subjectId);
				while (rs.next()) {
					elementsList.add(rs.getString(1));
				}
						
				replay = new Replay(ActionType.GET_NUMBER_BOOK_OF_AUTHOR, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		}

		
		case DELETE_DOMAIN: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String DomainId = data.get(0);

				stmt.executeUpdate("DELETE FROM domains WHERE id="+DomainId);
				replay = new Replay(ActionType.DELETE_DOMAIN, true);
			}catch (SQLException e) {
						e.printStackTrace();
					}
			break;
		}
		
		case DELETE_AUTHOR: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String AuthorId = data.get(0);

				stmt.executeUpdate("DELETE FROM authors WHERE id="+AuthorId);
				replay = new Replay(ActionType.DELETE_DOMAIN, true);
			}catch (SQLException e) {
						e.printStackTrace();
					}
			break;
		}
		
		case DELETE_SUBJECT: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String subjectId = data.get(0);

				stmt.executeUpdate("DELETE FROM subjects WHERE id="+subjectId);
				replay = new Replay(ActionType.DELETE_SUBJECT, true);
			}catch (SQLException e) {
						e.printStackTrace();
					}
			break;
		}
		
		
		case EDIT_SUBJECT: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String subjectId = data.get(0);
				String subjectName = data.get(1);
				String subjectDomain = data.get(2);

				stmt.executeUpdate("UPDATE subjects SET name ='"+subjectName+"', domain='"+subjectDomain+"' WHERE id="+subjectId);
				replay = new Replay(ActionType.EDIT_SUBJECT, true);
			}catch (SQLException e) {
						e.printStackTrace();
					}
			break;
		}
		
		case ADD_DOMAIN: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String DomainName = data.get(0);

				stmt.executeUpdate("INSERT INTO domains (name,subjectsCount) VALUES ('"+DomainName+"',0)");
				replay = new Replay(ActionType.ADD_DOMAIN, true);
			}catch (SQLException e) {
						e.printStackTrace();
					}
			break;
		}
		
		case ADD_AUTHOR: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String AuthorFirstName = data.get(0);
				String AuthorLastName = data.get(1);

				stmt.executeUpdate("INSERT INTO authors (firstName,lastName,booksCount) VALUES ('"+AuthorFirstName+"','"+AuthorLastName+"','0')");
				replay = new Replay(ActionType.ADD_AUTHOR, true);
			}catch (SQLException e) {
						e.printStackTrace();
					}
			break;
		}
		
		case ADD_SUBJECT: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String subjectName = data.get(0);
				String subjectDomain = data.get(1);

				stmt.executeUpdate("INSERT INTO subjects (name,booksCount,domain) VALUES ('"+subjectName+"',0,'"+subjectDomain+"')");
				stmt.executeUpdate("UPDATE domains SET subjectsCount=subjectsCount+1 WHERE id="+subjectDomain);
				replay = new Replay(ActionType.ADD_SUBJECT, true);
			}catch (SQLException e) {
						e.printStackTrace();
					}
			break;
		}
		
		case EDIT_DOMAIN: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String DomainId = data.get(0);
				String DomainName = data.get(1);
				stmt.executeUpdate("UPDATE domains SET name ='"+DomainName+"' WHERE id="+DomainId);
				replay = new Replay(ActionType.EDIT_DOMAIN, true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case EDIT_AUTHOR: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String AuthorId = data.get(0);
				String AuthorFirstName = data.get(1);
				String AuthorLastName = data.get(2);
				stmt.executeUpdate("UPDATE authors SET firstName ='"+AuthorFirstName+"',lastName ='"+AuthorLastName+"' WHERE id="+AuthorId);
				replay = new Replay(ActionType.EDIT_DOMAIN, true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case DELETE_BOOK: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				String sn = data.get(0);
				ResultSet rs = stmt.executeQuery("SELECT subjectId FROM project.book_subjects WHERE bookId="+sn);
				
				while (rs.next()) {
					elementsList.add(rs.getString(1));
				}
				for(int i=0;i<elementsList.size();i++)
				{
					stmt.executeUpdate("UPDATE subjects SET booksCount=booksCount-1 WHERE id=" + elementsList.get(i));
				}
				
				stmt.executeUpdate("DELETE FROM books WHERE sn=" + sn);
				stmt.executeUpdate("DELETE FROM reviews WHERE bookId=" + sn);
				stmt.executeUpdate("DELETE FROM bought_book WHERE bookId=" + sn);
				stmt.executeUpdate("DELETE FROM book_by_date WHERE bookId=" + sn);
				stmt.executeUpdate("DELETE FROM book_authors WHERE bookId=" + sn);
				stmt.executeUpdate("DELETE FROM book_subjects WHERE bookId=" + sn);
				replay = new Replay(ActionType.DELETE_BOOK, true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case HIDE_BOOK: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String sn = data.get(0);
				String hide = data.get(1);
				stmt.executeUpdate("UPDATE books SET hide="+hide+" WHERE sn="+sn);
				replay = new Replay(ActionType.HIDE_BOOK, true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		
		case ADD_BOOK: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String TitleBook = data.get(0);
				String keywords = data.get(2);
				String language = data.get(3);
				String tableOfContent = data.get(5);
				String summary = data.get(6);
				String picture = data.get(7);
				Float price = Float.parseFloat(data.get(8));
				String authorsId = data.get(1);
				String SubjectsList = data.get(4);
				
				String subjectsId[];
				subjectsId = SubjectsList.split("\\^");
				String query = "SELECT COUNT(distinct domain) FROM subjects WHERE id="+subjectsId[0];
				for(int i=1;i<subjectsId.length;i++){
					query+=" OR id="+subjectsId[i];		
				}
				System.out.println(query);				
				ResultSet rs = stmt.executeQuery(query);
				
				String authorsArr[];
				authorsArr = authorsId.split("\\^");
				
				rs.next();
				int DomainsCount=Integer.parseInt(rs.getString(1));
				int SubjectsCount=subjectsId.length;
				int authorsCount=authorsArr.length;
				int bookSn;
				
				//add book
				if(picture.equals("noPicture"))
					query="INSERT INTO books (`title`, `language`, `authorsCount`, `summary`, `tableOfContent`, `keywords`, `price`, `hide`, `domainsCount`, `subjectsCount`) "
							+ "VALUES('"+TitleBook+"', '"+language+"' , '"+authorsCount+"', '"+summary+"', '"+tableOfContent+"', '"+keywords+"', '"+price+"', '0', '"+DomainsCount+"', '"+SubjectsCount+"')";		
				else{
					query="INSERT INTO books (`title`, `language`, `authorsCount`, `summary`, `tableOfContent`, `keywords`, `price`, `hide`, `domainsCount`, `subjectsCount`, `image`) "
							+ "VALUES('"+TitleBook+"', '"+language+"' , '"+authorsCount+"', '"+summary+"', '"+tableOfContent+"', '"+keywords+"', '"+price+"', '0', '"+DomainsCount+"', '"+SubjectsCount+"', '"+picture+"')";	
				System.out.println("sfdsasafsafsaf");
				}
				PreparedStatement statement = (PreparedStatement) DatabaseController.connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
				int affectedRows = statement.executeUpdate();
				if (affectedRows == 0) {
		            throw new SQLException("Creating user failed, no rows affected.");
		        }
		        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		            	bookSn=(int) generatedKeys.getLong(1);
		            }
		            else {
		                throw new SQLException("Creating book failed, no ID obtained.");
		            }
		        }//end add book
		        
		        //update book_authors
		        for(int i=0;i<authorsArr.length;i++){
		        	stmt.executeUpdate("INSERT INTO book_authors (`bookId`, `authorId`) VALUES('"+bookSn+"', '"+authorsArr[i]+"')");
		        }
		        
		        //update book_subjects
		        for(int i=0;i<subjectsId.length;i++){
		        	stmt.executeUpdate("INSERT INTO book_subjects (`bookId`,`subjectId`) VALUES('"+bookSn+"', '"+subjectsId[i]+"')");
		        }
		        
		        //update book_by_date
		        Date Currentdate=new Date();
				SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
				String date=(String)dateFormat.format(Currentdate);
				DatabaseController.addToDatabase("INSERT INTO book_by_date VALUES ("+bookSn+",'"+date+"','0','0')");

		        
				
		        //update booksCount at subjects
		        for(int i=0;i<subjectsId.length;i++){
		        	stmt.executeUpdate("UPDATE subjects SET booksCount = booksCount + 1 WHERE id="+subjectsId[i]);
		        }
		        
		        //update bookscount at authors
		        for(int i=0;i<authorsArr.length;i++){
		        	stmt.executeUpdate("UPDATE authors SET booksCount = booksCount + 1 WHERE id="+authorsArr[i]);
		        }

				//stmt.executeUpdate("UPDATE books SET hide="+hide+" WHERE sn="+sn);
				replay = new Replay(ActionType.HIDE_BOOK, true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		case EDIT_BOOK: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				String Sn = data.get(9);
				String TitleBook = data.get(0);
				String keywords = data.get(2);
				String language = data.get(3);
				String tableOfContent = data.get(5);
				String summary = data.get(6);
				String picture = data.get(7);
				Float price = Float.parseFloat(data.get(8));
				String authorsId = data.get(1);
				String SubjectsList = data.get(4);
				//System.out.println("asasassas");
				
				//get the subjects of the book before update
				ResultSet rs = stmt.executeQuery(
						"SELECT book_subjects.subjectId FROM project.book_subjects WHERE book_subjects.bookId="+Sn);
				while (rs.next()) {
					elementsList.add(rs.getString(1));
					System.out.println("===="+rs.getString(1));
				}
				//update the book count of subject
				for(int i=0;i<elementsList.size();i++){
					stmt.executeUpdate("UPDATE subjects SET booksCount = booksCount - 1 WHERE id="+elementsList.get(i));
				}
				elementsList = new ArrayList<String>();
				System.out.println("1");
				
				//get the authors of the book before update
				ResultSet rs1 = stmt.executeQuery(
						"SELECT book_authors.authorId FROM project.book_authors WHERE book_authors.bookId="+Sn);
				while (rs1.next()) {
					elementsList.add(rs1.getString(1));
					System.out.println("===="+rs1.getString(1));
				}
				//update the book count of authors
				for(int i=0;i<elementsList.size();i++){
					stmt.executeUpdate("UPDATE authors SET booksCount = booksCount - 1 WHERE id="+elementsList.get(i));
				}
				elementsList = new ArrayList<String>();
				System.out.println("2");
				
				//delete all the old authors
				stmt.executeUpdate("DELETE FROM book_authors WHERE bookId="+Sn);
				//delete all the old subjects
				stmt.executeUpdate("DELETE FROM book_subjects WHERE bookId="+Sn);
				System.out.println("3");
				
				
				String subjectsId[];
				subjectsId = SubjectsList.split("\\^");
				String query = "SELECT COUNT(distinct domain) FROM subjects WHERE id="+subjectsId[0];
				for(int i=1;i<subjectsId.length;i++){
					query+=" OR id="+subjectsId[i];		
				}
				System.out.println(query);				
				ResultSet rs11 = stmt.executeQuery(query);
				
				String authorsArr[];
				authorsArr = authorsId.split("\\^");
				
				rs11.next();
				int DomainsCount=Integer.parseInt(rs11.getString(1));
				int SubjectsCount=subjectsId.length;
				int authorsCount=authorsArr.length;
				int bookSn;
						
				System.out.println("4");
				//add book
				if(picture.equals("noPicture"))
					query="UPDATE books SET title='"+TitleBook+"', language='"+language+"', authorsCount='"+authorsCount+"', summary='"+summary+"', tableOfContent='"+tableOfContent+"', keywords='"+keywords+"', price='"+price+"', domainsCount='"+DomainsCount+"', subjectsCount='"+SubjectsCount+"' "
							+ "WHERE sn="+Sn;		
				else{
					query="UPDATE books SET title='"+TitleBook+"', language='"+language+"', authorsCount='"+authorsCount+"', summary='"+summary+"', tableOfContent='"+tableOfContent+"', keywords='"+keywords+"', price='"+price+"', domainsCount='"+DomainsCount+"', subjectsCount='"+SubjectsCount+"', image='"+picture+"' "
							+ "WHERE sn="+Sn;	
				}
				stmt.executeUpdate(query);
				//end add book
		        
		        //update book_authors
		        for(int i=0;i<authorsArr.length;i++){
		        	stmt.executeUpdate("INSERT INTO book_authors (`bookId`, `authorId`) VALUES('"+Sn+"', '"+authorsArr[i]+"')");
		        }
		        
		        //update book_subjects
		        for(int i=0;i<subjectsId.length;i++){
		        	stmt.executeUpdate("INSERT INTO book_subjects (`bookId`,`subjectId`) VALUES('"+Sn+"', '"+subjectsId[i]+"')");
		        }
				
		        //update booksCount at subjects
		        for(int i=0;i<subjectsId.length;i++){
		        	stmt.executeUpdate("UPDATE subjects SET booksCount = booksCount + 1 WHERE id="+subjectsId[i]);
		        }
		        
		        //update bookscount at authors
		        for(int i=0;i<authorsArr.length;i++){
		        	stmt.executeUpdate("UPDATE authors SET booksCount = booksCount + 1 WHERE id="+authorsArr[i]);
		        }

				//stmt.executeUpdate("UPDATE books SET hide="+hide+" WHERE sn="+sn);
				replay = new Replay(ActionType.HIDE_BOOK, true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		case GET_BOOK_LANGUAGE: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				String bookSn = data.get(0);
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT language FROM books WHERE sn="+bookSn);
				while (rs.next()) {
					elementsList.add(rs.getString(1));
				}
						
				replay = new Replay(ActionType.GET_BOOK_LANGUAGE, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case GET_BOOK_TABLE_OF_CONTENT: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				String bookSn = data.get(0);
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT tableOfContent FROM books WHERE sn="+bookSn);
				while (rs.next()) {
					elementsList.add(rs.getString(1));
				}
						
				replay = new Replay(ActionType.GET_BOOK_TABLE_OF_CONTENT, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case GET_BOOK_AUTHORS: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				String bookSn = data.get(0);
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT authors.id,authors.firstName,authors.lastName,book_authors.bookId,book_authors.authorId FROM project.authors,project.book_authors "
						+ "WHERE book_authors.authorId=authors.id AND book_authors.bookId="+bookSn);
				while (rs.next()) {
					elementsList.add(rs.getString(1) + "^" + rs.getString(2) + "^" + rs.getString(3));
				System.out.println(rs.getString(1));
				}
						
				replay = new Replay(ActionType.GET_BOOK_AUTHORS, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		
		case GET_PENDING_ACCOUNTS: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT username,firstName,lastName,accountType,accountStatus FROM project.clients WHERE accountStatus<>'Standard' AND accountStatus<>'PendingPayment' AND isBlocked=0");
				while (rs.next()) {
					elementsList.add(rs.getString(1)); // username
					elementsList.add(rs.getString(2)); // first name
					elementsList.add(rs.getString(3)); // last name
					elementsList.add(rs.getString(4)); // account type
					elementsList.add(rs.getString(5)); // account status (requested account type)
				}
				replay = new Replay(ActionType.GET_PENDING_ACCOUNTS, true, elementsList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case UPDATE_PENDING_ACCOUNT: {
			try {
				Statement stmt = DatabaseController.connection.createStatement();
				String username = data.get(0);
				String operation = data.get(1);
				
				//System.out.println(data.get(0) + " " + data.get(1) + " " + data.get(2));
				
				if(operation.equals("approve"))
				{
					stmt.executeUpdate("UPDATE clients SET accountType='" + data.get(2) + "' , accountStatus='PendingPayment' WHERE username=" + data.get(0));
				}
				else
				{
					stmt.executeUpdate("UPDATE clients SET accountType='" + data.get(2) + "' , accountStatus='Standard' WHERE username=" + data.get(0));
				}
		
				replay = new Replay(ActionType.UPDATE_PENDING_ACCOUNT, true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case CHECK_WRITE_REVIEW: {
			try {
				boolean allowed = false;
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT bought_book.userId,bought_book.bookId FROM project.bought_book WHERE NOT EXISTS (SELECT reviews.userId,reviews.bookId FROM project.reviews WHERE bought_book.userId = reviews.userId AND bought_book.bookId = reviews.bookId) AND bought_book.userId = '" + data.get(0) + "' AND bought_book.bookId = '" + data.get(1) + "';");
				while (rs.next()) {
					allowed = true;
				}
				replay = new Replay(ActionType.CHECK_WRITE_REVIEW, allowed);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case GET_TOTAL_PRICE:
		{
			ArrayList<String> elementsList = new ArrayList<String>();
			try {
				ResultSet rs = DatabaseController.searchInDatabase("SELECT bookId,SUM(purchaseCount) FROM book_by_date GROUP BY bookId ;");
				if (!rs.isBeforeFirst())
					replay = new Replay(ActionType.GET_TOTAL_PRICE, false);// no data
				else {
					while (rs.next()) {
						elementsList.add(String.valueOf(rs.getInt(1)) + "^"+String.valueOf(rs.getInt(2)) );
					}
					for(int i=0;i<elementsList.size();i++)
						System.out.println(elementsList.get(i));
			
					replay = new Replay(ActionType.GET_TOTAL_PRICE, true, elementsList);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		}
		
		case GET_BOOK_IMG: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT books.image FROM project.books WHERE books.sn ='" + data.get(0) + "';");
				while (rs.next()) {
					sqlResult = true;
					elementsList.add(rs.getString(1)); // image
				}
				if(sqlResult == true)
					replay = new Replay(ActionType.GET_BOOK_IMG, true,elementsList);
				else
					replay = new Replay(ActionType.GET_BOOK_IMG, false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case GET_BUY_STATUS: {
			try {
				String operation = "0"; // user not allowed to buy this book
				sqlResult = false;
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT clients.username FROM project.clients, project.bought_book WHERE clients.username = '" + data.get(0) + "' AND"
						+ " clients.username = bought_book.userId AND bought_book.bookId = '" + data.get(1) + "';");
				while (rs.next()) {
					sqlResult = true;
					operation = "1"; //already bought this book
				}
				if(sqlResult == false)
				{
					rs = stmt.executeQuery("SELECT clients.username FROM project.clients WHERE clients.username = '" + data.get(0) + "' AND "
					+ "clients.isBlocked=0 AND clients.accountType = 'PerBook' AND "
					+ "clients.username NOT IN (SELECT bought_book.userId FROM bought_book WHERE bought_book.bookId='" + data.get(1) + "');");
					while (rs.next()) {
						sqlResult = true;
						operation = "2"; //user PerBook account type and allowed to buy this book
					}
				}
				if(sqlResult == false)
				{
					rs = stmt.executeQuery("SELECT DISTINCT clients.username FROM project.clients,project.books WHERE clients.username = '" + data.get(0) + "' "
					+ "AND clients.isBlocked=0 AND (clients.accountType = 'Monthly' OR clients.accountType = 'Yearly') AND books.sn = '" + data.get(1) + "' "
					+ "AND clients.username NOT IN (SELECT bought_book.userId FROM bought_book WHERE bought_book.bookId='" + data.get(1) + "') "
					+ "AND clients.credits>=books.price AND STR_TO_DATE(clients.endSubscription, '%d/%m/%Y')>=curdate();");
					while (rs.next()) {
						sqlResult = true;
						operation = "3"; //user Subscribed account type and allowed to buy this book
					}				
				}
				replay = new Replay(ActionType.GET_BUY_STATUS, true,operation);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case BUY_BOOK: {
			boolean subs = false;
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			
			//System.out.println(dateFormat.format(date).toString());
			
			//add boughtBook.
			String sqlStmt = "INSERT INTO bought_book (`userId`, `bookId`, `purchaseDate`, `price`)"
					+ "VALUES ('"+data.get(0)+"','"+data.get(1)+"','"+dateFormat.format(date).toString()+"','"+data.get(2)+"')";
	
			try {
				DatabaseController.addToDatabase(sqlStmt);
				//sqlResult = true;
				int count=0;
				float credits=0;
				
				//increase the purchase counter.
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT book_by_date.purchaseCount FROM project.book_by_date "
						+ "WHERE book_by_date.bookId ='" + data.get(1) + "' AND book_by_date.date = '" + dateFormat.format(date).toString() + "';");
				while (rs.next()) {
					//sqlResult = true;
					count = Integer.parseInt(rs.getString(1));
				}
				count++;
				
				stmt.executeUpdate("UPDATE book_by_date SET purchaseCount="+count+" WHERE "
						+ "book_by_date.bookId ='" + data.get(1) + "' AND book_by_date.date = '" + dateFormat.format(date).toString() + "';");
				
				
				if(data.get(3).equals("2") || data.get(3).equals("3"))//decrease the credits.
				{
					stmt = DatabaseController.connection.createStatement();
					rs = stmt.executeQuery("SELECT clients.credits FROM project.clients "
							+ "WHERE clients.username ='" + data.get(0) + "';");
					while (rs.next()) {
						//sqlResult = true;
						credits = Float.parseFloat(rs.getString(1));
					}
					credits = credits - Float.parseFloat(data.get(2));
					
					stmt.executeUpdate("UPDATE clients SET credits="+credits+" WHERE "
							+ "clients.username ='" + data.get(0) + "';");		
					
					subs = true;
				}
				
				sqlResult = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (sqlResult == true)
			{
				if(subs == true)
					replay = new Replay(ActionType.BUY_BOOK_SUBS,true,data.get(3));
				else
					replay = new Replay(ActionType.BUY_BOOK,true,data.get(3));
			}
			else {
				if(subs == true)
					replay = new Replay(ActionType.BUY_BOOK_SUBS,false,data.get(3));
				else
					replay = new Replay(ActionType.BUY_BOOK, false,data.get(3));
			}
			break;
		}
		case BUY_SUBSCRIPTION: {
			try {
				//
				float credits=0;
				ResultSet rs = DatabaseController.searchInDatabase("SELECT clients.credits FROM project.clients "
						+ "WHERE clients.username ='" + data.get(0) + "';");
				while (rs.next()) {
					credits = Float.parseFloat(rs.getString(1));
				}
				credits = credits + Float.parseFloat(data.get(1));
				
				DatabaseController.updateDatabase("UPDATE clients SET credits="+credits+" , endSubscription='" + data.get(2) + "' , accountStatus='Standard' WHERE "
						+ "clients.username ='" + data.get(0) + "';");

				sqlResult = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (sqlResult == true)
			{
				replay = new Replay(ActionType.BUY_SUBSCRIPTION,true,data.get(3));
			}
			else {
				replay = new Replay(ActionType.BUY_SUBSCRIPTION, false,data.get(3));
			}
			break;
		}
		case CHECK_ACCOUNT_TYPE: {
			try {
				ArrayList<String> elementsList = new ArrayList<String>();
				Statement stmt = DatabaseController.connection.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT clients.accountType,clients.accountStatus,clients.credits,clients.endSubscription FROM project.clients"
						+ " WHERE clients.username ='" + data.get(0) + "';");
				while (rs.next()) {
					sqlResult = true;
					elementsList.add(rs.getString(1)); // account type
					elementsList.add(rs.getString(2)); // account status
					elementsList.add(rs.getString(3)); // credits
					elementsList.add(rs.getString(4)); // expiration date
				}
				if(sqlResult == true)
					replay = new Replay(ActionType.CHECK_ACCOUNT_TYPE, true,elementsList);
				else
					replay = new Replay(ActionType.CHECK_ACCOUNT_TYPE, false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}

		}
		return replay;

	}

	/**
	 * When the user pressed menu -> "help" it displays the user the message
	 * about us. 
	 * @param event - Gets the event.
	 */
	@FXML
	public void PressedHelpMenu(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Library");
		alert.setHeaderText(null);
		alert.setContentText(GeneralMessages.ABOUT_US);
		alert.showAndWait();
	}

	/**
	 * This function called when the user pressed on the button connect or
	 * disconnect the function connect databse, and server.
	 * @param event - Gets the event.
	 * @throws IOException
	 */
	@FXML
	public void buttonPressed(ActionEvent event) throws IOException {
		if (connectButton.getText().equals("Connect")) {
			try {
				if (userField.getText().equals(""))
					throw new IOException();
				DatabaseController databaseController = new DatabaseController();
				databaseController.SetConnection(userField.getText(), passField.getText());
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("user or password are empty. cannot connect to server");
				alert.showAndWait();
				return;
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("user or password are incorrect. cannot connect to server");
				alert.showAndWait();
				return;
			} catch (Exception e) {
				writeToLog("Java driver not found.");
				return;
			}
			writeToLog("Connected to database");
			try {
				usernameLabel.setVisible(false);
				passLabel.setVisible(false);
				userField.setVisible(false);
				passField.setVisible(false);
				userField.clear();
				passField.clear();
				// problem
				connectButton.setText("Disconnect");
				this.listen();
			} catch (Exception e) {
				writeToLog("Cant connect server.");
				return;
			}
			return;
		}
		if (connectButton.getText().equals("Disconnect")) {
			writeToLog("Disconnected sucessfully");
			try {
				this.close();
				DatabaseController.CloseConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			usernameLabel.setVisible(true);
			passLabel.setVisible(true);
			userField.setVisible(true);
			passField.setVisible(true);
			userField.clear();
			passField.clear();
			connectButton.setText("Connect");
		}

	}

	/**
	 * This function send the parameter to file, and to I/O after getting the
	 * time. it appends the string.
	 * @param msg - Gets themessage that will be write in log file, and into server GUI.
	 */
	void writeToLog(String msg) {
		Date datelog = new Date();
		logger.info(msg);
		logField.appendText(datelog.toString() + " " + msg + "\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ocsf.server.AbstractServer#serverStarted()
	 */
	@Override
	protected void serverStarted() {
		writeToLog("Server listening for connections\non port: " + getPort());
	}

	
	/**
	 * This function returns relevant data for book search
	 * @author itain
	 * @return - all unhidden books from server with relevant data for books search
	 */
	private ArrayList<String> makeSearchBook() throws IOException 
	{

		ArrayList<String> elementsList = new ArrayList<String>();
		ArrayList<String> res = new ArrayList<String>();
		
		try {

			ArrayList<String> book_sn = new ArrayList<String>();
			ArrayList<String> bookId_authorId = new ArrayList<String>();
			ArrayList<String> authors = new ArrayList<String>();
			ArrayList<String> bookId_authorName = new ArrayList<String>();

			ArrayList<String> book_subjects = new ArrayList<String>();
			ArrayList<String> domains = new ArrayList<String>();
			ArrayList<String> subjects = new ArrayList<String>();
			ArrayList<String> book_subjects_domains = new ArrayList<String>();
			
			

			/* add sn, title, language, authors count for each book */
			Statement stmt = DatabaseController.connection.createStatement();
			ResultSet rs_books = stmt.executeQuery("SELECT sn, title, language, summary, tableOfContent, keywords, authorsCount FROM books;");
			while(rs_books.next())
			{

				elementsList.add(rs_books.getString(1) + "^" + rs_books.getString(2) + "^" + rs_books.getString(3) + "^" + rs_books.getString(4)+ "^" + rs_books.getString(5)+ "^" + rs_books.getString(6)+ "^" + rs_books.getString(7));
				
				book_sn.add(rs_books.getString(1));
			}
			
			
			/* add authors names for each book */
			ResultSet rs_book_authors = stmt.executeQuery("SELECT * FROM book_authors;");
			while (rs_book_authors.next())
			{
				bookId_authorId.add(rs_book_authors.getString(1)); // bookId
				bookId_authorId.add(rs_book_authors.getString(2)); // authorId
			}

			ResultSet rs_authors = stmt.executeQuery("SELECT * FROM authors;");
			while (rs_authors.next())
			{
				authors.add(rs_authors.getString(1)); // author id
				authors.add(rs_authors.getString(2) + " " + rs_authors.getString(3)); // author name
			}

			for (int i = 0; i < bookId_authorId.size(); i = i + 2)
			{
				for (int j = 0; j < authors.size(); j = j + 2)
				{
					if (bookId_authorId.get(i + 1).equals(authors.get(j)))
					{
						bookId_authorName.add(bookId_authorId.get(i)); // book id
						bookId_authorName.add(authors.get(j + 1)); // book's author full name
						break;
					}
				}
			}

			for (int i = 0; i < bookId_authorName.size(); i = i + 2)
			{
				for (int j = 0; j < elementsList.size(); j++) 
				{
					String bookid[] = new String[4];
					bookid = elementsList.get(j).split("\\^");
					if (bookId_authorName.get(i).equals(bookid[0]))
					{
						String tmp2 = elementsList.get(j);
						tmp2 = tmp2 + "^" + bookId_authorName.get(i + 1);
						elementsList.add(j + 1, tmp2);
						elementsList.remove(j);
						break;

					}

				}

			}
			
			
			/* add subjects count for each book */ 
			ResultSet rs_books2 = stmt.executeQuery("SELECT subjectsCount FROM books;");
			int k = 0;
			while (rs_books2.next()) {
				String tmp = elementsList.get(k);
				tmp = tmp + "^" + rs_books2.getString(1);
				elementsList.add(k + 1, tmp);
				elementsList.remove(k);
				k++;
			}
			
		
			//download book_subjects table from DB
			ResultSet rs_bookSubjects= stmt.executeQuery("SELECT * from book_subjects;");
			while(rs_bookSubjects.next())
			{
				book_subjects.add(rs_bookSubjects.getString(1)); //book id
				book_subjects.add(rs_bookSubjects.getString(2)); //subject id
			}



			// download subjects table from DB
			ResultSet rs_subjects = stmt.executeQuery("SELECT * from subjects;");
			while (rs_subjects.next()) 
			{
				subjects.add(rs_subjects.getString(1)); // subject id
				subjects.add(rs_subjects.getString(2)); // subject name
				subjects.add(rs_subjects.getString(4)); // domain id
			}
			
	

			// download domains table from DB
			ResultSet rs_domains = stmt.executeQuery("SELECT * from domains;");
			while (rs_domains.next())
			{
				domains.add(rs_domains.getString(1)); // domain id
				domains.add(rs_domains.getString(2)); // domain name
			}
			

			// build book_subjects_domains

			for (int i = 0; i < book_sn.size(); i++)
				book_subjects_domains.add("");

			for (int i = 0; i < book_subjects_domains.size(); i++) //
			{
				for (int j = 0; j < book_subjects.size(); j = j + 2)
				{
					if (book_sn.get(i).equals(book_subjects.get(j))) // found book id in books_subjects, now find its name
					{
						for (int m = 0; m < subjects.size(); m = m + 3) 
						{
							if (book_subjects.get(j + 1).equals(subjects.get(m))) // subjects ids are equal, now take its name
							{
								String tmp = book_subjects_domains.get(i);
								tmp = tmp + "^" + subjects.get(m + 1);

								book_subjects_domains.add(i + 1, tmp);
								book_subjects_domains.remove(i);

								// now take domain's name
								for (int n = 0; n < domains.size(); n = n + 2)
								{

									if ((subjects.get(m + 2).equals(domains.get(n)))) // domains ids are equal,now take its name
									{
										String tmp2 = book_subjects_domains.get(i);
										tmp2=tmp2+"^"+domains.get(n+1);
										book_subjects_domains.add(i+1, tmp2);
										book_subjects_domains.remove(i);
										break;
									}
								}
								break;
							}
						}
					}
				}
			}
			
			for (int i = 0; i < elementsList.size(); i++)
			{
				String tmp = elementsList.get(i);
				tmp = tmp + book_subjects_domains.get(i);
				elementsList.add(i + 1, tmp);
				elementsList.remove(i);
			}

			
			
			
			ResultSet rs_books3 = stmt.executeQuery("SELECT price FROM books;");
			k = 0;
			while (rs_books3.next())
			{
				String tmp = elementsList.get(k);
				tmp = tmp + "^" + rs_books3.getString(1); //price
				elementsList.add(k + 1, tmp);
				elementsList.remove(k);
				k++;
			}
			
			ArrayList<String> filter = new ArrayList<String>();
			ResultSet rs_books4 = stmt.executeQuery("SELECT hide FROM books;");
			while(rs_books4.next())
				filter.add(rs_books4.getString(1));
			
			//return only not hidden books
			for(int i=0;i<filter.size();i++)
				if(Integer.parseInt(filter.get(i))==0)
					res.add(elementsList.get(i));
		
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	
	
	
	/**
	 * This function returns number of items on each book row on search
	 * @param str - Gets str string.
	 * @param loofFor - Gets lookFor string.
	 * @return - the count of items.
	 */
	private int countItems(String str, String lookFor)
	{
		String findStr = lookFor;
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {

		    lastIndex = str.indexOf(findStr, lastIndex);

		    if (lastIndex != -1) {
		        count++;
		        lastIndex += findStr.length();
		    }
		}
		return count+1;
	}

	/**
	 * generateFile encode the file to bytes, and receive the file encoded.
	 * @param destinationPath - Gets the destination path to the file.
	 * @param sourceFilePath - Gets the source path.
	 * @return The file encoded.
	 */
	public FileEvent generateFile(String destinationPath, String sourceFilePath) {

		FileEvent fileEvent = new FileEvent();
		String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("/") + 1, sourceFilePath.length());
		String path = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("/") + 1);
		fileEvent.setDestinationDirectory(destinationPath);
		fileEvent.setFilename(fileName);
		fileEvent.setSourceDirectory(sourceFilePath);
		File file = new File(sourceFilePath);
		if (file.isFile()) {
		try {
		DataInputStream diStream = new DataInputStream(new FileInputStream(file));
		long len = (int) file.length();
		byte[] fileBytes = new byte[(int) len];
		int read = 0;
		int numRead = 0;
		while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
		read = read + numRead;
		}
		fileEvent.setFileSize(len);
		fileEvent.setFileData(fileBytes);
		fileEvent.setStatus("Success");
		} catch (Exception e) {
		e.printStackTrace();
		fileEvent.setStatus("Error");
		}
		} else {
		System.out.println("path specified is not pointing to a file");
		fileEvent.setStatus("Error");
		}
		return fileEvent;
	}
	
}
