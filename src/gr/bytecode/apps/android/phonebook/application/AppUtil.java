package gr.bytecode.apps.android.phonebook.application;

import gr.bytecode.apps.android.phonebook.repositories.PreferencesRepository;
import gr.bytecode.apps.android.phonebook.services.DataUpdaterService;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Application level utilities
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class AppUtil {

	/**
	 * Get a logger instance
	 */
	public static final BasicAndroidLogger logger = BasicAndroidLogger.getInstance();

	/**
	 * Handles an unexpected error
	 * 
	 * @param error
	 */
	public static void handleSevereError(String error) {

		// log the severe error
		logger.wtf(error);

		// TODO: add error in local database for later syncs
	}

	/**
	 * Checks if whether we should be starting the data-check service, and
	 * launch it if needed
	 */
	public static void initiateDataCheckService(Context context) {

		// get an instance of user preferences repository
		PreferencesRepository preferencesRepository = PreferencesRepository.getInstance(context);

		// retrieve the check interval from the preferences
		int checkInterval = preferencesRepository.getDataCheckInteral();

		// check data if we are connected and auto data check is enabled by the
		// user
		if (preferencesRepository.isAutoDataCheckEnabled() && AppUtil.isDeviceConnected(context)
				&& checkInterval > 0) {

			boolean checkRequired = false;

			// if the check interval is set to a positive integer (days)
			Date lastCheckDate = preferencesRepository.getLastDataCheckDate();

			// if the last check is null (never updated(!)) or the last
			// update did not occur recently, then check is required
			if (lastCheckDate != null) {

				int diffInDays = (int) ((new Date().getTime() - lastCheckDate.getTime()) / (1000 * 60 * 60 * 24));
				if (diffInDays > checkInterval) {
					checkRequired = true;
				}
			} else {
				checkRequired = true;
			}

			// start the service if required
			if (checkRequired) {

				startDataCheckService(context);
			}
		}
	}

	/**
	 * Check if the device has internet access
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isDeviceConnected(Context context) {

		// get a ConnectivityManager from the context
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// double check if we got a ConnectivityManager at all
		if (connectivity == null) {

			return false;
		} else {

			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 * 
	 * @param context
	 *            The application's environment.
	 * @param action
	 *            The Intent action to check for availability.
	 * 
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {

		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * Log a debug message on the logger
	 * 
	 * @param string
	 */
	public static void logDebug(String string) {

		logger.logDebug(string);

	}

	/**
	 * Log an informative text
	 * 
	 * @param text
	 */
	public static void logInfo(String text) {

		logger.logInfo(text);
	}

	/**
	 * Logs severe Exceptions that were caught but should never had thrown
	 * 
	 * @param exception
	 *            The severe exception
	 */
	public static void logSevereException(Exception exception) {

		// log the severe exception
		handleSevereError(exception.getMessage());

		exception.printStackTrace();
	}

	/**
	 * Log a warning message
	 * 
	 * @param text
	 */
	public static void logWarning(String text) {

		logger.logWarning(text);
	}

	/**
	 * Posts a new Toast
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void postToast(Context context, CharSequence text) {

		postToast(context, text, Toast.LENGTH_SHORT, Gravity.CENTER);
	}

	/**
	 * Posts a new Toast
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void postToast(Context context, CharSequence text, int duration) {

		postToast(context, text, duration, Gravity.CENTER);
	}

	/**
	 * Posts a new Toast
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 * @param gravity
	 */
	public static void postToast(Context context, CharSequence text, int duration, int gravity) {

		duration = duration == 0 ? Toast.LENGTH_LONG : duration;
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}

	/**
	 * Starts the data check service
	 * 
	 * @param context
	 */
	public static void startDataCheckService(Context context) {

		Intent intent = new Intent(context, DataUpdaterService.class);
		context.startService(intent);
	}

}
