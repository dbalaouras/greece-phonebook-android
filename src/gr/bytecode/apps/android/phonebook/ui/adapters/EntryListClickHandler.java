package gr.bytecode.apps.android.phonebook.ui.adapters;

import gr.bytecode.apps.android.phonebook.entities.Entry;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public interface EntryListClickHandler {

	/**
	 * React on a tap event on a phone item
	 * 
	 * @param entry
	 */
	abstract void onPhoneClicked(Entry entry);

	/**
	 * React on a tap event on a phone item
	 * 
	 * @param position
	 */
	abstract void onPhoneClicked(int position);

	/**
	 * React on a tap event on a phone item
	 * 
	 * @param entry
	 */
	abstract void onWebsiteClicked(Entry entry);

	/**
	 * React on a tap event on a phone item
	 * 
	 * @param position
	 */
	abstract void onWebsiteClicked(int position);

	/**
	 * React on a tap event on a phone item
	 * 
	 * @param phone
	 */
	abstract void onEntryListItemClicked(int position);

}
