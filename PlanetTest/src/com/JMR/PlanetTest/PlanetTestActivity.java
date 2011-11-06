package com.JMR.PlanetTest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.JMR.PlanetTest.TestView;

public class PlanetTestActivity extends Activity {
    /** Called when the activity is first created. */
	TestView mTestView;
	Thread mTestThread;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("PlanetTestActivity","super.onCreate()");
        super.onCreate(savedInstanceState);
        
        /* DEBUG */
        Log.d("PlanetTestActivity", "Beginning activity creation.");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        Log.d("PlanetTestActivity", "Setting Content View");
        setContentView(R.layout.main);
        
        Log.d("PlanetTestActivity", "Creating View.");
        mTestView = (TestView) findViewById(R.id.test);
        mTestThread = mTestView.getThread();
        
        Log.d("PlanetTestActivity", "Starting thread.");
        mTestThread.start();
    }
    
    @Override
    public void onPause() {
    	((TestView.TestThread)mTestThread).running = false;
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	((TestView.TestThread)mTestThread).running = true;
    	super.onResume();
    }
}