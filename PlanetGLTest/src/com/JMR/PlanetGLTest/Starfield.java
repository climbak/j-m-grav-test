package com.JMR.PlanetGLTest;

public class Starfield implements GLDrawable {
	private static final String starVertexShaderCode = 
		"uniform mat4 uMVPMatrix;   \n" +
		"attribute vec4 vPosition; \n" +
		"attribute vec4 vColor; \n" +
        "void main(){              \n" +
        " gl_Position = uMVPMatrix * vPosition; \n" +
        " gl_Color = vColor; \n" +
        "}                         \n";

	private static final String starFragmentShaderCode = 
		"varying vec4 gl_Color; \n" +
        "precision mediump float;  \n" +
        "void main(){              \n" +
        " gl_FragColor = gl_Color; \n" +
        "}                         \n";
	
	@Override
	public void draw(float[] sceneMatrix) {
		// TODO Auto-generated method stub

	}

}
