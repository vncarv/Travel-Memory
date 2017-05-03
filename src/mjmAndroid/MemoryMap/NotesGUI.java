package mjmAndroid.MemoryMap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class NotesGUI extends Fragment{
	EditText et;
	String retrieved;
	int value;
	int bydrawer;
	public NotesGUI()
	{}
	public NotesGUI(final String text,int value,int bydrawer)
	{
		this.retrieved=text;
		this.value=value;
		this.bydrawer =bydrawer;
		First_Screen.bydrawer=bydrawer;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.notes_gui, container,false);
		
		 et=(EditText) rootView.findViewById(R.id.note);
		 
		 Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "desyrel.ttf");
		 et.setTypeface(font);
		 et.setText(retrieved);
		return rootView;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
			String text = et.getText().toString();
			SQLiteDatabase db= getActivity().openOrCreateDatabase("MyDB1" , getActivity().MODE_PRIVATE, null);
			Cursor z=db.rawQuery("SELECT * FROM AddressTable", null);
	        int count=z.getCount();
	        Cursor a=db.rawQuery("SELECT * FROM Note", null);
	        int count_note=a.getCount();
	        
	        SharedPreferences service=getActivity().getSharedPreferences("MYPREFS",Context.MODE_PRIVATE);
			String tval=service.getString("tvalue", "false");
			String serv = service.getString("service","true");
			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
			if(!text.equals(""))
			{
			 if(tval.equals("true")&& serv.equals("true"))
	        {
	        if(value==-1 )
	        {
			db.execSQL("INSERT INTO Note VALUES("+count_note+","+(count-1)+",\""+text+"\");");
	        }
	        else
	        {
		        value=count_note-value-1;
	        	db.execSQL("UPDATE Note SET Notes=\""+text+"\" WHERE Position = "+value+";");
	        }
	        if(!(retrieved.equals(text)))
	        Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();
		
		
			}
			 else if((tval.equals("false"))||(tval.equals("true")&&serv.equals("false")))
			{
				 if(value==-1 )
			        {
					 int x=-1;
					db.execSQL("INSERT INTO Note VALUES("+count_note+","+x+",\""+text+"\");");
			        }
			        else
			        {
				        value=count_note-value-1;
			        	db.execSQL("UPDATE Note SET Notes=\""+text+"\" WHERE Position = "+value+";");
			        }
			        if(!(retrieved.equals(text)))
			        Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();
				
				
			}
			}
        
       
	}
}
