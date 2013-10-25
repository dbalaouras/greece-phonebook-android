package gr.bytecode.apps.android.phonebook.exceptions;

/**
 * @author Dimitris Balaouras
 * @since Apr 25, 2012 8:15:36 PM
 * 
 */
public class EntityNotFoundException extends Exception {

	/**
	 * Required by the Exception class
	 */
	private static final long serialVersionUID = -8643681794182965922L;

	/**
	 * Default Constructor
	 */
	public EntityNotFoundException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *            A meaningful message describing this Exception
	 */
	public EntityNotFoundException(String s) {
		super(s);
	}
}
