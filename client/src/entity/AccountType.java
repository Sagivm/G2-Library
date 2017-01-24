package entity;

/**
 * Enum Class. Define available account types for Users
 * @author sagivm
 *
 */
public enum AccountType {
	/**
	 * States that the user doesn't have a payment agreement with the system.
	 */
	Intrested,
	/**
	 * States that the user has a per book subscription.
	 */
	PerBook,
	/**
	 * States that the user has a monthly subscription.
	 */
	Monthly,
	/**
	 * States that the user has a yearly subscription.
	 */
	Yearly

};
