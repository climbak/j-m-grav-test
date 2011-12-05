package com.JMR.PlanetGLTest;

import android.opengl.Matrix;
import android.util.Log;

public class Camera {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	
	public static final float[] START_LOC = {0.f, 0.f, -3.f};
	public static final float[] START_LOOK = {0.f, 0.f, 0.f};
	public static final float[] START_UP = {0.f, 1.f, 0.f};
	public static final float[] START_RIGHT = {1.f, 0.f, 0.f};
	
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
    public float[] right;
    public float[] forward;
    
	private Camera() {
		ratio = 1.f;
		
		location = new float[4];
		lookingAt = new float[4];
		up = new float[4];
		right = new float[4];
		forward = new float[4];
		mViewProjectionMatrix = new float[16];
		mViewMatrix = new float[16];
		mProjectionMatrix = new float[16];
		Matrix.setIdentityM(mProjectionMatrix, 0);
		Matrix.setIdentityM(mViewMatrix, 0);
		Matrix.setIdentityM(mViewProjectionMatrix, 0);
		
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
		
		right[X] = START_RIGHT[X];
		right[Y] = START_RIGHT[Y];
		right[Z] = START_RIGHT[Z];
		
		forward[X] = lookingAt[X] - location[X];
		forward[Y] = lookingAt[Y] - location[Y];
		forward[Z] = lookingAt[Z] - location[Z];
		
		float len = Matrix.length(forward[X], forward[Y], forward[Z]);
		
		forward[X] /= len;
		forward[Y] /= len;
		forward[Z] /= len;
		
		Matrix.frustumM(mProjectionMatrix, 0, -ratio*topClip, ratio*topClip, -topClip, topClip, nearClip, farClip);
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z], 
				lookingAt[X], lookingAt[Y], lookingAt[Z], 
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
		
		Log.d("Camera.Camera$mViewMatrix", new Float(mViewMatrix[0]).toString());
		Log.d("Camera.Camera$mProjectionMatrix", new Float(mProjectionMatrix[0]).toString());
		Log.d("Camera.Camera$mViewProjectionMatrix", new Float(mViewProjectionMatrix[0]).toString());
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
		translate(forward[X], forward[Y], forward[Z]);
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
		
		translate(right[X] * amount, right[Y] * amount, right[Z] * amount);
	}
	
	// Move the camera left relative to its orientation:
	public void left(float amount) {
		right(-amount);
	}
	
	// Make the camera look at the given point:
	public void lookAt(float x, float y, float z) { // TODO fix vectors
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
		roll(roll);
		pitch(pitch);
		yaw(yaw);
	}
	
	// Yaw the camera:
	public void yaw(float angle) {
		if (angle == 0.f) return;
		
		float[] oldLookingAt = new float[4];
		oldLookingAt = lookingAt;
//		float[] oldUp = up;
		float[] oldRight = new float[4];
		oldRight = right;
		
		float[] rot = new float[16];
		Matrix.setIdentityM(rot, 0);
		Matrix.setRotateM(rot, 0, angle, up[X], up[Y], up[Z]);
		
//		Matrix.multiplyMV(up, 0, rot, 0, oldUp, 0);
		Matrix.multiplyMV(lookingAt, 0, rot, 0, oldLookingAt, 0);
		Matrix.multiplyMV(right, 0, rot, 0, oldRight, 0);
		
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z],
				lookingAt[X], lookingAt[Y], lookingAt[Z],
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	// Roll the camera:
	public void roll(float angle) {
		if (angle == 0.f) return;
		
//		float[] oldLookingAt = lookingAt;
		float[] oldUp = new float[4];
		oldUp = up;
		float[] oldRight = new float[4];
		oldRight = right;
		
		float[] rot = new float[16];
		Matrix.setIdentityM(rot, 0);
		Matrix.setRotateM(rot, 0, angle, forward[X], forward[Y], forward[Z]);
		
		Matrix.multiplyMV(up, 0, rot, 0, oldUp, 0);
//		Matrix.multiplyMV(lookingAt, 0, rot, 0, oldLookingAt, 0);
		Matrix.multiplyMV(right, 0, rot, 0, oldRight, 0);
		
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z],
				lookingAt[X], lookingAt[Y], lookingAt[Z],
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	// Pitch the camera:
	public void pitch(float angle) {
		if (angle == 0.f) return;
		
		float[] oldLookingAt = new float[4];
		oldLookingAt = lookingAt;
		float[] oldUp = new float[4];
		oldUp = up;
//		float[] oldRight = right;
		
		float[] rot = new float[16];
		Matrix.setIdentityM(rot, 0);
		Matrix.setRotateM(rot, 0, angle, right[X], right[Y], right[Z]);
		
		Matrix.multiplyMV(up, 0, rot, 0, oldUp, 0);
		Matrix.multiplyMV(lookingAt, 0, rot, 0, oldLookingAt, 0);
//		Matrix.multiplyMV(right, 0, rot, 0, oldRight, 0);
		
		Matrix.setLookAtM(mViewMatrix, 0, location[X], location[Y], location[Z],
				lookingAt[X], lookingAt[Y], lookingAt[Z],
				up[X], up[Y], up[Z]);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	public void frame(int width, int height) {
		ratio = (float) width / height;
		
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, nearClip, farClip);
		Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
		Log.d("Camera.frame$mViewMatrix", new Float(mViewMatrix[0]).toString());
		Log.d("Camera.frame$mProjectionMatrix", new Float(mProjectionMatrix[0]).toString());
		Log.d("Camera.frame$mViewProjectionMatrix", new Float(mViewProjectionMatrix[0]).toString());
	}
}
