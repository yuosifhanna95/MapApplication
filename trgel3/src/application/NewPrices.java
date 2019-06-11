package application;

import java.io.Serializable;

public class NewPrices  implements Serializable{
	private static final long serialVersionUID = 1L;

	private String city;
	private int oneTimeCost;
	private int fixedCost;
	
	NewPrices(String city,int fixedCost, int oneTimeCost){
			
		this.city = city;
		this.fixedCost=fixedCost;
		this.oneTimeCost=oneTimeCost;
	}
	public String getCity() {
		return city;
	}

	public void setCity(String str) {
		this.city = str;
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
}
