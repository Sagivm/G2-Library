package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import enums.ActionType;

/**
 * This class include the methods for SQL queries. the class also include the connection
 * into the constructor.
 * @author nire
 *
 */
public class DatabaseController {

	
/**
 * static variable for database connection.
 */
public static Connection connection;

/**
 * Create new instance of driver for SQL connection. if the action failed
 * exceptions throw and catch into other class.
 * @throws InstantiationException Instantiation Exception.
 * @throws IllegalAccessException Illegal Access Exception.
 * @throws ClassNotFoundException Class Not Found Exception.
 */
public DatabaseController() throws InstantiationException,
IllegalAccessException,
ClassNotFoundException
{
	Class.forName("com.mysql.jdbc.Driver").newInstance();
}

/**
 * Set connection to database, and presave the connection in static variable connection.
 * @param username Gets the username
 * @param password Gets the database password
 * @throws SQLException SQL exceptions.
 */
void SetConnection(String username, String password) throws SQLException
{
	connection = DriverManager.getConnection("jdbc:mysql://localhost/project",username,password);
}

/**
 * Close the connection from database.
 */
static void CloseConnection()
{
	try {
		connection.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

/** Perform query to database (add).
 * @param statement Gets the statement.
 * @throws SQLException throw exception if there is problem in database.
 */
public static void addToDatabase(String statement) throws SQLException {
	Statement stmt=connection.createStatement();
	stmt.executeUpdate(statement);
    stmt.close();	
}

/** Perform query to database (remove).
 * @param statement Gets the statement.
 * @throws SQLException throw exception if there is problem in database.
 */
public static void removeFromDatabase(String statement) throws SQLException {
	Statement stmt=connection.createStatement();
	stmt.executeUpdate(statement);
    stmt.close();	
}

/** Perform query to database (update).
 * @param statement Gets the statement.
 * @throws SQLException throw exception if there is problem in database.
 */
public static void updateDatabase(String statement) throws SQLException {
	Statement stmt=connection.createStatement();
	stmt.executeUpdate(statement);
    stmt.close();	
}

/** Perform query to database (search).
 * @param statement Gets the statement.
 * @throws SQLException throw exception if there is problem in database.
 */
public static ResultSet searchInDatabase(String statement) throws SQLException {
	Statement stmt = connection.createStatement();
	ResultSet rs = stmt.executeQuery(statement);
	return rs;	
}

}
