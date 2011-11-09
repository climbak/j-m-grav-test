package com.JMR.PlanetTest;

import java.util.Iterator;

import android.graphics.Rect;
import android.util.Log;

public class StarFactory {
	public static final int MEAN_RADIUS = 50;
	public static final int RADIUS_JITTER = 10;
	
	public static Star getRandomStar() {
		// Create the star with a random type:
		int type = ((int)(Math.random()*500)%4);
		
		int radius = 0;
		int centerX = 0;
		int centerY = 0;
		boolean keepTrying = true;
		Iterator list;
		BoardObject testObj;
		
		// Keep coming up with centers and radii until we don't collide with anything:
		while (keepTrying)
		{
			// Calculate a random radius:
			radius = (int) (MEAN_RADIUS + (Math.random()*RADIUS_JITTER*2)-RADIUS_JITTER);
			
			// Calculate a random centerpoint:
			centerX = (int) (GameState.getInstance().gameCanvas.getWidth()*Math.random());
			centerY = (int) (GameState.getInstance().gameCanvas.getHeight()*Math.random());
			
			keepTrying = false;
			
			list = GameState.getInstance().boardObjects.iterator();
			while(list.hasNext())
			{
				testObj = (BoardObject)list.next();
				if (Math.sqrt((testObj.getBounds().centerX()-centerX)*(testObj.getBounds().centerX()-centerX)+
						(testObj.getBounds().centerY()-centerY)*(testObj.getBounds().centerY()-centerY))<
						(testObj.radius+radius)*2)
				{
					keepTrying = true;
					break;
				}
			}
		}		
		
		return getStar(type,centerX,centerY,radius);
	}
	
	public static Star getStar(int type, int centerX, int centerY, int radius) {
		// Create a new star with the given type:
		Star theStar = new Star(type);
		
		// Make a bounding rect:
		Rect boundingRect = new Rect(centerX-radius, centerY-radius, centerX+radius, centerY+radius);
		
		// Set the stars bounding rect:
		theStar.setBounds(boundingRect);
		
		return theStar;
	}
}
