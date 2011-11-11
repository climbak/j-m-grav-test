package com.JMR.PlanetGLTest;

import java.util.Iterator;
import java.util.LinkedList;

public class GameBoard implements GLDrawable {
	public LinkedList<GLDrawable> stuffToDraw;
	
	@Override
	public void draw() {
		Iterator<GLDrawable> iter = stuffToDraw.iterator();
		
		while (iter.hasNext())
		{
			iter.next().draw();
		}
	}

}
