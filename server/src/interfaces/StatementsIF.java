package interfaces;

/** This interface include in every entity specific statement preparation that will
 * send to the database. this behaivor helps to manage the statements to every entity.
 * @author nire
 *
 */
public interface StatementsIF {

/** Prepare statement for adding item to database.
 * @return String of the statement
 */
public String PrepareAddStatement();

/** Prepare statement for delete item from database.
 * @return String of the statement
 */
public String PrepareDeleteStatement();

/** Prepare statement for update item in database.
 * @return String of the statement
 */
public String prepareUpdateStatement();

/** Prepare statement for select item from database.
 * @return String of the statement
 */
public String PrepareSelectStatement();
	
}
