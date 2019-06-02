package application;

import java.util.Date;
import java.io.Serializable;
@SuppressWarnings("serial")
public class fixedPurchase implements Serializable {
	 private String user;
	 private int period;
	 private String city;
	 private Date sdate;
	 private Date edate;
	 private double price;
	 
	 fixedPurchase(String user,int period,String city,Date sdate,Date edate,double price){
		 this.user=user;
		 this.period=period;
		 this.city=city;
		 this.sdate=sdate;
		 this.edate=edate;
		 this.price=price;
	 }
	 public String getuser() {
	        return user;
	    }
	    public void setuser(String str) {
	        this.user = str;
	    }
	    public int getperiod() {
	        return period;
	    }
	    public void setperiod(int str) {
	        this.period = str;
	    }
	    public String getcity() {
	        return city;
	    }
	    public void setcity(String str) {
	        this.user = str;
	    }
	    public Date getsdate() {
	        return sdate;
	    }
	    public void setsdate(Date a) {
	        this.sdate = a;
	    }
	    public Date getedate() {
	        return edate;
	    }
	    public void setedate(Date a) {
	        this.edate = a;
	    }
	    public double getprice() {
	        return price;
	    }
	    public void setprice(double str) {
	        this.price = str;
	    }
}
