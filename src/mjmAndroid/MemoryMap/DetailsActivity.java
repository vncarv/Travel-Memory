
package mjmAndroid.MemoryMap;

import java.io.File;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends FragmentActivity implements ActionBar.TabListener {

 AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    
        getActionBar().setDisplayShowHomeEnabled(false);              
        getActionBar().setDisplayShowTitleEnabled(false);
mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
final ActionBar actionBar = getActionBar();
actionBar.setHomeButtonEnabled(true);
       actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
  mViewPager = (ViewPager) findViewById(R.id.pager);
       mViewPager.setAdapter(mAppSectionsPagerAdapter);
       mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
              
                actionBar.setSelectedNavigationItem(position);
            }
        });

       
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {          
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }  

   @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void onBackPressed() {
        if(mViewPager.getCurrentItem() == 3) {
            if (mAppSectionsPagerAdapter.getItem(3) instanceof DummySectionFragment) {

               // Toast.makeText(this, "back0", Toast.LENGTH_SHORT).show();
            	 ((DummySectionFragment) mAppSectionsPagerAdapter.getItem(3)).backPressed();

                 //Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
            }
            else if (mAppSectionsPagerAdapter.getItem(3) instanceof NotesFragment) {

                finish();
            }
        }
        else super.onBackPressed();
    }
    
    
    
    
   public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
FragmentManager mFragmentManager;
         public AppSectionsPagerAdapter(FragmentManager fm) {
             super(fm);
             this.mFragmentManager = fm;
         }

         private final class FirstPageListener implements
         FirstPageFragmentListener {
        	 public void onSwitchToNextFragment(String retrieve,int value)
        	 {
        		 mFragmentManager.beginTransaction().remove(mFragmentAtPos0)
                 .commit();
                 if (mFragmentAtPos0 instanceof NotesFragment){
                     mFragmentAtPos0 = new DummySectionFragment(listener,retrieve,value);
                 }else{ // Instance of NextFragment
                     mFragmentAtPos0 = new NotesFragment(listener);
                 }
                 notifyDataSetChanged();
        	 }
             public void onSwitchToNextFragment() {
                 mFragmentManager.beginTransaction().remove(mFragmentAtPos0)
                 .commit();
                 if (mFragmentAtPos0 instanceof NotesFragment){
                     mFragmentAtPos0 = new DummySectionFragment(listener);
                 }else{ // Instance of NextFragment
                     mFragmentAtPos0 = new NotesFragment(listener);
                 }
                 notifyDataSetChanged();
             }
         }

         public Fragment mFragmentAtPos0;

       public  FirstPageListener listener = new FirstPageListener();
         @Override
         public Fragment getItem(int i) {
             switch (i) {
             case 0:
            	 Fragment f = new MapsFragment();
                 Bundle arg = new Bundle();
                 arg.putInt(MapsFragment.ARG_SECTION_NUMBER, i + 1);
                 f.setArguments(arg);
                 return f;
             case 1:
            	 Fragment f1 = new PreviousVisits();
                 Bundle args1 = new Bundle();
                 args1.putInt(PreviousVisits.ARG_SECTION_NUMBER, i + 1);
                 f1.setArguments(args1);
                 return f1;
            
           case 2:
            	Fragment f2 = new FilesFragment();
                Bundle args2 = new Bundle();
                args2.putInt(FilesFragment.ARG_SECTION_NUMBER, i + 1);
                f2.setArguments(args2);
                return f2;
           case 3:
                       
               if (mFragmentAtPos0 == null)
               {
                   mFragmentAtPos0 = new NotesFragment(listener);
                   Bundle args3 = new Bundle();
                   args3.putInt(NotesFragment.ARG_SECTION_NUMBER, i + 1);
                  mFragmentAtPos0.setArguments(args3);
               }
               return mFragmentAtPos0;
          default: 
                   Fragment fragment = new DummySectionFragment();
                   Bundle args = new Bundle();
                   args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                   fragment.setArguments(args);
                   return fragment;
             }
         }

         @Override
         public int getCount() {
             return 4;
         }

         @Override
         public CharSequence getPageTitle(int position) {
        	 if(position==0)
             return "MapView" ;
        	 if(position==1)
        		 return "Visits";
        	 if(position == 2)
        		 return "Photos";
        	 if(position==3)
        		 return "Notes";
			return "Section";
         }
         
         public int getItemPosition(Object object)
         {
             if (object instanceof NotesFragment && 
                     mFragmentAtPos0 instanceof DummySectionFragment) {
                 return POSITION_NONE;
             }
             if (object instanceof DummySectionFragment && 
                     mFragmentAtPos0 instanceof NotesFragment) {
                 return POSITION_NONE;
             }
             return POSITION_UNCHANGED;
         }

     }

   public interface FirstPageFragmentListener {
	    void onSwitchToNextFragment();
	    void onSwitchToNextFragment(String retrieve, int value);
	}
   
   public static class MapsFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";
        LatLng place;
        private GoogleMap googleMap;
        SupportMapFragment mSupportMapFragment;
        String date, address, duration;
        double lat, longi;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.abc, container, false);
            mSupportMapFragment = SupportMapFragment.newInstance();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mapwhere, mSupportMapFragment);
            fragmentTransaction.commit();
            Bundle b=getActivity().getIntent().getExtras();
            String ll[]=new String[6];
            ll=b.getStringArray("LatLong");
            lat=Double.parseDouble(ll[0]);
            longi=Double.parseDouble(ll[1]);
            address=ll[2];
            duration=ll[3];
            date=ll[4];
        	place= new LatLng(lat, longi);
                       return rootView;
         }
        @Override
        public void onStart() {
          // TODO Auto-generated method stub
            super.onStart();

          if(mSupportMapFragment!=null){

              googleMap = mSupportMapFragment.getMap();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 14.0f));
            MarkerOptions mo=new MarkerOptions();
            mo.position(place);
            mo.title(address);
            mo.snippet("Visited on "+date+" for "+duration+" min");
            Marker m=googleMap.addMarker(mo);

            SQLiteDatabase db = getActivity().openOrCreateDatabase("MyDB1" , Context.MODE_PRIVATE, null);
    		Cursor z=db.rawQuery("SELECT * FROM AddressTable", null);
    		while(z.moveToNext())
    		{
    			String tdate = z.getString(z.getColumnIndex("Time"));
    			String dur = z.getString(z.getColumnIndex("Duration"));
    			String addr = z.getString(z.getColumnIndex("Address"));
    			double lat1=z.getDouble(z.getColumnIndex("Lat"));
    			double longi1=z.getDouble(z.getColumnIndex("Longi"));
    			if((tdate.substring(0, 18)).equals(date.substring(0, 18))&& !(lat1==lat && longi1==longi))
    			{
    		    	LatLng p= new LatLng(lat1,longi1 );
    				MarkerOptions n=new MarkerOptions();
    		        n.position(p);
    		        n.title(addr);
    		        n.icon(BitmapDescriptorFactory.fromResource(R.drawable.other_position));
    		        n.snippet("Visited on "+tdate+" for "+dur+" min");
    		        googleMap.addMarker(n);			
    		        }
            }
          }
    }


}

   public static class PreviousVisits extends Fragment {
	   String cur_date, cur_duration;
       public static final String ARG_SECTION_NUMBER = "section_number";

       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
               Bundle savedInstanceState) {
           View rootView = inflater.inflate(R.layout.previous_visits, container, false);
           Bundle args = getArguments();
           Bundle b=getActivity().getIntent().getExtras();
           String ll[]=new String[6];
           ll=b.getStringArray("LatLong");
           String address=ll[2];
           cur_duration=ll[3];
           cur_date=ll[4];
           SQLiteDatabase db = getActivity().openOrCreateDatabase("MyDB1" , Context.MODE_PRIVATE, null);
   			Cursor z=db.rawQuery("SELECT * FROM AddressTable", null);
   			while(z.moveToNext())
   			{
   				if(address.equals(z.getString(z.getColumnIndex("Address"))))
   			{
   					String date=z.getString(z.getColumnIndex("Time"));
   					String duration=z.getString(z.getColumnIndex("Duration"));
           date=date.substring(0, 17)+"\n"+date.substring(17);
           duration=duration+" min";
           TableLayout tl=(TableLayout)rootView.findViewById(R.id.table);  
           TableRow tr1 = new TableRow(getActivity());
           tr1.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
           ImageView iv=new ImageView(getActivity());
           iv.setBackgroundResource(R.drawable.calendar);
           tr1.addView(iv);
           TextView textview = new TextView(getActivity());
           textview.setText(date);
           textview.setTextColor(Color.BLUE);
           textview.setTextSize(18.00f);
           textview.setPadding(15, 10, 15, 10);
           tr1.addView(textview);
           ImageView clock=new ImageView(getActivity());
           clock.setBackgroundResource(R.drawable.clock);
           tr1.addView(clock);
           TextView dur= new TextView(getActivity());
           dur.setText(duration);
           dur.setTextColor(Color.BLUE);
           dur.setTextSize(20.00f);
           dur.setPadding(20, 10, 20, 10);
           tr1.addView(dur);
           tr1.setShowDividers(1);
           tl.addView(tr1, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
   			}
   			}
           return rootView;
        }
   }

   		public static class DummySectionFragment extends Fragment {

	    static FirstPageFragmentListener firstPageListener;
	    public String retrieve;
	    public int value;
	    EditText et;
	    public DummySectionFragment()
	    {
	    	
	    }
	    public DummySectionFragment ( FirstPageFragmentListener listener)
	    {
	    	this.firstPageListener = listener;
	    }
	    public DummySectionFragment(FirstPageFragmentListener listener, String retrieve, int value)
	    {
	    	this.firstPageListener= listener;
	    	this.retrieve = retrieve;
	    	this.value = value;
	    }
        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.notes_gui, container, false);
            Bundle args = getArguments();
            et=(EditText) rootView.findViewById(R.id.note);
            
			 Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "desyrel.ttf");
			 et.setTypeface(font);
            et.setText(retrieve);
   		
         return rootView;
        
         }
        
        public void backPressed() {
        	String text = et.getText().toString();
			SQLiteDatabase db= getActivity().openOrCreateDatabase("MyDB1" , getActivity().MODE_PRIVATE, null);
			Cursor z=db.rawQuery("SELECT * FROM AddressTable", null);
	        int count=z.getCount();
	        Bundle b=getActivity().getIntent().getExtras();
	           String ll[]=new String[6];
	           ll=b.getStringArray("LatLong");
	           int num=Integer.parseInt(ll[5]);

		    //    Toast.makeText(getActivity(), "value of seection"+Integer.toString(num), Toast.LENGTH_SHORT).show();
	        Cursor a=db.rawQuery("SELECT * FROM Note where Num = "+num, null);
	        int count_note=a.getCount();
	         a.moveToFirst();
	         int first = a.getInt(a.getColumnIndex("Position"));
		        //Toast.makeText(getActivity(), "first ofact"+Integer.toString(first), Toast.LENGTH_SHORT).show();
	        if(!text.equals(""))
	        {
	        	//this will not be used
	        if(value==-1 )
	        {
			db.execSQL("INSERT INTO Note VALUES("+count_note+","+(count-1)+",\""+text+"\");");
	        }
	        else
	        {
		        value=value+first;
	        	db.execSQL("UPDATE Note SET Notes=\""+text+"\" WHERE Position = "+value+";");

		       // Toast.makeText(getActivity(),"val ofpos"+ Integer.toString(value), Toast.LENGTH_SHORT).show();
	        }
	        if(!(retrieve.equals(text)))
	        Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();
		}
            firstPageListener.onSwitchToNextFragment();
        }
        
       
        
    }
   public static class NotesFragment extends Fragment{
	   static FirstPageFragmentListener firstPageListener;

       public String[] textViewValues;
       public static final String ARG_SECTION_NUMBER = "section_number";
       public int i;
       public NotesFragment()
       {
    	   
       }
       public NotesFragment(FirstPageFragmentListener listener)
       {
    	   this.firstPageListener = listener;
       }
       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
               Bundle savedInstanceState) {
           View rootView = inflater.inflate(R.layout.files_notes, container, false);
           Bundle args = getArguments();
           SQLiteDatabase db = getActivity().openOrCreateDatabase("MyDB1" ,Context.MODE_PRIVATE, null);
           Bundle b=getActivity().getIntent().getExtras();
           String ll[]=new String[6];
           ll=b.getStringArray("LatLong");
           int num=Integer.parseInt(ll[5]);
           Cursor c=db.rawQuery("SELECT * FROM Note", null);
           int len=c.getCount(); 	   
           GridView gv=(GridView) rootView.findViewById(R.id.gridView1);
           
           textViewValues=new String[len];
           i=0;
           while(c.moveToNext())
           {
        	   if(c.getInt(c.getColumnIndex("Num"))==num)
        	   {
        		   int strLen= ((c.getString(c.getColumnIndex("Notes"))).length()<20)?(c.getString(c.getColumnIndex("Notes"))).length():20;
        		   if(strLen<20)
        			   textViewValues[i]=c.getString(c.getColumnIndex("Notes")).substring(0, strLen);
        		   else
        			   textViewValues[i]=c.getString(c.getColumnIndex("Notes")).substring(0, strLen)+"...";
        		   i++;
        	   }
           }
           if(i==0)
        	   gv.setBackgroundResource(R.drawable.no_notes);
           gv.setAdapter(new TextViewAdapter(getActivity()));
           db.close();
        return rootView;
        }
       public class TextViewAdapter extends BaseAdapter {
           private Context context;
           public TextViewAdapter(Context context) {
               this.context = context;
           }

           public View getView(final int position, View convertView, ViewGroup parent) {

               LayoutInflater inflater = (LayoutInflater) context
                   .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

               View gridView;

               if (convertView == null) {

                   gridView = new View(context);

                   // get layout from mobile.xml
                   gridView = inflater.inflate(R.layout.grid_notes_item, null);
                   // set value into textview
                   TextView textView = (TextView) gridView
                           .findViewById(R.id.grid_item_label);
                   
      			 Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "desyrel.ttf");
      			 textView.setTypeface(font);
                   textView.setText(textViewValues[position]);
                   switch((position%4))
                   {
                   case 0:
                       textView.setBackgroundResource(R.drawable.note_3);
                       break;
                   case 1:
                       textView.setBackgroundResource(R.drawable.note_1);
                       break;
                   case 2:
                       textView.setBackgroundResource(R.drawable.note_2);
                       break;
                   case 3:
                       textView.setBackgroundResource(R.drawable.note_4);
                       break;
                	   
                   }
                   final String retrieve ;
                   SQLiteDatabase db= getActivity().openOrCreateDatabase("MyDB1" , getActivity().MODE_PRIVATE, null);
   				Bundle b=getActivity().getIntent().getExtras();
   	           String ll[]=new String[6];
   	           ll=b.getStringArray("LatLong");
   	           int num=Integer.parseInt(ll[5]);
   			    Cursor z=db.rawQuery("SELECT * FROM Note where Num = "+num, null);
   			    int p=position;
   			    z.moveToPosition(p);
   			   retrieve = z.getString(z.getColumnIndex("Notes"));
                   textView.setOnClickListener(new OnClickListener() {
           			
           			@Override
           			public void onClick(View arg0) {

           			 firstPageListener.onSwitchToNextFragment(retrieve,position);
           			 Log.d("POSITION",Integer.toString(position));
           			}
           		});

               } else {
                   gridView = (View) convertView;
               }

               return gridView;
           }

           @Override
           public int getCount() {
               return i;
           }

           @Override
           public Object getItem(int position) {
               return null;
           }

           @Override
           public long getItemId(int position) {
               return 0;
           }
       }
	
   }
   
  
   
   @SuppressWarnings("deprecation")
   public static class FilesFragment extends Fragment{
	   public String paths[];
	   public int i;
       public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.files_fragment, container, false);
        Bundle args = getArguments();

        
        SQLiteDatabase db = getActivity().openOrCreateDatabase("MyDB1" ,Context.MODE_PRIVATE, null);
        Bundle b=getActivity().getIntent().getExtras();
        String ll[]=new String[6];
        ll=b.getStringArray("LatLong");
        int num=Integer.parseInt(ll[5]);
        Cursor c=db.rawQuery("SELECT * FROM Photos", null);
        int len=c.getCount();
        paths=new String[len];
        i=0;
        while(c.moveToNext())
        {
     	   if(c.getInt(c.getColumnIndex("Num"))==num && (new File(c.getString(c.getColumnIndex("Path"))).length()!=0))
     	   {
     		   paths[i]=c.getString(c.getColumnIndex("Path"));
     		   i++;
     	   }
        }

			final ImageView imageView = (ImageView)rootView. findViewById(R.id.image1);
			if(i!=0)
			{imageView.setImageBitmap(getSmallBitmap(paths[0]));
        Gallery gallery = (Gallery) rootView.findViewById(R.id.gallery1);
			gallery.setAdapter(new ImageAdapter(getActivity()));
			gallery.setOnItemClickListener(new OnItemClickListener() {
   				public void onItemClick(AdapterView<?> parent, View v, int position,long id)
   		{
   					//Toast.makeText(getActivity(),"pic" + (position + 1) + " selected",
   					//		Toast.LENGTH_SHORT).show();
   		   			imageView.setImageBitmap(getSmallBitmap(paths[position]));
   		}
			});
			}
			else
				imageView.setBackgroundResource(R.drawable.no_photos);
     return rootView;
     }
		public Bitmap getSmallBitmap(String filePath) {

   		    File file = new File(filePath);
   		    long originalSize = file.length();
   		    final BitmapFactory.Options options = new BitmapFactory.Options();
   		    options.inJustDecodeBounds = true;
   		    BitmapFactory.decodeFile(filePath, options);
   		    options.inSampleSize = calculateInSampleSize(options, 240, 400);
   		    options.inJustDecodeBounds = false;
   		    Bitmap compressedImage = BitmapFactory.decodeFile(filePath, options);
   		    return compressedImage;
   		}
   		public int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {

   		    final int height = options.outHeight;
   		    final int width = options.outWidth;
   		    int inSampleSize = 1; // default to not zoom image

   		    if (height > reqHeight || width > reqWidth) {
   		             final int heightRatio = Math.round((float) height/ (float) reqHeight);
   		             final int widthRatio = Math.round((float) width / (float) reqWidth);
   		             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
   		    }
   		        return inSampleSize;
   		}

    public class ImageAdapter extends BaseAdapter {

   		private Context context;
   		private int itemBackground;
   		public ImageAdapter(Context c)
   		{
   			context = c;
   			// sets a grey background; wraps around the images
   			TypedArray a =getActivity().obtainStyledAttributes(R.styleable.MyGallery);
   			itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
   			a.recycle();
   		}
   		// returns the number of images
   		public int getCount() {
   			return i;
   		}
   		// returns the ID of an item
   		public Object getItem(int position) {
   			return position;
   		}
   		// returns the ID of an item
   		public long getItemId(int position) {
   			return position;
   		}
   		// returns an ImageView view
   		public View getView(int position, View convertView, ViewGroup parent) {
   			ImageView imageView = new ImageView(context);
   			imageView.setImageBitmap(getSmallBitmap(paths[position]));
   			imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
   			imageView.setBackgroundResource(itemBackground);
   			return imageView;
   		}

   	}
   }
   
}
