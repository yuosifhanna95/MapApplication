package application;
import java.io.Serializable;
public class Reports implements Serializable {
	private static final long serialVersionUID = 1L;
	private String city;
	private int numOfMaps;
	private int numofonetimepurchase;
	private int numOfSubscribers;
	private int numofRenews;
	private int numOfViews;
	private int numOfDownloads;
	private java.sql.Date StartDate;
	private java.sql.Date EndDate;
	Reports(String city,  int numOfMaps, int numofonetimepurchase, int numOfSubscribers, int numofRenews, int numOfViews,
			int numOfDownloads, java.sql.Date StartDate, java.sql.Date EndDate) {
		this.city = city;
		this.numOfMaps=numOfMaps;
		this.numofonetimepurchase=numofonetimepurchase;
		this.numOfSubscribers=numOfSubscribers;
		this.numofRenews=numofRenews;
		this.numOfViews=numOfViews;
		this.numOfDownloads=numOfDownloads;
		this.StartDate=StartDate;
		this.EndDate=EndDate;
	}
	public String getCity() {
		return city;
	}

	public void setCity(String str) {
		this.city = str;
	}
	public int getNumMaps() {
		return this.numOfMaps;
	}

	public void setNumMaps(int str) {
		this.numOfMaps = str;
	}
	public int getNumOtPurchase() {
		return this.numofonetimepurchase;
	}

	public void setNumOtPurchase(int str) {
		this.numofonetimepurchase = str;
	}
	public int getNumSubscribers() {
		return this.numOfSubscribers;
	}

	public void setNumSubscribers(int str) {
		this.numOfSubscribers = str;
	}
	public int getNumRenews() {
		return this.numofRenews;
	}

	public void setNumRenews(int str) {
		this.numofRenews = str;
	}
	public int getNumViews() {
		return this.numOfViews;
	}

	public void setNumViews(int str) {
		this.numOfViews = str;
	}
	public int getNumDownloads() {
		return this.numOfDownloads;
	}

	public void setNumDownloads(int str) {
		this.numOfDownloads = str;
	}
	public java.sql.Date getStartDate() {
		return this.StartDate;
	}

	public void setStartDate(java.sql.Date str) {
		this.StartDate = str;
	}
	public java.sql.Date getEndDate() {
		return this.EndDate;
	}

	public void setEndDate(java.sql.Date str) {
		this.EndDate = str;
	}
}
