package mjmAndroid.MemoryMap;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class List extends Fragment {
	public RelativeLayout rl;
	MyCustomAdapter dataAdapter = null;
	ListView listView;
	ArrayList<Address_info> address_arr = new ArrayList<Address_info>();
	public List() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_history_list, container,
				false);
		listView = (ListView) rootView.findViewById(R.id.list);
		rl=(RelativeLayout) rootView.findViewById(R.id.rl);
		displayListView();
		return rootView;
	}


	public class MyCustomAdapter extends ArrayAdapter<Address_info> {
		// Declare Variables
		Context context;
		LayoutInflater inflater;
		ArrayList<Address_info> addresslist;
		private SparseBooleanArray mSelectedItemsIds;

		public MyCustomAdapter(Context context, int resourceId,
				ArrayList<Address_info> addresslist) {
			super(context, resourceId, addresslist);
			mSelectedItemsIds = new SparseBooleanArray();
			this.context = context;
			this.addresslist= addresslist;
			inflater = LayoutInflater.from(context);
		}

		private class ViewHolder {
			 TextView Address;
			   TextView Date;
			   TextView Duration;
		}

		public View getView(int position, View view, ViewGroup parent) {
			final ViewHolder holder;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(
					     Context.LAYOUT_INFLATER_SERVICE);
					   view = vi.inflate(R.layout.list_item, null);
				holder = new ViewHolder();
				// Locate the TextViews in listview_item.xml
				holder.Address = (TextView) view.findViewById(R.id.Address);
				   holder.Date = (TextView) view.findViewById(R.id.Date);
				   holder.Duration = (TextView) view.findViewById(R.id.Duration);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			 Address_info address_obj = address_arr.get(position);
			   holder.Address.setText(address_obj.getaddress());
			   holder.Date.setText(address_obj.getdate());
			   holder.Duration.setText(address_obj.getduration()+" min");
			return view;
		}

		@Override
		public void remove(Address_info object) {
			addresslist.remove(object);
			notifyDataSetChanged();
		}

		public ArrayList<Address_info> getAddress() {
			return addresslist;
		}

		public void toggleSelection(int position) {
//			 SQLiteDatabase db = getContext().openOrCreateDatabase("MyDB1" , getContext().MODE_PRIVATE, null);
//			 Cursor z=db.rawQuery("SELECT * FROM AddressTable", null);
//			 int temp = z.getCount()-position-1;
//			z= db.rawQuery("SELECT * from AddressTable WHERE Num = "+Integer.toString(temp) ,null);
//			z.moveToFirst();
//			temp = z.getInt(z.getColumnIndex("Num"));
//			Toast.makeText(getContext(), Integer.toString(temp), Toast.LENGTH_SHORT).show();
			selectView(position, !mSelectedItemsIds.get(position));
		}

		public void removeSelection() {
			mSelectedItemsIds = new SparseBooleanArray();
			notifyDataSetChanged();
		}

		public void selectView(int position, boolean value) {
			if (value)
				mSelectedItemsIds.put(position, value);
			else
				mSelectedItemsIds.delete(position);
			notifyDataSetChanged();
		}

		public int getSelectedCount() {
			return mSelectedItemsIds.size();
		}

		public SparseBooleanArray getSelectedIds() {
			return mSelectedItemsIds;
		}
	}
	
	 private void displayListView() {
	
		 SQLiteDatabase db = getActivity().openOrCreateDatabase("MyDB1" , Context.MODE_PRIVATE, null);
		 db.execSQL("CREATE TABLE IF NOT EXISTS AddressTable (Num INT , Address VARCHAR, Time VARCHAR, Duration INT, Lat DOUBLE, Longi DOUBLE);");
		// db.execSQL("INSERT INTO AddressTable VALUES (3, \"hke\", \"Fhi, Mar 13, 2015 20:56:09\",15,25.543,78.987,\"\");");
		 	Cursor z=db.rawQuery("SELECT * FROM AddressTable", null);
		 	z.moveToLast();
		 	int count=z.getCount();
		 	if(count!=0)
		 	{
	     	do
		        {
	     			String taddress = z.getString(z.getColumnIndex("Address"));
		        	int tduration=z.getInt(z.getColumnIndex("Duration"));
					String tdate = z.getString(z.getColumnIndex("Time"));
					double lat=z.getDouble(z.getColumnIndex("Lat"));
					double longi=z.getDouble(z.getColumnIndex("Longi"));
	  	        	Address_info add_obj = new Address_info(taddress,tdate,Integer.toString(tduration), lat, longi);
	  		     	address_arr.add(add_obj);
		        }
	     	while(z.moveToPrevious());
		 	}
		 	else
		 	{
		 		ImageView iv=new ImageView(getActivity());
		 		iv.setImageResource(R.drawable.no_entry);
		 		iv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		 		rl.addView(iv);
		 	return;
		 	}
	     	dataAdapter = new MyCustomAdapter(getActivity(),
			R.layout.list_item, address_arr);
			listView.setAdapter(dataAdapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

				@Override
				public void onItemCheckedStateChanged(ActionMode mode,
						int position, long id, boolean checked) {
					// Capture total checked items
					final int checkedCount = listView.getCheckedItemCount();
					// Set the CAB title according to total checked items
					mode.setTitle(checkedCount + " Selected");
					// Calls toggleSelection method from ListViewAdapter Class
					dataAdapter.toggleSelection(position);
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
					case R.id.delete:
						// Calls getSelectedIds method from ListViewAdapter Class
						SparseBooleanArray selected = dataAdapter
								.getSelectedIds();
						SQLiteDatabase db= getActivity().openOrCreateDatabase("MyDB1" , getActivity().MODE_PRIVATE, null);
						Cursor z=db.rawQuery("SELECT * FROM AddressTable", null);
	 					int count= z.getCount();
						// Captures all selected ids with a loop
						for (int i = (selected.size() - 1); i >= 0; i--) {
							if (selected.valueAt(i)) {
								Address_info selecteditem = dataAdapter
										.getItem(selected.keyAt(i));
								// Remove selected items following the ids
								dataAdapter.remove(selecteditem);
								int pos=count-selected.keyAt(i)-1;
	 							
	 						    db.execSQL("DELETE FROM AddressTable WHERE Num="+pos+";");
		 						db.execSQL("UPDATE Note SET Num=-1 WHERE Num="+pos+";");
		 						Cursor a=db.rawQuery("SELECT * FROM Photos where Num="+pos, null);
		 						if(a.getCount()!=0)
		 							db.execSQL("DELETE FROM Photos WHERE Num="+pos+";");
							}
						}
						int c=0;
	 					 z=db.rawQuery("SELECT * FROM AddressTable", null);
	 					 count = z.getCount();
	 					if(count!=0)
	 					
						{z.moveToFirst();
	 					do
	 					{
	 						int temp=z.getInt(z.getColumnIndex("Num"));
	 						db.execSQL("UPDATE AddressTable SET Num="+c+" WHERE Num="+temp+";");
	 						db.execSQL("UPDATE Note SET Num = "+c+" WHERE Num = "+temp+";");
	 						db.execSQL("UPDATE Photos SET Num = "+c+" WHERE Num = "+temp+";");
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
					// TODO Auto-generated method stub
					dataAdapter.removeSelection();
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
					Address_info add=address_arr.get(position);
					Intent i=new Intent(getActivity(), DetailsActivity.class);
					String value[]=new String[6];
					SQLiteDatabase db = getActivity().openOrCreateDatabase("MyDB1" , Context.MODE_PRIVATE, null);
					Cursor z=db.rawQuery("SELECT count(*) as Count FROM AddressTable",null);
					z.moveToFirst();
					int num=z.getInt(z.getColumnIndex("Count"));
					num=num-position-1;
					value[0]=Double.toString(add.lat);
					value[1]=Double.toString(add.longi);
					value[2]=add.address;
					value[3]=add.duration;
					value[4]=add.date;
					value[5]=Integer.toString(num);
					i.putExtra("LatLong", value);
					startActivity(i);

					final LatLng place= new LatLng(25.268, 62.122);
				}
			});
	 }
	
}