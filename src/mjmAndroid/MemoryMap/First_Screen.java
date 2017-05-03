package mjmAndroid.MemoryMap;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class First_Screen extends Activity implements OnClickListener{

	ArrayList<Drawer_Item> drawer_arr= new ArrayList<Drawer_Item>();
	public DrawerLayout mDrawerLayout;
	public static ListView mDrawerList;
	public CustomDrawerAdapter adapter;
	public ActionBarDrawerToggle mDrawerToggle;
	public CharSequence mDrawerTitle;
	public CharSequence mTitle;
	public ToggleButton toggle_button;  
	public Bundle savedInstance;
	public final int REQUEST_IMAGE_CAPTURE = 1;
	public  String mCurrentPhotoPath;
	public static SQLiteDatabase db;
	public static String[] drawer_array = { "Home","Travel History" ,"Gallery", "Notes" , "About App"};
	public FragmentManager fm;
	public FragmentTransaction ft;
	public static Fragment frag , basicfrag ,basicfrag2;
	public static  int bydrawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first__screen);
		if (savedInstanceState == null) {
			fm =getFragmentManager();
			ft=fm.beginTransaction();
			 frag=new PlaceholderFragment();
			ft.add(R.id.container, frag);
			ft.addToBackStack(null);
			ft.commit();
		}
		
	
	     SQLiteDatabase db = openOrCreateDatabase("MyDB1" ,MODE_PRIVATE, null);
			db.execSQL("CREATE TABLE IF NOT EXISTS AddressTable (Num INT , Address VARCHAR, Time VARCHAR, Duration INT, Lat DOUBLE, Longi DOUBLE);");
			db.execSQL("CREATE TABLE IF NOT EXISTS Photos (Position INT primary key ,Num INT,Path VARCHAR);");
			db.execSQL("CREATE TABLE IF NOT EXISTS Note (Position INT primary key ,Num INT,Notes TEXT);");
			//db.execSQL("DELETE * from Photos");
			//db.execSQL("CREATE TABLE IF NOT EXISTS OfflinePhotos (Path VARCHAR , Descripton TEXT);");
			SharedPreferences service=this.getSharedPreferences("MYPREFS",Context.MODE_PRIVATE);
			int i=0;
			Drawer_Item obj;
			int ids[]=new int[5];
			ids[0]=R.drawable.one;
			ids[1]=R.drawable.two;
			ids[2]=R.drawable.three;
			ids[3]=R.drawable.four;
			ids[4]=R.drawable.six;
		//	ids[5]=R.drawable.six;
			
			for(i=0;i<5;i++)
			{
				String title = drawer_array[i];
				obj = new Drawer_Item(title,ids[i]);
				drawer_arr.add(obj);
			}
	    	   mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.left_drawer);

	    	mTitle = mDrawerTitle = "Travel Memory Map";
	        // Set the adapter for the list view
	    	adapter = new CustomDrawerAdapter(this, R.layout.drawer_item,
					drawer_arr);

	         mDrawerList.setAdapter(adapter); 
	        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	        
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerToggle = new ActionBarDrawerToggle(
	                this,                  /* host Activity */
	                mDrawerLayout,         /* DrawerLayout object */
	                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
	                R.string.drawer_open,  /* "open drawer" description */
	                R.string.drawer_close  /* "close drawer" description */
	                ) {
	            /** Called when a drawer has settled in a completely closed state. */
	            public void onDrawerClosed(View view) {
	                super.onDrawerClosed(view);
	                getActionBar().setTitle(mTitle);

	        		mDrawerLayout.closeDrawer(mDrawerList);
	            }
	            

	            /** Called when a drawer has settled in a completely open state. */
	            public void onDrawerOpened(View drawerView) {
	                super.onDrawerOpened(drawerView);
	                getActionBar().setTitle(mDrawerTitle);
	            }
	        };

	        service=this.getSharedPreferences("MYPREFS",Context.MODE_PRIVATE);
	    	String value=service.getString("tvalue", "false");
	    	
	        getActionBar().setTitle("Travel Memory Map");
	        ActionBar actionBar = getActionBar();
	        actionBar.setCustomView(R.layout.actionbar_top);
	        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);

	 	   toggle_button = (ToggleButton) findViewById(R.id.actionbar_service_toggle);
		   if(value.equals("true"))
		   toggle_button.setChecked(true);
		        toggle_button.setOnClickListener(this);
		      
		        // Set the drawer toggle as the DrawerListener
		        mDrawerLayout.setDrawerListener(mDrawerToggle);

		        getActionBar().setDisplayHomeAsUpEnabled(true);
		        getActionBar().setHomeButtonEnabled(true);
		
	}
	@Override
	protected void onStart() {
		super.onStart();

		mDrawerLayout.closeDrawer(mDrawerList);
	
	};
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	 if (mDrawerToggle.onOptionsItemSelected(item)) {
             return true;
           }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
	private void selectItem(int position) {
    	
      mDrawerList.setItemChecked(position, true);
		if(position==0)
		{
			
			fm =getFragmentManager();
			ft=fm.beginTransaction();
			frag=new PlaceholderFragment();
			ft.add(R.id.container, frag);
			ft.addToBackStack(null);
			ft.commit();
		}

		if(position==1)
		{
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			frag = new List();
//			Toast.makeText(this, "history", Toast.LENGTH_SHORT).show();
			ft.replace(R.id.container,frag);
			ft.addToBackStack(null);
			ft.commit();
		}
		if(position==2)
		{
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			basicfrag2=frag =new Gallery();
			ft.replace(R.id.container,frag);
			ft.addToBackStack(null);
			ft.commit();
		}
		if(position==3)
		{
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			basicfrag=frag =new NotesList();
			ft.replace(R.id.container,frag);
			ft.addToBackStack(null);
			ft.commit();
		}if(position==4)
		{

			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			frag =new AppInfo();
			ft.replace(R.id.container,frag);
			ft.addToBackStack(null);
			ft.commit();
		}
		mDrawerLayout.closeDrawer(mDrawerList);
		}	 
	@Override
	public void onClick(View arg0) {
		
			if(arg0.getId()==R.id.actionbar_service_toggle)
			{ 
				SharedPreferences service=getSharedPreferences("MYPREFS", 0);
				SharedPreferences.Editor editor=service.edit();
				MyBroadcastReceiver mbobj=new MyBroadcastReceiver();
			    LocationManager locationManager = (LocationManager) this
	                    .getSystemService(LOCATION_SERVICE);
	            boolean isGPSEnabled = locationManager
	                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
	 
	            boolean isNetworkEnabled = locationManager
	                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	        		if(toggle_button.isChecked())
	        			{
					
							if(!haveNetworkConnection())
							{
								toggle_button.setChecked(false);
								Toast.makeText(this, "No working internet connection", Toast.LENGTH_SHORT).show();
								return;
							}
							if (!isGPSEnabled && !isNetworkEnabled)
							{
								toggle_button.setChecked(false);
								AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
								alertDialog.setTitle("GPS is settings");
								alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");;
								alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int which) {
										Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
										startActivity(intent);
									}
								});
								alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
		                			dialog.cancel();
									}
								});
								alertDialog.show();
							}
							else
		            		{
		            				editor.putString("tvalue", "true");
		            				editor.commit();
		            				Toast.makeText(this,"Service on",Toast.LENGTH_SHORT).show();
		            				mbobj.startAlarm(this);	
		            		}
		                       
	        			}   
						else
						{   
						
							editor.putString("tvalue", "false");
							editor.commit();	
							Toast.makeText(this,"Service off",Toast.LENGTH_SHORT).show();
							mbobj.cancelAlarm(this);
					
						}
	 		}
		
	}
	
	private boolean haveNetworkConnection() {
		 boolean haveConnectedWifi = false;
		 boolean haveConnectedMobile = false;

		 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		 for (NetworkInfo ni : netInfo) {
		     if (ni.getTypeName().equalsIgnoreCase("WIFI"))
		         if (ni.isConnected())
		             haveConnectedWifi = true;
		     if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
		         if (ni.isConnected())
		             haveConnectedMobile = true;
		 }
		 return haveConnectedWifi || haveConnectedMobile;
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first__screen, menu);
		return true;
	}

	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnClickListener {
		ImageView notes;
       	ImageView camera , travel;
       	public final int REQUEST_IMAGE_CAPTURE = 1;
    	public  String mCurrentPhotoPath;

    	public RelativeLayout topLevelLayout;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_first__screen,
					container, false);
			
			topLevelLayout = (RelativeLayout) rootView.findViewById(R.id.top_layout);
		     if (isFirstTime()) {
		         	topLevelLayout.setVisibility(View.INVISIBLE);
		         }
			
			  travel=(ImageView) rootView.findViewById(R.id.travel_history); 
		     travel.setOnClickListener(this);
		     camera=(ImageView)rootView.findViewById(R.id.click_photos);
		     camera.setOnClickListener(this);
		    notes=(ImageView)rootView.findViewById(R.id.add_notes);
		     notes.setOnClickListener(this);
			return rootView;
		}

		@Override
		public void onClick(View arg0) {
			if(arg0.getId()==R.id.travel_history)
			{
				FragmentManager fm=getFragmentManager();
				FragmentTransaction ft=fm.beginTransaction();
				frag = new List();
				ft.replace(R.id.container,frag);
				ft.addToBackStack(null);
				ft.commit();
			}

			if(arg0.getId()==R.id.click_photos)
			{
				SharedPreferences service=getActivity().getSharedPreferences("MYPREFS",Context.MODE_PRIVATE);
				String value=service.getString("tvalue", "false");
		    	
		    			SQLiteDatabase db = getActivity().openOrCreateDatabase("MyDB1" ,Context.MODE_PRIVATE, null);
		    			Cursor z=db.rawQuery("SELECT * FROM AddressTable",null);
		    			
		    			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    			if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
		    				File photoFile = null;
		    				try {
							photoFile = createImageFile();
		    				} catch (IOException ex) 
		    				{						             
		    				}
		    				if (photoFile != null) {
		    						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				                      Uri.fromFile(photoFile));
		    						startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
				   		   
		    				}
		    			} 	
		    			galleryAddPic();	
			}   
		if(arg0.getId()==R.id.add_notes)
			{
				
				FragmentManager fm=getFragmentManager();
				FragmentTransaction ft=fm.beginTransaction();
				frag=new NotesGUI("",-1,0);
				bydrawer = 0;
				ft.replace(R.id.container, frag);
				
				ft.addToBackStack(null);
				ft.commit();
	    			
	    		
			}
			
			
		}
		

		private boolean isFirstTime()
		{
		SharedPreferences preferences = getActivity().getPreferences(MODE_PRIVATE);
		boolean ranBefore = preferences.getBoolean("RanBefore", false);
		if (!ranBefore) {

		    SharedPreferences.Editor editor = preferences.edit();
		    editor.putBoolean("RanBefore", true);
		    editor.commit();
		    topLevelLayout.setVisibility(View.VISIBLE);
		    
		    topLevelLayout.setOnTouchListener(new View.OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
		topLevelLayout.setVisibility(View.INVISIBLE);
		return false;
		}
		            
		            });


		    }
		return ranBefore;
		    
		}


		 private File createImageFile() throws IOException {
			    // Create an image file name
			    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			    String imageFileName = "JPEG_" + timeStamp + "_";
			    File imagesFolder= new File(Environment.getExternalStorageDirectory(),"TravelMemory");
			    imagesFolder.mkdirs();
			    File image = File.createTempFile(
			        imageFileName,  /* prefix */
			        ".jpg",         /* suffix */
			        imagesFolder      /* directory */
			    );

			    // Save a file: path for use with ACTION_VIEW intents
			    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		//Toast.makeText(getActivity(), mCurrentPhotoPath, Toast.LENGTH_LONG).show();
			    return image;
			}


			private void galleryAddPic() {
			    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			    File f = new File(mCurrentPhotoPath);
			    Uri contentUri = Uri.fromFile(f);
			    mediaScanIntent.setData(contentUri);
			 getActivity().sendBroadcast(mediaScanIntent);
			}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
		SharedPreferences service=getActivity().getSharedPreferences("MYPREFS",Context.MODE_PRIVATE);
		String tval=service.getString("tvalue", "false");
		String serv = service.getString("service", "true");
		if(tval.equals("true")&&serv.equals("true"))
		{
			if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK)
		    {

				SQLiteDatabase db =getActivity().openOrCreateDatabase("MyDB1" ,MODE_PRIVATE, null);
				Cursor z=db.rawQuery("SELECT * FROM AddressTable", null);
				z.moveToLast();
				String path=mCurrentPhotoPath.substring(5);
				Cursor c=db.rawQuery("SELECT * FROM Photos", null);
		    	int pos=c.getCount();
		    	
				if(z.getCount()!=0)
				{
				int ind=z.getInt(z.getColumnIndex("Num"));
		    	db.execSQL("INSERT INTO Photos VALUES("+pos+" ,"+ind+", \""+path+"\");");
		    	}
		    
			else
			{
				int ind=-1;
				db.execSQL("INSERT INTO Photos VALUES("+pos+" ,"+ind+", \""+path+"\");");
			}
		    }
			if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode==RESULT_CANCELED)
		    {

		    	File f=new File(mCurrentPhotoPath.substring(5));
		    	f.delete();
		    	//Toast.makeText(getActivity(),mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
		    }
		}
		if(tval.equals("false")||(tval.equals("true")&&serv.equals("false")))
		{
			
			if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK)
		    {

				SQLiteDatabase db =getActivity().openOrCreateDatabase("MyDB1" ,MODE_PRIVATE, null);
				int ind=-1;
		    	String path=mCurrentPhotoPath.substring(5);
		    	Cursor c=db.rawQuery("SELECT * FROM Photos", null);
		    	int pos=c.getCount();
		    	db.execSQL("INSERT INTO Photos VALUES("+pos+" ,"+ind+", \""+path+"\");");
		    	}
			if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode==RESULT_CANCELED)
		    {

		    	File f=new File(mCurrentPhotoPath.substring(5));
		    	f.delete();
		    	//Toast.makeText(getActivity(),mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
		    }

		}
		}		
		
	}
	@Override
	public void onBackPressed() {

		if(frag instanceof PlaceholderFragment)
		{
			//Toast.makeText(this, "home",Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		}
		if(frag instanceof NotesGUI)
		{
			if (bydrawer == 1)
			{
				//	Toast.makeText(this, "inside drawer", Toast.LENGTH_SHORT).show();
	        	FragmentManager fm=getFragmentManager();
	   			FragmentTransaction ft=fm.beginTransaction();
	   			Fragment fra = basicfrag;
	   			fra = new NotesList();
	   			First_Screen.frag = fra;
	   			ft.replace(R.id.container,fra);
	   			ft.addToBackStack(null);
	   			ft.commit();
			}   
			else
			{
				frag = new PlaceholderFragment();
				getFragmentManager().beginTransaction().replace(R.id.container, frag).commit();
			}
		}
		if(frag instanceof GalleryPhotoView)
		{
			FragmentManager fm=getFragmentManager();
   			FragmentTransaction ft=fm.beginTransaction();
   			Fragment frag;
   			basicfrag2=frag = new Gallery();
   			First_Screen.frag = frag;
   			ft.replace(R.id.container,frag);
   			ft.addToBackStack(null);
   			ft.commit();
		}
		else
		{
			frag = new PlaceholderFragment();
			getFragmentManager().beginTransaction().replace(R.id.container, frag).commit();
		}
			
	}
	public static class App_Info extends Fragment
	{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.app_info,
				container, false);
		 
		return rootView;
	}	
	}	
}