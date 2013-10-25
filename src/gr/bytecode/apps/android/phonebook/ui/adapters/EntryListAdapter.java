package gr.bytecode.apps.android.phonebook.ui.adapters;

import gr.bytecode.apps.android.phonebook.R;
import gr.bytecode.apps.android.phonebook.entities.Entry;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class EntryListAdapter extends ArrayAdapter<Entry> implements EntryListClickHandler {

	/**
	 * An activity context
	 */
	private Context mcontext;

	/**
	 * the id of the layout resource that we will be using to illustrate the
	 * phone
	 */
	private int layoutResourceId;

	/**
	 * A list of phones
	 */
	private List<Entry> entries = null;

	/**
	 * An instance of a layout inflater
	 */
	private LayoutInflater inflater;

	/**
	 * Main constructor
	 * 
	 * @param context
	 * @param layoutResourceId
	 * @param entries
	 * @param defaultIconResourceId
	 */
	public EntryListAdapter(Context context, int layoutResourceId, List<Entry> entries) {

		super(context, layoutResourceId, entries);
		this.mcontext = context;
		this.layoutResourceId = layoutResourceId;
		this.entries = entries;

		// load an inflater
		inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	public View getView(int pos, View convertView, ViewGroup parent) {

		if (convertView == null) {

			// inflate the layout resource into the views
			convertView = inflater.inflate(layoutResourceId, parent, false);
		}

		// get the @Entry
		Entry entry = entries.get(pos);

		// locate the text and image views
		TextView labelView = (TextView) convertView.findViewById(R.id.label);

		// locate the text and image views
		View iconSeperator = (View) convertView.findViewById(R.id.sep_icon);

		// set the text and image resources
		labelView.setText(entry.getName());

		// locate the text and image views
		labelView = (TextView) convertView.findViewById(R.id.number);

		// get the phone number
		String phoneNumber = entry.getPhoneNumber();

		// set the text and image resources
		labelView.setText(phoneNumber);

		// find the listview
		String website = entry.getWebsite();

		final int position = pos;

		ImageView netIcon = (ImageView) convertView.findViewById(R.id.net_icon);

		if (website != null && !"".equals(website)) {

			// set a click listener
			netIcon.setOnClickListener(new OnClickListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * android.view.View.OnClickListener#onClick(android.view.View)
				 */
				@Override
				public void onClick(View v) {

					onWebsiteClicked(position);
				}

			});

			// show the browser icon
			netIcon.setVisibility(View.VISIBLE);

			// show the seperator
			iconSeperator.setVisibility(View.VISIBLE);
		} else {

			// hide the browser icon
			netIcon.setVisibility(View.GONE);
			// hide the seperator
			iconSeperator.setVisibility(View.GONE);
		}

		if (phoneNumber != null && !"".equals(phoneNumber)) {

			// create the phone icon
			ImageView phoneIcon = (ImageView) convertView.findViewById(R.id.phone_icon);

			// set a click listener
			phoneIcon.setOnClickListener(new OnClickListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * android.view.View.OnClickListener#onClick(android.view.View)
				 */
				@Override
				public void onClick(View v) {

					// activate the phone click callback
					onPhoneClicked(position);
				}

			});

			// show the phone icon
			phoneIcon.setVisibility(View.VISIBLE);
		}

		// return the view
		return convertView;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.evima.apps.android.ui.adapters.IPhoneClickHandler#phoneClicked(gr.
	 * evima.domain.entities.Phone)
	 */
	@Override
	public void onPhoneClicked(Entry entry) {

		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse(String.format("tel:%s", entry.getPhoneNumber())));
		mcontext.startActivity(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gr.bytecode.apps.android.phonebook.ui.adapters.EntryListClickHandler
	 * #onPhoneClicked(int)
	 */
	@Override
	public void onPhoneClicked(int position) {

		// locate the entry
		Entry entry = entries.get(position);

		// check if we got a valid entry object, and call it
		if (entry != null) {
			onPhoneClicked(entry);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.evima.apps.android.ui.adapters.EntryListClickHandler#phoneListItemClicked
	 * (int)
	 */
	@Override
	public void onEntryListItemClicked(int position) {

		// use the the phone clicked action as the default for
		// list item taps
		onPhoneClicked(position);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.bytecode.apps.android.phonebook.ui.adapters.EntryListClickHandler#
	 * onWebsiteClicked(gr.bytecode.apps.android.phonebook.entities.Entry)
	 */
	@Override
	public void onWebsiteClicked(Entry entry) {

		// get the url of this @Entry
		String url = entry.getWebsite();

		// only start the browser with a valid url
		if (url != null && !"".equals(url)) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			mcontext.startActivity(browserIntent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.bytecode.apps.android.phonebook.ui.adapters.EntryListClickHandler#
	 * onWebsiteClicked(int)
	 */
	@Override
	public void onWebsiteClicked(int position) {

		// locate the @Entry
		Entry enry = entries.get(position);

		// check if we got a valid @Entry object
		if (enry != null) {
			onWebsiteClicked(enry);
		}
	}

	/**
	 * @param entries
	 */
	public void notifyDataChanged(List<Entry> entries) {

		this.entries = entries;

		notifyDataSetChanged();
	}

}
