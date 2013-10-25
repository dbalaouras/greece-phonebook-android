package gr.bytecode.apps.android.phonebook.ui.fragments;

import gr.bytecode.apps.android.phonebook.R;
import gr.bytecode.apps.android.phonebook.entities.Entry;
import gr.bytecode.apps.android.phonebook.repositories.EntryRepository;
import gr.bytecode.apps.android.phonebook.ui.adapters.EntryListAdapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * A fragment that contains a list of entries for a single category. The
 * fragment is be loaded by the @SectionsPagerAdapter.
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class EntryListFragment extends Fragment {

	/**
	 * A reference to the ListView of the fragment
	 */
	ListView entryListView;

	/**
	 * A reference to the Android Context
	 */
	private Context mcontext;

	/**
	 * An instance of the telephone adapter
	 */
	private EntryListAdapter entryAdapter;

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_POSITION = "section_number";

	/**
	 * The fragment argument representing the telephone category
	 */
	public static final String ARG_ENTRY_CATEGORY = "entry_category";

	/**
	 * The container of the the loading progress bar
	 */
	private View mProgressContainer;

	/**
	 * The container of the list
	 */
	private View mListContainer;

	/**
	 * Tells whether the list is visible or not
	 */
	boolean mListShown = false;

	/**
	 * The name of the category of the listed Entries
	 */
	private String categoryName;

	/**
	 * A reference to the @EntryRepository
	 */
	private EntryRepository entryRepository;

	/**
	 * An asynch loader for the @Entry data
	 */
	private AsynchDataLoader asynchDataLoader;

	/**
	 * A list of Entries shown in this fragment
	 */
	private List<Entry> entries = new ArrayList<Entry>();

	/**
	 * The position of the fragment
	 */
	private int position;

	private View emptyMessageView;

	/**
	 * Default Constructor
	 */
	public EntryListFragment() {

		// every @Fragment needs a default no argument constructor

		// get a reference of the @EntryRepository
		entryRepository = EntryRepository.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		this.registerForContextMenu(entryListView);

		// get the context
		mcontext = this.getActivity();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {

		// unregister this fragment on the parent activity

		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater
	 * , android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// get the arguments
		Bundle arguments = getArguments();

		// get the category name
		categoryName = arguments.getString(ARG_ENTRY_CATEGORY);

		// set the position
		position = arguments.getInt(ARG_POSITION);

		// locate the root view
		View rootView = inflater.inflate(R.layout.fragment_servicelist, container, false);

		// find the listview
		entryListView = (ListView) rootView.findViewById(android.R.id.list);

		emptyMessageView = (View) rootView.findViewById(android.R.id.empty);

		// locate the list container
		mListContainer = rootView.findViewById(R.id.list_container);

		// find the
		mProgressContainer = rootView.findViewById(R.id.progress_container);

		// Start with a progress indicator.
		setListShown(false);

		// craete a new instance of the AsynchDataLoader
		asynchDataLoader = new AsynchDataLoader();

		// start the asynch loader
		asynchDataLoader.execute(getCategoryName());

		// return the root view
		return rootView;
	}

	/**
	 * Control whether the list is being displayed. You can make it not
	 * displayed if you are waiting for the initial data to show in it. During
	 * this time an indeterminant progress indicator will be shown instead.
	 * 
	 * @param shown
	 *            If true, the list view is shown; if false, the progress
	 *            indicator. The initial value is true.
	 */
	private void setListShown(boolean shown) {

		if (mProgressContainer == null) {
			throw new IllegalStateException("progress container View is missing");
		}

		if (mListShown == shown) {
			return;
		}

		mListShown = shown;
		emptyMessageView.setVisibility(View.GONE);
		if (shown) {

			mProgressContainer.setVisibility(View.GONE);
			mListContainer.setVisibility(View.VISIBLE);
		} else {

			mProgressContainer.setVisibility(View.VISIBLE);
			mListContainer.setVisibility(View.GONE);
		}
	}

	/**
	 * Creates and sets a list adapter on the listfragment
	 * 
	 * @param entries
	 */
	private void setListAdapter(List<Entry> entries) {

		entryAdapter = new EntryListAdapter(mcontext, R.layout.service_list_item, entries);

		// set a click listener
		entryListView.setOnItemClickListener(new OnItemClickListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick
			 * (android.widget.AdapterView, android.view.View, int, long)
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// pass action onto the Phone List adapter
				entryAdapter.onEntryListItemClicked(position);
			}
		});

		// set the adapter
		entryListView.setAdapter(entryAdapter);

		// now check if we have something to show or not
		if (entries.size() > 0) {
			setListShown(true);

		} else {
			// no data!
			emptyMessageView.setVisibility(View.VISIBLE);
			mListContainer.setVisibility(View.GONE);
			mProgressContainer.setVisibility(View.GONE);
		}
	}

	/**
	 * @author Dimitris Balaouras
	 * @copyright 2013 ByteCode.gr
	 * 
	 */
	private class AsynchDataLoader extends AsyncTask<String, Integer, List<Entry>> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<Entry> doInBackground(String... params) {

			// read the category name
			String categoryName = params[0];

			// load all phones
			entries = entryRepository.getEntriesByCategoryName(categoryName);

			// return it
			return entries;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(List<Entry> entries) {

			super.onPostExecute(entries);

			setListAdapter(entries);
		}

	}

	/**
	 * Callback used when the dataset has changed
	 * 
	 * @param categoryName
	 */
	public void reloadDataSet() {

		// reload data asynchronously
		new AsynchDataLoader().execute(categoryName);
	}

	/**
	 * @return the position
	 */
	public int getPosition() {

		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {

		this.position = position;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {

		return categoryName;
	}

	/**
	 * @param categoryName
	 *            the categoryName to set
	 */
	public void setCategoryName(String categoryName) {

		this.categoryName = categoryName;
	}

	/**
	 * @param position
	 * @return
	 */
	public Entry getEntryAtPosition(int position) {

		return entries.get(position);
	}

	/**
	 * @param position
	 * @return
	 */
	public Long getEntryIdAtPosition(int position) {

		Entry entry = entries.get(position);

		return entry == null ? null : entry.getId();
	}

	/**
	 * @param position
	 */
	public void removeEntryAtPosition(int position) {

		// locate the @Entry item
		Entry entry = entries.get(position);

		// remove the item from the database
		entryRepository.deleteEntity(entry);

		// reload data
		reloadDataSet();

	}
}
