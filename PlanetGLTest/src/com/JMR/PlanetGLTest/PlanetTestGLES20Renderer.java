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
import android.util.Log;

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
	
    private float[] _cameraMatrix = new float[16];
    private float[] _lookAtMatrix = new float[16];
    private float[] _projectionMatrix = new float[16];
    
    private GLDrawable _background;
    
    public float mAngle, mAngleZ;
	public float dX, dY;
    
	@Override
	public void onDrawFrame(GL10 unused) {
		// Blank the frame:
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		//Matrix.setIdentityM(_projectionMatrix, 0);
		Matrix.rotateM(_cameraMatrix, 0, mAngle, 0, 0, 1f);
		Matrix.rotateM(_cameraMatrix, 0, mAngleZ, 1f, 0, 0);
		// Matrix.translateM(_cameraMatrix, 0, dX, dY, 0);
		
		PlanetDrawable.light[0] += dX;
		PlanetDrawable.light[2] += dY;
		
		_background.draw(_cameraMatrix);
		
        GameBoard.Instance.draw(_cameraMatrix);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Update the GL viewport:
		GLES20.glViewport(0, 0, width, height);
		
		float ratio = (float) width / height;
        Log.d("onSurfaceChanged", "called");
        // this projection matrix is applied to object coodinates
        // in the onDrawFrame() method
        Matrix.frustumM(_projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 12);
        Matrix.setLookAtM(_lookAtMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(_cameraMatrix, 0, _projectionMatrix, 0, _lookAtMatrix, 0);
        GameBoard.Instance.viewPortChange(width,height);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set background fill color to black:
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		_background = new Starfield();
		
		Log.d("onSurfaceCreated", "called");
		
        GameBoard.Instance.create();
        GameBoard.Instance.add(new PlanetDrawable(1, 0, 30));
        GameBoard.Instance.add(new PlanetDrawable(-1, 0, 30));

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
