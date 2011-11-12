package com.JMR.PlanetGLTest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class PlanetTestGLES20SurfaceView extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 360; //320;
    private PlanetTestGLES20Renderer _renderer;
    private float mPreviousX;
    private float mPreviousY;
	
	public PlanetTestGLES20SurfaceView(Context context) {
        super(context);
        
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        
        // set the mRenderer member
        _renderer = new PlanetTestGLES20Renderer();
        setRenderer(_renderer);
        
        // Render the view only when there is a change
        // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        // Set the Renderer for drawing on the GLSurfaceView
        //setRenderer(new PlanetTestGLES20Renderer());

    }
	
	@Override 
    public boolean onTouchEvent(MotionEvent e) {
       return false;
    }
}
