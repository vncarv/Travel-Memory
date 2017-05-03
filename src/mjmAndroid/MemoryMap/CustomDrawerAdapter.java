package mjmAndroid.MemoryMap;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDrawerAdapter extends ArrayAdapter<Drawer_Item> {
	
	Context context;
	LayoutInflater inflater;
	List<Drawer_Item> drawerlist;

	public CustomDrawerAdapter(Context context, int resourceId,
			List<Drawer_Item> drawerlist ) {
		super(context, resourceId, drawerlist);
		this.context = context;
		this.drawerlist = drawerlist;
		inflater = LayoutInflater.from(context);
	}

	private class ViewHolder {
TextView title;
ImageView icon;
	}
	
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.drawer_item, null);
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.icon = (ImageView) view.findViewById(R.id.icon_drawer);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Drawer_Item di = drawerlist.get(position);
		holder.title.setText(di.gettitle());
		int id= di.getid();
		holder.icon.setImageResource(id);
		
		return view;
	}
	
	}