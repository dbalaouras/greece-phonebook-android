package gr.bytecode.apps.android.phonebook.exceptions;

/**
 * Exception thrown when an repository fails to persists an entity
 * 
 * @author Dimitris Balaouras
 * @since Apr 25, 2012 8:15:19 PM
 * 
 */
public class EntityNotStoredException extends EntityPersistenceException {

	/**
	 * Generated serialid
	 */
	private static final long serialVersionUID = 3L;

	/**
	 * Default constructor
	 */
	public EntityNotStoredException() {

		super();
	}

	/**
	 * @param s
	 */
	public EntityNotStoredException(String s) {

		super(s);
	}
}
