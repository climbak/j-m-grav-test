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
    private ParticleSystem _expTestSys;
    private ParticleEffect _expTestEff;
    
    public float mAngle, mAngleZ;
	public float dX, dY;
    
	public PlanetTestGLES20Renderer() {
		super();
		//_background = new Starfield();
	}
	
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
        _expTestEff.update();
        _expTestSys.draw(_cameraMatrix);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Update the GL viewport:
		GLES20.glViewport(0, 0, width, height);
		
		float ratio = (float) width / height;
        Log.d("onSurfaceChanged", "called");
        // this projection matrix is applied to object coodinates
        // in the onDrawFrame() method
        Matrix.frustumM(_projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 7);
        Matrix.setLookAtM(_lookAtMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(_cameraMatrix, 0, _projectionMatrix, 0, _lookAtMatrix, 0);
        GameBoard.Instance.viewPortChange(width,height);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set background fill color to black:
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		//GLES20.glEnable(GLES20.GL_TEXTURE_2D);	// Enable texturing
		//Log.d("Renderer.onSurfaceCreated$Enable_Texturing.error",new Integer(GLES20.glGetError()).toString());

		GLES20.glEnable(GLES20.GL_BLEND);		// Enable blending
		Log.d("Renderer.onSurfaceCreated$Enable_Blending.error",new Integer(GLES20.glGetError()).toString());

		/*
		 * The following is equivalent to the below formula:
		 * final_pixel_color = new_color * 1.0
		 * This sounds redundant, but it's important to set this by default once
		 * we enable blending. IF YOU CHANGE THE BLEND FUNC WHEN YOU DRAW YOUR SHIT,
		 * CHANGE IT THE FUCK BACK SO YOU DON'T SCREW UP EVERYONE ELSE'S SHIT!!
		 * Seriously. 
		 */
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_SRC_COLOR);
		Log.d("Renderer.onSurfaceCreated$glBlendFunc.error",new Integer(GLES20.glGetError()).toString());

		_background = new Starfield();
		
		Log.d("onSurfaceCreated", "called");
		
        GameBoard.Instance.create();
        GameBoard.Instance.add(new PlanetDrawable(1, 0, 30));
        GameBoard.Instance.add(new PlanetDrawable(-1, 0, 30));

        _expTestSys = new ParticleSystem(3000, "particle_tex2");
        _expTestEff = new ExplosionEffect(2000,250,_expTestSys,new float[]{0,0},250);
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

	public void tap(float x, float y) {
		this._expTestEff.setCenter(x, y);
		this._expTestEff.start();
	}
}
