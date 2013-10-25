package gr.bytecode.apps.android.phonebook.repositories;

import gr.bytecode.apps.android.phonebook.application.Logger;
import gr.bytecode.apps.android.phonebook.exceptions.EntityNotRemovedException;

import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class BaseRepository<E extends Model> {

	// reference to the Class name of the model we are using
	Class<E> modelClass;

	protected Logger logger = null;

	/**
	 * Default Constructor
	 */
	public BaseRepository(Class<E> modelClass) {

		super();

		ActiveAndroid.setLoggingEnabled(true);

		this.modelClass = modelClass;
	}

	/**
	 * @throws EntityNotRemovedException
	 */
	public void deleteAllEntities() throws EntityNotRemovedException {

		// Delete the entity model without loading it from the database
		new Delete().from(modelClass).where("Id >= 0").execute();
	}

	/**
	 * Bulk save of entities
	 * 
	 * @param entities
	 */
	public void bulkSaveEntities(List<E> entities) {

		int listSize = entities.size();

		if (listSize > 0) {
			// create AA transactions
			ActiveAndroid.beginTransaction();

			try {
				// iterate entities
				for (E entity : entities) {

					// persist entity
					entity.save();

				}

				ActiveAndroid.setTransactionSuccessful();

			} finally {

				// we must end the transaction no matter what
				ActiveAndroid.endTransaction();
			}

		}

	}

	/**
	 * @param entity
	 */
	public void saveEntity(E entity) {

		// just call save on the entity
		entity.save();
	}

	/**
	 * @param entity
	 */
	public void deleteEntity(E entity) {

		entity.delete();
	}

	/**
	 * Find an entity given its ID
	 * 
	 * @param id
	 * @return
	 */
	public E getEntityById(Long id) {

		E entity = new Select().from(modelClass).where("Id = ?", id).executeSingle();

		return entity;
	}
}
