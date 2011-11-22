package com.JMR.PlanetGLTest;

import android.opengl.Matrix;

public class Camera {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	
	public static final float[] START_LOC = {0.f, 0.f, -3.f};
	public static final float[] START_LOOK = {0.f, 0.f, 0.f};
	public static final float[] START_UP = {0.f, 1.f, 0.f};
	
	public static final float DEFAULT_NEAR_CLIP = 1.f;
	public static final float DEFAULT_TOP_CLIP = 1.f;
	public static final float DEFAULT_FAR_CLIP = 7.f;
	
	public static Camera instance = new Camera();
	
	public float[] mViewProjectionMatrix;
    public float[] mViewMatrix;
    public float[] mProjectionMatrix;
	
    public float ratio;
    public float nearClip;
    public float farClip;
    public float topClip;
    public float[] location;
    public float[] lookingAt;
    public float[] up;
    
	private Camera() {
		ratio = 1.f;
		
		location = new float[3];
		lookingAt = new float[3];
		up = new float[3];
		mViewProjectionMatrix = new float[16];
		mViewMatrix = new float[16];
		mProjectionMatrix = new float[16];
		
		nearClip = DEFAULT_NEAR_CLIP;
		farClip = DEFAULT_FAR_CLIP;
		topClip = DEFAULT_TOP_CLIP;
		
		location[X] = START_LOC[X];
		location[Y] = START_LOC[Y];
		location[Z] = START_LOC[Z];
		
		lookingAt[X] = START_LOOK[X];
		lookingAt[Y] = START_LOOK[Y];
		lookingAt[Z] = START_LOOK[Z];
		
		up[X] = START_UP[X];
		up[Y] = START_UP[Y];
		up[Z] = START_UP[Z];
		
		Matrix.frustumM(mProjectionMatrix, 0, -ratio*topClip, ratio*topClip, -topClip, topClip, nearClip, farClip);
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z], 
				lookingAt[X], lookingAt[Y], lookingAt[Z], 
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	// Reset camera to default parameters:
	public void reset() {
		instance = new Camera();
	}
	
	// Perform an absolute translation of the camera:
	public void translate(float x, float y, float z) {
		location[X] += x;
		location[Y] += y;
		location[Z] += z;
		
		lookingAt[X] += x;
		lookingAt[Y] += y;
		lookingAt[Z] += z;
		
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z],
				lookingAt[X], lookingAt[Y], lookingAt[Z],
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	// Move the camera forward along its line of sight:
	public void forward(float amount) {
		float[] direction = new float[3];
		
		direction[X] = lookingAt[X] - location[X];
		direction[Y] = lookingAt[Y] - location[Y];
		direction[Z] = lookingAt[Z] - location[Z];
		
		float mag = (float) Math.sqrt(direction[X]*direction[X] + 
				direction[Y]*direction[Y] + 
				direction[Z]*direction[Z]);
		
		direction[X] *= amount/mag;
		direction[Y] *= amount/mag;
		direction[Z] *= amount/mag;
		
		translate(direction[X], direction[Y], direction[Z]);
	}
	
	// Move the camera backwards along its line of sight:
	public void reverse(float amount) {
		forward(-amount);
	}
	
	// Move the camera up relative to its current orientation:
	public void up(float amount) {
		translate(up[X] * amount, up[Y] * amount, up[Z] * amount);
	}
	
	// Move the camera down relative to its current orientation:
	public void down(float amount) {
		up(-amount);
	}
	
	// Move the camera right relative to its orientation:
	public void right(float amount) {
		float[] direction = new float[3];
		
		direction[X] = lookingAt[X] - location[X];
		direction[Y] = lookingAt[Y] - location[Y];
		direction[Z] = lookingAt[Z] - location[Z];
		
		float mag = (float) Math.sqrt(direction[X]*direction[X] + 
				direction[Y]*direction[Y] + 
				direction[Z]*direction[Z]);
		
		direction[X] /= mag;
		direction[Y] /= mag;
		direction[Z] /= mag;
		
		float[] cross = new float[3];
		
		// Cross product gives us direction to right of camera (right hand rule)
		cross[X] = direction[Y]*up[Z] - direction[Z]*up[Y];
		cross[Y] = direction[Z]*up[X] - direction[X]*up[Z];
		cross[Z] = direction[X]*up[Y] - direction[Y]*up[X];
		
		translate(cross[X] * amount, cross[Y] * amount, cross[Z] * amount);
	}
	
	// Move the camera left relative to its orientation:
	public void left(float amount) {
		right(-amount);
	}
	
	// Make the camera look at the given point:
	public void lookAt(float x, float y, float z) {
		lookingAt[X] = x;
		lookingAt[Y] = y;
		lookingAt[Z] = z;
		
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z],
				lookingAt[X], lookingAt[Y], lookingAt[Z],
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	// Rotate the camera arbitrarily:
	public void rotate(float pitch, float yaw, float roll) {
		pitch(pitch);
		yaw(yaw);
		roll(roll);
	}
	
	// Yaw the camera:
	public void yaw(float angle) {
		lookingAt[X] -= location[X];
		lookingAt[Z] -= location[Z];
		lookingAt[X] *= Math.cos(angle);
		lookingAt[Z] *= Math.sin(angle);
		lookingAt[X] += location[X];
		lookingAt[Z] += location[Z];
		
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z],
				lookingAt[X], lookingAt[Y], lookingAt[Z],
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	// Roll the camera:
	public void roll(float angle) {
		// up is always in camera space, so we don't need to translate to the origin
		//  before rotating.
		up[X] *= Math.cos(angle);
		up[Y] *= Math.sin(angle);
		
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z],
				lookingAt[X], lookingAt[Y], lookingAt[Z],
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	// Pitch the camera:
	public void pitch(float angle) {
		lookingAt[Y] -= location[Y];
		lookingAt[Z] -= location[Z];
		lookingAt[Y] *= Math.cos(angle);
		lookingAt[Z] *= Math.sin(angle);
		lookingAt[Y] += location[Y];
		lookingAt[Z] += location[Z];
		
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z],
				lookingAt[X], lookingAt[Y], lookingAt[Z],
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	public void frame(int width, int height) {
		ratio = (float) width / height;
		
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, nearClip, farClip);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
}
