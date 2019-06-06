package application;

import java.io.Serializable;

public class URoutePlace extends RoutePlace implements Serializable {

	private int RPlaceId;

	URoutePlace(int id, int RPlaceId, String place, int time) {
		super(id, place, time);
		this.RPlaceId = RPlaceId;
		// TODO Auto-generated constructor stub
	}

	public int getRPlaceId() {
		return RPlaceId;
	}

	public void setRPlaceId(int rPlaceId) {
		RPlaceId = rPlaceId;
	}
}
