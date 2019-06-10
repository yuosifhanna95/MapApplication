package application;

import java.io.Serializable;
import java.util.Date;

public class FixedPurchase implements Serializable{

	private static final long serialVersionUID = 1L;
	private String user;
	private String city;
	private Date startDate;
	private Date endDate;
	private double price;
	private int period;
	
	FixedPurchase(String user, int period, String city, Date startDate, Date endDate, double price) {
		this.user = user;
		this.city = city;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.period = period;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}
}