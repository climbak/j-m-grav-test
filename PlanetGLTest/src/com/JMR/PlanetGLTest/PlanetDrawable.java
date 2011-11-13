package com.JMR.PlanetGLTest;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

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
        " gl_FragColor = vec4 (0.23671875, 0.96953125, 0.22265625, 1.0); \n" +
        "}                         \n";
	
	private int _v_shader;
	private int _f_shader;
	private int _v_matrix;
	
	private int _x, _y;
	private int _radius;
	
	private int _program;
	
	private int _position;
	
	private FloatBuffer _tri_vb;
	private ByteBuffer _int_bb; 
	private int _vert_size;
	private int _tri_size;
	
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
		
		
		float [] tri = Sphere.ICOSAHEDRON_VERTICES;
		byte [] int_arr = Sphere.ICOSAHEDRON_INDICES;
	
		//Sphere.subdivide(tri, int_arr);
		
		_vert_size = tri.length / 3;
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(tri.length * Float.SIZE); // 4 bytes per float or something
		vbb.order(ByteOrder.nativeOrder());
		_tri_vb = vbb.asFloatBuffer();
		
		_tri_vb.put(tri);
		_tri_vb.position(0);
		
		
		
		ByteBuffer ibb = ByteBuffer.allocate(int_arr.length * Byte.SIZE);
		
		ibb.order(ByteOrder.nativeOrder());
		_int_bb = ibb; // ibb.asShortBuffer();
		
		_int_bb.put(int_arr);
		_int_bb.position(0);
		_tri_size = int_arr.length / 3;
		
		
		_position = GLES20.glGetAttribLocation(_program, "vPosition");
		_v_matrix = GLES20.glGetUniformLocation(_program, "uMVPMatrix");
	}
	
	
	
	
	@Override
	public void draw(float[] sceneMatrix) {
		GLES20.glUseProgram(_program);
		
		float[] ident = new float[] {
				1.f, 0, 0, 0,
				0, 1.f, 0, 0,
				0, 0, 1.f, 0,
				0, 0, 0, 1.f
		};
		
		Matrix.scaleM(ident, 0, .3f, .3f, .3f);
		float[] result = new float[16];
		Matrix.multiplyMM(result, 0, sceneMatrix, 0, ident, 0);
		
		GLES20.glUniformMatrix4fv(_v_matrix, 1, false, result, 0);
		
		GLES20.glVertexAttribPointer(_position, 3, GLES20.GL_FLOAT, false, 0, _tri_vb);
		
		//Log.d("PlanetDrawable.draw_before",new Integer(GLES20.glGetError()).toString());
		
		GLES20.glEnableVertexAttribArray(_position);
		
		// GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, _vert_size * 12, GLES20.GL_UNSIGNED_BYTE, _int_bb);

		//Log.d("PlanetDrawable.draw_after",new Integer(GLES20.glGetError()).toString());
		
		GLES20.glDisableVertexAttribArray(_position);
	}
}
