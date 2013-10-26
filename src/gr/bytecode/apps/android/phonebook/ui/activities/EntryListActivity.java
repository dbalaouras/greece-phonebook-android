package gr.bytecode.apps.android.phonebook.ui.activities;

import gr.bytecode.apps.android.phonebook.R;
import gr.bytecode.apps.android.phonebook.application.AppConstants;
import gr.bytecode.apps.android.phonebook.application.AppUtil;
import gr.bytecode.apps.android.phonebook.entities.Entry;
import gr.bytecode.apps.android.phonebook.entities.EntryCategory;
import gr.bytecode.apps.android.phonebook.repositories.CategoryRepository;
import gr.bytecode.apps.android.phonebook.repositories.EntryRepository;
import gr.bytecode.apps.android.phonebook.ui.adapters.SectionsPagerAdapter;
import gr.bytecode.apps.android.phonebook.ui.fragments.EntryListFragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class EntryListActivity extends BaseActivity {

	/**
	 * parameter passed with the starting Intent from the main Activity
	 */
	public static final String PARAM_CATEGORY = "category";

	private static final int REQUEST_NEW = 0;

	private static final int REQUEST_EDIT = 1;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	/**
	 * A repository instance for EntryCategories
	 */
	private CategoryRepository entryCategoryRepository;

	/**
	 * A repository instance for Entries
	 */
	private EntryRepository entryRepository;

	/**
	 * 
	 */
	private String selectedCategory;

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

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servicelist);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.

		// get a category repository
		entryCategoryRepository = CategoryRepository.getInstance();

		// get a entry repository
		entryRepository = EntryRepository.getInstance();

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);

		// get the intent that started this activity
		Intent intent = getIntent();

		// try to locate which category we should load
		selectedCategory = intent.getStringExtra(PARAM_CATEGORY);

		// Register mMessageReceiver to receive messages.
		registerReceiver(dataUpdatedReceiver, new IntentFilter(AppConstants.DATA_UPDATED_ACTION));

		loadViewPager();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onDestroy()
	 */
	public void onDestroy() {

		// call parent
		super.onDestroy();

		// Unregister since the activity is not visible
		unregisterReceiver(dataUpdatedReceiver);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		System.out.println(requestCode);
		System.out.println(resultCode);

		// if all was okay, update the fragment
		if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {

			// notify the current fragment that the data has changed
			notifyCurrentFragmentForDataChanged();

		}
	}

	/**
	 * Loads fresh data from the database and creates or resets the
	 * SectionsPagerAdapter
	 */
	private void loadViewPager() {

		List<EntryCategory> entryCategories = loadCategories();

		mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),
				mViewPager, entryCategories);

		// set the adapter
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// browse to the selected category
		if (selectedCategory != null && !"".equals(selectedCategory)) {
			int num = 0;
			for (EntryCategory cat : entryCategories) {

				// check if this is the matched category
				if (selectedCategory.equals(cat.getName())) {
					mViewPager.setCurrentItem(num);
					break;
				}
				num++;
			}
		}

	}

	/**
	 * Loads fresh data from the database and resets the SectionsPagerAdapter
	 */
	private void reLoadViewPager() {

		List<EntryCategory> entryCategories = loadCategories();

		// instantiate the section adapter if it's null (should never be?)
		if (mSectionsPagerAdapter == null) {
			mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),
					mViewPager, entryCategories);
		} else {
			mSectionsPagerAdapter.setEntryCategories(entryCategories);
		}

		// set the adapter
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	/**
	 * @return
	 */
	private List<EntryCategory> loadCategories() {

		return entryCategoryRepository.getEntryCategories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// add the default menu
		getSupportMenuInflater().inflate(R.menu.service_list_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 * android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		android.view.MenuInflater inflater = getMenuInflater();
		menu.setHeaderTitle(R.string.TXT_MANAGE_ENTRY);
		inflater.inflate(R.menu.service_list_item_menu, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		// get the current fragment
		EntryListFragment currentFragment = mSectionsPagerAdapter.getCurrentFragment();

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		Entry selectedEntry = currentFragment.getEntryAtPosition(info.position);

		switch (item.getItemId()) {

			case R.id.edit:

				// find the entry category that maps with the given itemId
				String entryCategoryName = mSectionsPagerAdapter.getCurrentFragment()
						.getCategoryName();

				// prepare the new intent
				Intent startEditEntryIntent = new Intent(this, EditEntryActivity.class);
				startEditEntryIntent.putExtra("entryCategory", entryCategoryName);
				startEditEntryIntent.putExtra("entryId", selectedEntry.getId());

				// start the edit entry
				startActivityForResult(startEditEntryIntent, REQUEST_EDIT);

				return true;

			case R.id.delete:

				entryRepository.deleteEntity(selectedEntry);

				AppUtil.postToast(mcontext, getString(R.string.TXT_ENTRY_REMOVED));

				// notify fragments that data have changed
				notifyCurrentFragmentForDataChanged();
				return true;
		}

		// if nothing matched
		return super.onContextItemSelected(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gr.bytecode.apps.android.phonebook.ui.activities.BaseActivity#
	 * onMenuItemSelected(int, com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
			case R.id.add: {

				// find the entry category that maps with the given itemId
				String entryCategoryName = mSectionsPagerAdapter.getCurrentFragment()
						.getCategoryName();

				// prepare the new intent
				Intent startEditEntryIntent = new Intent(this, EditEntryActivity.class);
				startEditEntryIntent.putExtra("entryCategory", entryCategoryName);

				startActivityForResult(startEditEntryIntent, REQUEST_NEW);

				return true;
			}

			default: {
				return super.onMenuItemSelected(featureId, item);
			}
		}
	}

	/**
	 * Notifies the active fragments that data have changed
	 * 
	 */
	public void notifyCurrentFragmentForDataChanged() {

		// get the current fragment
		EntryListFragment currentFragment = mSectionsPagerAdapter.getCurrentFragment();
		currentFragment.reloadDataSet();
	}

	// handler for received Intents for the "my-event" event
	private BroadcastReceiver dataUpdatedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			// (re)load the PageViewer with fresh data
			reLoadViewPager();
		}
	};

}
