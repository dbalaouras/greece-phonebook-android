package gr.bytecode.apps.android.phonebook.services;

/**
 * Phonebook webservice interface. Defined the construct that a concrete
 * implementation of a WS client should honor.
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public interface WebServiceClient {

	/**
	 * Get the download size
	 * 
	 * @return
	 */
	abstract int getSizeInBytes();

	/**
	 * Check if there are new data available
	 * 
	 * @return
	 */
	abstract boolean isNewDataAvailable(String installedVersion);

	/**
	 * Download data
	 * 
	 * @return
	 */
	abstract String downloadData();

	/**
	 * Sets a callback that is called when new data are downloaded. The call
	 * value represents the amount of downloaded bytes
	 * 
	 * @param callback
	 */
	abstract void setDownloadUpdateCallcack(Callback<Integer> callback);

}
