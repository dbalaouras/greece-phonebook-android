package gr.bytecode.apps.android.phonebook.entities;

import java.io.Serializable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Phonebook Entry Entity
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
@Table(name = "Entry")
public class Entry extends Model implements Serializable {

	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = 2188408880059123231L;

	/**
	 * The name of the entry name
	 */
	@Column(name = "Name")
	public String name;

	/**
	 * The phone number
	 */
	@Column(name = "TelephoneNumber")
	public String telephoneNumber;

	/**
	 * The entry category
	 */
	@Column(name = "EntryCategory")
	public EntryCategory entryCategory;

	/**
	 * The Street Address of the entry
	 */
	@Column(name = "StreetAddress")
	public String streetAddress;

	/**
	 * The Website of the entry
	 */
	@Column(name = "Website")
	public String website;

	/**
	 * The Email address of the entry
	 */
	@Column(name = "Email")
	public String email;

	/**
	 * The Email address of the entry
	 */
	@Column(name = "UserOwned")
	private boolean userOwned;

	/**
	 * The latitude of the entry
	 */
	@Column(name = "Latitude")
	public Float latitude;

	/**
	 * The latitude of the entry
	 */
	@Column(name = "Longtitude")
	public Float longtitude;

	/**
	 * Used only during data synch
	 */
	@Column(name = "CategoryId")
	public Integer externalCategoryId;

	/**
	 * Default parameter-less constructor
	 */
	public Entry() {

		super();
	}

	/**
	 * Secondary constructor, use when only the id of the entry category is
	 * available
	 * 
	 * @param name
	 * @param telephoneNumber
	 * @param streetAddress
	 * @param website
	 * @param email
	 * @param latitude
	 * @param longtitude
	 */
	public Entry(String name, String telephoneNumber, Integer externalCategoryId,
			String streetAddress, String website, String email, Float latitude, Float longtitude) {

		super();
		this.name = name;
		this.telephoneNumber = telephoneNumber;
		this.externalCategoryId = externalCategoryId;
		this.streetAddress = streetAddress;
		this.website = website;
		this.email = email;
		this.latitude = latitude;
		this.longtitude = longtitude;

	}

	/**
	 * Default constructor
	 * 
	 * @param name
	 * @param telephoneNumber
	 * @param entryCategory
	 * @param streetAddress
	 * @param website
	 * @param email
	 * @param latitude
	 * @param longtitude
	 */
	public Entry(String name, String telephoneNumber, EntryCategory entryCategory,
			String streetAddress, String website, String email, Float latitude, Float longtitude) {

		super();
		this.name = name;
		this.telephoneNumber = telephoneNumber;
		this.entryCategory = entryCategory;
		this.streetAddress = streetAddress;
		this.website = website;
		this.email = email;
		this.latitude = latitude;
		this.longtitude = longtitude;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {

		return email;
	}

	/**
	 * @return the externalCategoryId
	 */
	public Integer getExternalCategoryId() {

		return externalCategoryId;
	}

	/**
	 * @return the latitude
	 */
	public Float getLatitude() {

		return latitude;
	}

	/**
	 * @return the longtitude
	 */
	public Float getLongtitude() {

		return longtitude;
	}

	/**
	 * @return the name
	 */
	public String getName() {

		return name;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {

		return telephoneNumber;
	}

	/**
	 * @return the entryCategory
	 */
	public EntryCategory getEntryategory() {

		return entryCategory;
	}

	/**
	 * @return the streetAddress
	 */
	public String getStreetAddress() {

		return streetAddress;
	}

	/**
	 * @return the telephoneNumber
	 */
	public String getTelephoneNumber() {

		return telephoneNumber;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {

		return website;
	}

	/**
	 * @return the userOwned
	 */
	public boolean isUserOwned() {

		return userOwned;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {

		this.email = email;
	}

	/**
	 * @param externalCategoryId
	 *            the externalCategoryId to set
	 */
	public void setExternalCategoryId(Integer externalCategoryId) {

		this.externalCategoryId = externalCategoryId;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Float latitude) {

		this.latitude = latitude;
	}

	/**
	 * @param longtitude
	 *            the longtitude to set
	 */
	public void setLongtitude(Float longtitude) {

		this.longtitude = longtitude;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {

		this.telephoneNumber = phoneNumber;
	}

	/**
	 * @param entryCategory
	 *            the entryCategory to set
	 */
	public void setEntryCategory(EntryCategory entryCategory) {

		this.entryCategory = entryCategory;
		this.setExternalCategoryId(entryCategory.externalId);
	}

	/**
	 * @param streetAddress
	 *            the streetAddress to set
	 */
	public void setStreetAddress(String streetAddress) {

		this.streetAddress = streetAddress;
	}

	/**
	 * @param telephoneNumber
	 *            the telephoneNumber to set
	 */
	public void setTelephoneNumber(String telephoneNumber) {

		this.telephoneNumber = telephoneNumber;
	}

	/**
	 * @param userOwned
	 *            the userOwned to set
	 */
	public void setUserOwned(boolean userOwned) {

		this.userOwned = userOwned;
	}

	/**
	 * @param website
	 *            the website to set
	 */
	public void setWebsite(String website) {

		this.website = website;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		return name + "/" + entryCategory.getName() + " (#" + this.getId() + ")";
	}
}
