package gr.bytecode.apps.android.phonebook.exceptions;

/**
 * Exception thrown in cases of currupted or invalid data
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
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
