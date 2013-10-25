package gr.bytecode.apps.android.phonebook.exceptions;

public class DataNotSetException extends Exception {

	/**
	 * Required by the Exception class
	 */
	private static final long serialVersionUID = -3295820817481664297L;

	/**
	 * Default Constructor
	 */
	public DataNotSetException() {

		super();
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *            A meaningful message describing this Exception
	 */
	public DataNotSetException(String s) {

		super(s);
	}
}
