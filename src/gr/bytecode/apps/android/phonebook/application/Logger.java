package gr.bytecode.apps.android.phonebook.application;

/**
 * Contract for a logger class
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 */
public interface Logger {

	/**
	 * Log a debug message
	 * 
	 * @param debug
	 *            the debug message
	 */
	public abstract void logDebug(String debug);

	/**
	 * Log an informational message
	 * 
	 * @param info
	 *            the informational message
	 */
	public abstract void logInfo(String info);

	/**
	 * Log an warning message
	 * 
	 * @param warning
	 *            the warning message
	 */
	public abstract void logWarning(java.lang.String warning);

	/**
	 * Log a severe error
	 * 
	 * @param error
	 *            the error message
	 */
	public abstract void logError(String error);

	/**
	 * Log an unexpected error (a.k.a. what-a-terrible-failure)
	 * 
	 * @param failure
	 */
	public abstract void wtf(String failure);

	/**
	 * @param exception
	 */
	public abstract <E extends Exception> void logException(E exception);

}
