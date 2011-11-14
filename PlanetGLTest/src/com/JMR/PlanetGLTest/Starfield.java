package com.JMR.PlanetGLTest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

public class Starfield implements GLDrawable {
	private static final String starVertexShaderCode = 
		"uniform mat4 uMVPMatrix;   \n" +
		"attribute vec4 position;   \n" +
		"attribute vec4 color;      \n" +
		"varying vec4 outColor;    \n" +
        "void main(){               \n" +
        //" gl_FrontColor = vColor;   \n" +
        //" gl_BackColor = vColor;    \n" +
        //" gl_Color = vColor;        \n" +
        " outColor = color;         \n" +
        " gl_PointSize = 1.5;       \n" +
        " gl_Position = uMVPMatrix * position; \n" +
        "}                          \n";

	private static final String starFragmentShaderCode = 
		//"varying vec4 gl_Color;    \n" +
        "precision mediump float;  \n" +
        "varying vec4 outColor;         \n" +
        "void main(){              \n" +
        //" gl_FragColor = vec4 (1.0, 1.0, 1.0, 1.0); \n" +
        " gl_FragColor = outColor; \n" +
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
	
	private static final int NUM_STARS = 10000;
	
	private int _vertexShader;
	private int _fragmentShader;
	private int _starProgram;
	private int _matrix;
	private int _position;
	private int _color;
	private FloatBuffer _points;
	private FloatBuffer _colors;
	
	public Starfield() {
		//Log.d("********************************************","*****************************************");

		// Setup Shaders:
		_vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		//Log.d("Starfield.Starfield$_vertexShader",new Integer(_vertexShader).toString());
		
		GLES20.glShaderSource(_vertexShader, starVertexShaderCode);
		GLES20.glCompileShader(_vertexShader);
		//Log.d("Starfield.Starfield$Vertex Shader Info Log",GLES20.glGetShaderInfoLog(_vertexShader));
		
		_fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		//Log.d("Starfield.Starfield$_fragmentShader",new Integer(_fragmentShader).toString());
		
		GLES20.glShaderSource(_fragmentShader, starFragmentShaderCode);
		GLES20.glCompileShader(_fragmentShader);
		
		//Log.d("Starfield.Starfield$Fragment Shader Info Log",GLES20.glGetShaderInfoLog(_fragmentShader));

		
		_starProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(_starProgram, _vertexShader);
		GLES20.glAttachShader(_starProgram, _fragmentShader);
		GLES20.glLinkProgram(_starProgram);
		//Log.d("Starfield.Starfield",GLES20.glGetProgramInfoLog(_starProgram));
		
		//Log.d("Starfield.Starfield$Program Created:GL_Error",new Integer(GLES20.glGetError()).toString());
		//Log.d("Starfiled.Starfiled$_program:value", new Integer(_starProgram).toString());
	
		// Get attributes/etc:
		_position = GLES20.glGetAttribLocation(_starProgram, "position"); // WHY IS THIS RETURNING -1!!!?!
		//Log.d("Starfield.Starfield$_position:GL_Error",new Integer(GLES20.glGetError()).toString());
		//Log.d("Starfield.Starfiled$_position:value", new Integer(_position).toString());
		_color = GLES20.glGetAttribLocation(_starProgram, "color");
		//Log.d("Starfield.Starfiled$_color:value", new Integer(_color).toString());
		_matrix = GLES20.glGetUniformLocation(_starProgram, "uMVPMatrix");
		//Log.d("Starfield.Starfiled$_matrix:value", new Integer(_matrix).toString());
		int[] length = new int[1];
		int[] size = new int[1];
		int[] type = new int[1];
		byte[] name = new byte[50];
		GLES20.glGetActiveAttrib(_starProgram, 0, 50, length, 0, size, 0, type, 0, name, 0);
		String attribName = new String(name, 0, length[0]);
		//Log.d("First attribute in program's name length",new Integer(attribName.length()).toString());
		//Log.d("Starfield.Starfield$First Attrib Name", attribName);
		//Log.d("********************************************","*****************************************");
		
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
		GLES20.glUseProgram(_starProgram);
		
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
