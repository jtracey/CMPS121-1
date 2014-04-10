package edu.ucsc.cmps121_1_timer;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	
	private long counter;
	private CountDownTimer timer;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        counter = 0;
        timer = null;

        // Prevents the screen from dimming and going to sleep.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		counter = 0;
		timer = null;		
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
    
	public void clickStart(View v) {
		startCounter();
	}
	
	public void clickPlus(View v) {
		counter += 60 * 1000;
		displayCount();
	}
	
	public void clickMinus(View v) {
		counter -= 60 * 1000;
		counter = Math.max(0, counter);	
		displayCount();
	}
	
	public void clickReset(View v) {
		counter = 0;
		timer = null;
		displayCount();
	}
	

private void startCounter() {
    if (timer != null) {
        timer.cancel();
    }
    displayCount();
    if (counter > 0) {
        timer = new CountDownTimer(counter, 1000) {
                public void onTick(long remainingTimeMillis) {
                    counter = remainingTimeMillis;
                    displayCount();
                }
                public void onFinish() {
                    counter = 0;
                    displayCount();
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
