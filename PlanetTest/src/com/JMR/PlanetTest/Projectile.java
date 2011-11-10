package com.JMR.PlanetTest;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public class Projectile extends Drawable implements Animatable {

	// TODO stub class
	
	public float vx;
	public float vy;
	public float x;
	public float y;

	public Projectile(float x, float y, float vx, float vy){
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
	}
	
	@Override
	public void draw(Canvas canvas) {
		Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
		myPaint.setARGB(255,255,255,255);
		
		canvas.drawCircle(x, y, 5, myPaint);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		// Ignore
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// Ignore
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
