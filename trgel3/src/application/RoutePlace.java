package application;

import java.io.Serializable;

public class RoutePlace implements Serializable {

	private static final long serialVersionUID = 1L;
	private long serialID;
	private int routeId;
	private String place;
	private int time;
	private int LocX, LocY;
	private String Type;

	RoutePlace(int routeId, String place, int time, int LocX, int LocY, long serialID, String Type) {
		this.serialID = serialID;
		this.routeId = routeId;
		this.place = place;
		this.time = time;
		this.LocX = LocX;
		this.LocY = LocY;
		this.Type = Type;
	}

	RoutePlace(int routeId, String place, int time, int LocX, int LocY, long serialID) {
		this.serialID = serialID;
		this.routeId = routeId;
		this.place = place;
		this.time = time;
		this.LocX = LocX;
		this.LocY = LocY;
	}

	public long getSerialID() {
		return serialID;
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

	public void setLocX(int locX) {
		LocX = locX;
	}

	public int getLocX() {
		return LocX;
	}

	public void setLocY(int locY) {
		LocY = locY;
	}

	public int getLocY() {
		return LocY;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}
}
