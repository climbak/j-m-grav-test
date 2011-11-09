package com.JMR.PlanetTest;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Background extends Drawable {
	public static final int NUM_STARS = 3000;
	
	public static final float YELLOW_PERCENT = 0.33f;
	public static final float WHITE_PERCENT = 0.33f;
	public static final float RED_PERCENT = 0.20f;
	public static final float BLUE_PERCENT = 0.14f;
	
	public static final int NUM_YELLOW = (int)(((float)NUM_STARS)*YELLOW_PERCENT);
	public static final int NUM_RED = (int)(((float)NUM_STARS)*RED_PERCENT);
	public static final int NUM_BLUE = (int)(((float)NUM_STARS)*BLUE_PERCENT);
	public static final int NUM_WHITE = (int)(((float)NUM_STARS)*WHITE_PERCENT);
	
	public static final int[] BLUE_RGB = {255*9/16,255*11/16,255};
	public static final int[] WHITE_RGB = {255,255,255};
	public static final int[] YELLOW_RGB = {255,255,128};
	public static final int[] RED_RGB = {255,128,128};
	
	private float redStars[];
	private float yellowStars[];
	private float blueStars[];
	private float whiteStars[];
	
	private Paint p;
	
	public Background(Canvas canvas) {
		// Create arrays for stars (multiply by 2 to hold x & y)
		redStars = new float[Background.NUM_RED*2];
		yellowStars = new float[Background.NUM_YELLOW*2];
		whiteStars = new float[Background.NUM_WHITE*2];
		blueStars = new float[Background.NUM_BLUE*2];
		
		// Fill each array
		for (int i = 0; i < Background.NUM_BLUE*2; i+=2)
		{
			blueStars[i] = (float)(Math.random()*canvas.getWidth());
			blueStars[i+1] = (float)(Math.random()*canvas.getHeight());
		}
		for (int i = 0; i < Background.NUM_RED*2; i+=2)
		{
			redStars[i] = (float)(Math.random()*canvas.getWidth());
			redStars[i+1] = (float)(Math.random()*canvas.getHeight());
		}
		for (int i = 0; i < Background.NUM_YELLOW*2; i+=2)
		{
			yellowStars[i] = (float)(Math.random()*canvas.getWidth());
			yellowStars[i+1] = (float)(Math.random()*canvas.getHeight());
		}
		for (int i = 0; i < Background.NUM_WHITE*2; i+=2)
		{
			whiteStars[i] = (float)(Math.random()*canvas.getWidth());
			whiteStars[i+1] = (float)(Math.random()*canvas.getHeight());
		}
		
		// Initialize the paint:
		p = new Paint();
	}
	
	@Override
	public void draw(Canvas canvas) {
		// Fill black:
		canvas.drawRGB(0, 0, 0);
		
		// Red Stars:
		for (int i = 0; i < Background.NUM_RED*2; i+=2)
		{
			p.setARGB(i % 256, Background.RED_RGB[0], Background.RED_RGB[1], Background.RED_RGB[2]);
			canvas.drawPoint(redStars[i],redStars[i+1], p);
		}
		
		// Yellow Stars:
		for (int i = 0; i < Background.NUM_YELLOW*2; i+=2)
		{
			p.setARGB(i % 256, Background.YELLOW_RGB[0], Background.YELLOW_RGB[1], Background.YELLOW_RGB[2]);
			canvas.drawPoint(yellowStars[i],yellowStars[i+1], p);
		}
		
		// White Stars:
		for (int i = 0; i < Background.NUM_WHITE*2; i+=2)
		{
			p.setARGB(i % 256, Background.WHITE_RGB[0], Background.WHITE_RGB[1], Background.WHITE_RGB[2]);
			canvas.drawPoint(whiteStars[i],whiteStars[i+1], p);
		}
		
		// Blue Stars:
		for (int i = 0; i < Background.NUM_BLUE*2; i+=2)
		{
			p.setARGB(i % 256, Background.BLUE_RGB[0], Background.BLUE_RGB[1], Background.BLUE_RGB[2]);
			canvas.drawPoint(blueStars[i],blueStars[i+1], p);
		}
	}

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// ignore		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// ignore		
	}

}
