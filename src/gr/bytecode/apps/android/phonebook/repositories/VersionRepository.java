package gr.bytecode.apps.android.phonebook.repositories;

import gr.bytecode.apps.android.phonebook.entities.Version;

import com.activeandroid.query.Select;

/**
 * A repository of <link>Version</link> records
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class VersionRepository extends BaseRepository<Version> {

	/**
	 * Singleton instance
	 */
	private static VersionRepository me;

	/**
	 * Prevent instantiation; use getInstance instead.
	 * 
	 * @param context
	 */
	private VersionRepository() {

		super(Version.class);
	}

	public static VersionRepository getInstance() {

		// check if there is an instance already
		if (me == null) {
			// if not, try obtaining a lock on the volatile object and
			// instantiate a Logger
			synchronized (VersionRepository.class) {
				if (me == null) {
					me = new VersionRepository();
				}
			}
		}
		return me;
	}

	/**
	 * Get a version by name
	 * 
	 * @param name
	 * @return
	 */
	public Integer getVersionByName(String name) {

		int number = 0;

		// locate the version
		Version versionModel = new Select().from(Version.class).where("Name = ?", name)
				.executeSingle();

		if (versionModel != null) {
			// set the return value
			number = versionModel.number;
		}

		return Integer.valueOf(number);
	}

	/**
	 * Get the version of the installed data
	 * 
	 * @return
	 */
	public Integer getDataVersion() {

		return getVersionByName("data");
	}
}
