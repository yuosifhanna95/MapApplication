package application;

import java.io.Serializable;

public class RoutePlace implements Serializable{

	private static final long serialVersionUID = 1L;
	private int routeId;
	private String place;
	private int time;
	
	RoutePlace(int id, String place, int time) {
		this.routeId = id;
		this.place = place;
		this.time = time;
	}
	
	public int getRouteId() {
		return routeId;
	}
	
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
}
