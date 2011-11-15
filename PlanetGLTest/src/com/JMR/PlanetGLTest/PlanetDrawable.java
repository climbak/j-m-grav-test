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
	
	public static float [] light = {0,0,1};
	
	
	final String vertexShader =
		    "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
		  + "uniform mat4 u_MVMatrix;       \n"     // A constant representing the combined model/view matrix.
		  + "uniform vec3 u_LightPos;       \n"     // The position of the light in eye space.
		  + "uniform vec4 a_Color;        \n"
		  
		  + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
		  + "attribute vec3 a_Normal;       \n"     // Per-vertex normal information we will pass in.
		 
		  + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.
		 
		  + "void main()                    \n"     // The entry point for our vertex shader.
		  + "{                              \n"
		// Transform the vertex into eye space.
		  + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
		// Transform the normal's orientation into eye space.
		  + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
		// Will be used for attenuation.
		  + "   float distance = length(u_LightPos - modelViewVertex);             \n"
		// Get a lighting direction vector from the light to the vertex.
		  + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
		// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
		// pointing in the same direction then it will get max illumination.
		  + "   float diffuse = max(dot(modelViewNormal, lightVector), .5);       \n"
		// Attenuate the light based on distance.
		  + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
		// Multiply the color by the illumination level. It will be interpolated across the triangle.
		  + "   v_Color = a_Color * diffuse;                                       \n"
		// gl_Position is a special variable used to store the final position.
		// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
		  + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
		  + "}  ";  
	
	final String fragmentShader =
		  "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
		                                          // precision in the fragment shader.
		+ "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
		                                          // triangle per fragment.
		+ "void main()                    \n"     // The entry point for our fragment shader.
		+ "{                              \n"
		+ "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
		+ "}                              \n";
	
	private int _v_shader;
	private int _f_shader;
	private int _mvp_matrix;
	private int _mv_matrix;
	
	private int _x, _y;
	private int _radius;
	
	private int _program;
	
	private int _normal;
	private int _position;
	private int _lightpos;
	private int _color;
	
	private FloatBuffer _tri_vb;
	private ByteBuffer _int_bb; 
	private FloatBuffer _col_fb;
	
	private int _vert_size;
	private int _tri_size;
	
	private float [] model;
	
	public PlanetDrawable(int x, int y, int radius){
		_x = x;
		_y = y;
		_radius = radius;
		
		_v_shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		
		model = new float[] {
				1.f, 0, 0, 0,
				0, 1.f, 0, 0,
				0, 0, 1.f, 0,
				0, 0, 0, 1.f
		};
		
		// Matrix.translateM(model, 0, _x, _y, 0);
		
		// Matrix.scaleM(m, mOffset, x, y, z)
		
		// Add the source code and compile:
		GLES20.glShaderSource(_v_shader, vertexShader);
		GLES20.glCompileShader(_v_shader);
		//Log.d("_vertexShader$_v_shader Info Log",GLES20.glGetShaderInfoLog(_v_shader));
		
		_f_shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		
		GLES20.glShaderSource(_f_shader, fragmentShader);
		//Log.d("GLES20.glShaderSource",new Integer(GLES20.glGetError()).toString());
		
		
		GLES20.glCompileShader(_f_shader);
		//Log.d("GLES20.glCompileShader",new Integer(GLES20.glGetError()).toString());
		
		
		_program = GLES20.glCreateProgram();
		GLES20.glAttachShader(_program, _v_shader);
		//Log.d("GLES20.glAttachShader",new Integer(GLES20.glGetError()).toString());
		
		GLES20.glAttachShader(_program, _f_shader);
		//Log.d("GLES20.glAttachShader",new Integer(GLES20.glGetError()).toString());
		
		GLES20.glLinkProgram(_program);
		//Log.d("GLES20.glLinkProgram",new Integer(GLES20.glGetError()).toString());
		
		
		//float [] tri = Sphere.ICOSAHEDRON_VERTICES;
		float [] tri = Sphere.ICOSAHEDRON;
		tri = Sphere.subdivide(tri);
		// tri = Sphere.subdivide(tri);
		
		// Matrix.translateM(tri, 0, _x, _y, 0);
		Matrix.translateM(model, 0, _x, _y, 0);
		Matrix.scaleM(model, 0, .5f, .5f, .5f);
		//tri = Sphere.subdivide(tri);
		byte [] int_arr = Sphere.ICOSAHEDRON_INDICES;
	
		
		
		_tri_size = tri.length;
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

		_position = GLES20.glGetAttribLocation(_program, "a_Position");
		_lightpos = GLES20.glGetUniformLocation(_program, "u_LightPos");
		_mvp_matrix = GLES20.glGetUniformLocation(_program, "u_MVPMatrix");
		_color = GLES20.glGetUniformLocation(_program, "a_Color");
		_normal = GLES20.glGetAttribLocation(_program, "a_Normal");
		//Log.d("GLES20.glGetUniformLocation$u_MVPMatrix", new Integer(GLES20.glGetError()).toString());
		
		_mv_matrix = GLES20.glGetUniformLocation(_program, "u_MVMatrix");
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
		
		
		//Matrix.scaleM(ident, 0, .3f, .3f, .3f);
		//Matrix.rotateM(ident, 0, 150, 1, 1, 0);
		
		float[] result = new float[16];
		Matrix.multiplyMM(result, 0, sceneMatrix, 0, ident, 0);
		Matrix.multiplyMM(result, 0, sceneMatrix, 0, model, 0);
		
		GLES20.glUniformMatrix4fv(_mvp_matrix, 1, false, result, 0);
		GLES20.glUniformMatrix4fv(_mv_matrix, 1, false, model, 0);
		//Log.d("GLES20.glUniformMatrix4fv$_mv_matrix",new Integer(GLES20.glGetError()).toString());
		
		GLES20.glVertexAttribPointer(_position, 3, GLES20.GL_FLOAT, false, 0, _tri_vb);
		GLES20.glVertexAttribPointer(_normal, 3, GLES20.GL_FLOAT, false, 0, _tri_vb);
		
		//Log.d("GLES20.glVertexAttribPointer",new Integer(GLES20.glGetError()).toString());
		
		GLES20.glUniform3f(_lightpos, light[0], light[1], light[2]);
		GLES20.glUniform4f(_color, 1, 0, 0, 1);
		
		GLES20.glEnableVertexAttribArray(_position);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, _tri_size);
		//GLES20.glDrawElements(GLES20.GL_TRIANGLES, _tri_size * 3, GLES20.GL_UNSIGNED_BYTE, _int_bb);

		//Log.d("PlanetDrawable.draw_after",new Integer(GLES20.glGetError()).toString());
		
		GLES20.glDisableVertexAttribArray(_position);
	}
}
