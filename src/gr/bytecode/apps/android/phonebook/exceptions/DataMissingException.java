package gr.bytecode.apps.android.phonebook.exceptions;

/**
 * @author Dimitris Balaouras
 * @since Apr 25, 2012 8:15:15 PM
 * 
 */
public class DataMissingException extends Exception {

	/**
	 * Required by the Exception class
	 */

	private static final long serialVersionUID = 867272116301144052L;

	/**
	 * Default Constructor
	 */
	public DataMissingException() {

		super();
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *            A meaningful message describing this Exception
	 */
	public DataMissingException(String s) {

		super(s);
	}

}
