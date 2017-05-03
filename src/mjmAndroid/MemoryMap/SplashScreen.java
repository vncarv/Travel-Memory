package mjmAndroid.MemoryMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		int id[]={R.id.textView1, R.id.textView2,R.id.textView3};
		for (int i = 0; i < 3; i++) {
		    TextView tv=(TextView) findViewById(id[i]);
		    Typeface font = Typeface.createFromAsset(getAssets(), "belligerent.ttf");
			 tv.setTypeface(font);
		    final Animation exitAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
		    exitAnim.setDuration(1500);
		    exitAnim.setStartOffset(1000 * i);
		    tv.startAnimation(exitAnim);
		    tv.setVisibility(TextView.INVISIBLE);
		    if(i==2)
		    {
		    	exitAnim.setAnimationListener(new Animation.AnimationListener(){
		    	    @Override
		    	    public void onAnimationStart(Animation arg0) {
		    	    }           
		    	    @Override
		    	    public void onAnimationRepeat(Animation arg0) {
		    	    }           
		    	    @Override
		    	    public void onAnimationEnd(Animation arg0) {
		    	    	Intent i=new Intent(getApplicationContext(), First_Screen.class);
		    	    	startActivity(i);
		    	    }
		    	});
		    }
		}
	}

}
