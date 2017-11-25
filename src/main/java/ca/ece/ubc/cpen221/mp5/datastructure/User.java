package ca.ece.ubc.cpen221.mp5.datastructure;

// WRITE SPECS LATER

public class User {
	protected Table userTable;
	private Table userVotes;
	
	public User(Table table) {
		this.userTable = table;
	}

	public User(Table table, Votes votes) {
		this.userTable = table;
		userVotes = votes;
	}
	
	/**
	 * Obtains the website URL for the User's account.
	 * 
	 * @return a String, which: - is not null. - is the website URL for the specific
	 *         user.
	 */
	String getWebsite() {
		return (String) userTable.getData("url").get(0);
	}
	
	/**
	 * Obtains the type of the data strcuture.
	 * 
	 * @return a string, which: - is not null. - is the status of the user.
	 */
	String getType() {
		return (String) userTable.getData("type").get(0);
	}

	/**
	 * Obtains the ID of the user.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         user.
	 */
	String getUserID() {
		return (String) userTable.getData("user_id").get(0);
	}
	
	/**
	 * Obtains the name of the user, with the last name initialized.
	 * 
	 * @return a string, which: - is not null. - is the name of the specific
	 *         user.
	 */
	String getName() {
		return (String) userTable.getData("name").get(0);
	}
	
	/**
	 * Compare two User objects for equality
	 * 
	 * @param other
	 * @return true if this User and the other User represent the same
	 *         user.
	 */
	@Override
	public boolean equals(Object other) {

		if (other instanceof User) {
			User otherDoc = (User) other;
			return (this.getUserID().equals(otherDoc.getUserID()));
		} else {
			return false;
		}
	}
	
	/**
	 * Compute the hashCode for this User object
	 * 
	 * @return the hashCode for this User object
	 */
	@Override
	public int hashCode() {
		return (int) (this.getName().hashCode() * this.getWebsite().hashCode() * 7);
	}
}
