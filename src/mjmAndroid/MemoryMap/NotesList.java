package mjmAndroid.MemoryMap;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NotesList extends Fragment
{
	ListView listView;
	ListViewAdapter listviewadapter;
	List<String> notes_arr = new ArrayList<String>();
	@Override
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
		{
		notes_arr.clear();
		View rootView = inflater.inflate(R.layout.notes_list, container,false);
		listView = (ListView) rootView.findViewById(R.id.list_notes);
		SQLiteDatabase db= getActivity().openOrCreateDatabase("MyDB1" , Context.MODE_PRIVATE, null);
		Cursor z=db.rawQuery("SELECT * FROM Note", null);
        if(z.getCount()==0)
        {
 //       	Toast.makeText(getActivity(), "No notes present", Toast.LENGTH_LONG).show();
			RelativeLayout rl=(RelativeLayout) rootView.findViewById(R.id.rl_notes);
     	   rl.setBackgroundResource(R.drawable.no_notes);
        	return rootView;
        }
        z.moveToLast();
        do
        {
        	int strLen= ((z.getString(z.getColumnIndex("Notes"))).length()<20)?(z.getString(z.getColumnIndex("Notes"))).length():20;
        	String str=z.getString(z.getColumnIndex("Notes")).substring(0, strLen);
        	StringBuilder buffer = new StringBuilder(str);
        	for(int i=0;i<str.length();i++)
        		if(buffer.charAt(i)=='\n')
        			buffer.setCharAt(i, ' ');
        	str= new String(buffer);
        	if(strLen==20)
        		str=str+"...";
        	notes_arr.add(str);
        }
        while(z.moveToPrevious());
    	listviewadapter = new ListViewAdapter(getActivity(), R.layout.notes_list_item,
				notes_arr);

         listView.setAdapter(listviewadapter); 
         listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        ////////////////////////////////////////LONG CLICK
     
         listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

 			@Override
 			public void onItemCheckedStateChanged(ActionMode mode,
 					int position, long id, boolean checked) {
 				// Capture total checked items
 				final int checkedCount = listView.getCheckedItemCount();
 				// Set the CAB title according to total checked items
 				mode.setTitle(checkedCount + " Selected");
 				// Calls toggleSelection method from ListViewAdapter Class
 				listviewadapter.toggleSelection(position);
 			}

 			@Override
 			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
 				SQLiteDatabase db= getActivity().openOrCreateDatabase("MyDB1" , getActivity().MODE_PRIVATE, null);
				switch (item.getItemId()) {
 				
 				case R.id.delete:
 					// Calls getSelectedIds method from ListViewAdapter Class
 					SparseBooleanArray selected = listviewadapter
 							.getSelectedIds();
 					// Captures all selected ids with a loop
 					Cursor z=db.rawQuery("SELECT * FROM Note", null);
 					int count= z.getCount();
 					for (int i = (selected.size() - 1); i >= 0; i--) {
 						if (selected.valueAt(i)) {
 							String selecteditem = listviewadapter
 									.getItem(selected.keyAt(i));
 							// Remove selected items following the ids
 							listviewadapter.remove(selecteditem);
 							int pos=count-selected.keyAt(i)-1;
 							//Toast.makeText(getActivity(), Integer.toString(pos), Toast.LENGTH_SHORT).show();
 						    db.execSQL("DELETE FROM Note WHERE Position="+pos+";");
 						}
 					}
 					int c=0;
 					 z=db.rawQuery("SELECT * FROM Note", null);
 					 count = z.getCount();
 					if(count!=0)
 					
					{z.moveToFirst();
 					do
 					{
 						int temp=z.getInt(z.getColumnIndex("Position"));
 						db.execSQL("UPDATE Note SET Position="+c+" where Position="+temp+";");
 			        	c++;
 					}
 					while(z.moveToNext());}
 					// Close CAB
 					mode.finish();
 					return true;
 				default:
 					return false;
 				}
 			}

 			@Override
 			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
 				mode.getMenuInflater().inflate(R.menu.activity_main, menu);
 				return true;
 			}

 			@Override
 			public void onDestroyActionMode(ActionMode mode) {
 				listviewadapter.removeSelection();
 			}

 			@Override
 			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
 				// TODO Auto-generated method stub
 				return false;
 			}
 		});
 	
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			SQLiteDatabase db= getActivity().openOrCreateDatabase("MyDB1" , getActivity().MODE_PRIVATE, null);
			Cursor z=db.rawQuery("SELECT * FROM Note", null);
		    int p=z.getCount()-position-1;
		    z.moveToPosition(p);
		    String retrieved = z.getString(z.getColumnIndex("Notes"));
		    
		    FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			Fragment frag = new NotesGUI(retrieved,position,1);
			First_Screen.frag = frag;
			ft.replace(R.id.container,frag);
			ft.addToBackStack(null);
			ft.commit();
		    

			}
		});

        
		return rootView;
	}

}
