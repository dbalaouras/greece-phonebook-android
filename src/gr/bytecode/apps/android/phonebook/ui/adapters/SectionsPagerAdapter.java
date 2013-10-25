package gr.bytecode.apps.android.phonebook.ui.adapters;

import gr.bytecode.apps.android.phonebook.entities.EntryCategory;
import gr.bytecode.apps.android.phonebook.ui.fragments.EntryListFragment;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

/**
 * An adapter for Fragments
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	/**
	 * Reference to App context
	 */
	Context context;

	/**
	 * The default locale
	 */
	Locale defaultLocale = Locale.getDefault();

	/**
	 * A list of EntryCategories to show
	 */
	private List<EntryCategory> entryCategories;

	private SparseArray<EntryListFragment> fragments = new SparseArray<EntryListFragment>();

	private ViewPager viewPager;

	/**
	 * Default constructor
	 * 
	 * @param context
	 * @param fm
	 * @param entryCategories
	 */
	public SectionsPagerAdapter(Context context, FragmentManager fm, ViewPager viewPager,
			List<EntryCategory> entryCategories) {

		super(fm);

		// store the context
		this.context = context;

		// hold a reference to the @ViewPager (our container)
		this.viewPager = viewPager;

		// load all categories
		this.entryCategories = entryCategories;

	}

	/**
	 * @param position
	 * @return
	 */
	public EntryCategory getEntryCategoryByFragmentPosition(int position) {

		return entryCategories.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {

		// getItem is called to instantiate the fragment for the given page.
		EntryListFragment fragment = new EntryListFragment();

		Bundle args = new Bundle();

		EntryCategory entryCategory = entryCategories.get(position);

		args.putInt(EntryListFragment.ARG_POSITION, position + 1);
		args.putString(EntryListFragment.ARG_ENTRY_CATEGORY, entryCategory.getName());
		fragment.setArguments(args);

		// save the fragment
		fragments.put(position, fragment);

		return fragment;
	}

	/**
	 * Get the fragment that is active (and visible)
	 * 
	 * @return
	 */
	public EntryListFragment getCurrentFragment() {

		int position = viewPager.getCurrentItem();

		return fragments.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {

		return entryCategories.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
	 */
	@Override
	public CharSequence getPageTitle(int position) {

		String title;
		if (entryCategories.size() > 0) {
			title = entryCategories.get(position).getName().toLowerCase(defaultLocale);
		} else {
			title = "";
		}

		// return the category name
		return title;
	}

	/**
	 * @param entryCategories
	 *            the entryCategories to set
	 */
	public void setEntryCategories(List<EntryCategory> entryCategories) {

		this.entryCategories = entryCategories;
	}

}
