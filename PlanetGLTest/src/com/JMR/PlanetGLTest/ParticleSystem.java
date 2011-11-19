package com.JMR.PlanetGLTest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class ParticleSystem implements GLDrawable {
	private static final String particleVertexShaderCode = 
		"uniform mat4 uMVPMatrix;              \n" +
		"attribute vec4 position;              \n" +
		"attribute vec4 color;                 \n" +
		"attribute float size;                 \n" +
		"varying vec4 outColor;                \n" +
        "void main(){                          \n" +
        " outColor = color;                    \n" +
        " gl_PointSize = size;                 \n" +
        " gl_Position = uMVPMatrix * position; \n" +
        "}                                     \n";

	private static final String particleFragmentShaderCode = 
        "precision mediump float;  \n" +
        "varying vec4 outColor;    \n" +
        "uniform sampler2D tex;    \n" +
        "void main(){              \n" +
        " vec4 tmpColor = texture2D(tex, gl_PointCoord); \n" +
        " gl_FragColor = tmpColor * outColor; \n" +
        "}                         \n";
	
	// Particle parameters:
	public float[] pos;
	public float[] size;
	public float[] color;
	public long[] age;
	public int numParticles;
	
	// Handles to shader variables:
	private int _uMVPMatrixHandle;
	private int _positionHandle;
	private int _colorHandle;
	private int _sizeHandle;
	private int _texHandle;
	
	// Handles to shaders and program:
	private int _vertexShaderHandle;
	private int _fragmentShaderHandle;
	private int _programHandle;
	
	// Texture stuff:
	private Bitmap _textureMap;
	private int _textureHandle;
	
	public ParticleSystem(int num, String textureFile) {
		numParticles = num;
		
		pos = new float[num * 3];
		size = new float[num];
		color = new float[num * 4];
		age = new long[num];
		
		// Compile shaders/etc:
		_vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		_fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		
		GLES20.glShaderSource(_vertexShaderHandle, particleVertexShaderCode);
		GLES20.glCompileShader(_vertexShaderHandle);
		Log.d("ParticleSystem.ParticleSystem:Vertex_Shader",GLES20.glGetShaderInfoLog(_vertexShaderHandle));
		
		GLES20.glShaderSource(_fragmentShaderHandle, particleFragmentShaderCode);
		GLES20.glCompileShader(_fragmentShaderHandle);
		Log.d("ParticleSystem.ParticleSystem:Vertex_Shader",GLES20.glGetShaderInfoLog(_fragmentShaderHandle));
		
		_programHandle = GLES20.glCreateProgram();
		GLES20.glAttachShader(_programHandle, _vertexShaderHandle);
		GLES20.glAttachShader(_programHandle, _fragmentShaderHandle);
		GLES20.glLinkProgram(_programHandle);
		
		// Get handles to shader variables:
		_uMVPMatrixHandle = GLES20.glGetUniformLocation(_programHandle, "uMVPMatrix");
		_positionHandle = GLES20.glGetAttribLocation(_programHandle, "position");
		_colorHandle = GLES20.glGetAttribLocation(_programHandle, "color");
		_sizeHandle = GLES20.glGetAttribLocation(_programHandle, "size");
		_texHandle = GLES20.glGetUniformLocation(_programHandle, "tex");
		
		// Set up us the texture:
		Resources res = PlanetGLTestActivity.instance.getResources();
		_textureMap = ((BitmapDrawable)res.getDrawable(res.getIdentifier(textureFile, "drawable",
				"com.JMR.PlanetGLTest"))).getBitmap();
		int[] temp = new int[1];
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		Log.d("Starfield:Starfield$glGenTextures.error",new Integer(GLES20.glGetError()).toString());

		GLES20.glGenTextures(1, temp, 0);
		Log.d("Starfield:Starfield$glGenTextures.error",new Integer(GLES20.glGetError()).toString());
		_textureHandle = temp[0];
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _textureHandle);
		
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, _textureMap, 0);
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		
		// TODO actually create the particles
	}
	
	@Override
	public void draw(float[] sceneMatrix) {
		// TODO Autogen.. whatever

	}

}
