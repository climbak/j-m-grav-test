package com.JMR.PlanetTest;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;

public class CirclePlanet extends BoardObject {

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
		
		myPaint.setARGB(255, 180, 0, 0);
		
		canvas.drawCircle(myRect.exactCenterX(), myRect.exactCenterY(), radius, myPaint);
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
