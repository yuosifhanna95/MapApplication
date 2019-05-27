package application;

import java.io.Serializable;

public class Place implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long serialID;
	private String CityName;
	private String PlaceName;
	private String Description;
	private String Classification;
	private int Accessibility;
	private int numOfmaps;

	public Place() {

	}

	public Place(String CityName, String PlaceName, String Description, String Classification, int Accessibility, int numOfmaps) {
		this.CityName = CityName;
		this.PlaceName = PlaceName;
		this.Description = Description;
		this.Classification = Classification;
		this.Accessibility = Accessibility;
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

	public int getNumOfmaps() {
		return numOfmaps;
	}

	public void setNumOfmaps(int numOfmaps) {
		this.numOfmaps = numOfmaps;
	}
}
