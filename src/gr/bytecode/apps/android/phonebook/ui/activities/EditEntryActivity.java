package gr.bytecode.apps.android.phonebook.ui.activities;

import gr.bytecode.apps.android.phonebook.R;
import gr.bytecode.apps.android.phonebook.application.AppUtil;
import gr.bytecode.apps.android.phonebook.entities.Entry;
import gr.bytecode.apps.android.phonebook.entities.EntryCategory;
import gr.bytecode.apps.android.phonebook.repositories.CategoryRepository;
import gr.bytecode.apps.android.phonebook.repositories.EntryRepository;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class EditEntryActivity extends BaseActivity {

	/**
	 * The entry that is being editted (if one was sent)
	 */
	private Entry entry;

	/**
	 * The category name for which we're adding a new entry
	 */
	private String entryCategoryName;

	/**
	 * The sent item id
	 */
	private int entryListItemId;

	/**
	 * Text field for the entry name
	 */
	private EditText nameTextView;

	/**
	 * Text field for the entry phone number
	 */
	private EditText phoneNumberTextView;

	/**
	 * Text field for the entry web address
	 */
	private EditText webAddressTextview;

	/**
	 * the Title text View
	 */
	private TextView titleTextview;

	/**
	 * get a EntryRepository instance
	 */
	private EntryRepository entryRepository = EntryRepository.getInstance();

	/**
	 * get a EntryCategoryRepository instance
	 */
	private CategoryRepository entryCategoryRepository = CategoryRepository.getInstance();

	/**
	 * Storage for a common error message
	 */
	private String requiredField = "";

	/**
	 * A reference to the UI section which enables sharing a new entry
	 */
	private View shareRow;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.bytecode.apps.android.phonebook.ui.activities.BaseActivity#onCreate
	 * (android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// we need an actionbar
		showActionBar = true;

		// set the content view
		setContentView(R.layout.activity_service_edit);

		// parse the caller Intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			// get the id of the sent Entry
			Long entryID = (Long) extras.getSerializable("entryId");

			if (entryID != null) {

				// Load the entity
				entry = entryRepository.getEntityById(entryID);
			}

			entryCategoryName = (String) extras.getSerializable("entryCategory");
		}

		// we need a EntryCategory
		if (entryCategoryName == null) {
			AppUtil.postToast(mcontext, "Undefined EntryCategory");
			this.finish();
		}

		// call parent now
		super.onCreate(savedInstanceState);

		// map the UI elements
		nameTextView = (EditText) findViewById(R.id.name);
		phoneNumberTextView = (EditText) findViewById(R.id.phone_number);
		webAddressTextview = (EditText) findViewById(R.id.website_address);
		titleTextview = (TextView) findViewById(R.id.txt_header);
		shareRow = (View) findViewById(R.id.share_entry_row);

		// initiate a entry if none was sent
		if (entry == null) {
			// set the title
			titleTextview.setText(getString(R.string.TITLE_ADD_ENTRY));

			entry = new Entry();

			// this is a user defined entry
			entry.setUserOwned(true);

			// this is a new addition: show the share row
			shareRow.setVisibility(View.VISIBLE);

		} else {

			// set the title
			titleTextview.setText(getString(R.string.TITLE_EDIT_ENTRY));

			// update UI with the received Entry's data
			nameTextView.setText(entry.getName());
			phoneNumberTextView.setText(entry.getPhoneNumber());

			String website = entry.getWebsite();

			if (website != null && "".equals(website) == false) {
				webAddressTextview.setText(entry.getWebsite());
			}

			// this is not a new addition; hide it
			shareRow.setVisibility(View.GONE);

		}

		// initiate some common constants
		requiredField = getString(R.string.ERROR_REQUIRED_FIELD);

		// set on focus change listeners on text fields
		nameTextView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				validateNameField();
			}
		});

		// set on focus change listeners on text fields
		phoneNumberTextView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				validatePhoneField();
			}
		});

		// set on focus change listeners on text fields
		webAddressTextview.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				validateWebField();
			}
		});

	}

	/**
	 * Starts the Offers Activity
	 * 
	 * @param caller
	 */
	public void save(View caller) {

		if (validateForm()) {

			// load the entryCategory object
			EntryCategory entryCategory = entryCategoryRepository
					.getEntryCategoryByName(entryCategoryName);

			// update the entry category
			entry.setEntryCategory(entryCategory);

			entry.setName(nameTextView.getText().toString());
			entry.setPhoneNumber(phoneNumberTextView.getText().toString());
			entry.setWebsite(webAddressTextview.getText().toString());
			entry.setLatitude(0F);
			entry.setLongtitude(0F);

			// save the entity
			entryRepository.saveEntity(entry);

			// prepare the intent to post back to the caller
			Intent intent = new Intent();
			intent.putExtra("entry", entry);
			intent.putExtra("entryCategory", entryCategory);
			intent.putExtra("listItem", entryListItemId);

			setResult(RESULT_OK, intent);
			this.finish();

		}

	}

	/**
	 * Sets the title on the actionbar
	 */
	protected void setTitle() {

		// set the title
		actionBar.setTitle(getString(R.string.TXT_UPDATE_CATEGORY) + ": " + entryCategoryName);
	}

	/**
	 * Validates the form
	 * 
	 * @return
	 */
	private boolean validateForm() {

		EditText focusErrEditText = null;

		// validate one by one, all form elements
		boolean nameValidation = validateNameField();
		boolean phoneValidation = validatePhoneField();
		boolean webValidation = validateWebField();

		// check where to focus
		if (!nameValidation) {
			focusErrEditText = nameTextView;
		} else if (!phoneValidation) {
			focusErrEditText = phoneNumberTextView;
		} else if (!webValidation) {
			focusErrEditText = webAddressTextview;
		}

		// check if we had an error
		if (focusErrEditText != null) {
			focusErrEditText.requestFocus();
			return false;
		}

		// no error
		return true;
	}

	/**
	 * Validates the name field
	 * 
	 * @return
	 */
	private boolean validateNameField() {

		String text = nameTextView.getText().toString();

		if (text.equals("")) {
			nameTextView.setError(requiredField);
			return false;
		}

		return true;
	}

	/**
	 * Validates the phone field
	 * 
	 * @return
	 */
	private boolean validatePhoneField() {

		String text = phoneNumberTextView.getText().toString();

		if (text.equals("")) {
			phoneNumberTextView.setError(requiredField);
			return false;
		}
		return true;
	}

	/**
	 * Validates the web address field
	 * 
	 * @return
	 */
	private boolean validateWebField() {

		String text = webAddressTextview.getText().toString();

		if (text.equals("") == false) {
			if (!text.toString().startsWith("http://") && !text.toString().startsWith("https://")) {

				// prefix the web address
				text = "http://" + webAddressTextview.getText();

				webAddressTextview.setText(text);

				Selection.setSelection(webAddressTextview.getText(), webAddressTextview.getText()
						.length());
			}

			// now validate the text
			if (URLUtil.isValidUrl(text) == false || text.equals("http://")
					|| text.equals("https://")) {
				webAddressTextview.setError(getString(R.string.ERROR_NOT_VALID_WEB_ADDRESS));
				return false;
			}
		}

		return true;
	}
}
