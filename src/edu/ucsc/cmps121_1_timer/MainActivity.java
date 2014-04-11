package edu.ucsc.cmps121_1_timer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private long counter;
	private CountDownTimer timer;
	private MediaPlayer beep;
	private Boolean mpavail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = 0;
        timer = null;
        beep = MediaPlayer.create(getApplicationContext(), R.raw.alarmclock_mechanical_trim);
        mpavail = true;
        
        // Prevents the screen from dimming and going to sleep.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

	@Override
	protected void onResume() {
		super.onResume();
		if (!mpavail)
			beep = MediaPlayer.create(getApplicationContext(), R.raw.alarmclock_mechanical_trim);
		mpavail = true;
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onPause() {
    	beep.release();
    	mpavail = false;
    	super.onPause();
    }
    
    @Override
    public void onStop() {
    	beep.release();
    	mpavail = false;
    	super.onStop();
    }

	public void clickStart(View v) {
		if (timer == null)
			startCounter();
		else {
			timer.cancel();
			timer = null;
		}
		
		if (beep.isPlaying())
			beep.pause();
	}

	public void clickPlus(View v) {
		counter += 60 * 1000;
		if (timer == null)
			displayCount();
		else startCounter();
	}

	public void clickMinus(View v) {
		counter -= 60 * 1000;
		counter = Math.max(0, counter);
		if (timer == null)
			displayCount();
		else if (counter == 0)
			clearTimer();
		else startCounter();
	}

	public void clickReset(View v) {
		clearTimer();
		if (beep.isPlaying())
			beep.pause();
	}

	private void clearTimer() {
		counter = 0;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		displayCount();
	}

	private void startCounter() {
	    if (timer != null) {
	        timer.cancel();
	    }
	    displayCount();
	    if (counter > 0) {
	    	// 100ms is far too often for a single-second precision timer,
	    	// but the way the timer class works makes the timer lag by the
	    	// given amount every time the + or - buttons are pressed.
	        timer = new CountDownTimer(counter, 100) {
	                @Override
					public void onTick(long remainingTimeMillis) {
	                    counter = remainingTimeMillis;
	                    displayCount();
	                }
	                @Override
					public void onFinish() {
	                    clearTimer();
	                    if (!mpavail) {
	                    	Log.d("test", "Beep is null");
	                    	beep = MediaPlayer.create(getApplicationContext(), R.raw.alarmclock_mechanical_trim);
	                    	mpavail = true;
	                    }
	                    beep.seekTo(0);
	                    beep.start();
	                }
	            };
	        timer.start();
	    }
	}


	private void displayCount() {
		TextView timetext = (TextView) findViewById(R.id.textView1);
		long seconds = (counter/1000)%60;
		long minutes = counter/(60*1000);
		// A little string formatting magic to keep leading 0 in seconds <10
		timetext.setText(String.format("%d:%02d", minutes, seconds));
	}

}
