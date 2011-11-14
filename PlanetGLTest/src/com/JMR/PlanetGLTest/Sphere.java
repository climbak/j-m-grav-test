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
	public static final int X = 0;
	public static final int Y = 1;
	public static final int Z = 2;
	public static float[] subdivide(float[] verts) {
		int newNumVerts = verts.length * 36;
		float dist;
		
		float [] newVerts = new float[newNumVerts];
		
		for (int i = 0; i < verts.length; i+=9)
		{
			/*
			 *  Subdivide Top face:
			 */
			// Vertex 1:
			newVerts[i*36+0] = verts[i+X];
			newVerts[i*36+1] = verts[i+Y];
			newVerts[i*36+2] = verts[i+Z];
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+0]*newVerts[i*36+0] + 
					newVerts[i*36+1]*newVerts[i*36+1] + 
					newVerts[i*36+2]*newVerts[i*36+2]));
			
			// Normalize:
			newVerts[i*36+0] /= dist;
			newVerts[i*36+1] /= dist;
			newVerts[i*36+2] /= dist;
			
			// Vertex 2:
			newVerts[i*36+3] = (verts[i+X]+verts[i+X+3])/2.f;
			newVerts[i*36+4] = (verts[i+Y]+verts[i+Y+3])/2.f;
			newVerts[i*36+5] = (verts[i+Z]+verts[i+Z+3])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+3]*newVerts[i*36+3] + 
					newVerts[i*36+4]*newVerts[i*36+4] + 
					newVerts[i*36+5]*newVerts[i*36+5]));
			
			// Normalize:
			newVerts[i*36+3] /= dist;
			newVerts[i*36+4] /= dist;
			newVerts[i*36+5] /= dist;
			
			// Vertex 3:
			newVerts[i*36+6] = (verts[i+X]+verts[i+X+6])/2.f;
			newVerts[i*36+7] = (verts[i+Y]+verts[i+Y+6])/2.f;
			newVerts[i*36+8] = (verts[i+Z]+verts[i+Z+6])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+6]*newVerts[i*36+6] + 
					newVerts[i*36+7]*newVerts[i*36+7] + 
					newVerts[i*36+8]*newVerts[i*36+8]));
			
			// Normalize:
			newVerts[i*36+6] /= dist;
			newVerts[i*36+7] /= dist;
			newVerts[i*36+8] /= dist;
			
			/***************************************************************/
			/*
			 *  Subdivide Bottom Left face:
			 */
			// Vertex 1:
			newVerts[i*36+9] = (verts[i+X]+verts[i+X+6])/2.f;
			newVerts[i*36+10] = (verts[i+Y]+verts[i+Y+6])/2.f;
			newVerts[i*36+11] = (verts[i+Z]+verts[i+Z+6])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+9]*newVerts[i*36+9] + 
					newVerts[i*36+10]*newVerts[i*36+10] + 
					newVerts[i*36+11]*newVerts[i*36+11]));
			
			// Normalize:
			newVerts[i*36+9] /= dist;
			newVerts[i*36+10] /= dist;
			newVerts[i*36+11] /= dist;
			
			// Vertex 2:
			newVerts[i*36+12] = (verts[i+X+3]+verts[i+X+6])/2.f;
			newVerts[i*36+13] = (verts[i+Y+3]+verts[i+Y+6])/2.f;
			newVerts[i*36+14] = (verts[i+Z+3]+verts[i+Z+6])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+12]*newVerts[i*36+12] + 
					newVerts[i*36+13]*newVerts[i*36+13] + 
					newVerts[i*36+14]*newVerts[i*36+14]));
			
			// Normalize:
			newVerts[i*36+12] /= dist;
			newVerts[i*36+13] /= dist;
			newVerts[i*36+14] /= dist;
			
			// Vertex 3:
			newVerts[i*36+15] = verts[i+X+6];
			newVerts[i*36+16] = verts[i+Y+6];
			newVerts[i*36+17] = verts[i+Z+6];
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+15]*newVerts[i*36+15] + 
					newVerts[i*36+16]*newVerts[i*36+16] + 
					newVerts[i*36+17]*newVerts[i*36+17]));
			
			// Normalize:
			newVerts[i*36+15] /= dist;
			newVerts[i*36+16] /= dist;
			newVerts[i*36+17] /= dist;
			
			/***************************************************************/
			/*
			 *  Subdivide Center face:
			 */
			// Vertex 1:
			newVerts[i*36+18] = (verts[i+X]+verts[i+X+6])/2.f;
			newVerts[i*36+19] = (verts[i+Y]+verts[i+Y+6])/2.f;
			newVerts[i*36+20] = (verts[i+Z]+verts[i+Z+6])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+18]*newVerts[i*36+18] + 
					newVerts[i*36+19]*newVerts[i*36+19] + 
					newVerts[i*36+20]*newVerts[i*36+20]));
			
			// Normalize:
			newVerts[i*36+18] /= dist;
			newVerts[i*36+19] /= dist;
			newVerts[i*36+20] /= dist;
			
			// Vertex 2:
			newVerts[i*36+21] = (verts[i+X+3]+verts[i+X])/2.f;
			newVerts[i*36+22] = (verts[i+Y+3]+verts[i+Y])/2.f;
			newVerts[i*36+23] = (verts[i+Z+3]+verts[i+Z])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+21]*newVerts[i*36+21] + 
					newVerts[i*36+22]*newVerts[i*36+22] + 
					newVerts[i*36+23]*newVerts[i*36+23]));
			
			// Normalize:
			newVerts[i*36+21] /= dist;
			newVerts[i*36+22] /= dist;
			newVerts[i*36+23] /= dist;
			
			// Vertex 3:
			newVerts[i*36+24] = (verts[i+X+3]+verts[i+X+6])/2.f;
			newVerts[i*36+25] = (verts[i+Y+3]+verts[i+Y+6])/2.f;
			newVerts[i*36+26] = (verts[i+Z+3]+verts[i+Z+6])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+24]*newVerts[i*36+24] + 
					newVerts[i*36+25]*newVerts[i*36+25] + 
					newVerts[i*36+26]*newVerts[i*36+26]));
			
			// Normalize:
			newVerts[i*36+24] /= dist;
			newVerts[i*36+25] /= dist;
			newVerts[i*36+26] /= dist;
			
			/***************************************************************/
			/*
			 *  Subdivide Bottom Right face:
			 */
			// Vertex 1:
			newVerts[i*36+27] = (verts[i+X]+verts[i+X+3])/2.f;
			newVerts[i*36+28] = (verts[i+Y]+verts[i+Y+3])/2.f;
			newVerts[i*36+29] = (verts[i+Z]+verts[i+Z+3])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+27]*newVerts[i*36+27] + 
					newVerts[i*36+28]*newVerts[i*36+28] + 
					newVerts[i*36+29]*newVerts[i*36+29]));
			
			// Normalize:
			newVerts[i*36+27] /= dist;
			newVerts[i*36+28] /= dist;
			newVerts[i*36+29] /= dist;
			
			// Vertex 2:
			newVerts[i*36+30] = verts[i+X+3];
			newVerts[i*36+31] = verts[i+Y+3];
			newVerts[i*36+32] = verts[i+Z+3];
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+30]*newVerts[i*36+30] + 
					newVerts[i*36+31]*newVerts[i*36+31] + 
					newVerts[i*36+32]*newVerts[i*36+32]));
			
			// Normalize:
			newVerts[i*36+30] /= dist;
			newVerts[i*36+31] /= dist;
			newVerts[i*36+32] /= dist;
			
			// Vertex 3:
			newVerts[i*36+33] = (verts[i+X+3]+verts[i+X+6])/2.f;
			newVerts[i*36+34] = (verts[i+Y+3]+verts[i+Y+6])/2.f;
			newVerts[i*36+35] = (verts[i+Z+3]+verts[i+Z+6])/2.f;
			
			// Calculate the radius:
			dist = (float) (Math.sqrt(newVerts[i*36+33]*newVerts[i*36+33] + 
					newVerts[i*36+34]*newVerts[i*36+34] + 
					newVerts[i*36+35]*newVerts[i*36+35]));
			
			// Normalize:
			newVerts[i*36+33] /= dist;
			newVerts[i*36+34] /= dist;
			newVerts[i*36+35] /= dist;
		}
		
		return newVerts;
	}
}
