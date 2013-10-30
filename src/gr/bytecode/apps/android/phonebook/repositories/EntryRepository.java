package gr.bytecode.apps.android.phonebook.repositories;

import gr.bytecode.apps.android.phonebook.entities.Entry;
import gr.bytecode.apps.android.phonebook.entities.EntryCategory;

import java.util.ArrayList;
import java.util.List;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class EntryRepository extends BaseRepository<Entry> {

	/**
	 * Singleton instance
	 */
	private static EntryRepository me;

	/**
	 * Prevent instantiation; use getInstance instead.
	 * 
	 * @param context
	 */
	private EntryRepository() {

		super(Entry.class);
	}

	public static EntryRepository getInstance() {

		// check if there is an instance already
		if (me == null) {
			// if not, try obtaining a lock on the volatile object and
			// instantiate a Logger
			synchronized (EntryRepository.class) {
				if (me == null) {
					me = new EntryRepository();
				}
			}
		}
		return me;
	}

	/**
	 * Get all entries by category name
	 * 
	 * @param categoryName
	 * @return
	 */
	public List<Entry> getEntriesByCategoryName(String categoryName) {

		List<Entry> entries = new ArrayList<Entry>();

		// Attempt to find the entity in the database, create a model object and
		// convert it into a Domain entity
		EntryCategory entryCategoryModel = new Select().from(EntryCategory.class)
				.where("name = ?", categoryName).executeSingle();

		// load all entries ordered by name
		if (entryCategoryModel != null)
			entries = new Select().from(Entry.class)
					.where("EntryCategory" + "=?", entryCategoryModel.getId()).orderBy("Name")
					.execute();

		return entries;
	}

	/**
	 * Get all entries given the EntryCategory
	 * 
	 * @param categoryName
	 * @return
	 */
	public List<Entry> getEntriesByCategory(EntryCategory entryCategory) {

		return getEntriesByCategoryName(entryCategory.name);
	}

	/**
	 * Remove all entries that are not user defined (aka shared)
	 * 
	 * @param entryCategory
	 * @return
	 */
	public void deleteSharedEntriesByCategory(EntryCategory entryCategory) {

		// Delete the entity model without loading it from the database
		new Delete().from(modelClass)
				.where("CategoryId = ? AND UserOwned = 0", entryCategory.getExternalId()).execute();
	}

	/**
	 * Remove all entries that are not user defined (aka shared)
	 * 
	 * @param entryCategory
	 * @return
	 */
	public List<Entry> getUserDefinedEntries() {

		// get all the user defined Entries
		List<Entry> entries = new Select().from(Entry.class).where("UserOwned = 1").execute();

		return entries;
	}
}
