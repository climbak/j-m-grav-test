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
	 * This subdivides a sphere to get more faces. It assumes the sphere has unit radius.
	 * COMPLETE, but breaks. Need to debug.
	 */
	public static void subdivide(float[] verts, int[] faces) {
		// Allocate new arrays of the proper size:
		//int newNumVerts = (int) Math.pow(3, numDivisions)*verts.length;
		//int newNumFaces = (int) Math.pow(4, numDivisions)*faces.length;
		int newNumVerts = 3 * faces.length + verts.length;
		int newNumFaces = 4 * faces.length;
		
		float[] inVerts = verts;
		int[] inFaces = faces;
		
		verts = new float[newNumVerts];
		faces = new int[newNumFaces];
		
		// Copy old verts into new vert array:
		for (int k = 0; k < inVerts.length; k++)
		{
			verts[k] = inVerts[k];
		}
		
		// Time to subdivide:
		float[] vert1 = new float[3];
		float[] vert2 = new float[3];
		float[] vert3 = new float[3];
		float[] newVert1 = new float[3];
		float[] newVert2 = new float[3];
		float[] newVert3 = new float[3];
		int newIndex1, newIndex2, newIndex3, currVertIndex, currFaceIndex;
		int[] face = new int[3];
		int[] newFace1 = new int[3];
		int[] newFace2 = new int[3];
		int[] newFace3 = new int[3];
		int[] newFace4 = new int[3];
		currVertIndex = inVerts.length;
		currFaceIndex = 0;
		for (int i = 0; i < inFaces.length; i++)
		{
			// Get current face:
			face[0] = inFaces[i*3+0];
			face[1] = inFaces[i*3+1];
			face[2] = inFaces[i*3+2];
			
			// Get those vertices:
			vert1[0] = inVerts[face[0]/3+0];
			vert1[1] = inVerts[face[0]/3+1];
			vert1[2] = inVerts[face[0]/3+2];
			vert2[0] = inVerts[face[1]/3+0];
			vert2[1] = inVerts[face[1]/3+1];
			vert2[2] = inVerts[face[1]/3+2];
			vert3[0] = inVerts[face[2]/3+0];
			vert3[1] = inVerts[face[2]/3+1];
			vert3[2] = inVerts[face[2]/3+2];
			
			// Make new vertices, positive permutation (12,23,31):
			newVert1[0] = (vert1[0] + vert2[0])/2.f;
			newVert1[1] = (vert1[1] + vert2[1])/2.f;
			newVert1[2] = (vert1[2] + vert2[2])/2.f;
			
			newVert2[0] = (vert2[0] + vert3[0])/2.f;
			newVert2[1] = (vert2[1] + vert3[1])/2.f;
			newVert2[2] = (vert2[2] + vert3[2])/2.f;
			
			newVert3[0] = (vert3[0] + vert1[0])/2.f;
			newVert3[1] = (vert3[1] + vert1[1])/2.f;
			newVert3[2] = (vert3[2] + vert1[2])/2.f;
			
			// Add those vertices to the array:
			newIndex1 = currVertIndex + 0;
			newIndex2 = currVertIndex + 3;
			newIndex3 = currVertIndex + 6;
			
			verts[currVertIndex++] = newVert1[0];
			verts[currVertIndex++] = newVert1[1];
			verts[currVertIndex++] = newVert1[2];
			
			verts[currVertIndex++] = newVert2[0];
			verts[currVertIndex++] = newVert2[1];
			verts[currVertIndex++] = newVert2[2];
			
			verts[currVertIndex++] = newVert3[0];
			verts[currVertIndex++] = newVert3[1];
			verts[currVertIndex++] = newVert3[2];
			
			/*
			 * Make new faces:
			 * {1,new1,new3}
			 * {3,new3,new2}
			 * {new1,new2,new3}
			 * {2,new2,new1}
			 */
			newFace1[0] = face[0];
			newFace1[1] = newIndex1;
			newFace1[2] = newIndex3;
			
			newFace2[0] = face[2];
			newFace2[1] = newIndex3;
			newFace2[2] = newIndex2;
			
			newFace3[0] = newIndex1;
			newFace3[1] = newIndex2;
			newFace3[2] = newIndex3;
			
			newFace4[0] = face[1];
			newFace4[1] = newIndex2;
			newFace4[2] = newIndex1;
			
			// AAAAaaand...add those to the face array:
			faces[currFaceIndex++] = newFace1[0];
			faces[currFaceIndex++] = newFace1[1];
			faces[currFaceIndex++] = newFace1[2];
			
			faces[currFaceIndex++] = newFace2[0];
			faces[currFaceIndex++] = newFace2[1];
			faces[currFaceIndex++] = newFace2[2];
			
			faces[currFaceIndex++] = newFace3[0];
			faces[currFaceIndex++] = newFace3[1];
			faces[currFaceIndex++] = newFace3[2];
			
			faces[currFaceIndex++] = newFace4[0];
			faces[currFaceIndex++] = newFace4[1];
			faces[currFaceIndex++] = newFace4[2];
		}
	}
}
