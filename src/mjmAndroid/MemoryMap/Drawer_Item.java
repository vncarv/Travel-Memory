package mjmAndroid.MemoryMap;

import android.widget.ImageView;

public class Drawer_Item {
String title;
int id;

public Drawer_Item(String title, int id)
{
	super();
	this.title=title;
	this.id = id;
}

public String gettitle()
{
	return this.title;
	
}
public void setTitle(String title)
{
	this.title=title;
}

public int getid()
{
	return this.id;
	
}
public void setIcon(int icon)
{
	this.id=icon;
}


}
