package com.JMR.PlanetGLTest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

public class Starfield implements GLDrawable {
	private static final String starVertexShaderCode = 
		"uniform mat4 uMVPMatrix;   \n" +
		"attribute vec4 vPosition; \n" +
		"attribute vec4 vColor; \n" +
        "void main(){              \n" +
        " gl_Position = uMVPMatrix * vPosition; \n" +
        " gl_FrontColor = vColor; \n" +
        " gl_BackColor = vColor; \n" +
        " gl_Color = vColor; \n " +
        " gl_PointSize = 3.0; \n" +
        "}                         \n";

	private static final String starFragmentShaderCode = 
		"varying vec4 gl_Color; \n" +
        "precision mediump float;  \n" +
        "void main(){              \n" +
        //" gl_FragColor = vec4 (1.0, 1.0, 1.0, 1.0); \n" +
        " gl_FragColor = gl_Color; \n" +
        "}                         \n";
	
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
	
	private static final float[] RED_RGB = {1.f, 200.f/255.f, 200.f/255.f};
	private static final float[] YELLOW_RGB = {1.f, 1.f, 96.f/255.f};
	private static final float[] WHITE_RGB = {1.f, 1.f, 1.f};
	private static final float[] BLUE_RGB = {200.f/255.f, 200.f/255.f, 1.f};
	
	private static final float MIN_X = -10.f;
	private static final float MAX_X = 10.f;
	private static final float MIN_Y = -10.f;
	private static final float MAX_Y = 10.f;
	private static final float MIN_Z = -1.f;
	private static final float MAX_Z = -3.f;
	
	private static final int NUM_STARS = 10000;
	
	private int _v_shader;
	private int _f_shader;
	private int _program;
	private int _matrix;
	private int _position;
	private int _color;
	private FloatBuffer _points;
	private FloatBuffer _colors;
	
	public Starfield() {
		// Setup Shaders:
		_v_shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		
		GLES20.glShaderSource(_v_shader, starVertexShaderCode);
		GLES20.glCompileShader(_v_shader);
		
		_f_shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		
		GLES20.glShaderSource(_f_shader, starFragmentShaderCode);
		GLES20.glCompileShader(_f_shader);
			
		_program = GLES20.glCreateProgram();
		GLES20.glAttachShader(_program, _v_shader);
		GLES20.glAttachShader(_program, _f_shader);
		GLES20.glLinkProgram(_program);
		
		Log.d("********************************************","*****************************************");
		Log.d("Starfield.Starfield$Program Created:GL_Error",new Integer(GLES20.glGetError()).toString());
	
		// Get attributes/etc:
		_position = GLES20.glGetAttribLocation(_program, "vPosition"); // WHY IS THIS RETURNING -1!!!?!
		Log.d("Starfield.Starfield$_position:GL_Error",new Integer(GLES20.glGetError()).toString());
		Log.d("Starfield.Starfiled$_position:value", new Integer(_position).toString());
		Log.d("Starfiled.Starfiled$_program:value", new Integer(_program).toString());
		_color = GLES20.glGetAttribLocation(_program, "vColor");
		_matrix = GLES20.glGetUniformLocation(_program, "uMVPMatrix");
		
		Log.d("********************************************","*****************************************");
		
		// Create points:
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
		
		
	}
	
	@Override
	public void draw(float[] sceneMatrix) {
		GLES20.glUseProgram(_program);
		
		GLES20.glUniformMatrix4fv(_matrix, 1, false, sceneMatrix, 0);

		GLES20.glVertexAttribPointer(_position, 3, GLES20.GL_FLOAT, false, 12, _points);
		GLES20.glVertexAttribPointer(_color, 4, GLES20.GL_FLOAT, false, 16, _colors);
		
		// Enable the attribute arrays:
		GLES20.glEnableVertexAttribArray(_position);
		GLES20.glEnableVertexAttribArray(_color);
		
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, NUM_STARS);
		
		// Disable the attribute arrays:
		GLES20.glDisableVertexAttribArray(_position);
		GLES20.glDisableVertexAttribArray(_color);
	}

}
