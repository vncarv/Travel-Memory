package mjmAndroid.MemoryMap;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.ImageView;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver
{
	 public double getDistance(double lat1,double lat2, double long1, double long2) {
		    double distance = 0;
		    Location locationA = new Location("A");
		    locationA.setLatitude(lat1);
		    locationA.setLongitude(long1);
		    Location locationB = new Location("B");
		    locationB.setLatitude(lat2);
		    locationB.setLongitude(long2);
		    distance = locationA.distanceTo(locationB);
		    return distance;

		}
	@Override
	public void onReceive(final Context arg0, Intent arg1)
	{
		double lat1, lat2, longi1, longi2,distance=0;
		LongLat ll=new LongLat(arg0);
		if(ll.canGetLocation)
		{
		    try 
		    {       
		    	SQLiteDatabase db= arg0.openOrCreateDatabase("MyDB1" , arg0.MODE_PRIVATE, null);
		        lat2=ll.getLatitude();
				longi2=ll.getLongitude();
				Geocoder geocoder= new Geocoder(arg0, Locale.ENGLISH);
		        List<Address> addresses = geocoder.getFromLocation(lat2,longi2, 1);
	            if(addresses != null) 
	              {
	                 try
	                  {
	                	  Address fetchedAddress = addresses.get(0);
	                      StringBuilder strAddress = new StringBuilder();
	                
	             	      for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++)
	                	  {
	                     	 strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
	                	  }
	             	        //Toast.makeText(arg0,strAddress.toString(),Toast.LENGTH_SHORT).show();
                 			ContentValues insobj=new ContentValues();
                   			Calendar cal=Calendar.getInstance();
                 		 	SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm:ss");
                 			String cur_date= sdf.format(cal.getTime());
                 			Cursor z=db.rawQuery("SELECT * FROM AddressTable", null);
                 	     	int count=0;
                 			count+=z.getCount();
                 			insobj.put("Num", count);
                 			insobj.put("Address", strAddress.toString());
                 			insobj.put("Time", cur_date);
                 			insobj.put("Duration", 0);
                 			insobj.put("Lat",lat2);
                 			insobj.put("Longi", longi2);
                 			Cursor a=db.rawQuery("SELECT * FROM AddressTable", null);
             				if(count!=0)
                 			{
                 				a.moveToLast();
                 				String time=a.getString(a.getColumnIndex("Time")).substring(18);
                 				int min=Integer.parseInt(time.substring(0,2))*60+Integer.parseInt(time.substring(3,5));
                     			int cur_min=Integer.parseInt(cur_date.substring(18, 20))*60+Integer.parseInt(cur_date.substring(21, 23));
                     			lat1=a.getDouble(a.getColumnIndex("Lat"));
                 				longi1=a.getDouble(a.getColumnIndex("Longi"));
                 				distance=getDistance(lat1,lat2, longi1, longi2);
                 				if(distance<200|| a.getString(a.getColumnIndex("Address")).equals(strAddress.toString()))
                 				{
                 					int duration=a.getInt(a.getColumnIndex("Duration"));
                 					if(cur_date.substring(0, 18).equals(a.getString(a.getColumnIndex("Time")).substring(0, 18)) && (cur_min-(duration+min))<=15)
                 					{
                 						duration=(cur_min-min);
                 						db.execSQL("UPDATE AddressTable SET Duration="+duration+" WHERE Num="+(count-1)+";");
                 						//Toast.makeText(arg0,Integer.toString(duration),Toast.LENGTH_SHORT).show();
                 					}
                 					else
                     					db.insert("AddressTable", null,insobj);
                         		}
                 				else
                 					db.insert("AddressTable", null,insobj);
                 			}
                 			else
                 			{
                 				db.insert("AddressTable", null,insobj);
                 				}
                 			 
                 		    
	                  }
	              	  catch(Exception e)
	              	  { 
	              		
         				e.printStackTrace();	 
	              	  }
	               }
	           }
	           catch (IOException e) 
	           { 
    				e.printStackTrace();	                
	           }
			
		}
		
		   ll.stopUsingGPS();
	}
}
