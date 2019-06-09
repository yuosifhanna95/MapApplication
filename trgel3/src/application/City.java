package application;

import java.io.Serializable;

public class City implements Serializable {

	private static final long serialVersionUID = 1L;

	private String city;
	private String Description;
	private String numOfMaps;
	private String numOfPlaces;
	private String Places;
	private final String numOfPaths;
	private int oneTimeCost;
	private int fixedCost;
	private int Version;
	private int NewUpdate;
	private Boolean VersionUpdate;

	City(String city, String Description, String numOfMaps, String numOfPlaces, String numOfPaths, String Places,
			int oneTimeCost, int fixedCost, int Version, int NewUpdate, Boolean VersionUpdate) {
		this.city = city;
		this.Description = Description;
		this.numOfMaps = numOfMaps;
		this.numOfPlaces = numOfPlaces;
		this.numOfPaths = numOfPaths;
		this.Places = Places;
		this.oneTimeCost = oneTimeCost;
		this.fixedCost = fixedCost;
		this.NewUpdate = NewUpdate;
		this.Version = Version;
		this.VersionUpdate = VersionUpdate;
	}

	public int getVersion() {
		return Version;
	}

	public void setVersion(int version) {
		Version = version;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String str) {
		this.city = str;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String str) {
		this.Description = str;
	}

	public String getNumOfMaps() {
		return numOfMaps;
	}

	public void setNumOfMaps(String str) {
		this.numOfMaps = str;
	}

	public String getNumOfPlaces() {
		return numOfPlaces;
	}

	public void setNumOfPlaces(String str) {
		this.numOfPlaces = str;
	}

	public String getNumOfPaths() {
		return numOfPaths;
	}

	public void setNumOfPaths(String str) {
		this.numOfMaps = str;
	}

	public String getPlaces() {
		return Places;
	}

	public void setPlaces(String places) {
		Places = places;
	}

	public int getOneTimeCost() {
		return oneTimeCost;
	}

	public void setOneTimeCost(int oneTimeCost) {
		this.oneTimeCost = oneTimeCost;
	}

	public int getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(int fixedCost) {
		this.fixedCost = fixedCost;
	}

	public int getNewUpdate() {
		return NewUpdate;
	}

	public void setNewUpdate(int newUpdate) {
		NewUpdate = newUpdate;
	}

	public Boolean getVersionUpdate() {
		return VersionUpdate;
	}

	public void setVersionUpdate(Boolean versionUpdate) {
		VersionUpdate = versionUpdate;
	}
}
