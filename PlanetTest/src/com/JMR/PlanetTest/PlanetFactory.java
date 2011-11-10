package com.JMR.PlanetTest;

import java.util.Iterator;
import java.util.Random;

import android.graphics.Rect;
import android.util.Log;

public class PlanetFactory {
	public static final int MEAN_RADIUS = 60;
	public static final int RADIUS_JITTER = 28;
	
	public static BoardObject getRandomPlanet() {
		CirclePlanet circ = new CirclePlanet();
		
		int radius = 0;
		int centerX = 0;
		int centerY = 0;
		int deltaX, deltaY, radSum;
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
				deltaX = testObj.getBounds().centerX()-centerX;
				deltaY = testObj.getBounds().centerY()-centerY;
				radSum = testObj.radius+radius;
				if (Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= radSum * 2)
				{
					Log.d("PlanetFactory.getRandomPlanet", "Collision!");
					keepTrying = true;
					break;
				}
			}
		}
		
		// Make a bounding rect:
		Rect boundingRect = new Rect(centerX-radius, centerY-radius, centerX+radius, centerY+radius);
		circ.setBounds(boundingRect);
		circ.Create();
		return circ;
	}
}
