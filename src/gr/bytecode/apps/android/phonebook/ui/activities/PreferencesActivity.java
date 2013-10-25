package gr.bytecode.apps.android.phonebook.ui.activities;

import gr.bytecode.apps.android.phonebook.R;
import gr.bytecode.apps.android.phonebook.application.AppConstants;
import gr.bytecode.apps.android.phonebook.application.AppUtil;
import gr.bytecode.apps.android.phonebook.repositories.PreferencesRepository;
import gr.bytecode.apps.android.phonebook.repositories.VersionRepository;
import gr.bytecode.apps.android.phonebook.services.DataUpdaterService;
import gr.bytecode.apps.android.phonebook.services.JavaWebServiceClient;

import java.text.DateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class PreferencesActivity extends SherlockPreferenceActivity implements
		OnSharedPreferenceChangeListener {

	/**
	 * Reference to the data-check interval list-preference
	 */
	private ListPreference dataCheckListPreference;

	/**
	 * Preference Item/Button used to initiate the data synch check
	 */
	private Preference checkDataNowPreference;

	/**
	 * Preference Item/Button used to send us an email
	 */
	private Preference sendEmailPreference;

	/**
	 * Preference Item/Button used to visit ByteCode.gr
	 */
	private Preference visitBytecodePreference;

	/**
	 * Repository of App preferences
	 */
	protected PreferencesRepository preferencesRepository;

	/**
	 * Reference to the applications SharedPreferences
	 */
	private SharedPreferences prefs;

	/**
	 * App Context
	 */
	private Context mcontext;

	/**
	 * The preference Screen
	 */
	private PreferenceScreen prefScreen;

	private static String PREF_AUTO_DATA_CHECK_INTERVAL;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		super.onCreate(savedInstanceState);

		// save the context in the field
		mcontext = this;

		// initiate indeterminate the progress bar
		setProgressBarIndeterminate(true);

		// start with a hidden progress bar
		setSupportProgressBarIndeterminateVisibility(false);

		// set the content view
		setContentView(R.layout.activity_preferences);

		// load the usage repository
		preferencesRepository = PreferencesRepository.getInstance(this);

		addPreferencesFromResource(R.xml.preferences);

		// initiate the preference screen
		prefScreen = getPreferenceScreen();

		// get the name of the check interval reference
		PREF_AUTO_DATA_CHECK_INTERVAL = getString(R.string.PREF_AUTO_DATA_CHECK_INTERVAL);

		// locate the check interval preference
		dataCheckListPreference = (ListPreference) prefScreen
				.findPreference(PREF_AUTO_DATA_CHECK_INTERVAL);

		checkDataNowPreference = (Preference) prefScreen
				.findPreference(getString(R.string.PREF_DATA_CHECK_NOW));

		sendEmailPreference = (Preference) prefScreen
				.findPreference(getString(R.string.PREF_EMAIL_US));

		visitBytecodePreference = (Preference) prefScreen
				.findPreference(getString(R.string.PREF_VISIT_BYTECODE));

		// initiate the reference to the SharedPreferences
		prefs = prefScreen.getSharedPreferences();

		ActionBar actionBar = getSupportActionBar();

		// configure the default look of the actionbar
		if (actionBar != null) {

			actionBar.setDisplayShowHomeEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);

			actionBar.setDisplayShowTitleEnabled(true);

			actionBar.setTitle(getString(R.string.LBL_SETTINGS));

			actionBar.setIcon(R.drawable.ic_ab_settings);
		}

		// set tap listeners
		setPreferencetapListeners();

	}

	/**
	 * Starts the mail activity
	 * 
	 */
	private void startMailActivity() {

		/* Create the Intent */
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		/* Fill it with Data */
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { getString(R.string.TXT_BYTECODE_EMAIL) });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				getString(R.string.TXT_SEND_EMAIL_SUBJ));

		/* Send it off to the Activity-Chooser */
		mcontext.startActivity(Intent.createChooser(emailIntent,
				getString(R.string.TITLE_SEND_EMAIL_CHOOSER)));

	}

	/**
	 * Starts the browser app on the ByteCode homepage
	 * 
	 */
	private void visitBytecode() {

		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getText(
				R.string.TXT_BYTECODE_URL).toString()));
		startActivity(browserIntent);

	}

	/**
	 * Starts the data check service
	 */
	private void startDataUpdateService() {

		// check if connected
		if (checkConnected()) {

			Intent intent = new Intent(this, DataUpdaterService.class);
			intent.putExtra(DataUpdaterService.PARAM_CHECK_DATA_VERSION_FIRST, false);
			intent.putExtra(DataUpdaterService.PARAM_OPEN_MAIN_ACTIVITY_AFTER_UPDATE, false);
			startService(intent);

			// inform the user that the check has started
			AppUtil.postToast(mcontext, getString(R.string.TXT_UPDATE_STARTED));

		}
	}

	/**
	 * Checks whether the device is connected and emits a notification if not
	 * 
	 * @return
	 */
	private boolean checkConnected() {

		boolean verdict = AppUtil.isDeviceConnected(this);

		if (verdict == false) {

			// no network; notify the user
			AppUtil.postToast(this, getString(R.string.ERROR_NO_NETWORK));
		}

		return verdict;
	}

	/**
	 * Starts the data check task
	 */
	private void startDataCheckTask() {

		if (checkConnected()) {

			// disable the data-check button
			checkDataNowPreference.setEnabled(false);

			// show indeterminate progress bar
			setSupportProgressBarIndeterminateVisibility(true);

			AsyncDataChecker dataChecker = new AsyncDataChecker();

			// get an instance of the version repository
			VersionRepository versionRepostiroy = VersionRepository.getInstance();

			// get the current version
			Integer version = versionRepostiroy.getDataVersion();

			// create a version
			String versionStr = version == null ? "" : version.toString();

			// execute the task
			dataChecker.execute(versionStr);
		}
	}

	/**
	 * Called when the update is finished
	 */
	private void finishedDataCheckTask() {

		// hide the progress bar
		setSupportProgressBarIndeterminateVisibility(false);

		// re-enable the data-check button
		checkDataNowPreference.setEnabled(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockPreferenceActivity#onOptionsItemSelected
	 * (android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {

		// Call super
		super.onResume();

		// Register mMessageReceiver to receive messages.
		registerReceiver(dataUpdatedReceiver, new IntentFilter(AppConstants.DATA_UPDATED_ACTION));

		// Setup the initial values
		setDataCheckIntervalPrefDescription(prefs);

		// Set up a listener whenever a key changes
		prefScreen.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

		setLastDataUpdateDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {

		super.onPause();

		// Unregister the listener whenever a key changes
		prefScreen.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

		// Unregister since the activity is not visible
		unregisterReceiver(dataUpdatedReceiver);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#
	 * onSharedPreferenceChanged(android.content.SharedPreferences,
	 * java.lang.String)
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		if (key.equals(PREF_AUTO_DATA_CHECK_INTERVAL)) {
			setDataCheckIntervalPrefDescription(sharedPreferences);
		}
	}

	/**
	 * Updates the description of the data-check-interval preference with
	 * dynamic data
	 * 
	 * @param prefs
	 *            The SharedPreferences object
	 */
	private void setDataCheckIntervalPrefDescription(SharedPreferences prefs) {

		// String value = prefs.getString(PREF_AUTO_DATA_CHECK_INTERVAL, "");

		int value = preferencesRepository.getDataCheckInteral();

		if (value == 0) {
			dataCheckListPreference.setSummary(getString(R.string.TXT_INACTIVE));
		} else {
			dataCheckListPreference.setSummary(getString(
					R.string.TXT_SUMMARY_AUTO_DATA_CHECK_INTERVAL, value));
		}

	}

	/**
	 * Sets the last update's date on the related preference
	 */
	private void setLastDataUpdateDate() {

		// get the last data check date
		Date lastUpdate = preferencesRepository.getLastDataUpdateDate();

		if (checkDataNowPreference != null && lastUpdate != null) {

			checkDataNowPreference.setSummary(getString(R.string.TXT_TITLE_LAST_DATA_UPDATE) + ": "
					+ DateFormat.getDateInstance().format(lastUpdate));
		}
	}

	/**
	 * Sets some tap listeners on button-preferences
	 */
	private void setPreferencetapListeners() {

		// set a click listener on the check
		checkDataNowPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.preference.Preference.OnPreferenceClickListener#
			 * onPreferenceClick(android.preference.Preference)
			 */
			public boolean onPreferenceClick(Preference preference) {

				startDataCheckTask();

				return true;
			}

		});

		sendEmailPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.preference.Preference.OnPreferenceClickListener#
			 * onPreferenceClick(android.preference.Preference)
			 */
			public boolean onPreferenceClick(Preference preference) {

				startMailActivity();

				return true;
			}

		});

		visitBytecodePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.preference.Preference.OnPreferenceClickListener#
			 * onPreferenceClick(android.preference.Preference)
			 */
			public boolean onPreferenceClick(Preference preference) {

				visitBytecode();

				return true;
			}

		});

	}

	/**
	 * Show the update dialog
	 * 
	 * @param newDataFound
	 */
	private void showUpdateDialog(boolean newDataFound) {

		// instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

		// choose the correct message to show
		String message = newDataFound ? getString(R.string.TXT_NEW_DATA_FOUND)
				: getString(R.string.TXT_DATA_IS_UP_TO_DATE);

		// choose the correct download button label
		String downloadButtonLabel = newDataFound ? getString(R.string.TXT_UPDATE_NOW)
				: getString(R.string.TXT_UPDATE_ANYWAY);

		// set the message and title of the dialog;
		builder.setTitle(message);

		// set the positive button
		builder.setPositiveButton(downloadButtonLabel, new DialogInterface.OnClickListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * android.content.DialogInterface.OnClickListener#onClick(android
			 * .content.DialogInterface, int)
			 */
			public void onClick(DialogInterface dialog, int id) {

				// start the update service
				startDataUpdateService();
			}
		});

		// set the cancel button
		builder.setNegativeButton(getString(R.string.TXT_CLOSE),
				new DialogInterface.OnClickListener() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * android.content.DialogInterface.OnClickListener#onClick
					 * (android .content.DialogInterface, int)
					 */
					public void onClick(DialogInterface dialog, int id) {

						// User cancelled the dialog; do nothing
					}
				});

		// get the AlertDialog from create()
		AlertDialog dialog = builder.create();

		// show the dialog
		dialog.show();
	}

	/**
	 * @author Dimitris Balaouras
	 * @copyright 2013 ByteCode.gr
	 * 
	 */
	private class AsyncDataChecker extends AsyncTask<String, Integer, Boolean> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(String... params) {

			// create a Web Service client
			JavaWebServiceClient client = new JavaWebServiceClient();

			// the first parameter *must* me the current version
			String currentVersion = params[0];

			return client.isNewDataAvailable(currentVersion);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);

			// we are now done
			finishedDataCheckTask();

			// open the data-update dialog
			showUpdateDialog(result);

		}

	}

	/**
	 * Handler for data updates
	 */
	private BroadcastReceiver dataUpdatedReceiver = new BroadcastReceiver() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.BroadcastReceiver#onReceive(android.content.Context,
		 * android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {

			// update the date
			setLastDataUpdateDate();
		}
	};
}
