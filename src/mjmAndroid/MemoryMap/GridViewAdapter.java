package mjmAndroid.MemoryMap;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends ArrayAdapter {
	private Context context;
	private int layoutResourceId;
	ArrayList<String> paths;

	public GridViewAdapter(Context context, int layoutResourceId,ArrayList<String> paths) {
		super(context, layoutResourceId, paths);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.paths = paths;
	}
	public Bitmap getSmallBitmap(String filePath) {

		    File file = new File(filePath);
		    long originalSize = file.length();
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(filePath, options);
		    options.inSampleSize = calculateInSampleSize(options, 200, 200);
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


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) row.findViewById(R.id.image);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		holder.image.setImageBitmap(getSmallBitmap(paths.get(position)));
		return row;
	}

	static class ViewHolder {
		ImageView image;
	}
}