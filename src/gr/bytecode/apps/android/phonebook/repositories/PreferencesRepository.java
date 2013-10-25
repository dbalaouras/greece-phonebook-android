package gr.bytecode.apps.android.phonebook.repositories;

import static gr.bytecode.apps.android.phonebook.application.AppConstants.DATE_FORMAT;
import gr.bytecode.apps.android.phonebook.R;
import gr.bytecode.apps.android.phonebook.application.AppConstants;
import gr.bytecode.apps.android.phonebook.application.AppUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class PreferencesRepository {

	/**
	 * Preference name for auto data check flag
	 */
	private static String PREF_SETUP_COMPLETE = "pref_setup_complete";

	/**
	 * Preference name for auto data check flag
	 */
	private static String PREF_AUTO_DATA_CHECK = null;

	/**
	 * Preference name for auto data check interval
	 */
	private static String PREF_AUTO_DATA_CHECK_INTERVAL = null;

	/**
	 * Preference name for the last data check date
	 */
	private static String PREF_LAST_DATA_CHECK = null;

	/**
	 * Preference name for the last data update date
	 */
	private static String PREF_LAST_DATA_UPDATE = null;

	/**
	 * Preference name for the last data update date
	 */
	private static String PREF_HOVER_CALL = null;

	/**
	 * Shared preferences instances
	 */
	private static SharedPreferences sp;

	/**
	 * Preferences editor
	 */
	private static Editor spEditor;

	/**
	 * singleton instance of this class
	 */
	private static PreferencesRepository me;

	/**
	 * Application context
	 */
	private static Context mcontext;

	/**
	 * Private constructor
	 * 
	 * @param context
	 */
	private PreferencesRepository(Context context) {

		mcontext = context;
	}

	/**
	 * Create a singleton instance of this class
	 * 
	 * @return a singleton instance of this class
	 */
	@SuppressLint("CommitPrefEdits")
	public static PreferencesRepository getInstance(Context context) {

		if (me == null) {
			me = new PreferencesRepository(context);
			sp = PreferenceManager.getDefaultSharedPreferences(context);
			spEditor = sp.edit();

			// initiate the preference
			initiatePreferenceKeyNames();
		}

		return me;
	}

	/**
	 * 
	 */
	private static void initiatePreferenceKeyNames() {

		PREF_AUTO_DATA_CHECK = mcontext.getString(R.string.PREF_AUTO_DATA_CHECK);
		PREF_AUTO_DATA_CHECK_INTERVAL = mcontext.getString(R.string.PREF_AUTO_DATA_CHECK_INTERVAL);
		PREF_LAST_DATA_CHECK = mcontext.getString(R.string.PREF_LAST_DATA_CHECK);
		PREF_LAST_DATA_UPDATE = mcontext.getString(R.string.PREF_LAST_DATA_UPDATE);
		PREF_HOVER_CALL = mcontext.getString(R.string.PREF_HOVER_CALL);
	}

	/**
	 * Permanently remove a setting
	 * 
	 * @param settingName
	 *            the setting name
	 */
	private void deleteSetting(String settingName) {

		spEditor.remove(settingName);
		spEditor.commit();
	}

	/**
	 * Set the date of the last data check
	 * 
	 * @param date
	 */
	public void setLastDataCheckDate(Date date) {

		// prepare a Date Formatter
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);

		// put in preferences the formatted representation of the String

		spEditor.putString(PREF_LAST_DATA_CHECK, formatter.format(date));
		spEditor.commit();
	}

	/**
	 * Get the data when the last update occurred
	 * 
	 * @return
	 */
	public Date getLastDataCheckDate() {

		// initiate last data update date object
		Date lastDataCheckDate = null;

		// get the data update string from the preferences
		String lastUpdate = sp.getString(PREF_LAST_DATA_CHECK, null);

		if (lastUpdate != null) {

			// create a date formatter
			DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);

			try {
				// parse the date
				lastDataCheckDate = (Date) formatter.parse(lastUpdate);

			} catch (ParseException e) {

				// Date string from preferences is not valid; weird.
				deleteLastDataCheck();
			}
		}

		return lastDataCheckDate;
	}

	/**
	 * Set the date of the last data check
	 * 
	 * @param date
	 */
	public void setLastDataUpdateDate(Date date) {

		// prepare a Date Formatter
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);

		// put in preferences the formatted representation of the String

		spEditor.putString(PREF_LAST_DATA_UPDATE, formatter.format(date));
		spEditor.commit();
	}

	/**
	 * Get the data when the last update occurred
	 * 
	 * @return
	 */
	public Date getLastDataUpdateDate() {

		// initiate last data update date object
		Date lastUpdateDate = null;

		// get the data update string from the preferences
		String lastUpdate = sp.getString(PREF_LAST_DATA_UPDATE, null);

		if (lastUpdate != null) {
			DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);
			try {

				// parse the date
				lastUpdateDate = (Date) formatter.parse(lastUpdate);

			} catch (ParseException e) {

				// Date string from preferences is not valid; weird.
				deleteLastDataUpdate();
			}
		}

		return lastUpdateDate;
	}

	/**
	 * Get the check interval
	 * 
	 * @return
	 */
	public int getDataCheckInteral() {

		// initiate the return value
		int interval = 0;

		try {

			// read the value
			String value = sp.getString(PREF_AUTO_DATA_CHECK_INTERVAL, "0");

			// parse the value into an integer
			interval = Integer.parseInt(value);

		} catch (NumberFormatException e) {

			// restore default data check in case of an error
			if (isAutoDataCheckEnabled()) {
				AppUtil.logDebug("Restoring default data check interval");
				interval = AppConstants.DEFAULT_DATA_CHECK_INTERVAL_DAYS;
				setDataCheckInterval(interval);
			}

		}

		return interval;
	}

	/**
	 * Set the data check interval
	 * 
	 * @param interval
	 */
	public void setDataCheckInterval(Integer interval) {

		spEditor.putString(PREF_AUTO_DATA_CHECK_INTERVAL, interval.toString());
		spEditor.commit();

	}

	/**
	 * Delete the last data check date
	 */
	public void deleteLastDataUpdate() {

		deleteSetting(PREF_LAST_DATA_UPDATE);

	}

	/**
	 * Delete the last data check date
	 */
	public void deleteLastDataCheck() {

		deleteSetting(PREF_LAST_DATA_CHECK);

	}

	/**
	 * Delete auto data check
	 */
	public void deleteAutoDataCheck() {

		deleteSetting(PREF_AUTO_DATA_CHECK_INTERVAL);

	}

	/**
	 * Check whether auto data check is enabled
	 * 
	 * @return
	 */
	public boolean isAutoDataCheckEnabled() {

		return sp.getBoolean(PREF_AUTO_DATA_CHECK, false);
	}

	/**
	 * @param verdict
	 */
	public void setAutoDataCheckFlag(boolean verdict) {

		spEditor.putBoolean(PREF_AUTO_DATA_CHECK, verdict);
		spEditor.commit();

	}

	/**
	 * Delete the auto data check preference
	 */
	public void deleteDataUCheckSwitch() {

		deleteSetting(PREF_AUTO_DATA_CHECK);

	}

	/**
	 * @return
	 */
	public boolean isDefaultPreferencesSet() {

		return sp.getBoolean(PREF_SETUP_COMPLETE, false);

	}

	/**
	 * @param verdict
	 */
	public void setDefaultPreferencesSet(boolean verdict) {

		spEditor.putBoolean(PREF_SETUP_COMPLETE, verdict);
		spEditor.commit();

	}

	/**
	 * Check if the hover call feature is enabled
	 * 
	 * @return
	 */
	public boolean isHoverCallEnabled() {

		return sp.getBoolean(PREF_HOVER_CALL, false);
	}

	/**
	 * Update the hover-call feature
	 * 
	 * @param verdict
	 */
	public void setHoverCallEnabled(boolean verdict) {

		spEditor.putBoolean(PREF_HOVER_CALL, verdict);
		spEditor.commit();
	}

}
