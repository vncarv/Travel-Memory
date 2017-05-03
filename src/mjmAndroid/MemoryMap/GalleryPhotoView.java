package mjmAndroid.MemoryMap;

import java.io.File;
import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryPhotoView extends Fragment {
ArrayList<String> paths;
int position;
GestureDetector gd;
View.OnTouchListener gestureListener;
	public GalleryPhotoView(int position, ArrayList<String> paths)
	{
		this.position=position;
		this.paths=paths;
	}
	
	public Bitmap getSmallBitmap(String filePath) {

	    File file = new File(filePath);
	    long originalSize = file.length();
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);
	    options.inSampleSize = calculateInSampleSize(options, 480, 800);
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
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.gallery_photo_view,
			container, false);
	ImageView iv=(ImageView) rootView.findViewById(R.id.image_gallery);
	iv.setImageBitmap(getSmallBitmap(paths.get(position)));
	 final GestureDetector gesture = new GestureDetector(getActivity(),
	            new GestureDetector.SimpleOnGestureListener() {

	                @Override
	                public boolean onDown(MotionEvent e) {
	                    return true;
	                }

	                @Override
	                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	                    float velocityY) {
	                    final int SWIPE_MIN_DISTANCE = 120;
	                    final int SWIPE_MAX_OFF_PATH = 250;
	                    final int SWIPE_THRESHOLD_VELOCITY = 200;
	                    try {
	                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                            return false;
	                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
	                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
	                        {
	                        swipeRight();
	                        }
	                        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
	                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		                       swipeLeft();   }
	                    } catch (Exception e) {
	                        // nothing
	                    }
	                    return super.onFling(e1, e2, velocityX, velocityY);
	                }

					private void swipeLeft() {
						if(position!=0)
						{	
							position-=1;
						    FragmentManager fm=getFragmentManager();
							FragmentTransaction ft=fm.beginTransaction();
							Fragment frag = new GalleryPhotoView(position, paths);
							First_Screen.frag = frag;
							ft.replace(R.id.container,frag);
							ft.addToBackStack(null);
							ft.commit();
					}
	 }
					SQLiteDatabase db = getActivity().openOrCreateDatabase("MyDB1" ,Context.MODE_PRIVATE, null);
					Cursor c=db.rawQuery("SELECT * FROM Photos", null);
					
					private void swipeRight() {
						if(position<(c.getCount()-1))
						{
							position+=1;
							FragmentManager fm=getFragmentManager();
							FragmentTransaction ft=fm.beginTransaction();
							Fragment frag = new GalleryPhotoView(position, paths);
							First_Screen.frag = frag;
							ft.replace(R.id.container,frag);
							ft.addToBackStack(null);
							ft.commit();
						}
						}
	            });

	        iv.setOnTouchListener(new View.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                return gesture.onTouchEvent(event);
	            }
	        });
      
	return rootView;
}

	
}
