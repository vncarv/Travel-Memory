package mjmAndroid.MemoryMap;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewAdapter extends ArrayAdapter<String> {
	// Declare Variables
	Context context;
	LayoutInflater inflater;
	List<String> stringlist;
	private SparseBooleanArray mSelectedItemsIds;

	public ListViewAdapter(Context context, int resourceId,
			List<String> stringlist ) {
		super(context, resourceId, stringlist);
		mSelectedItemsIds = new SparseBooleanArray();
		this.context = context;
		this.stringlist = stringlist;
		inflater = LayoutInflater.from(context);
	}

	private class ViewHolder {
		TextView Notes_title;
	}

	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.notes_list_item, null);
			// Locate the TextViews in listview_item.xml
			holder.Notes_title = (TextView) view.findViewById(R.id.NotesTitle);
			 
			 Typeface font = Typeface.createFromAsset(context.getAssets(), "desyrel.ttf");
			 holder.Notes_title.setTypeface(font);
			// Locate the ImageView in listview_item.xml
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.Notes_title.setText(stringlist.get(position));
		return view;
	}

	@Override
	public void remove(String object) {
		stringlist.remove(object);
		notifyDataSetChanged();
	}

	public List<String> getString() {
		return stringlist;
	}

	public void toggleSelection(int position) {
		 SQLiteDatabase db = getContext().openOrCreateDatabase("MyDB1" , getContext().MODE_PRIVATE, null);
		 Cursor z=db.rawQuery("SELECT * FROM Note", null);
		 int temp = z.getCount()-position-1;
		z= db.rawQuery("SELECT * from Note WHERE Position = "+Integer.toString(temp) ,null);
		z.moveToFirst();
		temp = z.getInt(z.getColumnIndex("Num"));
		//Toast.makeText(getContext(), Integer.toString(temp), Toast.LENGTH_SHORT).show();

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