package com.JMR.PlanetGLTest;

import java.util.Iterator;
import java.util.LinkedList;

import android.opengl.Matrix;

public class GameBoard implements GLDrawable {
	public static final GameBoard Instance = new GameBoard();
	
	public LinkedList<GLDrawable> stuffToDraw = new LinkedList<GLDrawable>();
	
	private GameBoard(){}
	
	public void create(){
		
	}
	
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

	public void add(GLDrawable drawable){
		stuffToDraw.add(drawable);
	}
	
	public void remove(GLDrawable drawable){
		stuffToDraw.remove(drawable);
	}
}
