package entity;

import java.util.ArrayList;

import enums.ActionType;

/**
 * This class includes error constants.
 * the variables initialized with final strings, and they static variables.
 * @author nire
 */
public class GeneralMessages {

	/**
	 *  Constant server offline.
	 */
	public static final String SERVER_OFFLINE = "Server is currently offline.";
	
	
	/**
	 * Constant for wrong address.
	 */
	public static final String WRONG_IP = "Not legal IP Address";
	
	
	/**
	 * General error message.
	 */
	public static final String UNNKNOWN_ERROR = "Unnknown error occureed. system terminate.";
	
	
	/**
	 * About the software.
	 */
	public static final String ABOUT_US = "The system built during the course"
				+ " ''Laboratory in software engineering'' on the 5th semester. "
				+ "The system communicates with client-server TCP/IP protocol, "
				+ "and manage digital library.";
	
	
	/**
	 * Empty fields.
	 */
	public static final String EMPTY_FIELDS = "One or more fields are missing.";
	
	/**
	 * Empty fields.
	 */
	public static final String ILLEGAL_CHARACTER = "'^' is an illegal character";
	
	
	/**
	 * Error during communication.
	 */
	public static final String UNNKNOWN_ERROR_DURING_SEND = "Error occurred during "
			+ "communication with server. system terminate.";


	/**
	 * Validation of the username
	 */
	public static final String MUST_INCLUDE_ONLY_DIGITS = "The username must include "
			+ "only numbers and be at least 9 digits.";
	
	/**
	 * Validation of the username
	 */
	public static final String MUST_INCLUDE_ONLY_DIGITS_VER2 = "The username must include "
			+ "only numbers.";


	/**
	 * The passwords not match.
	 */
	public static final String PASSWORD_NOT_MATCH = "Password not match";
	
	
	/**
	 * The action completed and sent to librarian's approval.
	 */
	public static final String PENDING_FOR_LIBRARIAN = "Action complete."
			+ " waiting for librarian confirmation";
	
	
	/**
	 * The password is too short.
	 */
	public static final String PASSWORD_TOO_SHORT = "Password needs to be at least 5 symbols.";


	/**
	 * The user is already exist in the system.
	 */
	public static final String USER_ALREADY_EXISTS = "Username already exists in system.";


	/**
	 * Empty fields for registration.
	 */
	public static final String EMPTY_FIELDS_REGISTER = "In order to register you need to "
			+ "provide valid IP address, and stay username & password empty.";


	/**
	 * User logged in.
	 */
	public static final String USER_LOGGED_IN_SUCESSFULLY = "Logged in sucessfully";


	/**
	 * User or password failed.
	 */
	public static final String USER_LOGGED_IN_FAILED = "Username or password are incorrect";
	
	/**
	 * User Already logged in.
	 */
	public static final String USER_ALREADY_LOGGED_IN = "You are already connected to the system!";

	/**
	 * Review can't be empty.
	 */
	public static final String EMPTY_REVIEW = "The review's content can't be empty";
	
	/**
	 * Operation succeeded
	 */
	public static final String OPERATION_SUCCEEDED = "Operation succeeded!";
	
	/**
	 * Operation failed
	 */
	public static final String OPERATION_FAILED = "Operation failed!";
	
	
	/**
	 * Validation of the name
	 */
	public static final String MUST_INCLUDE_ONLY_CHARACTERS = "The name must include only characters.";


	/**
	 * Message for librarian that there is new pending reviews.
	 */
	public static final String YOU_GOT_NEW_REVIEWS = "You got new pending reviews for confirmation.";

	/**
	 * Message for user that the card number must include only numbers.
	 */
	public static final String MUST_INCLUDE_ONLY_DIGIT_CARD = "The card number must include only numbers.";
	
	/**
	 * Message for user that the card number is too short or long.
	 */
	public static final String WRONG_COUNT_OF_DIGIT_CARD = "The card number is too short or too long.";
	
	
	/**
	 * Message for user that the date is wrong.
	 */
	public static final String INVALID_DATE = "The date must include only digit. Format MM for Month and YY for Year";
	
	/**
	 * Message for user that the CVV is wrong.
	 */
	public static final String CVV_INVALID = "The CVV must include only 3 or 4 digit.";
	
	/**
	 * Message for user that the ID in not 9 digits.
	 */
	public static final String MUST_INCLUDE_ONLY_DIGITS_ID = "The ID must include only 9 digits.";

	/**
	 * User blocked.
	 */
	public static final String USER_BLOCKED = "Blocked from system.";
	
	/**
	 * Purchase of the book was successfully done!.
	 */
	public static final String BOOK_PURCHASE_SUCCESS = "Purchase of the book was successfully done!";
	
	/**
	 * Purchase of the subscription was successfully done!.
	 */
	public static final String SUBSCRIPTION_PURCHASE_SUCCESS = "Purchase of the subscription was successfully done!";
	
	
}
