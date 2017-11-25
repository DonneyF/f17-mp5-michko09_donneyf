package ca.ece.ubc.cpen221.mp5.datastructure;

public class Business {

	private Table businessTable;

	/**
	 * Creates a Business Construc tor which extends methods from Table.java.
	 * 
	 * @param ID,
	 *            where: - it is not null. - it is the unique ID representing a
	 *            specific business.
	 */
	public Business(Table table) {
		this.businessTable = table;
	}

	/**
	 * Checks to see if a Business is open or not.
	 * 
	 * @return true, if: 
	 * 		- the restaurant status at the current time is open. false,
	 *         if: - the restaurant is closed.
	 */
	boolean isOpen() {
		return (boolean) businessTable.getData("open").get(0);
	}
	
	/**
	 * Modifies the current business status.
	 * 
	 * @return true, if: - the restaurant status at the current time is open. false,
	 *         if: - the restaurant is closed.
	 */
	void modifyOpenStatus(boolean status) {
		businessTable.clearEntry("open");
		businessTable.getData("open").add(status);
	}

	/**
	 * Obtains the website URL for the business.
	 * 
	 * @return a String, which: - is not null. - is the website URL for the specific
	 *         business.
	 */
	String getWebsite() {
		return (String) businessTable.getData("url").get(0);
	}
	
	/**
	 * Obtains the longitudinal geographical value of the business.
	 * 
	 * @return a double, which: - is not null. - is the longitude of the specific
	 *         business.
	 */
	public double getLongitude() {
		return (double) businessTable.getData("longitude").get(0);
	}
	
	/**
	 * Obtains the latitudinal geographical value of the business.
	 * 
	 * @return a double, which: - is not null. - is the latitude of the specific
	 *         business.
	 */
	public double getLatitude() {
		return (double) businessTable.getData("latitude").get(0);
	}
	
	/**
	 * Obtains the ID of the business.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         business.
	 */
	String getBusinessID() {
		return (String) businessTable.getData("business_id").get(0);
	}
	
	/**
	 * Obtains the name of the business.
	 * 
	 * @return a string, which: - is not null. - is the name of the specific
	 *         business.
	 */
	String getName() {
		return (String) businessTable.getData("name").get(0);
	}
	
	/**
	 * Obtains the state in which the business is located in.
	 * 
	 * @return a string, which: - is not null. - is the state where the business resides.
	 */
	String getState() {
		return (String) businessTable.getData("state").get(0);
	}
	
	/**
	 * Obtains the type of service the business offers.
	 * 
	 * @return a string, which: - is not null. - is the service of the specific
	 *         business.
	 */
	String getType() {
		return (String) businessTable.getData("type").get(0);
	}
	
	/**
	 * Obtains the city in which the business is located in.
	 * 
	 * @return a string, which: - is not null. - is the city where the business resides.
	 */
	String getCity() {
		return (String) businessTable.getData("city").get(0);
	}
	
	/**
	 * Obtains the address in which the business is located in.
	 * 
	 * @return a string, which: - is not null. - is the full address of the business.
	 */
	String getAddress() {
		return (String) businessTable.getData("full_address").get(0);
	}
	
	/**
	 * Obtains the image representing the business.
	 * 
	 * @return a string, which: - is not null. - is the photo URL of the business.
	 */
	String getPhotoURL() {
		return (String) businessTable.getData("photo_url").get(0);
	}
	
	/**
	 * Compare two Business objects for equality
	 * 
	 * @param other
	 * @return true if this Business and the other Business represent the same
	 *         business.
	 */
	@Override
	public boolean equals(Object other) {

		if (other instanceof Business) {
			Business otherDoc = (Business) other;
			return (this.getBusinessID().equals(otherDoc.getBusinessID()));
		} else {
			return false;
		}
	}
	
	/**
	 * Compute the hashCode for this Business object
	 * 
	 * @return the hashCode for this Business object
	 */
	@Override
	public int hashCode() {
		return (int) (this.getLatitude() * this.getLongitude() * 7);
	}
	
}
