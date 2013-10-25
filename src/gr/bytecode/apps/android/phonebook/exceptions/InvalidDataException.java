package gr.bytecode.apps.android.phonebook.exceptions;

/**
 * @author Dimitris Balaouras
 * @since Apr 25, 2012 8:15:15 PM
 * 
 */
public class InvalidDataException extends Exception {

	/**
	 * Required by the Exception class
	 */
	private static final long serialVersionUID = 7L;

	/**
	 * Default Constructor
	 */
	public InvalidDataException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *            A meaningful message describing this Exception
	 */
	public InvalidDataException(String s) {
		super(s);
	}

}
