package ca.ece.ubc.cpen221.mp5.datastructure;

public class Business {

	//Table businessTable;
	
	boolean openStatus;
	String websiteURL;
	double longitude;
	double latitude;
	String businessID;
	String name;
	String state;
	String type;
	double stars;
	String city;
	String fullAdress;
	int reviewCount;
	String photoURL;
	int price;

	/**
	 * In this class we will need the following: - Open Status - Website URL -
	 * Location - Neighborhood - Business ID - Name - Category (restaurant? retail?)
	 * - State - Type - Stars - City - Full Adress - Review Count - Photo URL -
	 * Schools - Latitude - Price
	 */

	/**
	 * Creates a Business Constructor which extends methods from Table.java.
	 * 
	 * @param ID,
	 *            where: - it is not null. - it is the unique ID representing a
	 *            specific business.
	 */
	//public Business(Table table) {
		//this.businessTable = table;
	//}

	/**
	 * Checks to see if a Business is open or not.
	 * 
	 * @return true, if: 
	 * 		- the restaurant status at the current time is open. false,
	 *         if: - the restaurant is closed.
	 */
	boolean isOpen() {
		return openStatus;
	}
	
	/**
	 * Modifies the current business status.
	 * 
	 * @return true, if: - the restaurant status at the current time is open. false,
	 *         if: - the restaurant is closed.
	 */
	void modifyOpenStatus(boolean status) {
		openStatus = status;
	}

	/**
	 * Obtains the website URL for the business.
	 * 
	 * @return a String, which: - is not null. - is the website URL for the specific
	 *         business.
	 */
	

}
