package com.JMR.PlanetTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.util.Log;

public class CirclePlanet extends BoardObject {

	private Bitmap texture;
	
	public CirclePlanet()
	{
		int start_cnt = 6;
		int width = 200;
		int height = 200;
		int rmin = 50;
		int rjitter = 20;
		
		texture = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas can = new Canvas(texture);
		Random rand = new Random();
		
		Paint p = new Paint();
		
		p.setColor(Color.BLUE);
				
		for (int i=0;i<start_cnt;i++){
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			
			Path path = new Path();
			
			boolean run = false;
			
			int radius;
			double r = -Math.PI;
			
			while (r < Math.PI)
			{
				if (!run){					
					radius = rmin + rand.nextInt(rjitter);
					float nx = (float)Math.cos(r) * radius + x;
					float ny = (float)Math.sin(r) * radius + y;
					
					path.moveTo(nx, ny);
					run = true;
				}
				
				r += rand.nextDouble() *.2;
				
				radius = rmin + rand.nextInt(rjitter);
				float nx1 = (float)Math.cos(r) * radius + x;
				float ny1 = (float)Math.sin(r) * radius + y;
				
				r += rand.nextDouble() *.2;
				
				radius = radius + (rjitter/2) - rand.nextInt(rjitter);
				float nx2 = (float)Math.cos(r) * radius + x;
				float ny2 = (float)Math.sin(r) * radius + y;
				
				r += rand.nextDouble() *.2;
				
				radius = radius + (rjitter/2) - rand.nextInt(rjitter);
				float nx3 = (float)Math.cos(r) * radius + x;
				float ny3 = (float)Math.sin(r) * radius + y;
				
				path.cubicTo(nx1, ny1, nx2, ny2, nx3, ny3);
				//path.lineTo(nx, ny);
			}
			
			can.drawPath(path, p);
		}
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
		
		myPaint.setARGB(255, 100, 100, 100);

		canvas.drawRect(100,100,300,300, myPaint);

		canvas.drawBitmap(texture, 100, 100, myPaint);
		
		/*
		canvas.drawCircle(
				100, 
				100, 90, 
				myPaint);*/
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
