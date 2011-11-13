package com.JMR.PlanetGLTest;

public class Sphere {
	public static float A = 2.0f/(1.0f + (float)Math.sqrt(5.0));
	
	public static float[] ICOSAHEDRON_VERTICES = {
		0.0f, A, -1.0f,
		-A, 1.0f, 0.0f,
		A, 1.0f, 0.0f,
		0.0f, A, 1.0f,
		-1.0f, 0.0f, A,
		0.0f, -A, 1.0f,
		1.0f, 0.0f, A,
		1.0f, 0.0f, -A,
		0.0f, -A, -1.0f,
		-1.0f, 0.0f, -A,
		-A, -1.0f, 0.0f,
		A, -1.0f, 0.0f
	};
	public static int[] ICOSAHEDRON_INDICES = {
		
	};
}
