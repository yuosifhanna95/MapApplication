
package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleMessage implements Runnable {
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static private final String DB = "eCp4XWJvNw";// sql2293675
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/" + DB + "?useSSL=false";// sql2.freemysqlhosting.net:3306///
	static private final String USER = "eCp4XWJvNw";// sql2293675
	static private final String PASS = "eSS7xZeTpg";// bW3%jS1%

	 public void run() {
		 Connection conn = null;
		 Connection conn1 = null;
			Statement stmt = null;
			System.out.println("purchase you can renew");
			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
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
			
			String sql = "SELECT * FROM fixedPurchase";
			ResultSet rs=null;
			try {
				 rs = stmt.executeQuery(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				while(rs.next()) {
					System.out.println("purchase you can renew1");
					Date date1=rs.getDate("endDate");
					Calendar c = Calendar.getInstance();
					Calendar c1 = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					c.setTime(date1);
					c.add(Calendar.DATE,-2);
					Date date2=new Date();
					c1.setTime(date2);
					String output = sdf.format(c.getTime());
					String output1 = sdf.format(c1.getTime());
					System.out.println(output1);
					if(output.equals(output1)) {
						String city=rs.getString("city");
						String mess="Three days left to purchase"+city+"City,you can renew the purchase to the same period and you will get discount 10%";  
						java.sql.Date date3=new java.sql.Date(date2.getTime());
						String user=rs.getString("user");
						String type="warning";
						PreparedStatement pr;
						
						
						String sql1="INSERT INTO messages(`userName`, `message`, `Datesend`, `read`, `typemessage`) VALUES (?,?,?,?,?)";
						if (conn1 != null) {
							System.out.println("purchase you can renew2");
							pr = conn1.prepareStatement(sql1);
							pr.setString(1,user );
							pr.setString(2, mess);
							pr.setDate(3, date3);
							pr.setInt(4, 0);
							pr.setString(5, type);
							pr.executeUpdate();
						
						}

					}

				}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
