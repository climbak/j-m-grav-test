package com.JMR.PlanetTest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class BlackHole extends BoardObject {
	public static final float BASE_G = 50.f;
	
	public BlackHole() {
		super();
		
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
		int centerX = this.myRect.centerX();
		int centerY = this.myRect.centerY();
		
				
		Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
		myPaint.setARGB(48, 200, 200, 255);
		
		// Draw a faint accretion disc like thing:
		canvas.drawCircle(centerX, centerY, radius, myPaint);
		
		// Draw the black center:
		myPaint.setARGB(255, 0, 0, 0);
		canvas.drawCircle(centerX, centerY, radius*.9f, myPaint);
	}
	
	@Override
	public void setBounds(Rect r)
	{
		super.setBounds(r);
		g = BASE_G;// (1.0f-(BlackHoleFactory.MEAN_RADIUS-radius))*BASE_G;
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
