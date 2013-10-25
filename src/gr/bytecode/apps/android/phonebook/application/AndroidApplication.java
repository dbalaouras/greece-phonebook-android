package gr.bytecode.apps.android.phonebook.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Configuration;

import com.activeandroid.ActiveAndroid;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */

public class AndroidApplication extends Application {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onConfigurationChanged(android.content.res.
	 * Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@SuppressLint("NewApi")
	@Override
	public void onCreate() {

		super.onCreate();
		ActiveAndroid.initialize(this);

		// // enable strict mode if android os version is above 8
		// if (android.os.Build.VERSION.SDK_INT > 8) {
		// // set strict mode
		// StrictMode.setThreadPolicy(new
		// StrictMode.ThreadPolicy.Builder().detectAll()
		// .penaltyLog().penaltyDialog().build());
		// StrictMode.setVmPolicy(new
		// StrictMode.VmPolicy.Builder().detectAll().penaltyLog()
		// .build());
		//
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onLowMemory()
	 */
	@Override
	public void onLowMemory() {

		super.onLowMemory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {

		super.onTerminate();
	}
}
