package com.JMR.PlanetGLTest;

import android.opengl.Matrix;

public class Camera {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	
	public static final float[] START_LOC = {0.f, 0.f, -3.f};
	public static final float[] START_LOOK = {0.f, 0.f, 0.f};
	public static final float[] START_UP = {0.f, 1.f, 0.f};
	
	public static Camera instance = new Camera();
	
	public float[] mViewProjectionMatrix = new float[16];
    public float[] mViewMatrix = new float[16];
    public float[] mProjectionMatrix = new float[16];
	
    public float ratio;
    public float[] location;
    public float[] lookingAt;
    public float[] up;
    
	private Camera() {
		ratio = 1.f;
		
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 7);
		Matrix.setLookAtM(mViewMatrix, 0, START_LOC[X], START_LOC[Y], START_LOC[Z], 
				START_LOOK[X], START_LOOK[Y], START_LOOK[Z], 
				START_UP[X], START_UP[Y], START_UP[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	
}
