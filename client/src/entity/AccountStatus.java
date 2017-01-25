package entity;

/**
 * Enum class. Defines available account status for user
 * @author sagivm
 *
 */
public enum AccountStatus {
	
	/**
	 * States that the user doesn't wish to change his account type.
	 */
	Standard,
	/**
	 * States that the user wants to change his account type to per book subscription.
	 */
	PendingPerBook,
	/**
	 * States that the user wants to change his account type to monthly subscription.
	 */
	PendingMonthly,
	/**
	 * States that the user wants to change his account type to yearly subscription.
	 */
	PendingYearly,
	
	/**
	 * States that the user have to pay for his subscription.
	 */
	PendingPayment

};
