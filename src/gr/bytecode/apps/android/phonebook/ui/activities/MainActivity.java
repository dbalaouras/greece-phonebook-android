package gr.bytecode.apps.android.phonebook.ui.activities;

import gr.bytecode.apps.android.phonebook.R;
import gr.bytecode.apps.android.phonebook.application.AppConstants;
import gr.bytecode.apps.android.phonebook.application.AppUtil;
import gr.bytecode.apps.android.phonebook.repositories.PreferencesRepository;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.pollfish.constants.Position;
import com.pollfish.main.PollFish;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class MainActivity extends BaseActivity {

	/**
	 * The intent that will start the list activity
	 */
	private Intent activityIntent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// set the layout
		setContentView(R.layout.activity_main);

		// create the activity that will start the list
		activityIntent = new Intent(mcontext, EntryListActivity.class);

		// get a preferences repository
		PreferencesRepository preferecensRepository = PreferencesRepository.getInstance(mcontext);

		// check whether we need to set the default values on the preferences
		if (!preferecensRepository.isDefaultPreferencesSet()) {
			PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {

		super.onResume();

		// initiate Pollfish (see http://www.pollfish.com)
		PollFish.init(this, AppConstants.POLLFISH_API_KEY, Position.TOP_LEFT, 30);

		AppUtil.initiateDataCheckService(mcontext);
	}

	/**
	 * Starts the @EntryListActivity on the emergency tab
	 * 
	 * @param caller
	 */
	public void showEntryList(View caller) {

		String category = "";

		switch (caller.getId()) {
			case R.id.menu_info:
				category = mcontext.getString(R.string.CAT_NAME_INFORMATION);
				break;
			case R.id.menu_transportation:
				category = mcontext.getString(R.string.CAT_NAME_TRANSPORTATION);
				break;
			case R.id.menu_embassies:
				category = mcontext.getString(R.string.CAT_NAME_EMBASSIES);
				break;
			case R.id.menu_public_services:
				category = mcontext.getString(R.string.CAT_NAME_PUBLIC_SERVICES);
				break;
			case R.id.menu_helplines:
				category = mcontext.getString(R.string.CAT_NAME_HELPLINES);
				break;
			case R.id.menu_police:
				category = mcontext.getString(R.string.CAT_NAME_POLICE);
				break;
			case R.id.menu_healthcare:
				category = mcontext.getString(R.string.CAT_NAME_HEALTHCARE);
				break;
			case R.id.menu_emergency:
				category = mcontext.getString(R.string.CAT_NAME_EMERGENCY);
				break;
			case R.id.menu_user:
				category = mcontext.getString(R.string.CAT_NAME_USER_DEFINED);
				break;

		}

		// send the tapped category to the @EntryListActivity
		activityIntent.putExtra(EntryListActivity.PARAM_CATEGORY, category);

		// start the activity
		startActivity(activityIntent);

	}

}
