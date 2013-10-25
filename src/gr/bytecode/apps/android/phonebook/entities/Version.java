package gr.bytecode.apps.android.phonebook.entities;

import java.io.Serializable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Data Version Entity
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
@Table(name = "Version")
public class Version extends Model implements Serializable {

	/**
	 * Generated unique id for Serialization purposes
	 */
	private static final long serialVersionUID = -1330887788122276453L;

	/**
	 * The Id used externaly
	 */
	@Column(name = "ExternalId")
	private Integer externalId;

	/**
	 * The name of the entry name
	 */
	@Column(name = "Name")
	public String name;

	/**
	 * The version number
	 */
	@Column(name = "Number")
	public Integer number;

	/**
	 * Default constructor
	 */
	public Version() {

	}

	/**
	 * @param externalId
	 * @param name
	 * @param number
	 */
	public Version(Integer externalId, String name, Integer number) {

		super();
		this.externalId = externalId;
		this.name = name;
		this.number = number;
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

}
