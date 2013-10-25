package gr.bytecode.apps.android.phonebook.services;

/**
 * Interface for a callback objects
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 * @param <T>
 */
public interface Callback<T> {

	/**
	 * The callback method
	 */
	public abstract void call(T value);
}
