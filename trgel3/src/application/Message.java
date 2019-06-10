package application;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user;
	private Date datesend;
	private String message;
	private String typemessage;
	
	Message(String user, String message, Date datesend, String typemessage) {
		this.user = user;
		this.message = message;
		this.datesend = datesend;
		this.typemessage = typemessage;
		
	}
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String mess) {
		this.message = mess;
	}
	public Date getDatesend() {
		return datesend;
	}

	public void setDatesend(Date date) {
		this.datesend = date;
	}
	public String getTypemessage() {
		return typemessage;
	}

	public void setTypemessage(String mess) {
		this.typemessage = mess;
	}
}
