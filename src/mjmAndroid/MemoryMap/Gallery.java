package mjmAndroid.MemoryMap;

import java.io.File;
import java.util.ArrayList;

import mjmAndroid.MemoryMap.List.MyCustomAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Gallery extends Fragment{
	MyCustomAdapter dataAdapter = null;
	ListView listView;
	ArrayList<String> paths=new ArrayList<String>();
		private GridView gridView=null;
		private GridViewAdapter customGridAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_gallery,
				container, false);
		SQLiteDatabase db = getActivity().openOrCreateDatabase("MyDB1" ,Context.MODE_PRIVATE, null);
		Cursor c=db.rawQuery("SELECT * FROM Photos", null);
		paths.clear();
		
		if(c.getCount()!=0)
		{while(c.moveToNext())
        {
     	   if((new File(c.getString(c.getColumnIndex("Path"))).length()!=0))
     	   {
     		   paths.add(c.getString(c.getColumnIndex("Path")));
     	   }
        }
		}
		else
		{
			RelativeLayout rl=(RelativeLayout) rootView.findViewById(R.id.rl_gallery);
     	   rl.setBackgroundResource(R.drawable.no_photos);
     return rootView;
		}
		gridView = (GridView) rootView.findViewById(R.id.gridView);
		customGridAdapter = new GridViewAdapter(getActivity(), R.layout.row_item, paths);
		gridView.setAdapter(customGridAdapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {

			    FragmentManager fm=getFragmentManager();
				FragmentTransaction ft=fm.beginTransaction();
				Fragment frag = new GalleryPhotoView(position, paths);
				First_Screen.frag = frag;
				ft.replace(R.id.container,frag);
				ft.addToBackStack(null);
				ft.commit();
			}

	});
		return rootView;
	}

	
}
