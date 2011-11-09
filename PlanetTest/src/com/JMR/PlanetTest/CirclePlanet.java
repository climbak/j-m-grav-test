package com.JMR.PlanetTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Bitmap.Config;
import android.graphics.Path.Direction;
import android.graphics.Shader.TileMode;
import android.util.Log;

public class CirclePlanet extends BoardObject {

	private Bitmap texture;
	
	public CirclePlanet()
	{

	}
	
	public void Create(){
		
		
		this.setDither(true);
		/*

		int start_cnt = 30;
		int width = 200;
		int height = 200;
		int rmin = 20;
		int rjitter = 10;
		*/
		
		int start_cnt = radius / 19;
		int width = this.radius;
		int rmin = 20;
		int rjitter = 10;
		
		texture = Bitmap.createBitmap(width, width, Config.ARGB_8888);
		Canvas can = new Canvas(texture);
		Random rand = new Random();
		
		Path path_circle = new Path();
		path_circle.addCircle(width/2, width/2, width/2.10f, Direction.CCW);
		Paint p = new Paint();
		
		p.setAntiAlias(true);
		
		Path halo_circle = new Path();
		halo_circle.addCircle(width/2, width/2, width/2, Direction.CCW);
		p.setARGB(50, 40, 100, 255);
		can.drawPath(halo_circle, p);
		
		can.clipPath(path_circle);
		
		p.setColor(Color.DKGRAY);
		can.drawRect(0,0,width,width, p);

		p.setColor(Color.BLUE);
		
		
		for (int i=0;i<start_cnt;i++){
			int x = rand.nextInt(width);
			int y = rand.nextInt(width);
			
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
		
		Path path_shadow = new Path();
		path_shadow.addCircle(width/2 - 20, width/2 - 20, width/2, Direction.CCW);
		
		int left = Color.argb(0, 0, 0, 0);
		int right = Color.argb(200, 0, 0, 0);
		
		
		Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		RadialGradient rg = new RadialGradient((float)(width/2 - 20), (float)(width/2 ), (float)(width/1.5), new int[]{left, left, right}, null, TileMode.CLAMP);
		p2.setShader(rg);
		can.drawPath(path_circle, p2);
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
		
		canvas.drawBitmap(texture, this.getBounds().exactCenterX(), this.getBounds().exactCenterY(), myPaint);
		
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
