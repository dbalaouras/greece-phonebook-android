package gr.bytecode.apps.android.phonebook.services;

import gr.bytecode.apps.android.phonebook.R;
import gr.bytecode.apps.android.phonebook.application.AppConstants;
import gr.bytecode.apps.android.phonebook.application.AppUtil;
import gr.bytecode.apps.android.phonebook.entities.Entry;
import gr.bytecode.apps.android.phonebook.entities.EntryCategory;
import gr.bytecode.apps.android.phonebook.entities.Version;
import gr.bytecode.apps.android.phonebook.exceptions.EntityNotRemovedException;
import gr.bytecode.apps.android.phonebook.repositories.CategoryRepository;
import gr.bytecode.apps.android.phonebook.repositories.EntryRepository;
import gr.bytecode.apps.android.phonebook.repositories.PreferencesRepository;
import gr.bytecode.apps.android.phonebook.repositories.VersionRepository;
import gr.bytecode.apps.android.phonebook.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.SparseArray;

/**
 * Background Android service that synchronizes the local database with the
 * remote web service.
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class DataUpdaterService extends IntentService {

	/**
	 * Parameter passed via a Intent extra when starting this service
	 */
	public static final String PARAM_CHECK_DATA_VERSION_FIRST = "check_data_version";

	/**
	 * Parameter passed via a Intent extra when starting this service
	 */
	public static final String PARAM_OPEN_MAIN_ACTIVITY_AFTER_UPDATE = "open_main_activity_after_update";

	/**
	 * An instance of the ServiceRepository used to query the database
	 */
	private EntryRepository entriesRepository = EntryRepository.getInstance();

	/**
	 * A reference to a service client
	 */
	private JavaWebServiceClient client = new JavaWebServiceClient();

	/**
	 * Defines whether the main app should be started via an intent after the
	 * update
	 */
	private boolean openMainActivity = true;

	/**
	 * Default Constructor
	 */
	public DataUpdaterService() {

		super("DataUpdaterService");
	}

	/**
	 * 
	 */
	private static Integer USER_DEFINED_CAT_ID = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		// get the app context
		Context context = getApplicationContext();

		// check if device is connected and start the service
		if (AppUtil.isDeviceConnected(context)) {

			// create a Web Service client
			JavaWebServiceClient client = new JavaWebServiceClient();

			// check whether we need to check the version of the data
			boolean checkVersionFirst = intent
					.getBooleanExtra(PARAM_CHECK_DATA_VERSION_FIRST, true);

			// check whether we need to check the version of the data
			openMainActivity = intent.getBooleanExtra(PARAM_OPEN_MAIN_ACTIVITY_AFTER_UPDATE, true);

			// flag that defines whether we should proceed with the data-synch
			boolean proceed = true;

			if (checkVersionFirst) {
				// get an instance of the version repository
				VersionRepository versionRepostiroy = VersionRepository.getInstance();

				// get the current version
				Integer version = versionRepostiroy.getDataVersion();

				AppUtil.logDebug("Version: " + version);

				if (version != null) {
					// update the proceed flag
					proceed = client.isNewDataAvailable(version.toString());

					// update the last-check date
					PreferencesRepository preferencesRepository = PreferencesRepository
							.getInstance(context);

					// set the last data check to now
					preferencesRepository.setLastDataCheckDate(new Date());
				}

			}

			if (proceed) {
				// start the update
				startUpdate(context);

			} else {

				AppUtil.logDebug("Already at the newest version. Skipping update.");
			}

		}

	}

	/**
	 * Starts and completes a full database update
	 * 
	 * @param context
	 */
	private void startUpdate(Context context) {

		// start downloading
		String data = client.downloadData();

		// create a data parser
		JsonDataParser parser = new JsonDataParser();

		// set the data
		parser.setData(data);

		// get an @Entry iterator
		Iterator<EntryCategory> categoryIterator = parser.getEntryCategoryIterator();

		// persist the categories
		SparseArray<EntryCategory> persistedCategories;
		try {

			// persist the categories first
			persistedCategories = persistEntryCategories(context, categoryIterator);

			if (persistedCategories.size() > 0) {

				// get am @Entry iterator
				Iterator<Entry> entryIterator = parser.getEntriesIterator();

				// now persist the entries
				persistEntries(context, entryIterator, persistedCategories);
			}

			// get the versions iterator
			Iterator<Version> versionIterator = parser.getVersionIterator();

			// persist the versions
			persistVersions(versionIterator);

			// get a singleton instance of the PreferencesRepository
			PreferencesRepository preferencesRepository = PreferencesRepository
					.getInstance(context);

			// set the last data check to now
			preferencesRepository.setLastDataUpdateDate(new Date());

			// notify user for the update
			emitDataUpdatedNotification(context);

			// send a broadcast
			Intent intent = new Intent();
			intent.setAction(AppConstants.DATA_UPDATED_ACTION);
			sendBroadcast(intent);

		} catch (EntityNotRemovedException e) {

			e.printStackTrace();
		}

	}

	/**
	 * @param versions
	 * @throws EntityNotRemovedException
	 */
	private void persistVersions(Iterator<Version> versions) throws EntityNotRemovedException {

		// get a versionRepository instance
		VersionRepository versionRepository = VersionRepository.getInstance();

		// delete old entities
		versionRepository.deleteAllEntities();

		// iterate and save
		while (versions.hasNext()) {

			// get next version
			Version version = versions.next();

			// persist it
			version.save();
		}

	}

	/**
	 * Persist EntryCategories
	 * 
	 * @param categoryIterator
	 * @return
	 * @throws EntityNotRemovedException
	 */
	private SparseArray<EntryCategory> persistEntryCategories(Context context,
			Iterator<EntryCategory> categoryIterator) throws EntityNotRemovedException {

		// map of the saved EntryCategory items using the final Id as the
		// key
		SparseArray<EntryCategory> entryCategories = new SparseArray<EntryCategory>();

		// check if we got valid data
		if (categoryIterator.hasNext()) {

			// delete ECategory database
			CategoryRepository entryCategoryRepository = CategoryRepository.getInstance();

			// delete old entities
			entryCategoryRepository.deleteAllEntities();

			// memorize the last entered id
			Integer enteredId = 0;

			// iterate the iterator
			while (categoryIterator.hasNext()) {
				// fetch the next category
				EntryCategory category = categoryIterator.next();

				// persist it (there are not many so there is not a huge benefit
				// to bulk save them)
				entryCategoryRepository.saveEntity(category);

				// delete all shared entries
				entriesRepository.deleteSharedEntriesByCategory(category);

				enteredId = category.externalId;

				// add the category to the list
				entryCategories.append(enteredId, category);
			}

			// create a new user defined category
			String userDefCategoryName = context.getString(R.string.CAT_NAME_USER_DEFINED);
			EntryCategory category = new EntryCategory(USER_DEFINED_CAT_ID, userDefCategoryName,
					userDefCategoryName);

			// persist it
			entryCategoryRepository.saveEntity(category);

			// add the category to the list
			entryCategories.append(USER_DEFINED_CAT_ID, category);

		}

		return entryCategories;

	}

	/**
	 * Persist Entries
	 * 
	 * @param entriesIterator
	 * @return
	 * @throws EntityNotRemovedException
	 */
	private void persistEntries(Context context, Iterator<Entry> entriesIterator,
			SparseArray<EntryCategory> persistedCategories) throws EntityNotRemovedException {

		// check if we got valid data
		if (entriesIterator.hasNext()) {

			List<Entry> entriesToSave = new ArrayList<Entry>();

			// a counter used to periodically flush updates to the database
			int counter = 0;

			// iterate the iterator
			while (entriesIterator.hasNext()) {
				Entry entry = entriesIterator.next();

				// locate the category
				EntryCategory category = persistedCategories.get(entry.getExternalCategoryId());

				// set the category to the Entry
				entry.setEntryCategory(category);

				// add the Entry to the save list
				entriesToSave.add(entry);

				// increment counter <== you must be kidding me!
				counter++;

				// save periodically
				if (counter >= 20) {
					entriesRepository.bulkSaveEntities(entriesToSave);
					entriesToSave.clear();
					counter = 0;
				}

			}

			// update user entries
			List<Entry> userDefinedEntries = entriesRepository.getUserDefinedEntries();

			if (userDefinedEntries != null && userDefinedEntries.size() > 0) {

				// iterate the user defined entries and update their category
				for (Entry userDefinedEntry : userDefinedEntries) {
					userDefinedEntry.setEntryCategory(persistedCategories.get(USER_DEFINED_CAT_ID));

					// add it in the entries to be saved
					entriesToSave.add(userDefinedEntry);
				}
			}

			// save the rest now
			if (entriesToSave.size() > 0) {
				entriesRepository.bulkSaveEntities(entriesToSave);
				AppUtil.logDebug("BulkSaving: " + entriesToSave);
			}
		}

	}

	/**
	 * Set a status bar notification
	 * 
	 * @param context
	 */
	public void emitDataUpdatedNotification(Context context) {

		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// get the title of the notification
		String notificationTitle = context.getString(R.string.TITLE_DATA_UPDATE_NOTIF_TITLE);

		// get the subtitle of the notification
		String notificationSubTitle = context.getString(R.string.TITLE_DATA_UPDATED);

		// check if we should be starting the main activity or not
		Intent actionIntent = openMainActivity ? new Intent(context, MainActivity.class)
				: new Intent();

		// create a notification
		NotificationCompat.Builder notificationbBuilder = new NotificationCompat.Builder(context)
				.setContentTitle(notificationTitle).setTicker(notificationTitle)
				.setContentText(notificationSubTitle).setSmallIcon(R.drawable.ic_notification)
				.setContentIntent(PendingIntent.getActivity(context, 0, actionIntent, 0));

		// add notification vibration
		long[] vibrate = { 0, 100, 100, 100 };

		// set the vibration
		notificationbBuilder.setVibrate(vibrate);

		// set the sound
		notificationbBuilder.setSound(RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

		// set the lights
		notificationbBuilder.setLights(0xffffffff, 300, 2000);

		// we want the notification to cancel itself on user interaction
		notificationbBuilder.setAutoCancel(true);

		// get a notification construct from the builder
		Notification notification = notificationbBuilder.getNotification();

		// ensure this is auto canceled
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// dispatch the notification
		mNotificationManager.notify(1, notification);
	}
}
