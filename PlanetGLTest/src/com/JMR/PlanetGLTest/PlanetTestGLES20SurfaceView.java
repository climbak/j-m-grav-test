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
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();
        
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
            	
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                
            	if (e.getPointerCount() == 1){

    
                _renderer.dX = -dx/100;
                _renderer.dY = -dy/100;
                
            	} else if (e.getPointerCount() == 2){
                // reverse direction of rotation above the mid-line
	                if (y > getHeight() / 2) {
	                  dx = dx * -1 ;
	                }
	    
	                // reverse direction of rotation to left of the mid-line
	                if (x < getWidth() / 2) {
	                  dy = dy * -1 ;
	                }
	              
	                _renderer.mAngle = (dx + dy) * TOUCH_SCALE_FACTOR;
            	} else if (e.getPointerCount() == 3){
                // reverse direction of rotation above the mid-line
	                if (y > getHeight() / 2) {
	                  dx = dx * -1 ;
	                }
	    
	                // reverse direction of rotation to left of the mid-line
	                if (x < getWidth() / 2) {
	                  dy = dy * -1 ;
	                }
	              
	                _renderer.mAngleZ = (dx + dy) * TOUCH_SCALE_FACTOR;
            	}
            	
                requestRender();
                break;
            case MotionEvent.ACTION_UP:
            	_renderer.mAngle = 0;
            	_renderer.mAngleZ = 0;
                _renderer.dX = 0;
                _renderer.dY = 0;
                
                if (x - mPreviousX < 0.01 && y - mPreviousY < 0.01) // Tap?
                {
                	_renderer.tap(x, y);
                }
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
