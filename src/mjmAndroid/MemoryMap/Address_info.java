package mjmAndroid.MemoryMap;

public class Address_info {
String address, date, duration;
double lat, longi;
public Address_info(String address,String date,String duration,double lat, double longi) {
	  super();
	  this.address = address;
	  this.date = date;
	  this.duration = duration;
	  this.lat=lat;
	  this.longi=longi;
	 }
public double getLat() {
	  return lat;
	 }
	 public void setLat(double lat) {
	  this.lat = lat;
	 }
	 public double getLongi() {
		  return longi;
		 }
		 public void setLongi(double longi) {
		  this.longi = longi;
		 }


public String getaddress() {
	  return address;
	 }
	 public void setaddress(String address) {
	  this.address = address;
	 }
	 public String getdate() {
	  return date;
	 }
	 public void setdate(String date) {
	  this.date = date;
	 }
	 
	 public String getduration() {
		  return duration;
		 }
		 public void setduration(String duration) {
		  this.duration = duration;
		 }
		 
}

