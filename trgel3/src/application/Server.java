package application;


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

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server {
	
	   static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	   static private final String DB = "eCp4XWJvNw";
	   static private final String DB_URL = "jdbc:mysql://remotemysql.com/"+ DB + "?useSSL=false";
	   static private final String USER = "eCp4XWJvNw";
	   static private final String PASS = "eSS7xZeTpg";
	   static private Object data;
	
	   public static void main(String[] args) throws ClassNotFoundException, SQLException {
		   
	        try
	        {
	        	@SuppressWarnings("resource")
	        	ServerSocket myServerSocket = new ServerSocket(5555);
	        	while(true)
	        	{      	
	            Socket skt = myServerSocket.accept();
                ObjectInputStream objectInput = new ObjectInputStream(skt.getInputStream());
                data=objectInput.readObject();
                if(data instanceof Object[] && !(data instanceof String[]))
                {
                	 if(((String)((Object[])(data))[0]).equals("Register")) {
                     	User client = ((User)((Object[])(data))[1]);
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
              				//stmt = conn.createStatement();
              				
              				PreparedStatement pr;
              				String sql = "INSERT INTO user (`firstName`, `lastName`, `phoneNumber`, `email`, `payment`, `userName`, `password`) VALUES (?,?,?,?,?,?,?)"; 
              				//ResultSet rs = stmt.executeQuery(sql);
              			
              				if(conn!=null) {
              		    		try {
              		    			if((fname1.equals(""))||(lname1.equals(""))||(phone.equals(""))||(email.equals(""))||(pay.equals(""))||(user.equals(""))||(password.equals(""))) {
              		    				JOptionPane.showMessageDialog(null, "Please fill all the fields");
              		    				
              		    			}
              		    			else if(checkuser(user,conn)==1){
              		    				//System.out.println("please");
              		    				JOptionPane.showMessageDialog(null, "the username already exist");
              		    			}
              		    			else {
              						pr=conn.prepareStatement(sql);
              						pr.setString(1, fname1);
              						pr.setString(2, lname1);
              						pr.setString(3, phone);
              						pr.setString(4, email);
              						pr.setString(5, pay);
              						pr.setString(6, user);
              						pr.setString(7, password);
              						if(pr.executeUpdate()>0) {
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
              				
              			}
              			catch (SQLException se) {
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
                
                else if(data instanceof String[])
                {
                	
                    if(((String[])(data))[0].equals("Exit"))
                    	break;
                    else if (((String[])(data))[0].equals("getMessages"))
                    {
                    	
                    
                    	Connection conn = null;
            			Statement stmt = null;            			
            			Class.forName(JDBC_DRIVER);       				 
        				conn = DriverManager.getConnection(DB_URL, USER, PASS);
        				stmt = conn.createStatement();
        				String userN=((String[])(data))[1];
        				String sql = "SELECT * FROM messages where userName='" + userN + "'";
        				ResultSet rs = stmt.executeQuery(sql);
        				Object[] result = new Object[2];
        				if(rs.next())
        				{
        					System.out.println("there is message");
        					String message = rs.getString("message");
	        				
	        				stmt.close();
	        				conn.close();
	        				
	        				ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
	     	                objectOutput.writeObject(message);
        				}
        				else
        				{
	        				ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
	     	                objectOutput.writeObject("No Message");
        				}
                    }
                    else if(((String[])(data))[0].equals("Login"))
                	{
                		
                		
                		int k = ((String[])(data)).length;               		
                        			   
            		    Connection conn = null;
            			Statement stmt = null;
            			boolean Connected=false;
            			try {
            				Class.forName(JDBC_DRIVER);
            				 
            				conn = DriverManager.getConnection(DB_URL, USER, PASS);
            				stmt = conn.createStatement();
            				String userN=((String[])(data))[1];
            				String passW=((String[])(data))[2];
            				String sql = "SELECT * FROM user where userName='" + userN + "' and password='" + passW +"'"   ;
            				ResultSet rs = stmt.executeQuery(sql);
            				Object[] result = new Object[2];
            				while (rs.next()) {
            				
            					System.out.println("Hello User");
            				
            					Connected=true;
            				
	            				String username = rs.getString("userName");            				  
	            				String password = rs.getString("password");
	            				String email = rs.getString("email");
	            			    String phonenumber = rs.getString("phoneNumber");
	            				String firstname = rs.getString("firstName");
	            				String lastname = rs.getString("lastName");
	            				String payment = rs.getString("payment");
	            				String type = rs.getString("type");
	            				  //String pathnum = rs.getString("pathNum");
	            				  
	            				  //data.add(new User(username, description, mapsnum , placesnum, pathnum ));
	            				User user = new User(firstname, lastname, email, username, password, phonenumber, payment,type);

	            				result[1]=user;

            				}
            				result[0]=Connected;
            				
            				stmt.close();
            				conn.close();
            				ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
         	                objectOutput.writeObject(result);
         	                
         	                objectInput.close();
         	                objectOutput.close();
         	                skt.close();
            				//return data;
            			}
            			catch (SQLException se) {
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
                    else if(((String[])(data))[0].equals("getCatalog"))
                	{   
        	            ObservableList<City> cityList = getCityFromDB();
        	           
        	            Object[] data = new Object[cityList.size()+1];
        	            data[0] = cityList.size();
        	            int counter = 1;
        	            for(City tu: cityList) {
        	                data[counter] = tu;
        	                counter++;
        	            }
        	            try {
        	                ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
        	                objectOutput.writeObject(data);       
        	            } 
        	            catch (IOException e) 
        	            {
        	                e.printStackTrace();
        	            } 
                	}
                    
                    else if(((String[])(data))[0].equals("getPlaceCatalog"))
                	{   
        	            ObservableList<Place> placeList = getPlaceFromDB();
        	           
        	            Object[] data = new Object[placeList.size()+1];
        	            data[0] = placeList.size();
        	            int counter = 1;
        	            for(Place tu: placeList) {
        	                data[counter] = tu;
        	                counter++;
        	            }
        	            try {
        	                ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
        	                objectOutput.writeObject(data);       
        	            } 
        	            catch (IOException e) 
        	            {
        	                e.printStackTrace();
        	            } 
                	}
                    
                    else if(((String[])(data))[0].equals("getMaps")) { 
        	            ObservableList<Map> mapList = getMapFromDB(((String[])(data))[1]);
        	           
        	            Object[] data = new Object[mapList.size()+1];
        	            data[0] = mapList.size();
        	            int counter = 1;
        	            for(Map tu: mapList) {
        	                data[counter] = tu;
        	                counter++;
        	            }
        	            try {
        	                ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
        	                objectOutput.writeObject(data);       
        	            } 
        	            catch (IOException e) 
        	            {
        	                e.printStackTrace();
        	            } 
                	}
                    
                    else if(((String[])(data))[0].equals("getMyMaps")) { 
        	            ObservableList<Map> mapList = getMyMapsFromDB(((String[])(data))[1]);
        	           
        	            Object[] data = new Object[mapList.size()+1];
        	            data[0] = mapList.size();
        	            int counter = 1;
        	            for(Map tu: mapList) {
        	                data[counter] = tu;
        	                counter++;
        	            }
        	            try {
        	                ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
        	                objectOutput.writeObject(data);       
        	            } 
        	            catch (IOException e) 
        	            {
        	                e.printStackTrace();
        	            } 
                	}
                    
                    else if(((String[])(data))[0].equals("addCityToMember")) { 
                    	Connection conn = null;
            			Statement stmt = null;
            			try {
            				Class.forName(JDBC_DRIVER);
            				conn = DriverManager.getConnection(DB_URL, USER, PASS);
              				System.out.println(((String[])(data))[1]);
              				System.out.println(((String[])(data))[2]);
              				PreparedStatement pr;
              				String sql = "INSERT INTO memberMap (`member`, `city`) VALUES (?,?)"; 
              				
              			
              				if(conn!=null) {
              		    		try {
              		    			
              						pr=conn.prepareStatement(sql);
              						pr.setString(1, ((String[])(data))[1]);
              						pr.setString(2, ((String[])(data))[2]);
              						pr.executeUpdate();
              					} catch (SQLException e) {
              						// TODO Auto-generated catch block
              						e.printStackTrace();
              					}
              		    	}
            			    
            				stmt.close();
            				conn.close();
            				
            			}
            			catch (SQLException se) {
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
	        }
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
	    }
	   
	  
	static ObservableList<City> getCityFromDB(){
			ObservableList<City> data = FXCollections.observableArrayList();
	
			   
		    Connection conn = null;
			Statement stmt = null;
			Statement stmt2 = null;
			
			try {
				Class.forName(JDBC_DRIVER);
				 
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				stmt = conn.createStatement();
				stmt2 = conn.createStatement();
				
				String sql = "SELECT * FROM CityCatalog"   ;
				ResultSet rs = stmt.executeQuery(sql);
			
				while (rs.next()) {
				  String city = rs.getString("name");
				  String description = rs.getString("description");
				  String mapsnum = rs.getString("mapsNum");
				  String placesnum = rs.getString("placesNum");
				  String pathnum = rs.getString("pathNum");
				  String places = null;
				  
				  String sql2 = "SELECT * FROM places WHERE Name ='" + city + "'"  ;
				  ResultSet rs2 = stmt2.executeQuery(sql2);
				   
				  while (rs2.next()) {		
				    String place = rs2.getString("Place");
				    places += (" + " + place);
				  }
				 
				  data.add(new City(city, description, mapsnum , placesnum, pathnum, places ));
				}
			    
				stmt2.close();
				stmt.close();
				conn.close();
				
				return data;
			}
			catch (SQLException se) {
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
	   
     static ObservableList<Place> getPlaceFromDB(){
		ObservableList<Place> data = FXCollections.observableArrayList();

		   
	    Connection conn = null;
		Statement stmt = null;
		
		
		try {
			Class.forName(JDBC_DRIVER);
			 
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
		
			
			String sql = "SELECT * FROM places"   ;
			ResultSet rs = stmt.executeQuery(sql);
		
			while (rs.next()) {
			  String city = rs.getString("Name");
			  String description = rs.getString("description");
			  String place = rs.getString("Place");
			  String Classification = rs.getString("Classification");
			  int Accessibility = rs.getInt("Accessibility");
			  int numOfMaps = rs.getInt("mapsNum");
			
			  data.add(new Place(city, place, description, Classification , Accessibility, numOfMaps ));
			}
		    
			
			stmt.close();
			conn.close();
			
			return data;
		}
		catch (SQLException se) {
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
	   
	static ObservableList<Map> getMapFromDB(String city){
		ObservableList<Map> data = FXCollections.observableArrayList();
		
	    Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			 
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			String sql = "SELECT * FROM maps WHERE city ='" + city + "'"  ;
			ResultSet rs = stmt.executeQuery(sql);
		
			while (rs.next()) {
			  int id = rs.getInt("id");
			  String description = rs.getString("description");
			  String linkC = rs.getString("linkCustomer");
			  String linkE = rs.getString("linkEmployee");
			 
			  data.add(new Map(id, city,description, linkC , linkE ));
			}
		    
			stmt.close();
			conn.close();
			
			return data;
		}
		catch (SQLException se) {
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
	   
	@SuppressWarnings("null")
	static ObservableList<Map> getMyMapsFromDB(String user){
		ObservableList<Map> data = FXCollections.observableArrayList();
		
	    
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName(JDBC_DRIVER);
			 
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			
			String sql = "SELECT * FROM memberMap WHERE member ='" + user + "'"  ;
			ResultSet rs = stmt.executeQuery(sql);
		
			while (rs.next()) {			  
			   String city = rs.getString("city");
			 
			   String sql2 = "SELECT * FROM maps WHERE city ='" + city + "'"  ;
			   ResultSet rs2 = stmt2.executeQuery(sql2);
			   
				while (rs2.next()) {
				  int id = rs2.getInt("id");
				  String description = rs2.getString("description");
				  String linkC = rs2.getString("linkCustomer");
				  String linkE = rs2.getString("linkEmployee");
				 
				  data.add(new Map(id, city,description, linkC , linkE ));
				}
				
			}

			stmt2.close();
			stmt.close();
			conn.close();
			
			return data;
		}
		catch (SQLException se) {
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
	   
	   
	public static int checkuser(String user2,Connection	conn) {
		Statement pr;
		
		ResultSet rs=null;
		int a=0;
    	String sql ="SELECT * FROM user WHERE userName ='" + user2 + "'";
    	//Connection	conn=connecttion();
    	if(conn!=null) {
    		try {
				pr=conn.createStatement();
    			 
    			rs=pr.executeQuery(sql);
    			 
				//pr.setString(1, user2);
				
				//rs=pr.executeQuery();
				//rs.next();
				//System.out.println(rs.getObject(1));
				if(rs.next()) {
					//System.out.println(user2);
				//rs.next();
				//System.out.println(rs.getString("first name"));
				a=1;
			/*	if(rs.next()) {
					//System.out.println(user2);
					a=1;
				}*/
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    	
    	return a;
	}
	   
	   
	   
	   
}