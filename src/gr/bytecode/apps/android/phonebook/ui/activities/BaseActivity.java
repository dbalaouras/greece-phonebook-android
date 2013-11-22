package gr.bytecode.apps.android.phonebook.ui.activities;

import gr.bytecode.apps.android.phonebook.R;
import gr.bytecode.apps.android.phonebook.repositories.PreferencesRepository;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * Implements the basic functionality of most activities of the app
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
abstract public class BaseActivity extends SherlockFragmentActivity implements SensorEventListener {

	/**
	 * Application's context
	 */
	protected Context mcontext;

	/**
	 * A sensor manager
	 */
	protected SensorManager mSensorManager;

	/**
	 * The proximity sensor
	 */
	protected Sensor mProximity;

	/**
	 * ActionBarSherloc Instance
	 */
	protected ActionBar actionBar;

	/**
	 * Defines whether we should be showing an actionbar or not
	 */
	protected boolean showActionBar = false;

	/**
	 * Holds the hover-call option value
	 */
	private boolean hoverCallEnabled;

	/**
	 * A repository for the application preferences (controlled by the user)
	 */
	protected PreferencesRepository preferencesRepository;

	/**
	 * ActivityforResult request codes
	 */
	public static final int REQUEST_NEW = 1;

	/**
	 * Perform an auto call event
	 */
	public void autoCallActivated() {

		// get instance of a Vibrator
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// create a vibration pattern
		long[] pattern = { 0, 50, 50, 50 };

		// vibrate using the pattern
		v.vibrate(pattern, -1);

		// call the default number
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse(String.format("tel:%s",
				getString(R.string.NUM_DEFAULT_CALL_NUMBER))));
		mcontext.startActivity(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	public void onStart() {

		super.onStart();

		// start G-A tracker
		EasyTracker.getInstance(this).activityStart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
	 */
	@Override
	public void onStop() {

		super.onStop();

		// stop G-A tracker
		EasyTracker.getInstance(this).activityStop(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onAccuracyChanged(android.hardware
	 * .Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// keep a reference to the current context
		mcontext = this;

		// get a PreferencesRepository instance
		preferencesRepository = PreferencesRepository.getInstance(mcontext);

		// Get an instance of the sensor service, and use that to get an
		// instance of
		// a particular sensor.
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager != null) {
			mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		}

		// configure action bar
		actionBar = getSupportActionBar();

		// configure the default look of the action-bar
		if (actionBar != null) {

			// if the action bar is visible, show the rest
			if (showActionBar) {

				// enable the activity a title
				actionBar.setDisplayShowTitleEnabled(true);

				// enable the home button
				actionBar.setDisplayShowHomeEnabled(true);
				actionBar.setDisplayHomeAsUpEnabled(true);
				actionBar.setHomeButtonEnabled(true);

				setTitle();

				// set the activity icon
				actionBar.setIcon(R.drawable.ic_launcher);

			} else {
				actionBar.hide();
			}

		}

	}

	/**
	 * Sets the title on the actionbar
	 */
	protected void setTitle() {

		// set the title
		actionBar.setTitle(getString(R.string.TXT_APPNAME));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu
	 * (android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onMenuItemSelected(int,
	 * android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		int itemId = item.getItemId();

		switch (itemId) {
			case R.id.action_settings: {

				// start the settings activity
				startPreferenceActivities(null);

				return true;
			}
			case android.R.id.home: {
				finish();
				return true;
			}
			default: {
				return false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {

		// Be sure to unregister the sensor when the activity pauses.
		super.onPause();
		mSensorManager.unregisterListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {

		// Register a listener for the sensor.
		super.onResume();

		// set the hoverCallEnabled boolean
		hoverCallEnabled = preferencesRepository.isHoverCallEnabled();

		if (hoverCallEnabled) {
			mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			mSensorManager.unregisterListener(this);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onSensorChanged(android.hardware
	 * .SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {

		// get the distance
		float distance = event.values[0];

		// if the distance is shorter than 2 cm, call 112
		if (distance < 2) {
			autoCallActivated();
		}

	}

	/**
	 * @param caller
	 */
	public void startPreferenceActivities(View caller) {

		// prepare the intent that starts the Preferences activity
		Intent intent = new Intent(mcontext, PreferencesActivity.class);

		// start the activity
		startActivity(intent);
	}

}
