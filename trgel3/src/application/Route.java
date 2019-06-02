package application;

import java.io.Serializable;

public class Route implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String city;
	private String description;
	private String link;
	
	Route(int id, String city, String description, String link) {
		this.id = id;
		this.city = city;
		this.description = description;
		this.link = link;
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
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	

}
