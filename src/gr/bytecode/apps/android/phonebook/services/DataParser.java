package gr.bytecode.apps.android.phonebook.services;

import gr.bytecode.apps.android.phonebook.entities.Entry;
import gr.bytecode.apps.android.phonebook.entities.EntryCategory;
import gr.bytecode.apps.android.phonebook.entities.Version;

import java.util.Iterator;

/**
 * Phonebook data parsing interface
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public interface DataParser<T> {

	/**
	 * Sets the data as a string
	 * 
	 * @param data
	 *            The data to parse
	 */
	abstract void setData(T data);

	/**
	 * Get an Iterator of Entry entities
	 * 
	 * @return
	 */
	abstract Iterator<Entry> getEntriesIterator();

	/**
	 * Get an Iterator of EntryCategory entities
	 * 
	 * @return
	 */
	abstract Iterator<EntryCategory> getEntryCategoryIterator();

	/**
	 * Get an Iterator of Version entities
	 * 
	 * @return
	 */
	abstract Iterator<Version> getVersionIterator();
}
