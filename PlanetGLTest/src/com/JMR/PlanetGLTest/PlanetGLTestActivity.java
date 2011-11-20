package com.JMR.PlanetGLTest;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class PlanetGLTestActivity extends Activity {
	private GLSurfaceView glView;
	public static PlanetGLTestActivity instance;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
        
        Geometry.Instance.setContext(this);
        
        glView = new PlanetTestGLES20SurfaceView(this);
        
        setContentView(glView);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        glView.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        glView.onResume();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, 0, Menu.NONE, "Exit");
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	if (item.getItemId() == 0){
    		System.exit(0);
    	}
    	
    	return true;
    }

}