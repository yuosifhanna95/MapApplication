package application;

import java.io.Serializable;
import java.util.Date;

public class FixedPurchase implements Serializable{

	private static final long serialVersionUID = 1L;
	private String user;
	private String city;
	private Date startDate;
	private Date endDate;
	
	FixedPurchase(String user, String city, Date startDate, Date endDate) {
		this.user = user;
		this.city = city;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
