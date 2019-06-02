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
import java.util.Calendar;

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

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		try {
			@SuppressWarnings("resource")
			ServerSocket myServerSocket = new ServerSocket(5555);
			while (true) {
				Socket skt = myServerSocket.accept();
				ObjectInputStream objectInput = new ObjectInputStream(skt.getInputStream());
				data = objectInput.readObject();
				if (data instanceof Object[] && !(data instanceof String[])) {
					if (((String) ((Object[]) (data))[0]).equals("Register")) {

						User client = ((User) ((Object[]) (data))[1]);
						String fname1 = client.getFirstName();
						String lname1 = client.getLastName();
						String phone = client.getPhoneNumber();
						String email = client.getEmail();
						String pay = client.getPayment();
						String user = client.getUserName();
						String password = client.getPassword();

						Connection conn = null;
						Statement stmt = null;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();

							PreparedStatement pr;
							String sql = "INSERT INTO user (`firstName`, `lastName`, `phoneNumber`, `email`, `payment`, `userName`, `password`) VALUES (?,?,?,?,?,?,?)";
							// ResultSet rs = stmt.executeQuery(sql);

							if (conn != null) {
								try {
									if ((fname1.equals("")) || (lname1.equals("")) || (phone.equals(""))
											|| (email.equals("")) || (pay.equals("")) || (user.equals(""))
											|| (password.equals(""))) {
										JOptionPane.showMessageDialog(null, "Please fill all the fields");

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
										if (pr.executeUpdate() > 0) {
											JOptionPane.showMessageDialog(null, "thanks for registeration");
										}
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
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

					}
				} else if (data instanceof String[]) {

					if (((String[]) (data))[0].equals("Exit"))
						break;
					else if (((String) ((String[]) (data))[0]).equals("ConfirmUpdate")) {

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

								String sql, sql2;// = "INSERT INTO places (`MapId` ,`Name`, `Place`, `description`,
								// `classification`, accessibility, LocX, LocY, `Type`) VALUES
								// (?,?,?,?,?,?,?,?,?)";
								// ResultSet rs = stmt.executeQuery(sql);

								if (place.getType().equals("NEW")) {
									sql = "INSERT INTO places (`MapId` ,`Name`, `Place`, `description`, `classification`, accessibility, LocX, LocY, mapsNum) VALUES (?,?,?,?,?,?,?,?,?)";
									sql2 = "INSERT INTO placeMap (`Name`,`MapId` ,LocX, LocY) VALUES (?,?,?,?)";
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
										if (pr.executeUpdate() > 0) {
											// JOptionPane.showMessageDialog(null, "thanks for Update");
											System.out.println("thanks for confirmation");
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
					}

					else if (((String[]) (data))[0].equals("getMessages")) {

						Connection conn = null;
						Statement stmt = null;
						Class.forName(JDBC_DRIVER);
						conn = DriverManager.getConnection(DB_URL, USER, PASS);
						stmt = conn.createStatement();
						String userN = ((String[]) (data))[1];
						String sql = "SELECT * FROM messages where userName='" + userN + "'";
						ResultSet rs = stmt.executeQuery(sql);
						Object[] result = new Object[2];
						if (rs.next()) {
							System.out.println("there is message");
							String message = rs.getString("message");

							stmt.close();
							conn.close();

							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject(message);
						} else {
							ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
							objectOutput.writeObject("No Message");
						}
					} else if (((String[]) (data))[0].equals("Login")) {

						int k = ((String[]) (data)).length;

						Connection conn = null;
						Statement stmt = null;
						boolean Connected = false;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();
							String userN = ((String[]) (data))[1];
							String passW = ((String[]) (data))[2];
							String sql = "SELECT * FROM user where userName='" + userN + "' and password='" + passW
									+ "'";
							ResultSet rs = stmt.executeQuery(sql);
							Object[] result = new Object[2];
							while (rs.next()) {

								System.out.println("Hello User");

								Connected = true;

								String username = rs.getString("userName");
								String password = rs.getString("password");
								String email = rs.getString("email");
								String phonenumber = rs.getString("phoneNumber");
								String firstname = rs.getString("firstName");
								String lastname = rs.getString("lastName");
								String payment = rs.getString("payment");
								String type = rs.getString("type");
								// String pathnum = rs.getString("pathNum");

								// data.add(new User(username, description, mapsnum , placesnum, pathnum ));
								User user = new User(firstname, lastname, email, username, password, phonenumber,
										payment, type);

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
					}
					else if (((String[]) (data))[0].equals("getUsers")) {
						ObservableList<User> userList = getUserFromDB();

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
					}
					else if (((String[]) (data))[0].equals("getCatalog")) {
						ObservableList<City> cityList = getCityFromDB();

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
					} else if (((String[]) (data))[0].equals("getMaps")) {
						ObservableList<Map> mapList = getMapFromDB(((String[]) (data))[1]);

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
						ObservableList<Map> mapList = getMyMapsFromDB(((String[]) (data))[1]);

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
					} else if (((String[]) (data))[0].equals("addCityToMember")) {

						Connection conn = null;
						Statement stmt = null;
						try {
							Class.forName(JDBC_DRIVER);
							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							PreparedStatement pr;
							String sql = "INSERT INTO fixedPurchase (`user`, `city`, period, `startDate`, `endDate`, `purchaseprice`) VALUES (?,?,?,?,?,?)";

							if (conn != null) {
								try {

									pr = conn.prepareStatement(sql);
									pr.setString(1, ((String[]) (data))[1]);
									pr.setString(2, ((String[]) (data))[2]);
									pr.setInt(3, 30);
									pr.setDate(4, java.sql.Date.valueOf("2013-09-04"));
									pr.setDate(5, java.sql.Date.valueOf("2013-09-04"));
									pr.setInt(6, 30);
									pr.executeUpdate();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

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
					} else if (((String[]) (data))[0].equals("getPlaces")) {

						Place[] list;
						int k = ((String[]) (data)).length;

						Connection conn = null;
						Statement stmt = null;
						boolean Connected = false;
						try {
							Class.forName(JDBC_DRIVER);

							conn = DriverManager.getConnection(DB_URL, USER, PASS);
							stmt = conn.createStatement();
							String mapid = ((String[]) (data))[1];
							String sql = "SELECT * FROM places where MapId='" + mapid + "'";
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

				if (currentDate.compareTo(endDate) > 0) {
					System.out.println(currentDate);
					rs.deleteRow();
				} else
					data.add(new FixedPurchase(user, city, startDate, endDate));
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
	
	static ObservableList<User> getUserFromDB() {
		ObservableList<User> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM Users";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String fname = rs.getString("firstName");
				String lname = rs.getString("lastName");
				String pnumber = rs.getString("phoneNumber");
				String email = rs.getString("email");
				String username = rs.getString("userName");
				String password = rs.getString("password");
				String payment = rs.getString("payment");
				String type = rs.getString("type");
				

	
				data.add(new User(fname, lname, email, username, password, pnumber, payment,type));
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

	
	static ObservableList<City> getCityFromDB() {
		ObservableList<City> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;

		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM CityCatalog";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String city = rs.getString("name");
				String description = rs.getString("description");
				String mapsnum = rs.getString("mapsNum");
				String placesnum = rs.getString("placesNum");
				String pathnum = rs.getString("pathNum");
				String places = null;
				Boolean NewUpdate = rs.getBoolean("NewUpdate");
				String sql2 = "SELECT * FROM places WHERE Name ='" + city + "'";
				ResultSet rs2 = stmt2.executeQuery(sql2);

				while (rs2.next()) {
					String place = rs2.getString("Place");
					places += (" + " + place);
				}

				data.add(new City(city, description, mapsnum, placesnum, pathnum, places, NewUpdate));
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

	static ObservableList<Map> getMapFromDB(String city) {
		ObservableList<Map> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			String sql = "SELECT * FROM maps WHERE city ='" + city + "'";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				// String city = rs.getString("city");
				String description = rs.getString("description");
				String linkC = rs.getString("linkCustomer");
				String linkE = rs.getString("linkEmployee");
				Boolean NewUpdate = rs.getBoolean("NewUpdate");

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

	static ObservableList<Map> getMyMapsFromDB(String city) {
		ObservableList<Map> data = FXCollections.observableArrayList();

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT * FROM maps WHERE city ='" + city + "'";
			ResultSet rs = stmt2.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String description = rs.getString("description");
				String linkC = rs.getString("linkCustomer");
				String linkE = rs.getString("linkEmployee");

				Boolean NewUpdate = rs.getBoolean("NewUpdate");
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

	public static void DeleteNewUpdates(String MapId) throws SQLException {
		Statement pr, pr1, pr2;
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		ResultSet rs = null;

		String sql = "", sql2 = "", sqlfind = "";
		sql = "UPDATE maps SET NewUpdate=" + false + " WHERE id=" + MapId;
		sql2 = "UPDATE CityCatalog SET NewUpdate=" + false + " WHERE Name='";
		sqlfind = "Select * FROM maps WHERE id ='" + MapId + "'";
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
						counter += rs.getInt("NewUpdate");
						city = rs.getString("city");
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

}