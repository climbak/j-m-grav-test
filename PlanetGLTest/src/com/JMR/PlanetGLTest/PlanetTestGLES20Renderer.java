package com.JMR.PlanetGLTest;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class PlanetTestGLES20Renderer implements Renderer {

	@Override
	public void onDrawFrame(GL10 unused) {
		// Blank the frame:
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Update the GL viewport:
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set background fill color to black:
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

}
