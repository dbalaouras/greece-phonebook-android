package gr.bytecode.apps.android.phonebook.exceptions;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class NotFoundException extends Exception {

	/**
	 * Required by the Exception class
	 */
	private static final long serialVersionUID = -7136639646452486069L;

	/**
	 * Default Constructor
	 */
	public NotFoundException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *            A meaningful message describing this Exception
	 */
	public NotFoundException(String s) {
		super(s);
	}
}
