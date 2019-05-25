package application;

public class Place {

	private long serialID;
	private String CityName;
	private String PlaceName;
	private String Description;
	private String Classification;
	private int Accessibility;

	public Place() {

	}

	public Place(String CityName, String PlaceName, String Description, String Classification, int Accessibility,
			long serialID) {
		this.CityName = CityName;
		this.PlaceName = PlaceName;
		this.Description = Description;
		this.Classification = Classification;
		this.Accessibility = Accessibility;
		this.serialID = serialID;
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
}
