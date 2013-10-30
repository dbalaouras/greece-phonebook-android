package gr.bytecode.apps.android.phonebook.entities;

import java.io.Serializable;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Phonebook Entry Category Entity
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
@Table(name = "EntryCategory")
public class EntryCategory extends Model implements Serializable {

	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = 4242490130291330373L;

	/**
	 * The category name
	 */
	@Column(name = "Name")
	public String name;

	/**
	 * A short description of the category
	 */
	@Column(name = "Description")
	public String description;

	/**
	 * Used only during data synch
	 */
	@Column(name = "ExternalId")
	public Integer externalId;

	/**
	 * Default parameter-less constructor
	 */
	public EntryCategory() {

		super();
	}

	/**
	 * No-id constructor
	 * 
	 * @param id
	 * @param name
	 */
	public EntryCategory(Integer externalCategoryId, String name, String description) {

		super();
		this.name = name;
		this.externalId = externalCategoryId;
		this.description = description;
	}

	/**
	 * Get a list of telephone that belong to this category ordered by category
	 * 
	 * @return
	 */
	public List<Entry> getEntries() {

		return getMany(Entry.class, "EntryCategory");
	}

	/**
	 * @return the name
	 */
	public String getName() {

		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {

		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	/**
	 * @return the externalId
	 */
	public Integer getExternalId() {

		return externalId;
	}

	/**
	 * @param externalId
	 *            the externalCategoryId to set
	 */
	public void setExternalId(Integer externalCategoryId) {

		this.externalId = externalCategoryId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		return "EntryCategory: '" + getName() + "'  #" + getId() + ", ExternalId: "
				+ getExternalId();
	}

}
