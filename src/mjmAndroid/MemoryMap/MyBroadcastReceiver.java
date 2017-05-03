package mjmAndroid.MemoryMap;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyBroadcastReceiver extends BroadcastReceiver{
    private static final long REPEAT_TIME = 1000*180;
    AlarmManager ser=null;
    PendingIntent pi;
    Intent serviceIntent;
	@Override
    public void onReceive(Context context, Intent intent) {
    	SharedPreferences service=context.getSharedPreferences("MYPREFS",Context.MODE_PRIVATE);
    	String value=service.getString("tvalue", "false");
    	if(value.equals("true"))
    	{
    		   if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
    			  startAlarm(context);
    	          }
    	}
    	 String statusString = getConnectivityStatusString(context);
    	 //   Toast.makeText(context, statusString, Toast.LENGTH_SHORT).show();
    	
    	
    }
	
	 public int TYPE_CONNECTED = 1;
     public int TYPE_NOT_CONNECTED = 0;

     public int getConnectivityStatus(Context context) {
         ConnectivityManager cm = (ConnectivityManager) context
                 .getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
         if (null != activeNetwork) {
             if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                 return TYPE_CONNECTED;

         }
         return TYPE_NOT_CONNECTED;
     }

     public String getConnectivityStatusString(Context context) {
         int conn = getConnectivityStatus(context);
         String status = null;
         if (conn == TYPE_CONNECTED) {
             status = "Connected to internet";
             SharedPreferences service=context.getSharedPreferences("MYPREFS",Context.MODE_PRIVATE);
             SharedPreferences.Editor editor=service.edit();
         	editor.putString("service", "true");
			editor.commit();
			
			
         }
         else if (conn == TYPE_NOT_CONNECTED) {
             status = "Not connected to Internet";
             SharedPreferences service=context.getSharedPreferences("MYPREFS",Context.MODE_PRIVATE);
             SharedPreferences.Editor editor=service.edit();
         	editor.putString("service", "false");
			editor.commit();
			String tval = service.getString("tval", "false");
			
         }
         return status;
     }
	
	
	public void startAlarm(Context context)
	{
		ser=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(context, AlarmReceiver.class);
        pi=PendingIntent.getBroadcast(context, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.SECOND, 3);
        ser.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, pi);
	}
	public void cancelAlarm(Context context)
	{
	    if (ser != null) 
	    {
	        ser.cancel(pi);
	        ser=null;
	        // Toast.makeText(context, "Alarm Canceled", Toast.LENGTH_SHORT).show();
	    }
	    else
		{
    	   serviceIntent = new Intent(context, AlarmReceiver.class);
    	   PendingIntent.getBroadcast(context, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT).cancel();
		}
	}
}