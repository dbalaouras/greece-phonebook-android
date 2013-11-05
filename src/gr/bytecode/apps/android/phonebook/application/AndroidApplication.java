package gr.bytecode.apps.android.phonebook.application;

import android.annotation.SuppressLint;
import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Overrriding the default android application class so that we can set the
 * strict mode during debugging.
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */

public class AndroidApplication extends Application {

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
}
