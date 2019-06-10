package application;


public class HistoryOfUser {
	private String city;
	private String type;
	private String date;
	
	HistoryOfUser(String city, String type, String date) {
		this.city = city;
		this.type = type;
		this.date = date;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
