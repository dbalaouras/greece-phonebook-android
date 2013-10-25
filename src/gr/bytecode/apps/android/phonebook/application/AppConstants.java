/**
 * 
 */
package gr.bytecode.apps.android.phonebook.application;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class AppConstants {

	/**
	 * Last update preferences name
	 */
	public static final String PREF_LAST_DATA_CHECK = "pref_last_data_check";

	/**
	 * Data check interval preference
	 */
	public static final String PREF_AUTO_DATA_CHECK_INTERVAL = "pref_data_check_interval";

	/**
	 * Auto check preference
	 */
	public static final String PREF_AUTO_DATA_CHECK = "pref_auto_data_check";

	/**
	 * Default date format
	 */
	public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

	/**
	 * Id for the new data notification
	 */
	public static final int NOTIF_ID_NEW_DATA = 1;

	public static final int NOTIF_ID_NEW_MESSAGE = 2;

	public static final int DEFAULT_DATA_CHECK_INTERVAL_DAYS = 7;

	/**
	 * API Key for the PollFish service
	 */
	public static final String POLLFISH_API_KEY = "c9b0cbe7-a192-432d-a2d9-76d046d0ae7a";

	public static final String DATA_UPDATED_ACTION = "gr.bytecode.phonebook.DATA_UPDATED";

}
