package com.JMR.PlanetGLTest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class PlanetTestGLES20Renderer implements Renderer {
	// This is just a sort of fallback, we should probably write our shaders in separate files:
	public static final int NUM_BOOMS = 10;
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
	
    public float[] _viewProjectionMatrix = new float[16];
    public float[] _viewMatrix = new float[16];
    public float[] _projectionMatrix = new float[16];
    
    private GLDrawable _background;
//    private ParticleSystem _expTestSys;
//    private ParticleEffect _expTestEff;
    private LinkedList<ParticleSystem> _expTestSys;
    private LinkedList<ParticleEffect> _expTestEff;
    
    public float mAngle, mAngleZ;
	public float dX, dY;
	
	public int currBoom;
    
	public PlanetTestGLES20Renderer() {
		super();
		//_background = new Starfield();
	}

	public void onDrawFrame(GL10 unused) {
		// Blank the frame:
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		//Matrix.setIdentityM(_projectionMatrix, 0);
		///Matrix.rotateM(_viewProjectionMatrix, 0, mAngle, 0, 0, 1f);
		///Matrix.rotateM(_viewProjectionMatrix, 0, mAngleZ, 1f, 0, 0);
		// Matrix.translateM(_cameraMatrix, 0, dX, dY, 0);
		Camera.instance.rotate(mAngleZ, 0.f, mAngle);
		
		PlanetDrawable.light[0] += dX;
		PlanetDrawable.light[2] += dY;
		
		_background.draw(Camera.instance.mViewProjectionMatrix);
		
        GameBoard.Instance.draw(Camera.instance.mViewProjectionMatrix);
        
        
        Iterator<ParticleSystem> i = _expTestSys.iterator();
        Iterator<ParticleEffect> e = _expTestEff.iterator();
        LinkedList<ParticleSystem> sysToRemove = new LinkedList<ParticleSystem>();
        LinkedList<ParticleEffect> effToRemove = new LinkedList<ParticleEffect>();
        ParticleSystem sys;
        ParticleEffect eff;
        while(i.hasNext())
        {
        	sys = i.next();
        	eff = e.next();
        	
        	if (!eff.isRunning)
        	{
        		sysToRemove.add(sys);
        		effToRemove.add(eff);
//        		_expTestEff.remove(eff);
//        		_expTestSys.remove(sys);
        		continue;
        	}
        	eff.update();
        	sys.draw(Camera.instance.mViewProjectionMatrix);
        }
	    
//        i = sysToRemove.iterator();
//        while(i.hasNext())
//        {
//        	_expTestSys.remove(i.next());
//        }
//        
//        e = effToRemove.iterator();
//        while(e.hasNext())
//        {
//        	_expTestEff.remove(e.next());
//        }
//        _expTestEff.update();
//        _expTestSys.draw(_cameraMatrix);
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Update the GL viewport:
		GLES20.glViewport(0, 0, width, height);
		
		Camera.instance.frame(width, height);
        GameBoard.Instance.viewPortChange(width,height);
	}

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
        GameBoard.Instance.add(new PlanetDrawable(0, 0, 30));
        //GameBoard.Instance.add(new PlanetDrawable(-1, 0, 30));

        _expTestSys = new LinkedList<ParticleSystem>();
        _expTestEff = new LinkedList<ParticleEffect>();
        
        currBoom = 0;
        
        for (int i = 0; i < NUM_BOOMS; i++)
        {
	        ParticleSystem foo = new ParticleSystem(3000, "particle_tex2");
			ParticleEffect bar = new ExplosionEffect(2000,250,foo,new float[]{0,0},250);
			_expTestSys.add(foo);
			_expTestEff.add(bar);
        }
//        Log.d("PlanetTestGLES20Renderer.OnSurfaceCreated$listSize", new Integer(_expTestEff.size()).toString());
        
//        _expTestSys = new ParticleSystem(3000, "particle_tex2");
//        _expTestEff = new ExplosionEffect(2000,250,_expTestSys,new float[]{0,0},250);
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
		Log.d("PlanetTestGLES20Renderer.tap$currBoom", new Integer(currBoom).toString());
        Log.d("PlanetTestGLES20Renderer.tap$listSize", new Integer(_expTestEff.size()).toString());

		ParticleEffect bar = _expTestEff.get(currBoom);
		bar.setCenter(x, y);
		bar.start();
		currBoom++;
		if (currBoom >= NUM_BOOMS) currBoom = 0;
	}
}
