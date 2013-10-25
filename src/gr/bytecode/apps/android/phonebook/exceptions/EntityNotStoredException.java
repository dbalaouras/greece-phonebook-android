package gr.bytecode.apps.android.phonebook.exceptions;

/**
 * @author Dimitris Balaouras
 * @since Apr 25, 2012 8:15:19 PM
 *
 */
public class EntityNotStoredException extends EntityPersistenceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	public EntityNotStoredException(){
		super();
	}

	/**
	 * @param s
	 */
	public EntityNotStoredException(String s){
		super(s);
	}
}
