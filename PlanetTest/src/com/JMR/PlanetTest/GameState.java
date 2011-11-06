package com.JMR.PlanetTest;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameState {
	public static final int CANVAS_WIDTH = 800;
	public static final int CANVAS_HEIGHT = 1024;
	
	public static GameState instance;
	
	public static GameState getInstance() {
		if (instance == null)
			instance = new GameState();
		return instance;
	}
	
	private GameState(){
		// Create the game map:
		gameMap = Bitmap.createBitmap(CANVAS_WIDTH, CANVAS_HEIGHT, Bitmap.Config.ARGB_8888);
		gameCanvas = new Canvas(gameMap);
		scale = 1.0f;
	}
	
	/////////////////////////////
	public Canvas gameCanvas;
	public Bitmap gameMap;
	public float scale;
}
