package application;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server {

	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static private final String DB = "eCp4XWJvNw";// sql2293675
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/" + DB + "?useSSL=false";// sql2.freemysqlhosting.net:3306///
	static private final String USER = "eCp4XWJvNw";// sql2293675
	static private final String PASS = "eSS7xZeTpg";// bW3%jS1%
	static private Object data;
	static private int checked = 0;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {

		try {
			@SuppressWarnings("resource")
			ServerSocket myServerSocket = new ServerSocket(5555);
			while (true) {

				Date date2 = new Date();

				if (((date2.getHours() == 4 && date2.getMinutes() > 45) || date2.getHours() == 1) && (checked == 0)) {
					if (checkthesystem() == 1) {
						new Thread(new ScheduleMessage()).start();
					}
					checked = 1;
				}

				Socket skt = myServerSocket.accept();
				ObjectInputStream objectInput = new ObjectInputStream(skt.getInputStream());
				data = objectInput.readObject();

				if (data instanceof Object[] && !(data instanceof String[])) {
					if (((String) ((Object[]) (data))[0]).equals("dofixedpurchase")) {
						Connection conn = null;

						FixedPurchase fp = ((FixedPurchase) ((Object[]) (data))[1]);

						Class.forName(JDBC_DRIVER);

						conn = DriverManager.getConnection(DB_URL, USER, PASS);
						PreparedStatement pr;
						String sql = "INSERT INTO fixedPurchase(`user`, `city`, `period`, `startdate`, `endDate`, `purchaseprice`) VALUES (?,?,?,?,?,?)";

						if (conn != null) {
							pr = conn.prepareStatement(sql);

							Date StartDate = fp.getStartDate();
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							Calendar c = Calendar.getInstance();
							String startDateAsString = df.format(StartDate);
							c.setTime(df.parse(startDateAsString));
							c.add(Calendar.DATE, 1);
							startDateAsString = df.format(c.getTime());
							Date endDate = fp.getEndDate();
							String endDateAsString = df.format(endDate);
							c.setTime(df.parse(endDateAsString));
							c.add(Calendar.DATE, 1);
							endDateAsString = df.format(c.getTime());

							pr.setString(1, fp.getUser());
							pr.setString(2, fp.getCity());
							pr.setInt(3, fp.getPeriod());
							pr.setDate(4, java.sql.Date.valueOf(startDateAsString));
							pr.setDate(5, java.sql.Date.valueOf(endDateAsString));
							pr.setString(6, Double.toString(fp.getPrice()));
							if (pr.executeUpdate() > 0) {
								ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
								String message = "thanks for purchace,you will enjoy";
								objectOutput.writeObject(message);
							}

							Date sdate = Calendar.getInstance().getTime();
							String date = df.format(sdate);
							statics("fixedpurchase", fp.getCity(), java.sql.Date.valueOf(endDateAsString));
							AddPurchaseToHistory(fp.getCity(), 1, fp.getUser(), "FT", date, conn);

						}

					} else if (((String) ((Object[]) (data))[0]).equals("getreportcities")) {
						ObservableList<Reports> data1 = FXCollections.observableArrayList();
						Reports r = ((Reports) ((Object[]) (data))[2]);
						data1 = getReportFromDB1(r);
						Object[] data = new Object[data1.size() + 1];
						data[0] = data1.size();
						int counter = 1;
						for (Reports tu : data1) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else if (((String) ((Object[]) (data))[0]).equals("getreport")) {
						ObservableList<Reports> data1 = FXCollections.observableArrayList();

						String type = ((String) ((Object[]) (data))[1]);
						if (type.equals("One city")) {
							Reports r = ((Reports) ((Object[]) (data))[2]);
							Reports report = getReportFromDB(r);
							data1.add(report);
							Object[] data = new Object[data1.size() + 1];
							data[0] = data1.size();
							int counter = 1;
							for (Reports tu : data1) {
								data[counter] = tu;
								counter++;
							}
							try {
								ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
								objectOutput.writeObject(data);
							} catch (IOException e) {
								e.printStackTrace();
							}

						} else {
							ObservableList<City> cityList = getCityFromDB(-1);
							Object[] data2 = new Object[cityList.size() + 1];
							data2[0] = cityList.size();

							for (City ms : cityList) {
								Reports r1 = ((Reports) ((Object[]) (data))[2]);
								r1.setCity(ms.getCity());
								Reports report = getReportFromDB(r1);
								data1.add(report);
							}
							Object[] data = new Object[data1.size() + 1];
							data[0] = data1.size();
							int counter1 = 1;
							for (Reports tu : data1) {
								data[counter1] = tu;
								counter1++;
							}

							try {
								ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
								objectOutput.writeObject(data);
							} catch (IOException e) {
								e.printStackTrace();
							}

						}

					} else if (((String) ((Object[]) (data))[0]).equals("confirm the prices and save it")) {
						Connection conn = null;
						NewPrices n = ((NewPrices) ((Object[]) (data))[1]);

						Class.forName(JDBC_DRIVER);

						conn = DriverManager.getConnection(DB_URL, USER, PASS);
						// conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
						PreparedStatement pr = null;
						PreparedStatement pr1 = null;
						Statement stmt = null;
						String sql = "INSERT INTO newprices( `city`, `fixedcost`, `onetimecost`) VALUES (?,?,?)";
						String sql1 = "INSERT INTO messages(`userName`, `message`, `Datesend`, `read`, `typemessage`) VALUES (?,?,?,?,?)";
						String sqlfind = "SELECT * FROM newprices WHERE city='" + n.getCity() + "'";
						if ((conn != null)) {
							stmt = conn.createStatement();
							ResultSet rs = stmt.executeQuery(sqlfind);
							if (rs.next()) {
								System.out.println("city prices have been already updated");
								ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
								String message = "city prices have been already updated";
								objectOutput.writeObject(message);

							} else {
								pr = conn.prepareStatement(sql);
								pr1 = conn.prepareStatement(sql1);
								Date date5 = new Date();
								java.sql.Date date3 = new java.sql.Date(date5.getTime());
								String type = "Confirmation of change of prices of city " + n.getCity();
								String us = "yuosif";
								String mess = "Dear sir,i updated the prices of purchase the maps of " + n.getCity()
										+ " city, i see that we will profit more money without losing any clint,so please confirm the price to update prices and publish it.";
								pr1.setString(1, us);
								pr1.setString(2, mess);
								pr1.setDate(3, date3);
								pr1.setInt(4, 0);
								pr1.setString(5, type);

								pr.setString(1, n.getCity());
								pr.setInt(2, n.getFixedCost());
								pr.setInt(3, n.getOneTimeCost());

								if ((pr1.executeUpdate() > 0)) {
									if (pr.executeUpdate() > 0) {
										ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
										String message = "we save the update price,we will listen for the confirmation,and we will send to you the answer of the directory maneger";
										objectOutput.writeObject(message);
									}
								}
							}
						}

					} else if (((String) ((Object[]) (data))[0]).equals("updateUserInfo")) {

						Connection conn = null;

						User user = ((User) ((Object[]) (data))[1]);

						Class.forName(JDBC_DRIVER);

						conn = DriverManager.getConnection(DB_URL, USER, PASS);

						PreparedStatement pr;
						String sql = "UPDATE user SET firstName = ?, lastName = ?, phoneNumber = ?, email = ?, payment = ?, userName = ?, password = ? WHERE id = ?";
						if (conn != null) {
							pr = conn.prepareStatement(sql);

							pr.setString(1, user.getFirstName());
							pr.setString(2, user.getLastName());
							pr.setString(3, user.getPhoneNumber());
							pr.setString(4, user.getEmail());
							pr.setString(5, user.getPayment());
							pr.setString(6, user.getUserName());
							pr.setString(7, user.getPassword());
							pr.setInt(8, user.getId());

							pr.executeUpdate();

						}

					} else if (((String) ((Object[]) (data))[0]).equals("getfixedcostandpayinfo")) {

						Connection conn = null;
						Statement stmt = null;
						Statement stmt1 = null;
						Object[] ob = new Object[2];
						double i = -1;
						String pay = "";
						String cityname = ((String) ((Object[]) (data))[1]);
						String username = ((String) ((Object[]) (data))[2]);
						System.out.println(cityname);
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();
							stmt1 = conn.createStatement();
							String sql = "SELECT * FROM CityCatalog";
							String sql1 = "SELECT * FROM user";
							ResultSet rs = stmt.executeQuery(sql);
							ResultSet rs1 = stmt1.executeQuery(sql1);

							while (rs.next()) {
								String x = rs.getString("name");
								if (x.equals(cityname)) {
									i = rs.getDouble("fixedCost");

								}
							}
							while (rs1.next()) {
								String y = rs1.getString("userName");
								if (y.equals(username)) {
									pay = rs1.getString("payment");

								}

							}

							ob[0] = i;
							ob[1] = pay;
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(ob);
							stmt.close();
							conn.close();

						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						}
					} else if (((String) ((Object[]) (data))[0]).equals("OneTimePurchase")) {
						Connection conn = null;
						User user = (User) ((Object[]) (data))[1];
						String city = (String) ((Object[]) (data))[2];
						int version = (int) ((Object[]) (data))[3];
						String date = (String) ((Object[]) (data))[4];
						Class.forName(JDBC_DRIVER);
						conn = DriverManager.getConnection(DB_URL, USER, PASS);
						statics("onepurchase", city, null);
						statics("download", city, null);
						AddPurchaseToHistory(city, version, user.getUserName(), "OT", date, conn);
					}

					else if (((String) ((Object[]) (data))[0]).equals("addNewDate")) {
						Connection conn = null;
						Statement stmt = null;

						Date NewEndDate = ((Date) ((Object[]) (data))[1]);
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Calendar c = Calendar.getInstance();
						String NewEndDateAsString = df.format(NewEndDate);
						c.setTime(df.parse(NewEndDateAsString));
						c.add(Calendar.DATE, 1);
						NewEndDateAsString = df.format(c.getTime());

						Date NewStartDate = ((Date) ((Object[]) (data))[2]);
						String NewStartDateAsString = df.format(NewStartDate);
						c.setTime(df.parse(NewStartDateAsString));
						c.add(Calendar.DATE, 1);
						NewStartDateAsString = df.format(c.getTime());

						String cityname = ((String) ((Object[]) (data))[5]);
						String username = ((String) ((Object[]) (data))[4]);
						int newPeriod = ((int) ((Object[]) (data))[3]);
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();

							String sql = "update fixedPurchase set endDate = ? , startDate = ? , period = ? where user = ? and city = ?";
							PreparedStatement pr;

							if (conn != null) {
								pr = conn.prepareStatement(sql);
								pr.setDate(1, java.sql.Date.valueOf(NewEndDateAsString));
								pr.setDate(2, java.sql.Date.valueOf(NewStartDateAsString));
								pr.setInt(3, newPeriod);
								pr.setString(4, username);
								pr.setString(5, cityname);
								pr.executeUpdate();
							}
							statics("fixedpurchase", cityname, java.sql.Date.valueOf(NewEndDateAsString));
							statics("renewpurchase", cityname, java.sql.Date.valueOf(NewEndDateAsString));
							stmt.close();
							conn.close();

						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						}
					} else if (((String) ((Object[]) (data))[0]).equals("Register")) {

						User client = ((User) ((Object[]) (data))[1]);
						String fname1 = client.getFirstName();
						String lname1 = client.getLastName();
						String phone = client.getPhoneNumber();
						String email = client.getEmail();
						String pay = client.getPayment();
						String user = client.getUserName();
						String password = client.getPassword();
						String history = client.getHistory();

						Connection conn = null;
						Statement stmt = null;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							// stmt = conn.createStatement();

							PreparedStatement pr;
							String sql = "INSERT INTO user (`firstName`, `lastName`, `phoneNumber`, `email`, `payment`, `userName`, `password`,`History`) VALUES (?,?,?,?,?,?,?,?)";
							// ResultSet rs = stmt.executeQuery(sql);

							if (conn != null) {
								try {
									if ((fname1.equals("")) || (lname1.equals("")) || (phone.equals(""))
											|| (email.equals("")) || (pay.equals("")) || (user.equals(""))
											|| (password.equals(""))) {
										ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
										String message = "fill all the fields";
										objectOutput.writeObject(message);

									} else if (checkuser(user, conn) == 1) {
										// System.out.println("please");
										JOptionPane.showMessageDialog(null, "the username already exist");
									} else {
										pr = conn.prepareStatement(sql);
										pr.setString(1, fname1);
										pr.setString(2, lname1);
										pr.setString(3, phone);
										pr.setString(4, email);
										pr.setString(5, pay);
										pr.setString(6, user);
										pr.setString(7, password);
										pr.setString(8, history);
										if (pr.executeUpdate() > 0) {
											ObjectOutputStream objectOutput = new ObjectOutputStream(
													skt.getOutputStream());
											String message = "thanks for registeration";
											objectOutput.writeObject(message);
										}
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							// stmt.close();
							conn.close();

						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (conn != null)
									conn.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}
					} else if (((String) ((Object[]) (data))[0]).equals("UpdatePlace")) {

						System.out.println("this is update");
						Place place;
						if (((Object[]) (data))[1] instanceof UPlace) {
							place = ((UPlace) ((Object[]) (data))[1]);
						} else
							place = ((Place) ((Object[]) (data))[1]);

						long serialid = place.getSerialID();
						String MapId = place.getMapId();
						int PlaceId = -1;
						if (place.getType().equals("UPDATE")) {
							PlaceId = Integer.parseInt(((String) ((Object[]) (data))[2]));
						}
						if (place instanceof UPlace) {
							if (((UPlace) place).getPlaceId() == -1) {
								PlaceId = (int) ((UPlace) place).getPlaceId();
							} else {
								PlaceId = ((UPlace) place).getPlaceId();
							}
						}
						String CityName = place.getCityName();
						String PlaceName = place.getPlaceName();
						String description = place.getDescription();
						String classification = place.getClassification();
						int accessibility = place.getAccessibility();
						int LocX = place.getLocX();
						int LocY = place.getLocY();
						int mapsNum = place.getNumOfmaps();
						String Type = place.getType();

						Connection conn = null;
						Statement stmt = null;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();

							PreparedStatement pr;
							String sql = "INSERT INTO Updates (`MapId`,PlaceId ,`Name`, `Place`, `description`, `classification`, accessibility, LocX, LocY, mapsNum, `Type`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
							// ResultSet rs = stmt.executeQuery(sql);

							if (conn != null) {
								if (PlaceId == -1)
									checkuplace(place, conn, "NOP");
								else
									checkuplace(place, conn, "YESP");

								pr = conn.prepareStatement(sql);
								pr.setString(1, MapId);
								pr.setInt(2, PlaceId);
								pr.setString(3, CityName);
								pr.setString(4, PlaceName);
								pr.setString(5, description);
								pr.setString(6, classification);
								pr.setInt(7, accessibility);
								pr.setInt(8, LocX);
								pr.setInt(9, LocY);
								pr.setInt(10, mapsNum);
								pr.setString(11, Type);

								if (pr.executeUpdate() > 0) {
									// JOptionPane.showMessageDialog(null, "thanks for Update");
									System.out.println("thanks for Update");
									SetUpdateForMap(Integer.parseInt(MapId), conn);
									SetUpdateForCity(CityName, conn);

								}
							}
							skt.close();

							conn.close();

						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (conn != null)
									conn.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}

					} else if (((String) ((Object[]) (data))[0]).equals("UpdateRPlace")) {

						System.out.println("this is update");
						RoutePlace Rplace;
						if (((Object[]) (data))[1] instanceof URoutePlace) {
							Rplace = ((URoutePlace) ((Object[]) (data))[1]);
						} else
							Rplace = ((RoutePlace) ((Object[]) (data))[1]);

						String CityName = ((String) ((Object[]) (data))[2]);
						long serialid = Rplace.getSerialID();
						int routeId = Rplace.getRouteId();
						int RPlaceId = -1;
						if (Rplace.getType().equals("UPDATE")) {
							RPlaceId = (int) Rplace.getSerialID();
						}
						if (Rplace instanceof URoutePlace) {
							if (((URoutePlace) Rplace).getRPlaceId() == -1) {
								RPlaceId = (int) ((URoutePlace) Rplace).getRPlaceId();
							} else {
								RPlaceId = ((URoutePlace) Rplace).getRPlaceId();
							}
						}
						// String CityName = Rplace.getCityName();
						String PlaceName = Rplace.getPlace();
						int LocX = Rplace.getLocX();
						int LocY = Rplace.getLocY();
						int time = Rplace.getTime();
						String Type = Rplace.getType();

						Connection conn = null;
						Statement stmt = null;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();

							PreparedStatement pr;
							String sql = "INSERT INTO RoutesUpdates (RouteId, RPlaceId,`place`, time, LocX, LocY, `Type`) VALUES (?,?,?,?,?,?,?)";
							// ResultSet rs = stmt.executeQuery(sql);

							if (conn != null) {
								if (RPlaceId == -1)
									checkurplace(Rplace, conn, "NOP");
								else
									checkurplace(Rplace, conn, "YESP");

								pr = conn.prepareStatement(sql);
								pr.setInt(1, routeId);
								pr.setInt(2, RPlaceId);
								pr.setString(3, PlaceName);
								pr.setInt(4, time);
								pr.setInt(5, LocX);
								pr.setInt(6, LocY);
								pr.setString(7, Type);

								if (pr.executeUpdate() > 0) {
									// JOptionPane.showMessageDialog(null, "thanks for Update");
									System.out.println("thanks for Update");
									SetUpdateForRoute(routeId, conn);
									SetUpdateForCity(CityName, conn);

								}
							}
							skt.close();

							conn.close();

						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (conn != null)
									conn.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}

					} else if (((String) ((Object[]) (data))[0]).equals("AddMap")) {
						int x = 0;
						Connection conn = null;
						Statement stmt = null;
						Map map = ((Map) ((Object[]) (data))[1]);

						Class.forName(JDBC_DRIVER);

						conn = DriverManager.getConnection(DB_URL, USER, PASS);
						PreparedStatement pr;

						AddCityIfNotExist(map, conn, ((String) ((Object[]) (data))[2]));
						String sql = "INSERT INTO maps(`city`, `description`, `linkCustomer`, `linkEmployee`, NewUpdate) VALUES (?,?,?,?,?)";
						if (conn != null) {
							pr = conn.prepareStatement(sql);
							pr.setString(1, map.getCity());
							pr.setString(2, map.getDescription());
							pr.setString(3, map.getLinkCustomer());
							pr.setString(4, map.getLinkEmployee());
							pr.setInt(5, map.getNewUpdate());
							if (pr.executeUpdate() > 0) {
								// ObjectOutputStream objectOutput = new
								// ObjectOutputStream(skt.getOutputStream());
								// String message = "thanks for adding map";
								// objectOutput.writeObject(message);
								AddCityNumberOfMaps(map.getCity(), conn);
								System.out.println("thanks for adding map");
							}

						}

					} else if (((String) ((Object[]) (data))[0]).equals("AddRoute")) {
						int x = 0;
						Connection conn = null;
						Statement stmt = null;
						Route Route = ((Route) ((Object[]) (data))[1]);

						Class.forName(JDBC_DRIVER);

						conn = DriverManager.getConnection(DB_URL, USER, PASS);
						PreparedStatement pr;

						AddCityIfNotExist(Route, conn, ((String) ((Object[]) (data))[2]));
						String sql = "INSERT INTO Routes(`city`, `description`, `link`, NewUpdate) VALUES (?,?,?,?)";
						if (conn != null) {
							pr = conn.prepareStatement(sql);
							pr.setString(1, Route.getCity());
							pr.setString(2, Route.getDescription());
							pr.setString(3, Route.getLink());
							pr.setInt(4, Route.getNewUpdate());
							if (pr.executeUpdate() > 0) {
								// ObjectOutputStream objectOutput = new
								// ObjectOutputStream(skt.getOutputStream());
								// String message = "thanks for adding map";
								// objectOutput.writeObject(message);
								AddCityNumberOfRoutes(Route.getCity(), conn);
								System.out.println("thanks for adding route");
							}

						}

					}

				} else if (data instanceof String[]) {

					if (((String[]) (data))[0].equals("Exit"))
						break;
					else if (((String) ((String[]) (data))[0]).equals("staticDownload")) {

						Date StartDate = Calendar.getInstance().getTime();
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Calendar c = Calendar.getInstance();
						String startDateAsString = df.format(StartDate);
						c.setTime(df.parse(startDateAsString));
						c.add(Calendar.DATE, 1);
						startDateAsString = df.format(c.getTime());
						statics("download", ((String) ((String[]) (data))[1]),
								java.sql.Date.valueOf(startDateAsString));

					} else if (((String) ((String[]) (data))[0]).equals("VersionUpdate")) {

						System.out.println("this is version");
						String City = ((String[]) (data))[1];
						String sql = "UPDATE CityCatalog SET VersionUpdate=" + true + " WHERE name='" + City + "'";
						Class.forName(JDBC_DRIVER);
						Connection conn = null;
						PreparedStatement pr;
						conn = DriverManager.getConnection(DB_URL, USER, PASS);
						pr = conn.prepareStatement(sql);
						if (pr.executeUpdate() > 0) {
							System.out.println("thanks for requesting new version");
						}

					} else if (((String) ((String[]) (data))[0]).equals("AgreeVUpdate")) {

						System.out.println("this is confirm version");

						String City = ((String[]) (data))[1];
						String sql = "UPDATE CityCatalog SET VersionUpdate=" + false + " WHERE name='" + City + "'";
						String sqlfind = "SELECT * FROM CityCatalog WHERE name='" + City + "'";

						Class.forName(JDBC_DRIVER);
						Connection conn = null;
						Statement stmt = null;
						PreparedStatement pr;
						conn = DriverManager.getConnection(DB_URL, USER, PASS);
						stmt = conn.createStatement();

						pr = conn.prepareStatement(sql);
						if (pr.executeUpdate() > 0) {
						}
						ResultSet rs = stmt.executeQuery(sqlfind);
						if (rs.next()) {
							int Version = rs.getInt("version");
							Version++;
							sql = "UPDATE CityCatalog SET version=" + Version + " WHERE name='" + City + "'";
							pr = conn.prepareStatement(sql);
							if (pr.executeUpdate() > 0) {
								System.out.println("thanks for Confirming new version");
								SendVerMessage(City, conn, Version);
							}
						}

					} else if (((String) ((String[]) (data))[0]).equals("DisagreeVUpdate")) {

						System.out.println("this is disagree version");

						String City = ((String[]) (data))[1];
						String sql = "UPDATE CityCatalog SET VersionUpdate=" + false + " WHERE name='" + City + "'";
						Class.forName(JDBC_DRIVER);
						Connection conn = null;
						Statement stmt = null;
						PreparedStatement pr;
						conn = DriverManager.getConnection(DB_URL, USER, PASS);
						stmt = conn.createStatement();

						pr = conn.prepareStatement(sql);
						if (pr.executeUpdate() > 0) {
						}
						System.out.println("thanks for Canceling new version");

					} else if (((String) ((String[]) (data))[0]).equals("ConfirmUpdate")) {

						System.out.println("this is confirm");
						Boolean flag = false;
						UPlace place;

						String MapId = ((String[]) (data))[1];
						UPlace[] list = getPlaces(MapId);

						for (int i = 0; i < list.length; i++) {
							place = list[i];
							int PlaceId = -1;
							if (place.getType().equals("UPDATE")) {
								PlaceId = place.getPlaceId();
							}
							if (place instanceof UPlace) {
								if (((UPlace) place).getPlaceId() == -1) {
									PlaceId = (int) ((UPlace) place).getPlaceId();
								} else {
									PlaceId = ((UPlace) place).getPlaceId();
								}
							}
							String CityName = place.getCityName();
							String PlaceName = place.getPlaceName();
							String description = place.getDescription();
							String classification = place.getClassification();
							int accessibility = place.getAccessibility();
							int LocX = place.getLocX();
							int LocY = place.getLocY();
							String Type = place.getType();
							int mapsnum = place.getNumOfmaps();
							Connection conn = null;
							Statement stmt = null, stmt2 = null;
							try {
								Class.forName(JDBC_DRIVER);

								conn = DriverManager.getConnection(DB_URL, USER, PASS);
								stmt = conn.createStatement();
								stmt2 = conn.createStatement();
								PreparedStatement pr, pr2;

								String sql = "", sql2 = "", sql1 = "";// = "INSERT INTO places (`MapId` ,`Name`,
																		// `Place`,
								// `description`,
								// `classification`, accessibility, LocX, LocY, `Type`) VALUES
								// (?,?,?,?,?,?,?,?,?)";
								// ResultSet rs = stmt.executeQuery(sql);

								if (place.getType().equals("NEW")) {
									sql = "INSERT INTO places (`MapId` ,`Name`, `Place`, `description`, `classification`, accessibility, LocX, LocY, mapsNum) VALUES (?,?,?,?,?,?,?,?,?)";
									sql1 = "SELECT * FROM places WHERE Place='" + place.getPlaceName() + "' AND Name='"
											+ place.getCityName() + "'";
									sql2 = "INSERT INTO placeMap (`Name`,`MapId` ,LocX, LocY) VALUES (?,?,?,?)";
								} else if (place.getType().equals("DELETE")) {
									sql = "DELETE FROM places WHERE id =" + place.getPlaceId();
									sql1 = "SELECT * FROM placeMap WHERE Name='" + place.getPlaceName() + "'";
									sql2 = "DELETE FROM placeMap WHERE Name='" + place.getPlaceName() + "' AND mapID="
											+ place.getMapId();
								} else {
									sql = "UPDATE places SET `Place` = '" + PlaceName + "', `description` = '"
											+ description + "', `classification` = '" + classification
											+ "', accessibility = " + accessibility + " WHERE id=" + PlaceId;

									int PlaceMapId = getPlaceMapId(PlaceName, conn, MapId);
									sql2 = "UPDATE placeMap SET `Name` = '" + PlaceName + "', LocX = " + LocX
											+ ", LocY = " + LocY + " WHERE id=" + PlaceMapId;
								}
								if (conn != null) {
									// if (PlaceId == -1)
									// checkplace(place, conn, "NOP");
									// else
									// checkplace(place, conn, "YESP");

									if (place.getType().equals("NEW")) {
										pr = conn.prepareStatement(sql);
										pr.setString(1, MapId);
										pr.setString(2, CityName);
										pr.setString(3, PlaceName);
										pr.setString(4, description);
										pr.setString(5, classification);
										pr.setInt(6, accessibility);
										pr.setInt(7, LocX);
										pr.setInt(8, LocY);
										pr.setInt(9, mapsnum);

										pr2 = conn.prepareStatement(sql2);
										pr2.setString(1, PlaceName);
										pr2.setString(2, MapId);
										pr2.setInt(3, LocX);
										pr2.setInt(4, LocY);

										ResultSet rs = stmt.executeQuery(sql1);
										if (!rs.next()) {
											if (pr.executeUpdate() > 0) {
												// JOptionPane.showMessageDialog(null, "thanks for Update");
												System.out.println("thanks for confirmation");
												if (pr2.executeUpdate() > 0) {
													// JOptionPane.showMessageDialog(null, "thanks for Update");
													System.out.println("thanks for confirmation");
													AddCityNumberOfPlaces(CityName, conn);
													AddMapNumberOfPlace(PlaceName, conn);
													flag = true;
												}
											}
										} else {
											if (pr2.executeUpdate() > 0) {
												// JOptionPane.showMessageDialog(null, "thanks for Update");
												System.out.println("thanks for confirmation");
												flag = true;
											}
										}

									} else if (place.getType().equals("UPDATE")) {
										if (stmt.executeUpdate(sql) > 0) {
											// JOptionPane.showMessageDialog(null, "thanks for Update");
											System.out.println("thanks for confirmation");
											if (stmt2.executeUpdate(sql2) > 0) {
												// JOptionPane.showMessageDialog(null, "thanks for Update");
												System.out.println("thanks for confirmation");
												AddMapNumberOfPlace(PlaceName, conn);
												flag = true;
											}
										}

									} else if (place.getType().equals("DELETE")) {
										if (stmt.executeUpdate(sql2) > 0) {
											// JOptionPane.showMessageDialog(null, "thanks for Update");

											System.out.println("thanks for confirmation");
											ResultSet rs = stmt.executeQuery(sql1);
											if (!rs.next())
												if (stmt2.executeUpdate(sql) > 0) {
													// JOptionPane.showMessageDialog(null, "thanks for Update");
													RemoveCityNumberOfPlaces(CityName, conn);
													System.out.println("thanks for confirmation");
													flag = true;
												}
										}

									}
								}
								skt.close();
								conn.close();

							} catch (SQLException se) {
								se.printStackTrace();
								System.out.println("SQLException: " + se.getMessage());
								System.out.println("SQLState: " + se.getSQLState());
								System.out.println("VendorError: " + se.getErrorCode());
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try {
									if (stmt != null)
										stmt.close();
									if (conn != null)
										conn.close();
								} catch (SQLException se) {
									se.printStackTrace();
								}
							}
						}
						DeleteUpdates(MapId);
						DeleteNewUpdates(MapId);
					} else if (((String) ((String[]) (data))[0]).equals("ConfirmRUpdate")) {

						System.out.println("this is routes confirm");
						Boolean flag = false;
						URoutePlace place;

						int routeId = Integer.parseInt(((String[]) (data))[1]);
						ObservableList<URoutePlace> list = getURoutePlacesFromDB(routeId);
						// URoutePlace[] list;

						for (int i = 0; i < list.size(); i++) {
							place = list.get(i);
							int RPlaceId = -1;
							if (place.getType().equals("UPDATE")) {
								RPlaceId = place.getRPlaceId();
							}
							if (place instanceof URoutePlace) {
								if (((URoutePlace) place).getRPlaceId() == -1) {
									RPlaceId = (int) ((URoutePlace) place).getRPlaceId();
								} else {
									RPlaceId = ((URoutePlace) place).getRPlaceId();
								}
							}
							String City = ((String[]) (data))[2];
							String PlaceName = place.getPlace();
							int id = (int) place.getSerialID();
							int LocX = place.getLocX();
							int LocY = place.getLocY();
							int time = place.getTime();
							String Type = place.getType();
							Connection conn = null;
							Statement stmt = null, stmt2 = null;
							try {
								Class.forName(JDBC_DRIVER);

								conn = DriverManager.getConnection(DB_URL, USER, PASS);
								stmt = conn.createStatement();
								stmt2 = conn.createStatement();
								PreparedStatement pr, pr2;

								String sql, sql2;// = "INSERT INTO places (`MapId` ,`Name`, `Place`, `description`,
								// `classification`, accessibility, LocX, LocY, `Type`) VALUES
								// (?,?,?,?,?,?,?,?,?)";
								// ResultSet rs = stmt.executeQuery(sql);

								if (place.getType().equals("NEW")) {
									sql = "INSERT INTO RoutePlaces (RouteId, `Place`, time, LocX, LocY) VALUES (?,?,?,?,?)";
								} else if (place.getType().equals("DELETE")) {
									// sql = "DELETE FROM RoutePlaces WHERE id =" + place.getPlaceId();
									sql = "DELETE FROM RoutePlaces WHERE place='" + place.getPlace() + "' AND RouteID="
											+ place.getRouteId();
								} else {
									sql = "UPDATE RoutePlaces SET `Place` = '" + PlaceName + "' , time=" + time
											+ " ,LocX=" + LocX + ", LocY=" + LocY + " WHERE id=" + RPlaceId;

									// int PlaceMapId = getPlaceMapId(PlaceName, conn, routeId);
								}
								if (conn != null) {
									// if (PlaceId == -1)
									// checkplace(place, conn, "NOP");
									// else
									// checkplace(place, conn, "YESP");

									if (place.getType().equals("NEW")) {
										pr = conn.prepareStatement(sql);
										pr.setInt(1, routeId);
										pr.setString(2, PlaceName);
										pr.setInt(3, time);
										pr.setInt(4, LocX);
										pr.setInt(5, LocY);

										// pr2 = conn.prepareStatement(sql2);
										// pr2.setInt(1, routeId);
										// pr2.setString(2, PlaceName);
										// pr2.setInt(3, time);
										// pr2.setInt(3, LocX);
										// pr2.setInt(4, LocY);
										if (pr.executeUpdate() > 0) {
											// JOptionPane.showMessageDialog(null, "thanks for Update");
											System.out.println("thanks for R confirmation");
											// if (pr2.executeUpdate() > 0) {
											// JOptionPane.showMessageDialog(null, "thanks for Update");
											// System.out.println("thanks for confirmation");
											// flag = true;
											// }
										}

									} else if (place.getType().equals("UPDATE") || place.getType().equals("DELETE")) {
										if (stmt.executeUpdate(sql) > 0) {
											// JOptionPane.showMessageDialog(null, "thanks for Update");
											System.out.println("thanks for R confirmation");
											// if (stmt2.executeUpdate(sql2) > 0) {
											// JOptionPane.showMessageDialog(null, "thanks for Update");
											// System.out.println("thanks for confirmation");
											// flag = true;
											// }
										}

									}
								}
								skt.close();
								conn.close();

							} catch (SQLException se) {
								se.printStackTrace();
								System.out.println("SQLException: " + se.getMessage());
								System.out.println("SQLState: " + se.getSQLState());
								System.out.println("VendorError: " + se.getErrorCode());
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try {
									if (stmt != null)
										stmt.close();
									if (conn != null)
										conn.close();
								} catch (SQLException se) {
									se.printStackTrace();
								}
							}
						}
						DeleteRUpdates(routeId);
						DeleteRNewUpdates(routeId);
					} else if (((String[]) (data))[0].equals("getNewPrices")) {
						ObservableList<NewPrices> NewPricesList = getMyNewPricesFromDB(((String[]) (data))[1]);
						Object[] data = new Object[NewPricesList.size() + 1];
						data[0] = NewPricesList.size();
						int counter = 1;
						for (NewPrices ms : NewPricesList) {
							data[counter] = ms;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (((String[]) (data))[0].equals("send message to contentmaneger")) {
						Connection conn = null;
						Connection conn1 = null;
						Statement stmt = null;
						String ncity1 = ((String[]) (data))[4];
						PreparedStatement pr;
						String sql1 = "DELETE FROM newprices WHERE city = ?";
						String sql = "SELECT * FROM newprices where city='" + ncity1 + "'";
						String sql2 = "update CityCatalog set fixedCost = ?,oneTimeCost=? where name = ?";
						int fc1 = -1;
						int otcost1 = -1;

						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
							if (conn != null && conn1 != null) {
								stmt = conn.createStatement();
								ResultSet rs = stmt.executeQuery(sql);
								pr = conn1.prepareStatement(sql1);
								pr.setString(1, ncity1);
								while (rs.next()) {
									fc1 = rs.getInt("fixedcost");
									otcost1 = rs.getInt("onetimecost");
								}
								pr.executeUpdate();
							}

							if (((String[]) (data))[2].equals("yes")) {
								if (conn != null) {
									pr = conn.prepareStatement(sql2);
									pr.setInt(1, fc1);
									pr.setInt(2, otcost1);
									pr.setString(3, ncity1);
									pr.executeUpdate();
								}
							}

							PreparedStatement pr1 = null;

							String sql3 = "INSERT INTO messages(`userName`, `message`, `Datesend`, `read`, `typemessage`) VALUES (?,?,?,?,?)";
							if ((conn != null)) {

								pr1 = conn1.prepareStatement(sql3);
								String type2 = "the answer of the directorymaneger about the NewPrices for city "
										+ ncity1;
								java.sql.Date date3 = new java.sql.Date(new Date().getTime());
								String us = ((String[]) (data))[1];
								String mess = ((String[]) (data))[3];
								pr1.setString(1, us);
								pr1.setString(2, mess);
								pr1.setDate(3, date3);
								pr1.setInt(4, 0);
								pr1.setString(5, type2);
								if (pr1.executeUpdate() > 0) {
									ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
									String message = "done";
									objectOutput.writeObject(message);
								}
							}
						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						}
					} else if (((String[]) (data))[0].equals("getMessages")) {

						ObservableList<Message> messageList = getMyMessagesFromDB(((String[]) (data))[1]);
						Object[] data = new Object[messageList.size() + 1];
						data[0] = messageList.size();
						int counter = 1;
						for (Message ms : messageList) {
							data[counter] = ms;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else if (((String[]) (data))[0].equals("Login")) {

						int k = ((String[]) (data)).length;

						Connection conn = null;
						Statement stmt = null;
						boolean Connected = false;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
							String userN = ((String[]) (data))[1];
							String passW = ((String[]) (data))[2];
							String sql = "SELECT * FROM user where userName='" + userN + "' and password='" + passW
									+ "'";
							ResultSet rs = stmt.executeQuery(sql);
							Object[] result = new Object[3];
							while (rs.next()) {

								System.out.println("Hello User");

								Connected = true;
								int id = rs.getInt("id");
								String username = rs.getString("userName");
								String password = rs.getString("password");
								String email = rs.getString("email");
								String phonenumber = rs.getString("phoneNumber");
								String firstname = rs.getString("firstName");
								String lastname = rs.getString("lastName");
								String payment = rs.getString("payment");
								String type = rs.getString("type");
								String history = rs.getString("History");
								int online = rs.getInt("online");

								User user = new User(id, firstname, lastname, email, username, password, phonenumber,
										payment, type, history);
								if (online == 0) {
									rs.updateInt("online", 1);// 1
									rs.updateRow();
								}
								result[2] = online;
								result[1] = user;

							}
							result[0] = Connected;

							stmt.close();
							conn.close();
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(result);

							objectInput.close();
							objectOutput.close();
							skt.close();
							// return data;
						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (conn != null)
									conn.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}

						/////////////////////
					} else if (((String[]) (data))[0].equals("LogOut")) {
						Connection conn = null;
						Statement stmt = null;

						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
							String userN = ((String[]) (data))[1];
							String passW = ((String[]) (data))[2];
							String sql = "SELECT * FROM user where userName='" + userN + "' and password='" + passW
									+ "'";
							ResultSet rs = stmt.executeQuery(sql);

							while (rs.next()) {
								System.out.println("Bye User");
								int online = rs.getInt("online");

								if (online == 1) {
									rs.updateInt("online", 0);
									rs.updateRow();
								}
							}

							stmt.close();
							conn.close();

						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (conn != null)
									conn.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}
					} else if (((String[]) (data))[0].equals("getUsers")) {

						ObservableList<User> userList = getUserFromDB(((String[]) (data))[1]);

						Object[] data = new Object[userList.size() + 1];
						data[0] = userList.size();
						int counter = 1;
						for (User tu : userList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (((String[]) (data))[0].equals("getCatalog")) {
						int Mode = Integer.parseInt(((String[]) (data))[1]);
						ObservableList<City> cityList = getCityFromDB(Mode);

						Object[] data = new Object[cityList.size() + 1];
						data[0] = cityList.size();
						int counter = 1;
						for (City tu : cityList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (((String[]) (data))[0].equals("getPlaceCatalog")) {
						ObservableList<Place> placeList = getPlaceFromDB1();

						Object[] data = new Object[placeList.size() + 1];
						data[0] = placeList.size();
						int counter = 1;
						for (Place tu : placeList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (((String[]) (data))[0].equals("getOPlaceCatalog")) {
						ObservableList<Place> placeList = getPlaceFromDB();

						Object[] data = new Object[placeList.size() + 1];
						data[0] = placeList.size();
						int counter = 1;
						for (Place tu : placeList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (((String[]) (data))[0].equals("getRoutes")) {
						int Mode = Integer.parseInt(((String[]) (data))[2]);
						ObservableList<Route> routeList = getRouteFromDB(((String[]) (data))[1], Mode);

						Object[] data = new Object[routeList.size() + 1];
						data[0] = routeList.size();
						int counter = 1;
						for (Route tu : routeList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (((String[]) (data))[0].equals("getMaps")) {
						int Mode = Integer.parseInt(((String[]) (data))[2]);
						ObservableList<Map> mapList = getMapFromDB(((String[]) (data))[1], Mode);

						Object[] data = new Object[mapList.size() + 1];
						data[0] = mapList.size();
						int counter = 1;
						for (Map tu : mapList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					else if (((String[]) (data))[0].equals("getMyMaps")) {
						int Mode = Integer.parseInt(((String[]) (data))[2]);
						ObservableList<Map> mapList = getMyMapsFromDB(((String[]) (data))[1], Mode);

						Object[] data = new Object[mapList.size() + 1];
						data[0] = mapList.size();
						int counter = 1;
						for (Map tu : mapList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					else if (((String[]) (data))[0].equals("getMyRoutes")) {
						int Mode = Integer.parseInt(((String[]) (data))[2]);
						ObservableList<Route> RouteList = getMyRoutesFromDB(((String[]) (data))[1], Mode);

						Object[] data = new Object[RouteList.size() + 1];
						data[0] = RouteList.size();
						int counter = 1;
						for (Route tu : RouteList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (((String[]) (data))[0].equals("getRoutePlaces")) {
						ObservableList<RoutePlace> RouteList = getRoutePlacesFromDB(
								Integer.parseInt(((String[]) (data))[1]));

						Object[] data = new Object[Math.max(RouteList.size() + 1, 2)];
						RoutePlace[] data2 = new RoutePlace[RouteList.size()];
						data[0] = RouteList.size();
						int counter = 0;
						for (RoutePlace tu : RouteList) {
							data2[counter] = tu;
							counter++;
						}
						try {
							if (data.length > 1)
								data[1] = data2;

							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else if (((String[]) (data))[0].equals("getURoutePlaces")) {
						ObservableList<URoutePlace> URouteList = getURoutePlacesFromDB(
								Integer.parseInt(((String[]) (data))[1]));

						Object[] data = new Object[Math.max(URouteList.size() + 1, 2)];
						URoutePlace[] data2 = new URoutePlace[URouteList.size()];
						data[0] = URouteList.size();
						int counter = 0;
						for (URoutePlace tu : URouteList) {
							data2[counter] = tu;
							counter++;
						}
						try {
							if (data.length > 1)
								data[1] = data2;
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else if (((String[]) (data))[0].equals("getRoutesCity")) {
						int Mode = Integer.parseInt(((String[]) (data))[2]);
						ObservableList<Route> userList = getRoutesFromDB(((String[]) (data))[1], Mode);

						Object[] data = new Object[userList.size() + 1];
						data[0] = userList.size();
						int counter = 1;
						for (Route tu : userList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (((String[]) (data))[0].equals("getFixedPurchase")) {
						ObservableList<FixedPurchase> FixedPurchaseList = getFixedPurchaseFromDB(
								((String[]) (data))[1]);

						Object[] data = new Object[FixedPurchaseList.size() + 1];
						data[0] = FixedPurchaseList.size();
						int counter = 1;
						for (FixedPurchase tu : FixedPurchaseList) {
							data[counter] = tu;
							counter++;
						}
						try {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (((String[]) (data))[0].equals("checkCityExist")) {

						Connection conn = null;
						Statement stmt = null;
						String user = ((String[]) (data))[1];
						String city = ((String[]) (data))[2];

						try {
							Class.forName(JDBC_DRIVER);
							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();

							String sql = "SELECT * FROM fixedPurchase where user ='" + user + "' and city ='" + city
									+ "'";
							ResultSet rs = stmt.executeQuery(sql);
							if (!rs.next()) {
								stmt.close();
								conn.close();

								ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
								objectOutput.writeObject("No");
							} else {
								stmt.close();
								conn.close();
								ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
								objectOutput.writeObject("Yes");
							}

						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (conn != null)
									conn.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}
					}

					else if (((String[]) (data))[0].equals("getPlaces")) {
						Place[] list, list2, list3;
						int k = ((String[]) (data)).length;

						Connection conn = null;
						Statement stmt = null;
						boolean Connected = false;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							String mapid = null, City = null;

							stmt = conn.createStatement();
							if (((String[]) data)[1] != null)
								mapid = ((String[]) (data))[1];
							if (((String[]) data)[1] != null)
								City = ((String[]) (data))[2];
							String type = ((String[]) (data))[3];

							String sql = "SELECT * FROM places where Name='" + City + "'";
							if (((String[]) data)[1] != null)
								sql = "SELECT * FROM places";
							ResultSet rs = stmt.executeQuery(sql);

							int index = 0;
							while (rs.next()) {
								index++;
							}
							rs.first();
							rs.previous();
							Object[] result = new Object[2];
							list = new Place[index];
							index = 0;
							while (rs.next()) {

								Connected = true;

								int id = rs.getInt("id");
								String MapId = rs.getString("MapId");
								String Name = rs.getString("Name");
								String Place = rs.getString("Place");
								String description = rs.getString("description");
								String classification = rs.getString("classification");
								int accessibility = rs.getInt("accessibility");

								Point p = new Point(0, 0);// GetLocationFromDB(Integer.parseInt(MapId), Place);
								int LocX = (int) p.getX();
								int LocY = (int) p.getY();
								// rs.getString("pathNum");

								// data.add(new User(username, description, mapsnum , placesnum, pathnum ));
								Place place = new Place(MapId, Name, Place, description, classification, accessibility,
										id, LocX, LocY);
								list[index] = (place);
								index++;

							}
							String sql2 = "SELECT * FROM placeMap where mapID=" + mapid;
							if (mapid == null)
								sql2 = "SELECT * FROM placeMap";
							ResultSet rs2 = stmt.executeQuery(sql2);
							index = 0;
							while (rs2.next()) {
								index++;
							}
							rs2.first();
							rs2.previous();
							Object[] result2 = new Object[2];
							list2 = new Place[index];
							index = 0;
							while (rs2.next()) {

								Connected = true;

								int id = rs2.getInt("id");
								String MapId = rs2.getString("MapId");
								String Name = City;
								String Place = rs2.getString("Name");
								String description = "";
								String classification = "";
								int accessibility = 0;

								// Point p = GetLocationFromDB(Integer.parseInt(MapId), Place);
								int LocX = rs2.getInt("LocX");
								int LocY = rs2.getInt("LocY");
								// rs.getString("pathNum");

								// data.add(new User(username, description, mapsnum , placesnum, pathnum ));
								Place place = new Place(MapId, Name, Place, description, classification, accessibility,
										id, LocX, LocY);
								list2[index] = (place);
								index++;
							}
							list3 = new Place[list2.length];
							for (int i = 0; i < list2.length; i++) {
								for (int c = 0; c < list.length; c++) {
									if (list2[i].getPlaceName().equals(list[c].getPlaceName())) {

										list3[i] = new Place(list2[i].getMapId(), list[c].getCityName(),
												list[c].getPlaceName(), list[c].getDescription(),
												list[c].getClassification(), list[c].getAccessibility(),
												list[c].getSerialID(), list2[i].getLocX(), list2[i].getLocY());
									}

								}

							}

							result[0] = Connected;
							result[1] = list3;

							if (type.equals("member")) {
								statics("view", City, null);
							}

							stmt.close();
							conn.close();
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(result);

							// objectInput.close();
							// objectOutput.close();
							skt.close();
							// return data;
						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (conn != null)
									conn.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}

					} else if (((String[]) (data))[0].equals("getPlacesForRoutes")) {

						Place[] list;
						int k = ((String[]) (data)).length;

						Connection conn = null;
						Statement stmt = null;
						boolean Connected = false;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();
							String name = ((String[]) (data))[1];
							String sql = "SELECT * FROM places where Name='" + name + "'";
							ResultSet rs = stmt.executeQuery(sql);
							int index = 0;
							while (rs.next()) {
								index++;
							}
							rs.first();
							rs.previous();
							Object[] result = new Object[2];
							list = new Place[index];
							index = 0;
							while (rs.next()) {

								Connected = true;

								int id = rs.getInt("id");
								String MapId = rs.getString("MapId");
								String Name = rs.getString("Name");
								String Place = rs.getString("Place");
								String description = rs.getString("description");
								String classification = rs.getString("classification");
								int accessibility = rs.getInt("accessibility");

								Point p = GetLocationFromDB(Integer.parseInt(MapId), Place);
								int LocX = (int) p.getX();
								int LocY = (int) p.getY();
								// rs.getString("pathNum");

								// data.add(new User(username, description, mapsnum , placesnum, pathnum ));
								Place place = new Place(MapId, Name, Place, description, classification, accessibility,
										id, LocX, LocY);
								list[index] = (place);
								index++;

							}
							result[0] = Connected;
							result[1] = list;

							stmt.close();
							conn.close();
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(result);

							// objectInput.close();
							// objectOutput.close();
							skt.close();
							// return data;
						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (conn != null)
									conn.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}

					} else if (((String[]) (data))[0].equals("getUPlaces")) {

						UPlace[] list;
						int k = ((String[]) (data)).length;

						Connection conn = null;
						Statement stmt = null;
						boolean Connected = false;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();
							String mapid = ((String[]) (data))[1];
							String sql = "SELECT * FROM Updates where MapId='" + mapid + "'";
							ResultSet rs = stmt.executeQuery(sql);
							int index = 0;
							while (rs.next()) {
								index++;
							}
							rs.first();
							rs.previous();
							Object[] result = new Object[2];
							list = new UPlace[index];
							index = 0;
							while (rs.next()) {

								Connected = true;

								int id = rs.getInt("id");
								String MapId = rs.getString("MapId");
								int PlaceId = rs.getInt("PlaceId");
								String Name = rs.getString("Name");
								String Place = rs.getString("Place");
								String description = rs.getString("description");
								String classification = rs.getString("classification");
								int accessibility = rs.getInt("accessibility");
								int LocX = rs.getInt("LocX");
								int LocY = rs.getInt("LocY");
								String Type = rs.getString("Type");
								// rs.getString("pathNum");
								int mapsNum = rs.getInt("mapsNum");
								// data.add(new User(username, description, mapsnum , placesnum, pathnum ));
								UPlace place = new UPlace(MapId, Name, Place, description, classification,
										accessibility, id, LocX, LocY, Type, mapsNum, PlaceId);
								list[index] = (place);
								index++;

							}
							result[0] = Connected;
							result[1] = list;

							stmt.close();
							conn.close();
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(result);

							// objectInput.close();
							// objectOutput.close();
							skt.close();
							// return data;
						} catch (SQLException se) {
							se.printStackTrace();
							System.out.println("SQLException: " + se.getMessage());
							System.out.println("SQLState: " + se.getSQLState());
							System.out.println("VendorError: " + se.getErrorCode());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (conn != null)
									conn.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}

					}

				}

			}
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

	static ObservableList<FixedPurchase> getFixedPurchaseFromDB(String user) {
		ObservableList<FixedPurchase> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

			String sql = "SELECT * FROM fixedPurchase WHERE user ='" + user + "'";
			ResultSet rs = stmt.executeQuery(sql);
			java.util.Date currentDate = Calendar.getInstance().getTime();

			while (rs.next()) {
				String city = rs.getString("city");
				java.sql.Date startDate = rs.getDate("startDate");
				java.sql.Date endDate = rs.getDate("endDate");
				int period = rs.getInt("period");
				double price = rs.getDouble("purchaseprice");
				if (currentDate.compareTo(endDate) > 0) {
					System.out.println(currentDate);
					rs.deleteRow();
				} else
					data.add(new FixedPurchase(user, period, city, startDate, endDate, price));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static Point GetLocationFromDB(int MapId, String Name) {
		ObservableList<FixedPurchase> data = FXCollections.observableArrayList();
		Point p = new Point();
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

			String sql = "SELECT * FROM placeMap WHERE Name ='" + Name + "' AND MapID=" + MapId;
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int X = rs.getInt("LocX");
				int Y = rs.getInt("LocY");
				p.setLocation(X, Y);
			}

			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return p;
	}

	static ObservableList<City> getCityFromDB(int Mode) {
		ObservableList<City> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM CityCatalog WHERE NewUpdate >=" + Mode;
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String city = rs.getString("name");
				String description = rs.getString("description");
				String mapsnum = rs.getString("mapsNum");
				String placesnum = rs.getString("placesNum");
				String pathnum = rs.getString("pathNum");
				int oneTimeCost = rs.getInt("oneTimeCost");
				int FixedCost = rs.getInt("FixedCost");
				int Version = rs.getInt("version");
				String places = "";
				int NewUpdate = rs.getInt("NewUpdate");
				String sql2 = "SELECT * FROM places WHERE Name ='" + city + "'";
				ResultSet rs2 = stmt2.executeQuery(sql2);
				Boolean VersionUpdate = rs.getBoolean("VersionUpdate");
				while (rs2.next()) {
					String place = rs2.getString("Place");
					places += (" + " + place);
				}

				data.add(new City(city, description, mapsnum, placesnum, pathnum, places, oneTimeCost, FixedCost,
						Version, NewUpdate, VersionUpdate));

			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static ObservableList<Place> getPlaceFromDB() {
		ObservableList<Place> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			String sql = "SELECT * FROM places";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String city = rs.getString("Name");
				String description = rs.getString("description");
				String place = rs.getString("Place");
				String Classification = rs.getString("Classification");
				int Accessibility = rs.getInt("Accessibility");
				int numOfMaps = rs.getInt("mapsNum");

				data.add(new Place(city, place, description, Classification, Accessibility, numOfMaps));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static ObservableList<Place> getPlaceFromDB1() {
		ObservableList<Place> data = FXCollections.observableArrayList();
		ObservableList<Place> data3 = FXCollections.observableArrayList();
		Place[] list2;
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			String sql = "SELECT * FROM places";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String city = rs.getString("Name");
				String description = rs.getString("description");
				String place = rs.getString("Place");
				String Classification = rs.getString("Classification");
				int Accessibility = rs.getInt("Accessibility");
				int numOfMaps = rs.getInt("mapsNum");
				Place p = new Place(city, place, description, Classification, Accessibility, numOfMaps);
				data.add(p);
			}

			String sql2 = "SELECT * FROM placeMap";
			ResultSet rs2 = stmt.executeQuery(sql2);
			int index = 0;
			while (rs2.next()) {
				index++;
			}
			rs2.first();
			rs2.previous();
			Object[] result2 = new Object[2];
			list2 = new Place[index];
			index = 0;
			while (rs2.next()) {

				// Connected = true;

				int id = rs2.getInt("id");
				String MapId = rs2.getString("MapId");
				String Name = "";
				String Place = rs2.getString("Name");
				String description = "";
				String classification = "";
				int accessibility = 0;

				// Point p = GetLocationFromDB(Integer.parseInt(MapId), Place);
				int LocX = rs2.getInt("LocX");
				int LocY = rs2.getInt("LocY");
				// rs.getString("pathNum");

				// data.add(new User(username, description, mapsnum , placesnum, pathnum ));
				Place place = new Place(MapId, Name, Place, description, classification, accessibility, id, LocX, LocY);
				list2[index] = (place);
				index++;
			}
			Place[] list3 = new Place[list2.length];
			for (int i = 0; i < list2.length; i++) {

				for (Place place : data) {
					if (list2[i].getPlaceName().equals(place.getPlaceName())) {

						data3.add(new Place(list2[i].getMapId(), place.getCityName(), place.getPlaceName(),
								place.getDescription(), place.getClassification(), place.getAccessibility(),
								place.getSerialID(), list2[i].getLocX(), list2[i].getLocY()));
					}

				}

			}

			// result[0] = true;
			// result[1] = list3;
			stmt.close();
			conn.close();

			return data3;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static ObservableList<Route> getRouteFromDB(String city, int Mode) {
		ObservableList<Route> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			String sql = "SELECT * FROM Routes WHERE city ='" + city + "' AND NewUpdate >=" + Mode;
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				// String city = rs.getString("city");
				String description = rs.getString("description");
				String link = rs.getString("link");
				int NewUpdate = rs.getInt("NewUpdate");

				data.add(new Route(id, city, description, link, NewUpdate));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static ObservableList<Map> getMapFromDB(String city, int Mode) {
		ObservableList<Map> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			String sql = "SELECT * FROM maps WHERE city ='" + city + "' AND NewUpdate >=" + Mode;
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				// String city = rs.getString("city");
				String description = rs.getString("description");
				String linkC = rs.getString("linkCustomer");
				String linkE = rs.getString("linkEmployee");
				int NewUpdate = rs.getInt("NewUpdate");

				data.add(new Map(id, city, description, linkC, linkE, NewUpdate));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static ObservableList<Map> getMyMapsFromDB(String city, int Mode) {
		ObservableList<Map> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM maps WHERE city ='" + city + "' AND NewUpdate >=" + Mode;
			ResultSet rs = stmt2.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String description = rs.getString("description");
				String linkC = rs.getString("linkCustomer");
				String linkE = rs.getString("linkEmployee");

				int NewUpdate = rs.getInt("NewUpdate");
				data.add(new Map(id, city, description, linkC, linkE, NewUpdate));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static ObservableList<Route> getMyRoutesFromDB(String city, int Mode) {
		ObservableList<Route> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM Routes WHERE city ='" + city + "' AND NewUpdate >=" + Mode;
			ResultSet rs = stmt2.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String description = rs.getString("description");
				String link = rs.getString("link");
				int NewUpdate = rs.getInt("NewUpdate");

				data.add(new Route(id, city, description, link, NewUpdate));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static ObservableList<RoutePlace> getRoutePlacesFromDB(int routeid) {
		ObservableList<RoutePlace> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM RoutePlaces WHERE RouteId =" + routeid;
			ResultSet rs = stmt2.executeQuery(sql);

			while (rs.next()) {
				String place = rs.getString("place");
				int time = rs.getInt("time");
				int LocX = rs.getInt("LocX");
				int LocY = rs.getInt("LocY");
				long serialid = rs.getInt("id");
				data.add(new RoutePlace(routeid, place, time, LocX, LocY, serialid));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static ObservableList<URoutePlace> getURoutePlacesFromDB(int id) {
		ObservableList<URoutePlace> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM RoutesUpdates WHERE RouteId =" + id;
			ResultSet rs = stmt2.executeQuery(sql);

			while (rs.next()) {
				String place = rs.getString("place");
				int time = rs.getInt("time");
				int URPID = rs.getInt("RPlaceId");
				int LocX = rs.getInt("LocX");
				int LocY = rs.getInt("LocY");
				long serialID = rs.getInt("id");
				String Type = rs.getString("Type");
				data.add(new URoutePlace(id, URPID, place, time, LocX, LocY, serialID, Type));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static UPlace[] getPlaces(String MapId) {
		Connection conn = null;
		Statement stmt = null;
		UPlace[] list;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			String sql = "SELECT * FROM Updates WHERE MapId=" + MapId;
			ResultSet rs = stmt.executeQuery(sql);

			int index = 0;
			while (rs.next()) {
				index++;
			}
			list = new UPlace[index];
			index = 0;
			rs.first();
			rs.previous();
			while (rs.next()) {
				long id = rs.getInt("id");
				String city = rs.getString("Name");
				String description = rs.getString("description");
				String place = rs.getString("Place");
				String Classification = rs.getString("Classification");
				int Accessibility = rs.getInt("Accessibility");
				int palceid = rs.getInt("PlaceId");
				// int numOfMaps = rs.getInt("mapsNum");
				// Point p = GetLocationFromDB(Integer.parseInt(MapId), place);
				int LocX = rs.getInt("LocX");
				int LocY = rs.getInt("LocY");
				String Type = rs.getString("Type");
				int mapsNum = rs.getInt("mapsNum");
				list[index] = new UPlace(MapId, city, place, description, Classification, Accessibility, id, LocX, LocY,
						Type, mapsNum, palceid);
				index++;
			}

			stmt.close();
			conn.close();

			return list;

		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return null;

	}

	public static int checkuser(String user2, Connection conn) {
		Statement pr;

		ResultSet rs = null;
		int a = 0;
		String sql = "SELECT * FROM user WHERE userName ='" + user2 + "'";
		// Connection conn=connecttion();
		if (conn != null) {
			try {
				pr = conn.createStatement();

				rs = pr.executeQuery(sql);

				// pr.setString(1, user2);

				// rs=pr.executeQuery();
				// rs.next();
				// System.out.println(rs.getObject(1));
				if (rs.next()) {
					// System.out.println(user2);
					// rs.next();
					// System.out.println(rs.getString("first name"));
					a = 1;
					/*
					 * if(rs.next()) { //System.out.println(user2); a=1; }
					 */
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return a;
	}

	public static void checkurplace(RoutePlace place, Connection conn, String Type) {
		Statement pr;
		Statement pr2;

		ResultSet rs = null;
		int a = 0;

		String sqlfind = "";
		String sql = "";
		if (Type.equals("NOP")) {
			if (place instanceof URoutePlace) {
				sqlfind = "Select * FROM RoutesUpdates WHERE id ='" + ((URoutePlace) place).getSerialID() + "'";
				sql = "DELETE FROM RoutesUpdates WHERE id ='" + ((URoutePlace) place).getSerialID() + "'";
			} else
				return;

		} else if (Type.equals("YESP")) {
			if (place instanceof URoutePlace) {
				sqlfind = "Select * FROM RoutesUpdates WHERE id ='" + ((RoutePlace) place).getSerialID() + "'";
				sql = "DELETE FROM RoutesUpdates WHERE id ='" + ((RoutePlace) place).getSerialID() + "'";
			} else {
				sqlfind = "Select * FROM RoutesUpdates WHERE RPlaceId ='" + ((RoutePlace) place).getSerialID() + "'";
				sql = "DELETE FROM RoutesUpdates WHERE RPlaceId ='" + ((RoutePlace) place).getSerialID() + "'";
			}
		}
		// Connection conn=connecttion();
		if (conn != null) {
			try {
				pr = conn.createStatement();
				rs = pr.executeQuery(sqlfind);
				if (rs.next()) {
					pr2 = conn.createStatement();
					int rs2 = pr2.executeUpdate(sql);
					rs2++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void checkuplace(Place place, Connection conn, String Type) {
		Statement pr;
		Statement pr2;

		ResultSet rs = null;
		int a = 0;

		String sqlfind = "";
		String sql = "";
		if (Type.equals("NOP")) {
			if (place instanceof UPlace) {
				sqlfind = "Select * FROM Updates WHERE id ='" + ((UPlace) place).getSerialID() + "'";
				sql = "DELETE FROM Updates WHERE id ='" + ((UPlace) place).getSerialID() + "'";
			} else
				return;

		} else if (Type.equals("YESP")) {
			if (place instanceof UPlace) {
				sqlfind = "Select * FROM Updates WHERE id ='" + ((Place) place).getSerialID() + "'";
				sql = "DELETE FROM Updates WHERE id ='" + ((Place) place).getSerialID() + "'";
			} else {
				sqlfind = "Select * FROM Updates WHERE PlaceId ='" + ((Place) place).getSerialID() + "'";
				sql = "DELETE FROM Updates WHERE PlaceId ='" + ((Place) place).getSerialID() + "'";
			}
		}
		// Connection conn=connecttion();
		if (conn != null) {
			try {
				pr = conn.createStatement();
				rs = pr.executeQuery(sqlfind);
				if (rs.next()) {
					pr2 = conn.createStatement();
					int rs2 = pr2.executeUpdate(sql);
					rs2++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void DeleteUpdates(String MapId) throws SQLException {
		Statement pr;
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		ResultSet rs = null;

		String sql = "";
		sql = "DELETE FROM Updates WHERE MapId ='" + MapId + "'";

		// Connection conn=connecttion();
		if (conn != null) {
			try {
				pr = conn.createStatement();
				int k = pr.executeUpdate(sql);
				if (k > 0) {

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void DeleteRUpdates(int RouteId) throws SQLException {
		Statement pr;
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		ResultSet rs = null;

		String sql = "";
		sql = "DELETE FROM RoutesUpdates WHERE RouteId =" + RouteId;

		// Connection conn=connecttion();
		if (conn != null) {
			try {
				pr = conn.createStatement();
				int k = pr.executeUpdate(sql);
				if (k > 0) {

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void DeleteNewUpdates(String MapId) throws SQLException {
		Statement pr, pr1, pr2;
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		ResultSet rs = null;
		ResultSet rs2 = null;

		String sql = "", sql2 = "", sqlfind = "", sqlfind2 = "";
		sql = "UPDATE maps SET NewUpdate=" + false + " WHERE id=" + MapId;
		sql2 = "UPDATE CityCatalog SET NewUpdate=" + false + " WHERE Name='";
		sqlfind = "Select * FROM maps WHERE id ='" + MapId + "'";
		sqlfind2 = "Select * FROM Routes Where city='";
		// Connection conn=connecttion();
		int counter = 0;
		if (conn != null) {
			try {
				pr = conn.createStatement();
				int k = pr.executeUpdate(sql);
				if (k > 0) {
					pr1 = conn.createStatement();
					rs = pr1.executeQuery(sqlfind);
					String city = "";
					while (rs.next()) {
						counter += Math.abs(rs.getInt("NewUpdate"));
						city = rs.getString("city");
					}
					sqlfind2 += city + "'";
					rs2 = pr1.executeQuery(sqlfind2);
					while (rs2.next()) {
						counter += Math.abs(rs2.getInt("NewUpdate"));
					}
					if (counter == 0) {
						sql2 += city + "'";
						pr2 = conn.createStatement();
						pr2.executeUpdate(sql2);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void DeleteRNewUpdates(int RouteId) throws SQLException {
		Statement pr, pr1, pr2;
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		ResultSet rs = null;
		ResultSet rs2 = null;

		String sql = "", sql2 = "", sqlfind = "", sqlfind2 = "";
		sql = "UPDATE Routes SET NewUpdate=" + false + " WHERE id=" + RouteId;
		sql2 = "UPDATE CityCatalog SET NewUpdate=" + false + " WHERE Name='";
		sqlfind = "Select * FROM Routes WHERE id ='" + RouteId + "'";
		sqlfind2 = "Select * FROM maps Where city='";
		// Connection conn=connecttion();
		int counter = 0;
		if (conn != null) {
			try {
				pr = conn.createStatement();
				int k = pr.executeUpdate(sql);
				if (k > 0) {
					pr1 = conn.createStatement();
					rs = pr1.executeQuery(sqlfind);
					String city = "";
					while (rs.next()) {
						counter += Math.abs(rs.getInt("NewUpdate"));
						city = rs.getString("city");
					}
					sqlfind2 += city + "'";
					rs2 = pr1.executeQuery(sqlfind2);
					while (rs2.next()) {
						counter += Math.abs(rs2.getInt("NewUpdate"));
					}
					if (counter == 0) {
						sql2 += city + "'";
						pr2 = conn.createStatement();
						pr2.executeUpdate(sql2);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static String checkplace(Place place, Connection conn, String Type) {
		Statement pr;

		ResultSet rs = null;

		String sqlfind = "";
		if (Type.equals("NOP")) {
			if (place instanceof UPlace) {
				sqlfind = "Select * FROM places WHERE id ='" + ((UPlace) place).getSerialID() + "'";
			} else
				return "Error";

		} else if (Type.equals("YESP")) {
			if (place instanceof UPlace) {
				sqlfind = "Select * FROM places WHERE id ='" + ((Place) place).getSerialID() + "'";

				sqlfind = "Select * FROM places WHERE PlaceId ='" + ((Place) place).getSerialID() + "'";
			}
		}
		// Connection conn=connecttion();
		if (conn != null) {
			try {
				pr = conn.createStatement();
				rs = pr.executeQuery(sqlfind);
				if (rs.next()) {
					return "UPDATE";
				}
				return "NEW";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "Error";
	}

	public static void SetUpdateForRoute(int routeid, Connection conn) {
		Statement pr;

		ResultSet rs = null;

		String sqlfind = "";

		sqlfind = "UPDATE Routes SET NewUpdate=" + true + " WHERE id=" + routeid;

		// Connection conn=connecttion();
		if (conn != null) {
			try {
				pr = conn.createStatement();
				int k = pr.executeUpdate(sqlfind);
				if (k > 0) {
					System.out.println("Thanks For Route NewUpdate");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void SetUpdateForMap(int mapid, Connection conn) {
		Statement pr;

		ResultSet rs = null;

		String sqlfind = "";

		sqlfind = "UPDATE maps SET NewUpdate=" + true + " WHERE id=" + mapid;

		// Connection conn=connecttion();
		if (conn != null) {
			try {
				pr = conn.createStatement();
				int k = pr.executeUpdate(sqlfind);
				if (k > 0) {
					System.out.println("Thanks For Map NewUpdate");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void SetUpdateForCity(String cityName, Connection conn) {
		Statement pr;

		ResultSet rs = null;

		String sqlfind = "";

		sqlfind = "UPDATE CityCatalog SET NewUpdate=" + true + " WHERE name='" + cityName + "'";

		// Connection conn=connecttion();
		if (conn != null) {
			try {
				pr = conn.createStatement();
				int k = pr.executeUpdate(sqlfind);
				if (k > 0) {
					System.out.println("Thanks For City NewUpdate");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static int getPlaceMapId(String name, Connection conn, String Mapid) {
		Statement pr;

		ResultSet rs = null;

		String sqlfind = "";

		sqlfind = "Select * FROM placeMap WHERE Name ='" + name + "' AND mapID=" + Mapid;

		if (conn != null) {
			try {
				pr = conn.createStatement();
				rs = pr.executeQuery(sqlfind);
				if (rs.next()) {
					return rs.getInt("id");
				}
				return -1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return -1;
	}

	static ObservableList<User> getUserFromDB(String Type) {
		ObservableList<User> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM user WHERE type='" + Type + "'";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String fname = rs.getString("firstName");
				String lname = rs.getString("lastName");
				String pnumber = rs.getString("phoneNumber");
				String email = rs.getString("email");
				String username = rs.getString("userName");
				String password = rs.getString("password");
				String payment = rs.getString("payment");
				String type = rs.getString("type");
				String history = rs.getString("History");

				data.add(new User(id, fname, lname, email, username, password, pnumber, payment, type, history));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static void AddCityIfNotExist(Map map, Connection conn, String CityDes) {

		Statement pr;
		PreparedStatement pr2;

		ResultSet rs = null;

		String sqlfind = "";
		String sql = "INSERT INTO CityCatalog(`name`, `description`, mapsNum, placesNum, pathNum, version, fixedCost, oneTimeCost, NewUpdate, VersionUpdate) VALUES (?,?,?,?,?,?,?,?,?,?)";
		sqlfind = "Select * FROM CityCatalog WHERE name ='" + map.getCity() + "'";

		if (conn != null) {
			try {
				pr = conn.createStatement();
				rs = pr.executeQuery(sqlfind);
				if (!rs.next()) {
					pr2 = conn.prepareStatement(sql);
					pr2.setString(1, map.getCity());
					pr2.setString(2, CityDes);
					pr2.setInt(3, 1);
					pr2.setInt(4, 0);
					pr2.setInt(5, 0);
					pr2.setInt(6, 1);
					pr2.setInt(7, 0);
					pr2.setInt(8, 0);
					pr2.setInt(9, -1);
					pr2.setBoolean(10, false);

					int k = pr2.executeUpdate();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	static void AddCityIfNotExist(Route route, Connection conn, String CityDes) {

		Statement pr;
		PreparedStatement pr2;

		ResultSet rs = null;

		String sqlfind = "";
		String sql = "INSERT INTO CityCatalog(`name`, `description`, mapsNum, placesNum, pathNum, version, fixedCost, oneTimeCost, NewUpdate, VersionUpdate) VALUES (?,?,?,?,?,?,?,?,?,?)";
		sqlfind = "Select * FROM CityCatalog WHERE name ='" + route.getCity() + "'";

		if (conn != null) {
			try {
				pr = conn.createStatement();
				rs = pr.executeQuery(sqlfind);
				if (!rs.next()) {
					pr2 = conn.prepareStatement(sql);
					pr2.setString(1, route.getCity());
					pr2.setString(2, CityDes);
					pr2.setInt(3, 1);
					pr2.setInt(4, 0);
					pr2.setInt(5, 0);
					pr2.setInt(6, 1);
					pr2.setInt(7, 0);
					pr2.setInt(8, 0);
					pr2.setInt(9, -1);
					pr2.setBoolean(10, false);

					int k = pr2.executeUpdate();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	static ObservableList<NewPrices> getMyNewPricesFromDB(String ncity) {
		ObservableList<NewPrices> data = FXCollections.observableArrayList();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "SELECT * FROM newprices where city='" + ncity + "'";
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String city = rs.getString("city");
				int fcost = rs.getInt("fixedcost");
				int otcost = rs.getInt("onetimecost");
				data.add(new NewPrices(city, fcost, otcost));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	static Reports getReportFromDB(Reports rep) {
		Reports rep1 = null;
		Connection conn = null;
		Statement stmt = null;
		Statement stmt1 = null;
		String cname = rep.getCity();
		java.sql.Date sdate = rep.getStartDate();
		java.sql.Date edate = rep.getEndDate();
		try {
			Class.forName(JDBC_DRIVER);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String f = "fixedpurchase";
		String sql = "SELECT * FROM staticinformation where city ='" + cname + "' and date >='" + sdate
				+ "' and date <='" + edate + "' ";
		String sql1 = "SELECT * FROM staticinformation where city ='" + cname + "' and type ='" + f + "' ";
		try {
			ResultSet rs = stmt.executeQuery(sql);
			ResultSet rs1 = stmt1.executeQuery(sql1);
			int nmap = 0;
			int nfixed = 0;
			int numotpurchase = 0;
			int numrenews = 0;
			int numviews = 0;
			int numdownloads = 0;
			while (rs1.next()) {
				if (rs1.getDate("date").compareTo(sdate) >= 0 && rs1.getDate("date").compareTo(edate) <= 0) {
					nfixed++;
				} else if (rs1.getDate("enddate").compareTo(sdate) >= 0
						&& rs1.getDate("enddate").compareTo(edate) <= 0) {
					nfixed++;
				} else if (rs1.getDate("enddate").compareTo(edate) >= 0 && rs1.getDate("date").compareTo(sdate) <= 0) {
					nfixed++;
				}
			}
			while (rs.next()) {
				if (rs.getString("type").equals("map")) {
					nmap++;
				}

				else if (rs.getString("type").equals("onetimepurchase")) {
					numotpurchase++;
				} else if (rs.getString("type").equals("renew")) {
					numrenews++;
				} else if (rs.getString("type").equals("view")) {
					numviews++;
				} else if (rs.getString("type").equals("download")) {
					numdownloads++;
				}
			}
			rep1 = new Reports(cname, nmap, numotpurchase, nfixed, numrenews, numviews, numdownloads, sdate, edate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt.close();
			stmt1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rep1;

	}

	static ObservableList<Reports> getReportFromDB1(Reports rep) {

		ObservableList<Reports> data = FXCollections.observableArrayList();
		Connection conn = null;
		Statement stmt = null;

		java.sql.Date sdate = rep.getStartDate();
		java.sql.Date edate = rep.getEndDate();
		try {
			Class.forName(JDBC_DRIVER);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt = conn.createStatement();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "SELECT * FROM staticinformation  where  date >='" + sdate + "' and date <='" + edate + "' ";

		try {
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				int nmap = 0;
				int nfixed = 0;
				int numotpurchase = 0;
				int numrenews = 0;
				int numviews = 0;
				int numdownloads = 0;
				String cname = rs.getString("city");
				if (rs.getString("type").equals("map")) {
					nmap++;
				} else if (rs.getString("type").equals("fixedpurchase")) {
					if (rs.getDate("date").compareTo(sdate) >= 0 && rs.getDate("date").compareTo(edate) <= 0) {
						nfixed++;
					} else if (rs.getDate("enddate").compareTo(sdate) >= 0
							&& rs.getDate("enddate").compareTo(edate) <= 0) {
						nfixed++;
					} else if (rs.getDate("enddate").compareTo(edate) >= 0
							&& rs.getDate("date").compareTo(sdate) <= 0) {
						nfixed++;
					}
				} else if (rs.getString("type").equals("onetimepurchase")) {
					numotpurchase++;
				} else if (rs.getString("type").equals("renew")) {
					numrenews++;
				} else if (rs.getString("type").equals("view")) {
					numviews++;
				} else if (rs.getString("type").equals("download")) {
					numdownloads++;
				}
				data.add(new Reports(cname, nmap, numotpurchase, nfixed, numrenews, numviews, numdownloads, sdate,
						edate));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	static ObservableList<Message> getMyMessagesFromDB(String user) throws SQLException {

		ObservableList<Message> data = FXCollections.observableArrayList();
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName(JDBC_DRIVER);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "SELECT * FROM messages where userName='" + user + "'";
		String sql1 = "SELECT * FROM messages where userName='ALL'";
		String sql2 = "SELECT * FROM fixedPurchase where user='" + user + "'";

		ResultSet rs1 = stmt.executeQuery(sql1);

		ResultSet rs2 = stmt2.executeQuery(sql2);
		while (rs2.next()) {
			String City = rs2.getString("city");
			while (rs1.next()) {
				String mss = rs1.getString("message");
				String tmss = rs1.getString("typemessage");
				Date d1 = rs1.getDate("Datesend");
				if (mss.indexOf(City) >= 0)
					data.add(new Message(user, mss, d1, tmss));
			}
			rs1.first();
			rs1.previous();
		}

		try {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String mss = rs.getString("message");
				String tmss = rs.getString("typemessage");
				Date d1 = rs.getDate("Datesend");
				data.add(new Message(user, mss, d1, tmss));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	public static void AddCityNumberOfRoutes(String City, Connection conn) {
		Statement stmt;

		// Connection conn=connecttion();
		if (conn != null) {
			try {

				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "SELECT * FROM CityCatalog where name='" + City + "'";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					rs.updateInt("pathNum", rs.getInt("pathNum") + 1);// 1
					rs.updateRow();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void AddMapNumberOfPlace(String Place, Connection conn) {
		Statement stmt;

		// Connection conn=connecttion();
		if (conn != null) {
			try {

				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "SELECT * FROM places where Place='" + Place + "'";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					rs.updateInt("mapsNum", rs.getInt("mapsNum") + 1);// 1
					rs.updateRow();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void AddCityNumberOfPlaces(String City, Connection conn) {
		Statement stmt;

		// Connection conn=connecttion();
		if (conn != null) {
			try {

				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "SELECT * FROM CityCatalog where name='" + City + "'";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					rs.updateInt("placesNum", rs.getInt("placesNum") + 1);// 1
					rs.updateRow();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void RemoveCityNumberOfPlaces(String City, Connection conn) {
		Statement stmt;

		// Connection conn=connecttion();
		if (conn != null) {
			try {

				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "SELECT * FROM CityCatalog where name='" + City + "'";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					rs.updateInt("placesNum", rs.getInt("placesNum") - 1);// 1
					rs.updateRow();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void AddCityNumberOfMaps(String City, Connection conn) {
		Statement stmt;

		// Connection conn=connecttion();
		if (conn != null) {
			try {

				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "SELECT * FROM CityCatalog where name='" + City + "'";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					rs.updateInt("mapsNum", rs.getInt("mapsNum") + 1);// 1
					rs.updateRow();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	static void AddPurchaseToHistory(String city, int version, String user, String Type, String date, Connection conn) {

		Statement pr;
		PreparedStatement pr2;

		String sqlfind = "";
		String sql = "UPDATE user SET History='";
		sqlfind = "Select * FROM user WHERE userName='" + user + "'";

		if (conn != null) {
			try {
				pr = conn.createStatement();
				ResultSet rs = pr.executeQuery(sqlfind);
				while (rs.next()) {
					String History = rs.getString("History");
					if (History.equals("")) {
						History = city + "," + Type + "," + version + "," + date;
					} else {
						History += "#" + city + "," + Type + "," + version + "," + date;
					}
					sql += History + "' WHERE userName='" + user + "'";
					pr2 = conn.prepareStatement(sql);
					int k = pr2.executeUpdate();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	static ObservableList<Route> getRoutesFromDB(String city, int Mode) {
		ObservableList<Route> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM Routes Where city='" + city + "' AND NewUpdate >=" + Mode;
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String description = rs.getString("description");
				String link = rs.getString("link");
				int NewUpdate = rs.getInt("NewUpdate");

				data.add(new Route(id, city, description, link, NewUpdate));
			}

			stmt.close();
			conn.close();

			return data;
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return data;
	}

	static int checkthesystem() {
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			String sql = "SELECT * FROM checksystem";
			ResultSet rs = stmt.executeQuery(sql);
			LocalDate sd = LocalDate.now();
			Calendar c = Calendar.getInstance();
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String stringdate = sd.format(dateFormatter);
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date = sdf1.parse(stringdate);
			java.util.Date date1 = sdf1.parse(stringdate);
			c.setTime(date1);
			c.add(Calendar.DATE, 1);
			String output = sdf1.format(c.getTime());
			date1 = sdf1.parse(output);
			java.sql.Date datenow = new java.sql.Date(date.getTime());
			while (rs.next()) {
				if (rs.getDate("date").compareTo(datenow) < 0) {
					PreparedStatement pr;
					String sql1 = "UPDATE checksystem SET date = ?";
					if (conn != null) {
						pr = conn.prepareStatement(sql1);
						java.sql.Date datenow1 = new java.sql.Date(date1.getTime());
						;

						pr.setDate(1, datenow1);

						pr.executeUpdate();

					}
					return 1;
				}
			}

		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}

		}

		return 0;
	}

	static void statics(String type, String city, java.sql.Date fixedenddate) {
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pr;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			String sql1 = "INSERT INTO staticinformation(`date`, `city`, `type`, `enddate`) VALUES (?,?,?,?)";
			if (conn != null) {
				pr = conn.prepareStatement(sql1);
				LocalDate sd = LocalDate.now();
				Calendar c = Calendar.getInstance();
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				String stringdate = sd.format(dateFormatter);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

				java.util.Date date1 = sdf1.parse(stringdate);
				c.setTime(date1);
				c.add(Calendar.DATE, 1);
				String output = sdf1.format(c.getTime());
				date1 = sdf1.parse(output);
				java.sql.Date datenow1 = new java.sql.Date(date1.getTime());

				pr.setDate(1, datenow1);
				pr.setString(2, city);
				pr.setString(3, type);
				if (type.equals("fixedpurchase")) {
					pr.setDate(4, fixedenddate);
				}
				pr.executeUpdate();

			}

		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getSQLState());
			System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}

		}

	}

	static void SendVerMessage(String City, Connection conn, int Ver) {

		Statement pr;
		PreparedStatement pr2;

		ResultSet rs = null;

		String sqlfind = "";
		String sql = "INSERT INTO messages(`userName`, `message`, `Datesend`, `read`, `typemessage`) VALUES (?,?,?,?,?)";

		if (conn != null) {
			try {

				pr2 = conn.prepareStatement(sql);
				Date date5 = new Date();
				java.sql.Date date3 = new java.sql.Date(date5.getTime());
				String type = "NewV " + City;
				String us = "ALL";
				String mess = "Dear member a new version of the city " + City + " is out";
				pr2.setString(1, us);
				pr2.setString(2, mess);
				pr2.setDate(3, date3);
				pr2.setInt(4, 0);
				pr2.setString(5, type);

				int k = pr2.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
