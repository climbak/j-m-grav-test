package com.JMR.PlanetTest;

import android.graphics.Rect;

public class StarFactory {
	public static final int MEAN_RADIUS = 20;
	public static final int RADIUS_JITTER = 5;
	
	public static Star getRandomStar() {
		// Create the star with a random type:
		int type = ((int)(Math.random()*500)%4);
		
		// Calculate a random radius:
		int radius = (int) (Math.random()*MEAN_RADIUS + (Math.random()*RADIUS_JITTER*2)-RADIUS_JITTER);
		
		// Calculate a random centerpoint:
		int centerX = (int) (GameState.getInstance().gameCanvas.getWidth()*Math.random());
		int centerY = (int) (GameState.getInstance().gameCanvas.getHeight()*Math.random());		
		
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
