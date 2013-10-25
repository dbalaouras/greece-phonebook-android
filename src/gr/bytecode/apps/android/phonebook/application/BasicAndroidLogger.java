package gr.bytecode.apps.android.phonebook.application;

import android.util.Log;

/**
 * Implements a logger using android's Logger implementation
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class BasicAndroidLogger implements Logger {

	private static volatile BasicAndroidLogger me;

	/**
	 * The logging tag that identifies log messages
	 */
	public static final String APP_LOG_TAG = "Phonebook.bytecode.gr";

	/**
	 * Prevent instantiation
	 */
	private BasicAndroidLogger() {

	}

	/**
	 * Singleton pattern getInstance method with double-checked locking
	 * 
	 * @return
	 */
	public static BasicAndroidLogger getInstance() {

		// check if there is an instance already
		if (me == null) {
			// if not, try obtaining a lock on the volatile object and
			// instantiate a Logger
			synchronized (BasicAndroidLogger.class) {
				if (me == null) {
					me = new BasicAndroidLogger();
				}
			}
		}
		return me;
	}

	@Override
	public void logDebug(String debug) {

		Log.d(APP_LOG_TAG, debug);
	}

	@Override
	public <E extends Exception> void logException(E exception) {

		// print stack trace
		exception.printStackTrace();

		// log the exception message
		Log.wtf(APP_LOG_TAG, exception.getMessage());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gr.evima.domain.application.ILogger#logInfo(java.lang.String)
	 */
	@Override
	public void logInfo(String info) {

		Log.i(APP_LOG_TAG, info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gr.evima.domain.application.ILogger#logWarning(java.lang.String)
	 */
	@Override
	public void logWarning(String warning) {

		Log.w(APP_LOG_TAG, warning);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gr.evima.domain.application.ILogger#logError(java.lang.String)
	 */
	@Override
	public void logError(String error) {

		Log.e(APP_LOG_TAG, error);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gr.evima.domain.utilities.ILogger#wtf(java.lang.String)
	 */
	@Override
	public void wtf(String failure) {

		Log.wtf(APP_LOG_TAG, failure);

	}
}
