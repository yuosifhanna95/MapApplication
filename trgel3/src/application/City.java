package application;


import java.io.Serializable;


public class City implements Serializable{
 
	private static final long serialVersionUID = 1L;
    private String city;
    private String Description;
    private String  numOfMaps;
    private String  numOfPlaces;
    private String  Places;
    private  String  numOfPaths;
    private double fixedcost;
    private double onetimecost;
    City(String city, String Description, String numOfMaps, String numOfPlaces, String numOfPaths, String Places,double fcost,double ocost) {
        this.city = city;
        this.Description = Description;
        this.numOfMaps = numOfMaps;
        this.numOfPlaces =numOfPlaces;
        this.numOfPaths = numOfPaths;
        this.Places = Places;
        this.fixedcost=fcost;
        this.onetimecost=ocost;
    }
    public double getfixedcost() {
        return fixedcost;
    }
    public void setfixedcost(double str) {
        this.fixedcost = str;
    }
    public double getonetimedcost() {
        return onetimecost;
    }
    public void setonetimecost(double str) {
        this.onetimecost = str;
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
    
  

}
