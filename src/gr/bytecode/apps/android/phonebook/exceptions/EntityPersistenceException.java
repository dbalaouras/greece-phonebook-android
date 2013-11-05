package gr.bytecode.apps.android.phonebook.exceptions;

/**
 * Generic Exception thrown by repositories in case of persistence errors
 * 
 * @author Dimitris Balaouras
 * @since Apr 25, 2012 8:15:12 PM
 * 
 */
public class EntityPersistenceException extends Exception {

	/**
	 * Required by the Exception interface
	 */
	private static final long serialVersionUID = -7557349995800504515L;

	/**
	 * Default Constructor
	 */
	public EntityPersistenceException() {

		super();
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *            A meaningful message describing this Exception
	 */
	public EntityPersistenceException(String s) {

		super(s);
	}
}
