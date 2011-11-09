package com.JMR.PlanetTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
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

	private enum PlanetType {
		Earth,
		Orange,
		Ice		
	}
	
	private PlanetType Type = PlanetType.Earth;
	private Bitmap texture;
	
	public CirclePlanet()
	{
		Type = PlanetType.values()[new Random().nextInt(3)];
	}
	
	private float parm_land_mass_pnt_percent;
	private int parm_ocean_color;
	private int parm_base_land_color;
	private int[][] parm_shade_colors;
	
	private void setPlanetParms(){
		switch (Type){
		case Earth:
			parm_land_mass_pnt_percent = .07f;
			parm_ocean_color = Color.BLUE;
			parm_base_land_color = Color.argb(255, 83, 105, 59);
			parm_shade_colors = new int[][]{{118, 99, 67}, {64, 75, 41}};
			break;
		case Ice:
			parm_land_mass_pnt_percent = .1f;
			parm_ocean_color = Color.BLUE;
			parm_base_land_color = Color.WHITE;
			parm_shade_colors = new int[][]{{200, 200, 200}};
			break;
		case Orange:
			parm_ocean_color = Color.RED;
			parm_land_mass_pnt_percent = .2f;
			parm_base_land_color = Color.RED;
			parm_shade_colors = new int[][]{{200, 200, 200}};
			break;
		}
	}
	
	public void Create(){
		
		setPlanetParms();
		
		Random rand = new Random();
		
		int start_cnt = (int)(this.radius * parm_land_mass_pnt_percent);
		int width = this.radius*2;
		int rmin = 20;
		int rjitter = 10;
		
		texture = Bitmap.createBitmap(width, width, Config.ARGB_8888);
		Canvas can = new Canvas(texture);
		
		
		Path path_circle = new Path();
		path_circle.addCircle(width/2, width/2, width/2.10f, Direction.CCW);
		Paint p = new Paint();
		
		p.setAntiAlias(true);

		Path halo_circle = new Path();
		halo_circle.addCircle(width/2, width/2, width/2, Direction.CCW);
		p.setARGB(50, 40, 100, 255);
		
		can.drawPath(halo_circle, p);
		
		can.clipPath(path_circle);
		
		p.setColor(parm_ocean_color);
		can.drawRect(0,0,width,width, p);

		p.setARGB(255, 102, 124, 38);
		
		p.setShader(getGroundBumpmap());
		p.setPathEffect(new CornerPathEffect(10f));
		
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
	
	private BitmapShader getGroundBumpmap(){
		int start_cnt = 6;
		int width = 200;
		int rmin = 20;
		int rjitter = 10;
		
		Bitmap b = Bitmap.createBitmap(width, width, Config.ARGB_4444);
		Canvas c = new Canvas(b);
		Random rand = new Random();
		Paint p = new Paint();
		
		int [][] colors = parm_shade_colors;

		// p.setARGB(255, 102, 124, 38);
		p.setColor(parm_base_land_color);
		
		c.drawRect(0,0, width, width, p);
		p.setPathEffect(new CornerPathEffect(10f));
		
		for (int col=0;col<colors.length;col++){
			
			p.setColor(Color.argb(130, colors[col][0], colors[col][1], colors[col][2]));
			p.setShader(getGroundBumpmap(colors[col][0], colors[col][1], colors[col][2], 0));
			
			for (int i=0;i<start_cnt;i++){
				int x = rand.nextInt(width);
				int y = rand.nextInt(width);
				
				Path path = new Path();
				
				switch (i){
					
				}
				
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
				
				
				c.drawPath(path, p);
			}
		}
		
		BitmapShader bs =  new BitmapShader(b, TileMode.REPEAT, TileMode.REPEAT);
		return bs;
	}
	
	private BitmapShader getGroundBumpmap(int red, int green, int blue, int depth){
		int start_cnt = 2;
		int width = 100;
		int rmin = 30/(depth+1);
		int rjitter = 5;
		
		Bitmap b = Bitmap.createBitmap(width, width, Config.ARGB_4444);
		Canvas c = new Canvas(b);
		Random rand = new Random();
		Paint p = new Paint();
		
		// p.setARGB(255, 102, 124, 38);
		p.setARGB(255, red,green,blue);
		c.drawRect(0,0, width, width, p);
		p.setPathEffect(new CornerPathEffect(10f));

		if (depth < 4){
			p.setShader(getGroundBumpmap(red + 20, green + 20, blue + 20, depth+1));
		}
		
		p.setColor(Color.argb(255, red + 20, green + 20, blue + 20));
		for (int i=0;i<start_cnt;i++){
			int x = rand.nextInt(width);
			int y = rand.nextInt(width);
			
			Path path = new Path();
			
			switch (i){
				
			}
			
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
			

			c.drawPath(path, p);
		}
		
		BitmapShader bs =  new BitmapShader(b, TileMode.REPEAT, TileMode.REPEAT);
		return bs;
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
		
		//Log.d("CirclePlanet.draw", "");
		
		canvas.drawBitmap(texture, myRect.exactCenterX(), myRect.exactCenterY(), myPaint);
		
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
