package com.JMR.PlanetGLTest;

public class Sphere {
	/*
	 * Create a buffer out of the vertices as normal. ALSO create a buffer out of the indices.
	 * Then use glDrawElements, not glDrawArrays. Not sure if "type" parameter should be GLES20.GL_INT
	 * or GLES20.GL_UNSIGNED_INT. Spec says unsigned int, but Java does not have unsigned ints...
	 */
	
	/*
	 * Icosahedron:
	 */
	public static final int ZA = 0;
	public static final int ZB = 1;
	public static final int ZC = 2;
	public static final int ZD = 3;
	public static final int YA = 4;
	public static final int YB = 5;
	public static final int YC = 6;
	public static final int YD = 7;
	public static final int XA = 8;
	public static final int XB = 9;
	public static final int XC = 10;
	public static final int XD = 11;
	
	// These give an icosahedron where all the vertices lie on the unit sphere.
	public static float C1 = 0.8506508084f;
	public static float C2 = 0.5257311121f;
	
	public static float[] ICOSAHEDRON_VERTICES = {
		C1, C2, 0,		// ZA
		-C1, C2, 0,		// ZB
		-C1, -C2, 0,	// ZC
		C1, -C2, 0,		// ZD
		C2, 0, C1,		// YA
		C2, 0, -C1,		// YB
		-C2, 0, -C1,	// YC
		-C2, 0, C1,		// YD
		0, C1, C2,		// XA
		0, -C1, C2,		// XB
		0, -C1, -C2,	// XC
		0, C1, -C2		// XD
	};
	
	public static byte[] ICOSAHEDRON_INDICES = {
		YA, XA, YD,
		YA, YD, XB,
		YB, YC, XD,
		YB, XC, YC,
		ZA, YA, ZD,
		ZA, ZD, YB,
		ZC, YD, ZB,
		ZC, ZB, YC,
		XA, ZA, XD,
		XA, XD, ZB,
		XB, XC, ZD,
		XB, ZC, XC,
		XA, YA, ZA,
		XD, ZA, YB,
		YA, XB, ZD,
		YB, ZD, XC,
		YD, XA, ZB,
		YC, ZB, XD,
		YD, ZC, XB,
		YC, XC, ZC
	};
	
	/*
	 * Or just do drawArrays with this:
	 */
	public static float[] ICOSAHEDRON = {
		 C2,   0,  C1,   0,  C1,  C2, -C2,   0,  C1,
		 C2,   0,  C1, -C2,   0,  C1,   0, -C1,  C2,
		 C2,   0, -C1, -C2,   0, -C1,   0,  C1, -C2,
		 C2,   0, -C1,   0, -C1, -C2, -C2,   0, -C1,
		 C1,  C2,   0,  C2,   0,  C1,  C1, -C2,   0,
		 C1,  C2,   0,  C1, -C2,   0,  C2,   0, -C1,
		-C1, -C2,   0, -C2,   0,  C1, -C1,  C2,   0,
		-C1, -C2,   0, -C1,  C2,   0, -C2,   0, -C1,
		  0,  C1,  C2,  C1,  C2,   0,   0,  C1, -C2,
		  0,  C1,  C2,   0,  C1, -C2, -C1,  C2,   0,
		  0, -C1,  C2,   0, -C1, -C2,  C1, -C2,   0,
		  0, -C1,  C2, -C1, -C2,   0,   0, -C1, -C2,
		  0,  C1,  C2,  C2,   0,  C1,  C1,  C2,   0,
		  0,  C1, -C2,  C1,  C2,   0,  C2,   0, -C1,
		 C2,   0,  C1,   0, -C1,  C2,  C1, -C2,   0,
		 C2,   0, -C1,  C1, -C2,   0,   0, -C1, -C2,
		-C2,   0,  C1,   0,  C1,  C2, -C1,  C2,   0,
		-C2,   0, -C1, -C1,  C2,   0,   0,  C1, -C2,
		-C2,   0,  C1, -C1, -C2,   0,   0, -C1,  C2,
		-C2,   0, -C1,   0, -C1, -C2, -C1, -C2,   0
	};
	
	/*
	 * This subdivides a sphere to get more faces. It assumes the sphere has unit radius.
	 * COMPLETE, but breaks. Need to debug.
	 */
	public static float[] subdivide(float[] verts) {
		int newNumVerts = verts.length * 4;
		float dist;
		
		float[] inVerts = verts;
		float [] newVerts = new float[newNumVerts];
		
		for (int i = 0; i < inVerts.length; i+=3) // Every 3 is a vert, every 3 verts is a face
		{
			/*
			 *  Subdivide Top face:
			 */
			newVerts[i*4+0] = inVerts[i];
			newVerts[i*4+1] = (inVerts[i]+inVerts[i+1])/2.f;
			newVerts[i*4+2] = (inVerts[i]+inVerts[i+2])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*4+0]*newVerts[i*4+0] + 
					newVerts[i*4+1]*newVerts[i*4+1] + 
					newVerts[i*4+2]*newVerts[i*4+2]));
			
			// Normalize:
			newVerts[i*4+0] /= dist;
			newVerts[i*4+1] /= dist;
			newVerts[i*4+2] /= dist;
			
			/*
			 *  Subdivide Bottom Left face:
			 */
			newVerts[i*4+3] = (inVerts[i]+inVerts[i+2])/2.f;
			newVerts[i*4+4] = (inVerts[i+1]+inVerts[i+2])/2.f;
			newVerts[i*4+5] = inVerts[i+2];
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*4+3]*newVerts[i*4+3] + 
					newVerts[i*4+4]*newVerts[i*4+4] + 
					newVerts[i*4+5]*newVerts[i*4+5]));
			
			// Normalize:
			newVerts[i*4+3] /= dist;
			newVerts[i*4+4] /= dist;
			newVerts[i*4+5] /= dist;
			
			/*
			 *  Subdivide Center face:
			 */
			newVerts[i*4+6] = (inVerts[i]+inVerts[i+2])/2.f;
			newVerts[i*4+7] = (inVerts[i]+inVerts[i+1])/2.f;
			newVerts[i*4+8] = (inVerts[i+1]+inVerts[i+2])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*4+6]*newVerts[i*4+6] + 
					newVerts[i*4+7]*newVerts[i*4+7] + 
					newVerts[i*4+8]*newVerts[i*4+8]));
			
			// Normalize:
			newVerts[i*4+6] /= dist;
			newVerts[i*4+7] /= dist;
			newVerts[i*4+8] /= dist;
			
			/*
			 *  Subdivide Bottom Right face:
			 */
			newVerts[i*4+9] = (inVerts[i]+inVerts[i+1])/2.f;
			newVerts[i*4+10]= inVerts[i+1];
			newVerts[i*4+11]= (inVerts[i+1]+inVerts[i+2])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*4+9]*newVerts[i*4+9] + 
					newVerts[i*4+10]*newVerts[i*4+10] + 
					newVerts[i*4+11]*newVerts[i*4+11]));
			
			// Normalize:
			newVerts[i*4+9] /= dist;
			newVerts[i*4+10] /= dist;
			newVerts[i*4+11] /= dist;
		}
		
		return newVerts;
	}
}
