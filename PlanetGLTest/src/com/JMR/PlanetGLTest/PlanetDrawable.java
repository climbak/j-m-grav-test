package com.JMR.PlanetGLTest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class PlanetDrawable implements GLDrawable{
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
	
	private int _v_shader;
	private int _f_shader;
	private int _v_matrix;
	
	private int _x, _y;
	private int _radius;
	
	private int _program;
	
	private int _position;
	private FloatBuffer _tri_vb;
	
	
	public PlanetDrawable(int x, int y, int radius){
		_x = x;
		_y = y;
		_radius = radius;
		
		_v_shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		
		// Add the source code and compile:
		GLES20.glShaderSource(_v_shader, defaultVertexShaderCode);
		GLES20.glCompileShader(_v_shader);
		
		_f_shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		
		GLES20.glShaderSource(_f_shader, defaultFragmentShaderCode);
		GLES20.glCompileShader(_f_shader);
			
		_program = GLES20.glCreateProgram();
		GLES20.glAttachShader(_program, _v_shader);
		GLES20.glAttachShader(_program, _f_shader);
		GLES20.glLinkProgram(_program);
		
		
		float [] tri = new float[]{
				-.5f, -.25f, 0,
				.5f, -.25f, 0,
				0f, .449016994f, 0
		};
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(tri.length * 4); // 4 bytes per float or something
		vbb.order(ByteOrder.nativeOrder());
		_tri_vb = vbb.asFloatBuffer();
		
		_tri_vb.put(tri);
		_tri_vb.position(0);
		
		_position = GLES20.glGetAttribLocation(_program, "vPosition");
		_v_matrix = GLES20.glGetUniformLocation(_program, "uMVPMatrix");
		Log.d("~~~~~~~~~~~~~~~~~",new Integer(_position).toString());
		Log.d("~~~~~~~~~~~~~~~~~",new Integer(_v_matrix).toString());
	}
	
	
	
	
	@Override
	public void draw(float[] sceneMatrix) {
		GLES20.glUseProgram(_program);
		
		GLES20.glUniformMatrix4fv(_v_matrix, 1, false, sceneMatrix, 0);

		GLES20.glVertexAttribPointer(_position, 3, GLES20.GL_FLOAT, false, 12, _tri_vb);
		GLES20.glEnableVertexAttribArray(_position);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
		
		GLES20.glDisableVertexAttribArray(_position);
	}
}
