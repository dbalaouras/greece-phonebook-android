package gr.bytecode.apps.android.phonebook.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class DataUpdateDialogFragment extends SherlockDialogFragment {

	public static final String NEW_DATA_FOUND = "new_data_found";

	private int dialogId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Bundle args = getArguments();

		dialogId = args.getInt("id");

		// boolean newDataFound = args.getBoolean(NEW_DATA_FOUND);

		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Chain together various setter methods to set the dialog
		// characteristics
		builder.setMessage("Data Update").setTitle("asdasdas");

		builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				((IDataUpdateDialogCallback) getActivity()).updateClick(dialogId);
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				((IDataUpdateDialogCallback) getActivity()).cancelClick(dialogId);
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();

		// return the dialog
		return dialog;
	}

	/**
	 * @author Dimitris Balaouras
	 * @copyright 2013 ByteCode.gr
	 * 
	 */
	public interface IDataUpdateDialogCallback {

		/**
		 * @param dialogId
		 */
		public abstract boolean updateClick(int dialogId);

		/**
		 * @param dialogId
		 */
		public abstract boolean cancelClick(int dialogId);

	}

}
