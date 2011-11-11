package com.JMR.PlanetGLTest;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class PlanetTestGLES20Renderer implements Renderer {
	// This is just a sort of fallback, we should probably write our shaders in separate files:
	private static final String defaultVertexShaderCode = 
		"attribute vec4 vPosition; \n" +
        "void main(){              \n" +
        " gl_Position = vPosition; \n" +
        "}                         \n";

	private static final String defaultFragmentShaderCode = 
        "precision mediump float;  \n" +
        "void main(){              \n" +
        " gl_FragColor = vec4 (0.63671875, 0.76953125, 0.22265625, 1.0); \n" +
        "}                         \n";
	
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

	// Helper method to take shader code and compile it.
	private int loadShader(int type, String shaderCode) {
		// Create the shader:
		int shader = GLES20.glCreateShader(type);
		
		// Add the source code and compile:
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		return shader;
	}
}
