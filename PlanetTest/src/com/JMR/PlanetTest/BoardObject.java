package com.JMR.PlanetTest;

import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public abstract class BoardObject extends Drawable implements Animatable {
	public float g = 0.0f;
	public int radius = 0;
	public boolean canHaveTurrets = false;
	public Rect myRect;
	
	/*
	 * handleImpact
	 * 
	 * Inputs:
	 * 	x - the x coordinate of the projectile
	 *  y - the y coordinate of the projectile
	 *  
	 * Returns:
	 * 	True - Projectile hit surface, and is destroyed
	 * 	False - Projectile just passed through the planet's space, but not destroyed
	 * 
	 * This method is called once the physics engine determines a projectile is within radius
	 * 	of the planet. It must perform additional collision detection (for more complex objects),
	 * 	as well as draw impact and proximity graphics effects.
	 */
	public abstract boolean handleImpactXY(float x, float y);
	
	@Override
	public void setBounds(Rect bounds) {
		myRect = bounds;
		radius = Math.min(bounds.height(), bounds.width());
	}
	
	/*
	 * Override these to set aspect ratio. Game determines absolute size.
	 * @see android.graphics.drawable.Drawable#getIntrinsicWidth()
	 */
	@Override
	public int getIntrinsicWidth() {
		return 1;
	}
	
	@Override
	public int getIntrinsicHeight() {
		return 1;
	}
	
	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}
}
