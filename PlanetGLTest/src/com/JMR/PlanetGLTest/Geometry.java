package com.JMR.PlanetGLTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Hashtable;

import android.content.Context;
import android.util.Log;


public class Geometry {
	public static final Geometry Instance = new Geometry();
	
	private Context _context;
	
	Hashtable<Integer, float[]> _cache_vert = new Hashtable<Integer, float[]>();
	Hashtable<Integer, float[]> _cache_norm = new Hashtable<Integer, float[]>();
	
	public void setContext(Context c){
		_context = c;
	}
	
	public FloatBuffer getVertices(int resource_id){
		if (!_cache_vert.contains(resource_id)){
			loadObje(resource_id);
		}
		
		FloatBuffer v_fb;
		float [] v_arr = _cache_vert.get(resource_id);
		ByteBuffer v_bb = ByteBuffer.allocateDirect(v_arr.length * Float.SIZE); // 4 bytes per float or something
		v_bb.order(ByteOrder.nativeOrder());
		v_fb = v_bb.asFloatBuffer();
		v_fb.put(v_arr);
		v_fb.position(0);

		return v_fb;
	}
	
	public FloatBuffer getNormals(int resource_id){
		if (!_cache_norm.contains(resource_id)){
			loadObje(resource_id);
		}
		
		FloatBuffer n_fb;
		float [] n_arr = _cache_norm.get(resource_id);
		ByteBuffer n_bb = ByteBuffer.allocateDirect(n_arr.length * Float.SIZE); // 4 bytes per float or something
		n_bb.order(ByteOrder.nativeOrder());
		n_fb = n_bb.asFloatBuffer();
		n_fb.put(n_arr);
		n_fb.position(0);
		
		return n_fb;
	}
	
	public int getVertCount(int resource_id){
		if (!_cache_vert.contains(resource_id)){
			loadObje(resource_id);
		}
		
		return _cache_vert.get(resource_id).length / 3; 
	}
	
	private void loadObje(int resource_id){
		Log.d("Geometry", "loadObje : " + resource_id);
		
		InputStream str = _context.getResources().openRawResource(resource_id);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(str));
		
		try {
			// read first line (should be "v [size of v array]")
			String line = br.readLine();
			
			int v_count = Integer.parseInt(line.split(" ")[1]);
			float [] v_arr = new float[v_count];
			
			for (int i=0;i<v_count;i++){
				line = br.readLine();
				
				 v_arr[i] = Float.parseFloat(line);
			}
			
			// now read normals "n [size of normal array]"
			line = br.readLine();
			
			int n_count = Integer.parseInt(line.split(" ")[1]);
			float [] n_arr = new float[n_count];
			
			for (int i=0;i<n_count;i++){
				line = br.readLine();
				
				 n_arr[i] = Float.parseFloat(line);
			}

			Log.d("Geometry", "v_arr.length : " + v_arr.length + "");
			Log.d("Geometry", "n_arr.length : " + n_arr.length + "");

			
			_cache_vert.put(resource_id, v_arr);
			_cache_norm.put(resource_id, n_arr);
			
			br.close();
		} catch (IOException e) {
			Log.d("Geometry", "ERROR : " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
