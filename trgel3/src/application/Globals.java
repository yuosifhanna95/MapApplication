package application;

import javafx.collections.transformation.FilteredList;

public class Globals {
	static int MODE;
	static User user;
	static User userView;
	static City city;
	static FixedPurchase FixedPurchase;
	static Map map;
	static Route route;
	static String backLink;
	static Boolean ThereIsCityUpdate = false;
	static Boolean ThereIsMapUpdate = false;
	static Boolean ThereIsRouteUpdate = false;
	static String IpAddress;
	static String SearchOp = "";
	static String Searchfilter = "";
	static Place place;
	static Place[] places;
	static FilteredList<Place> Fplaces;
	static FilteredList<Place> Fplaces1, Fplaces2;
	static String cityName = "";
}
