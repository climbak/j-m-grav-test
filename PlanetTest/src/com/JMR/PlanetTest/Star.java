package com.JMR.PlanetTest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.util.Log;

public class Star extends BoardObject {
	
	public static final int RED = 0;
	public static final int YELLOW = 1;
	public static final int WHITE = 2;
	public static final int BLUE = 3;
	
	public static final int NUM_LAYERS = 8;
		
	public int myColor;
	
	public Star(int myClass) {
		super();
		
		if (myClass < 0 || myClass > 3)
			myClass = 0;
		myColor = myClass;
	}
	
	@Override
	public boolean isRunning() {
		// Not animated, so always false
		return false;
	}

	@Override
	public void start() {
		// Not animated, so ignore
	}

	@Override
	public void stop() {
		// Not animated, so ignore
	}

	@Override
	public boolean handleImpactXY(float x, float y) {
		if (Math.sqrt((myRect.exactCenterX()-x)*(myRect.exactCenterX()-x)+(myRect.exactCenterY()-y)*(myRect.exactCenterY()-y)) <= radius)
			return true;
		return false;
	}

	@Override
	public void draw(Canvas canvas) {
		Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				
		for (int i = Star.NUM_LAYERS; i >= 0; i--)
		{
			switch(myColor)
			{
			case Star.BLUE:
				myPaint.setARGB(255/(Star.NUM_LAYERS-1)*(i+1), 200, 200, 255);
				break;
			case Star.RED:
				myPaint.setARGB(255/(Star.NUM_LAYERS-1)*(i+1), 255, 200, 200);
				break;
			case Star.WHITE:
				myPaint.setARGB(255/(Star.NUM_LAYERS-1)*(i+1), 255, 255, 255);
				break;
			case Star.YELLOW:
				myPaint.setARGB(255/(Star.NUM_LAYERS-1)*(i+1), 255, 255, 96);
				break;
			}
	
			canvas.drawCircle(myRect.exactCenterX(), myRect.exactCenterY(), 
					radius*.66f+(radius*.33f/Star.NUM_LAYERS*(i+1)), myPaint);
		}
		
	}

	@Override
	public void setAlpha(int alpha) {
		// Ignore alpha setting
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// Ignore
	}

}
