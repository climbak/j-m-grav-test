package com.JMR.PlanetGLTest;

import android.app.Activity;
import android.os.Bundle;
import android.opengl.GLSurfaceView;

public class PlanetGLTestActivity extends Activity {
	private GLSurfaceView glView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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

}