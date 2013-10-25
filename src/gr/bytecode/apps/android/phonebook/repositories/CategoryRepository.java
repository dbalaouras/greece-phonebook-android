package gr.bytecode.apps.android.phonebook.repositories;

import gr.bytecode.apps.android.phonebook.entities.EntryCategory;

import java.util.List;

import com.activeandroid.query.Select;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class CategoryRepository extends BaseRepository<EntryCategory> {

	/**
	 * Singleton instance
	 */
	private static CategoryRepository me;

	/**
	 * Default constructor
	 */
	private CategoryRepository() {

		super(EntryCategory.class);
	}

	/**
	 * Singleton instantiator
	 * 
	 * @return
	 */
	public static CategoryRepository getInstance() {

		// check if there is an instance already
		if (me == null) {
			// if not, try obtaining a lock on the volatile object and
			// instantiate a Logger
			synchronized (CategoryRepository.class) {
				if (me == null) {
					me = new CategoryRepository();
				}
			}
		}
		return me;
	}

	/**
	 * Get a list of all telephone categories
	 * 
	 * @return
	 */
	public List<EntryCategory> getEntryCategories() {

		// get all the entry categories
		List<EntryCategory> entryCategories = new Select().from(EntryCategory.class)
				.execute();

		return entryCategories;
	}

	/**
	 * Get all entries by category name
	 * 
	 * @param categoryName
	 * @return
	 */
	public EntryCategory getEntryCategoryByName(String categoryName) {

		// Attempt to find the entity in the database, create a model object and
		// convert it into a Domain entity
		EntryCategory entryCategoryModel = new Select().from(EntryCategory.class)
				.where("name = ?", categoryName).executeSingle();

		return entryCategoryModel;
	}

}
