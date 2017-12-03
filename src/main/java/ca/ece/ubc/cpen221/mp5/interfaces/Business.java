package ca.ece.ubc.cpen221.mp5.interfaces;

/**
 * A business interface. Supports getting information about this business.
 */
public interface Business {

	/**
	 * Checks to see if a Business is open or not.
	 * 
	 * @return true, if: 
	 * 		- the restaurant status at the current time is open. false,
	 *         if: - the restaurant is closed.
	 */
	boolean isOpen();
	
	/**
	 * Modifies the current business status.
	 */
	void setOpen (boolean status);

	/**
	 * Obtains the website URL for the business.
	 * 
	 * @return a String, which: - is not null. - is the website URL for the specific
	 *         business.
	 */
	String getUrl();

	/**
	 * Obtains the longitudinal geographical value of the business.
	 * 
	 * @return a double, which: - is not null. - is the longitude of the specific
	 *         business.
	 */
	double getLongitude();
	
	/**
	 * Obtains the latitudinal geographical value of the business.
	 * 
	 * @return a double, which: - is not null. - is the latitude of the specific
	 *         business.
	 */
	double getLatitude();
	
	/**
	 * Obtains the ID of the business.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         business.
	 */
	String getBusinessId();
	
	/**
	 * Obtains the name of the business.
	 * 
	 * @return a string, which: - is not null. - is the name of the specific
	 *         business.
	 */
	String getName();
	
	/**
	 * Obtains the state in which the business is located in.
	 * 
	 * @return a string, which: - is not null. - is the state where the business resides.
	 */
	String getState();
	
	/**
	 * Obtains the type of service the business offers.
	 * 
	 * @return a string, which: - is not null. - is the service of the specific
	 *         business.
	 */
	String getType();
	
	/**
	 * Obtains the city in which the business is located in.
	 * 
	 * @return a string, which: - is not null. - is the city where the business resides.
	 */
	String getCity();
	
	/**
	 * Obtains the address in which the business is located in.
	 * 
	 * @return a string, which: - is not null. - is the full address of the business.
	 */
	String getAddress();
	
	/**
	 * Obtains the image representing the business.
	 * 
	 * @return a string, which: - is not null. - is the photo URL of the business.
	 */
	String getPhotoUrl();
	
}
