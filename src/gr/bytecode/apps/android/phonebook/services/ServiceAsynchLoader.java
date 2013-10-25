package gr.bytecode.apps.android.phonebook.services;

import gr.bytecode.apps.android.phonebook.entities.Entry;
import gr.bytecode.apps.android.phonebook.repositories.EntryRepository;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class ServiceAsynchLoader extends AsyncTaskLoader<List<Entry>> {

	private EntryRepository entryRepository;

	private String categoryName;

	/**
	 * Default constructor
	 * 
	 * @param context
	 */
	public ServiceAsynchLoader(Context context, String categoryName) {

		super(context);

		entryRepository = EntryRepository.getInstance();

		// store the categoryName
		this.categoryName = categoryName;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
	 */
	@Override
	public List<Entry> loadInBackground() {

		// load all phones
		List<Entry> phones = entryRepository.getEntriesByCategoryName(categoryName);

		// return it
		return phones;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.content.Loader#onStartLoading()
	 */
	@Override
	protected void onStartLoading() {

		// TODO Auto-generated method stub
		super.onStartLoading();

	}

}
