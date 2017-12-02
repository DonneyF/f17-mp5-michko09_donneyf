package ca.ece.ubc.cpen221.mp5.interfaces;

// WRITE SPECS LATER

public interface User {

	
	/**
	 * Obtains the website URL for the User's account.
	 * 
	 * @return a String, which: - is not null. - is the website URL for the specific
	 *         user.
	 */
	String getWebsite();
	
	/**
	 * Obtains the type of the data strcuture.
	 * 
	 * @return a string, which: - is not null. - is the status of the user.
	 */
	String getType();

	/**
	 * Obtains the ID of the user.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         user.
	 */
	String getUserId();
	
	/**
	 * Obtains the name of the user, with the last name initialized.
	 * 
	 * @return a string, which: - is not null. - is the name of the specific
	 *         user.
	 */
	String getName();

	Votes getVotes();

}
