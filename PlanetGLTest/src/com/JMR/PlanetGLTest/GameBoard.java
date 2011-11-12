package com.JMR.PlanetGLTest;

import java.util.Iterator;
import java.util.LinkedList;

import android.opengl.Matrix;

public class GameBoard implements GLDrawable {
	public LinkedList<GLDrawable> stuffToDraw;
	
	@Override
	public void draw(float[] sceneMatrix) {
		Iterator<GLDrawable> iter = stuffToDraw.iterator();
		
		while (iter.hasNext())
		{
			iter.next().draw(sceneMatrix);
		}
	}

	public void viewPortChange(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
