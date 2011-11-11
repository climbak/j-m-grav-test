package com.JMR.PlanetGLTest;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class PlanetTestGLES20SurfaceView extends GLSurfaceView {
	public PlanetTestGLES20SurfaceView(Context context) {
        super(context);
        
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(new PlanetTestGLES20Renderer());
    }
}
