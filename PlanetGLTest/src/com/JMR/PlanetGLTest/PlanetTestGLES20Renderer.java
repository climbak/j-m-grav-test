package com.JMR.PlanetGLTest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

public class PlanetTestGLES20Renderer implements Renderer {
	// This is just a sort of fallback, we should probably write our shaders in separate files:
	private static final String defaultVertexShaderCode = 
		"uniform mat4 uMVPMatrix;   \n" +
		"attribute vec4 vPosition; \n" +
        "void main(){              \n" +
        " gl_Position = uMVPMatrix * vPosition; \n" +
        "}                         \n";

	private static final String defaultFragmentShaderCode = 
        "precision mediump float;  \n" +
        "void main(){              \n" +
        " gl_FragColor = vec4 (0.63671875, 0.76953125, 0.22265625, 1.0); \n" +
        "}                         \n";
	
	private GameBoard theGame;
	private int muMVPMatrixHandle;
    private float[] mMVPMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private float[] mProjMatrix = new float[16];
    
    public float mAngle;
	
	@Override
	public void onDrawFrame(GL10 unused) {
		// Blank the frame:
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		      
        // Apply a ModelView Projection transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        
        theGame.draw(mMVPMatrix);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Update the GL viewport:
		GLES20.glViewport(0, 0, width, height);
		
		float ratio = (float) width / height;
        
        // this projection matrix is applied to object coodinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        theGame.viewPortChange(width,height);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set background fill color to black:
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		theGame = new GameBoard();
	}

	// Helper method to take shader code and compile it.
	public static int loadShader(int type, String shaderCode) {
		// Create the shader:
		int shader = GLES20.glCreateShader(type);
		
		// Add the source code and compile:
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		return shader;
	}
}
