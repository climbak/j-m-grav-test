package com.JMR.PlanetGLTest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class Starfield implements GLDrawable {
	private static final String starVertexShaderCode = 
		"uniform mat4 uMVPMatrix;              \n" +
		"attribute vec4 position;              \n" +
		"attribute vec4 color;                 \n" +
		"varying vec4 outColor;                \n" +
        "void main(){                          \n" +
        " outColor = color;                    \n" +
        " gl_PointSize = 1.5;                  \n" +
        " gl_Position = uMVPMatrix * position; \n" +
        "}                                     \n";

	private static final String starFragmentShaderCode = 
        "precision mediump float;  \n" +
        "varying vec4 outColor;    \n" +
        "void main(){              \n" +
        " gl_FragColor = outColor; \n" +
        "}                         \n";
	
	private static final String galaxyVertexShaderCode = 
		"uniform mat4 uMVPMatrix;                 \n" + 
		"uniform mat4 uMMatrix;                   \n" +
		"void main() {                            \n" +
		" gl_Position = uMMatrix * position;      \n" +
		" gl_Position = uMVPMatrix * gl_Position; \n" +
		"}                                        \n";
	
	private static final String galaxyFragmentShaderCode = 
		"precision mediump float;                   \n" +
		"void main(){                               \n" +
		" gl_FragColor = vec4 (1.0, 0.7, 0.2, 0.1); \n" +
		"}                                          \n";
	
	private static final int NUM_BACKGROUND_GALAXIES = 20;
	
	private static final int RED = 0;
	private static final int YELLOW = 1;
	private static final int WHITE = 2;
	private static final int BLUE = 3;
	
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	
	private static final int R = 0;
	private static final int G = 1;
	private static final int B = 2;
	private static final int A = 3;
	
	private static final float[] RED_RGB = {1.f, 100.f/255.f, 100.f/255.f};
	private static final float[] YELLOW_RGB = {1.f, 1.f, 96.f/255.f};
	private static final float[] WHITE_RGB = {1.f, 1.f, 1.f};
	private static final float[] BLUE_RGB = {200.f/255.f, 200.f/255.f, 1.f};
	
	private static final float MIN_X = -10.f;
	private static final float MAX_X = 10.f;
	private static final float MIN_Y = -10.f;
	private static final float MAX_Y = 10.f;
	private static final float MIN_Z = 1.f;
	private static final float MAX_Z = 3.f;
	
	private static final float GALAXY_DISC_Z_SCALE = 0.2f;
	private static final float GALAXY_BULGE_Z_SCALE = 0.8f;
	private static final float GALAXY_BULGE_OVERALL_SCALE = 0.4f;
	
	private static final int NUM_STARS = 10000;
	
	private int _starVertexShader;
	private int _starFragmentShader;
	private int _galaxyVertexShader;
	private int _galaxyFragmentShader;
	private int _starProgram;
	private int _galaxyProgram;
	private int _sceneMatrix;
	private int _galaxyModelMatrix;
	private int _starPosition;
	private int _galaxyPosition;
	private int _color;
	private FloatBuffer _points;
	private FloatBuffer _colors;
	
	private float[] _galaxyPoints;
	private FloatBuffer _galaxyBuffer;
	
	private void setupShaders() {
		_starVertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		
		GLES20.glShaderSource(_starVertexShader, starVertexShaderCode);
		GLES20.glCompileShader(_starVertexShader);
		
		_starFragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		
		GLES20.glShaderSource(_starFragmentShader, starFragmentShaderCode);
		GLES20.glCompileShader(_starFragmentShader);
		
		_starProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(_starProgram, _starVertexShader);
		GLES20.glAttachShader(_starProgram, _starFragmentShader);
		GLES20.glLinkProgram(_starProgram);
		
		_galaxyVertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		
		GLES20.glShaderSource(_galaxyVertexShader, galaxyVertexShaderCode);
		GLES20.glCompileShader(_galaxyVertexShader);
		
		_galaxyFragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		
		GLES20.glShaderSource(_galaxyFragmentShader, galaxyFragmentShaderCode);
		GLES20.glCompileShader(_galaxyFragmentShader);
		
		_galaxyProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(_galaxyProgram, _galaxyVertexShader);
		GLES20.glAttachShader(_galaxyProgram, _galaxyFragmentShader);
		GLES20.glLinkProgram(_galaxyProgram);
		
		// Get attributes/etc:
		_starPosition = GLES20.glGetAttribLocation(_starProgram, "position"); 
		_galaxyPosition = GLES20.glGetAttribLocation(_galaxyProgram, "position");
		_color = GLES20.glGetAttribLocation(_starProgram, "color");
		_sceneMatrix = GLES20.glGetUniformLocation(_starProgram, "uMVPMatrix");
		_galaxyModelMatrix = GLES20.glGetUniformLocation(_galaxyProgram, "uMMatrix");
	}
	
	public Starfield() {
		// Setup Shaders:
		setupShaders();
		
		// Create starfield points:
		float[] points = new float[NUM_STARS*3];
		float[] colors = new float[NUM_STARS*4];
		int color;
		for (int i = 0; i < NUM_STARS; i++)
		{
			// Pick a random position in our bounding box:
			points[i*3+X] = (float) (Math.random()*(MAX_X-MIN_X)+MIN_X);
			points[i*3+Y] = (float) (Math.random()*(MAX_Y-MIN_Y)+MIN_Y);
			points[i*3+Z] = (float) (Math.random()*(MAX_Z-MIN_Z)+MIN_Z);
						
			// Pick a random color:
			color = (int) (Math.random()*4);
			switch(color)
			{
			case RED:
				colors[i*4+R] = RED_RGB[R];
				colors[i*4+G] = RED_RGB[G];
				colors[i*4+B] = RED_RGB[B];
				break;
			case YELLOW:
				colors[i*4+R] = YELLOW_RGB[R];
				colors[i*4+G] = YELLOW_RGB[G];
				colors[i*4+B] = YELLOW_RGB[B];
				break;
			case WHITE:
				colors[i*4+R] = WHITE_RGB[R];
				colors[i*4+G] = WHITE_RGB[G];
				colors[i*4+B] = WHITE_RGB[B];
				break;
			default: // BLUE
				colors[i*4+R] = BLUE_RGB[R];
				colors[i*4+G] = BLUE_RGB[G];
				colors[i*4+B] = BLUE_RGB[B];
			}
						
			// Pick a random alpha value:
			colors[i*4+A] = 1.0f; //(float) Math.random();
		}
		
		// Make those arrays into float buffers:
		ByteBuffer vbb = ByteBuffer.allocateDirect(points.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		_points = vbb.asFloatBuffer();
		
		_points.put(points);
		_points.position(0);
		
		ByteBuffer vbc = ByteBuffer.allocateDirect(colors.length * 4);
		vbc.order(ByteOrder.nativeOrder());
		_colors = vbc.asFloatBuffer();
		
		_colors.put(colors);
		_colors.position(0);
		
		// Create background galaxies geometry:
		int offs = Sphere.ICOSAHEDRON.length;
		_galaxyPoints = new float[offs*2];
		int i;
		
		for (i = 0; i < offs; i++)
		{
			_galaxyPoints[i] = Sphere.ICOSAHEDRON[i];
			_galaxyPoints[i+offs] = Sphere.ICOSAHEDRON[i] * GALAXY_BULGE_OVERALL_SCALE;
			
			// Squash:
			if (i % 3 == 2) // Z point
			{
				_galaxyPoints[i] *= GALAXY_DISC_Z_SCALE;
				_galaxyPoints[i+offs] *= GALAXY_BULGE_Z_SCALE;
			}
		}
		
		ByteBuffer vbd = ByteBuffer.allocateDirect(_galaxyPoints.length * 4);
		_galaxyBuffer = vbd.asFloatBuffer();
		
		_galaxyBuffer.put(_galaxyPoints);
		_galaxyBuffer.position(0);
	}
	
	@Override
	public void draw(float[] sceneMatrix) {
		// Draw Background Galaxies:
		GLES20.glDepthFunc(GLES20.GL_ALWAYS);
		GLES20.glUseProgram(_galaxyProgram);
		
		Random rand = new Random(12345);
		float[] ident = new float[] {
				1.f, 0, 0, 0,
				0, 1.f, 0, 0,
				0, 0, 1.f, 0,
				0, 0, 0, 1.f
		};
		
		float[] model = new float[ident.length];
		
		float[] pos = new float[4];
		
		for (int i = 0; i < NUM_BACKGROUND_GALAXIES; i++)
		{
			Log.d("Starfield.draw","IN GALAXY LOOP");
			for (int j = 0; j < ident.length; j++) model[j] = ident[j];
			
			// Random orientation:
			Matrix.rotateM(model, 0, rand.nextFloat()*360.f, rand.nextFloat(), 
					rand.nextFloat(), rand.nextFloat());
			
			// Random offset:
			pos[0] = (rand.nextFloat() * (MAX_X - MIN_X)) + MIN_X;
			pos[1] = (rand.nextFloat() * (MAX_Y - MIN_Y)) + MIN_Y;
			pos[2] = (rand.nextFloat() * (MAX_Z - MIN_Z)) + MIN_Z*2;
			pos[3] = 0;
			
			Matrix.translateM(model, 0, pos[0], pos[1], pos[2]);
			
			GLES20.glUniformMatrix4fv(_galaxyModelMatrix, 1, false, model, 0);
			GLES20.glUniformMatrix4fv(_sceneMatrix, 1, false, sceneMatrix, 0);
			
			GLES20.glVertexAttribPointer(_galaxyPosition, 3, GLES20.GL_FLOAT, false, 0, _galaxyBuffer);
			GLES20.glEnableVertexAttribArray(_galaxyPosition);
			
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, _galaxyPoints.length);
		}
		
		// Draw Stars:
		GLES20.glDepthFunc(GLES20.GL_LESS);
		GLES20.glUseProgram(_starProgram);
		
		GLES20.glUniformMatrix4fv(_sceneMatrix, 1, false, sceneMatrix, 0);

		GLES20.glVertexAttribPointer(_starPosition, 3, GLES20.GL_FLOAT, false, 12, _points);
		GLES20.glVertexAttribPointer(_color, 4, GLES20.GL_FLOAT, false, 16, _colors);
		
		// Enable the attribute arrays:
		GLES20.glEnableVertexAttribArray(_starPosition);
		GLES20.glEnableVertexAttribArray(_color);
		
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, NUM_STARS);
		
		// Disable the attribute arrays:
		GLES20.glDisableVertexAttribArray(_starPosition);
		GLES20.glDisableVertexAttribArray(_color);
	}

}
