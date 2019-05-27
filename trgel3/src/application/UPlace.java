package application;

import java.io.Serializable;

public class UPlace extends Place implements Serializable {
	private int PlaceId;

	public UPlace() {
		// TODO Auto-generated constructor stub
	}

	public UPlace(String MapId, String CityName, String PlaceName, String Description, String Classification,
			int Accessibility, int LocX, int LocY, String Type, int PlaceId) {
		super(MapId, CityName, PlaceName, Description, Classification, Accessibility, LocX, LocY, Type);
		this.PlaceId = PlaceId;

	}

	public UPlace(String MapId, String CityName, String PlaceName, String Description, String Classification,
			int Accessibility, long serialID, int LocX, int LocY, int PlaceId) {
		super(MapId, CityName, PlaceName, Description, Classification, Accessibility, serialID, LocX, LocY);
		this.PlaceId = PlaceId;

	}

	public UPlace(String MapId, String CityName, String PlaceName, String Description, String Classification,
			int Accessibility, long serialID, int LocX, int LocY, String Type, int PlaceId) {
		super(MapId, CityName, PlaceName, Description, Classification, Accessibility, serialID, LocX, LocY, Type);
		this.PlaceId = PlaceId;
	}

	public int getPlaceId() {
		return PlaceId;
	}

	public void setPlaceId(int placeId) {
		PlaceId = placeId;
	}
}
