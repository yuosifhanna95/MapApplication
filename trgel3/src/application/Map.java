package application;

import java.io.Serializable;

public class Map implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String city;
	private String Description;
	private String linkCustomer;
	private String linkEmployee;
	private Boolean NewUpdate;

	Map(int id, String city, String Description, String linkCustomer, String linkEmployee, Boolean NewUpdate) {
		this.id = id;
		this.city = city;
		this.Description = Description;
		this.linkCustomer = linkCustomer;
		this.linkEmployee = linkEmployee;
		this.NewUpdate = NewUpdate;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getLinkCustomer() {
		return linkCustomer;
	}

	public void setLinkCustomer(String linkCustomer) {
		this.linkCustomer = linkCustomer;
	}

	public String getLinkEmployee() {
		return linkEmployee;
	}

	public void setLinkEmployee(String linkEmployee) {
		this.linkEmployee = linkEmployee;
	}

	public void setNewUpdate(Boolean newUpdate) {
		NewUpdate = newUpdate;
	}

	public Boolean getNewUpdate() {
		return NewUpdate;
	}
}
