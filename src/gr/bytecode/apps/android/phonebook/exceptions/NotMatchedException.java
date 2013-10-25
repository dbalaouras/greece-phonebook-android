package gr.bytecode.apps.android.phonebook.exceptions;

/**
 * @author Dimitris Balaouras
 * @since Apr 25, 2012 8:15:15 PM
 * 
 */
public class NotMatchedException extends Exception {

	/**
	 * Required by the Exception class
	 */

	private static final long serialVersionUID = -1925077837428524102L;

	/**
	 * Default Constructor
	 */
	public NotMatchedException() {

		super();
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *            A meaningful message describing this Exception
	 */
	public NotMatchedException(String s) {

		super(s);
	}

}
