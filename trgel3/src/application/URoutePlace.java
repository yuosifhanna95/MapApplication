package application;

import java.io.Serializable;

public class URoutePlace extends RoutePlace implements Serializable {

	private int RPlaceId;

	URoutePlace(int id, int RPlaceId, String place, int time, int LocX, int LocY, long serialId, String Type) {
		super(id, place, time, LocX, LocY, serialId, Type);
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
