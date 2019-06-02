package application;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Place implements Serializable {

	private static final long serialVersionUID = 1L;
	private long serialID;
	private String MapId;
	private String CityName;
	private String PlaceName;
	private String Description;
	private String Classification;
	private int Accessibility;
	private int LocX;
	private int LocY;
	private String Type;
	private int numOfmaps;

	public Place() {

	}

	public Place(String MapId, String CityName, String PlaceName, String Description, String Classification,
			int Accessibility, int LocX, int LocY, String Type) {
		this.MapId = MapId;
		this.CityName = CityName;
		this.PlaceName = PlaceName;
		this.Description = Description;
		this.Classification = Classification;
		this.Accessibility = Accessibility;
		this.LocX = LocX;
		this.LocY = LocY;
		this.Type = Type;
	}

	public Place(String MapId, String CityName, String PlaceName, String Description, String Classification,
			int Accessibility, long serialID, int LocX, int LocY) {
		this.MapId = MapId;
		this.CityName = CityName;
		this.PlaceName = PlaceName;
		this.Description = Description;
		this.Classification = Classification;
		this.Accessibility = Accessibility;
		this.serialID = serialID;
		this.LocX = LocX;
		this.LocY = LocY;

	}

	public Place(String CityName, String PlaceName, String Description, String Classification, int Accessibility,
			int numOfmaps) {
		this.CityName = CityName;
		this.PlaceName = PlaceName;
		this.Description = Description;
		this.Classification = Classification;
		this.Accessibility = Accessibility;
		this.numOfmaps = numOfmaps;

	}

	public Place(String MapId, String CityName, String PlaceName, String Description, String Classification,
			int Accessibility, long serialID, int LocX, int LocY, String Type, int numOfmaps) {
		this.MapId = MapId;
		this.CityName = CityName;
		this.PlaceName = PlaceName;
		this.Description = Description;
		this.Classification = Classification;
		this.Accessibility = Accessibility;
		this.serialID = serialID;
		this.Type = Type;
		this.LocX = LocX;
		this.LocY = LocY;
		this.numOfmaps = numOfmaps;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getCityName() {
		return CityName;
	}

	public void setPlaceName(String placeName) {
		PlaceName = placeName;
	}

	public String getPlaceName() {
		return PlaceName;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getDescription() {
		return Description;
	}

	public void setClassification(String classification) {
		Classification = classification;
	}

	public String getClassification() {
		return Classification;
	}

	public void setAccessibility(int accessibility) {
		Accessibility = accessibility;
	}

	public int getAccessibility() {
		return Accessibility;
	}

	public long getSerialID() {
		return serialID;
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

	public void setMapId(String mapId) {
		MapId = mapId;
	}

	public String getMapId() {
		return MapId;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public int getNumOfmaps() {
		return numOfmaps;
	}

	public void setNumOfmaps(int numOfmaps) {
		this.numOfmaps = numOfmaps;
	}

}
