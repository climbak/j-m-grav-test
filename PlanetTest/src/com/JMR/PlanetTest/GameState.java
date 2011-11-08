package com.JMR.PlanetTest;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameState {
	public static final int BOARD_WIDTH = 1080;
	public static final int BOARD_HEIGHT = 1920;
	
	public static GameState instance;
	
	public static GameState getInstance() {
		if (instance == null)
			instance = new GameState();
		return instance;
	}
	
	private GameState(){
		// Create the game map:
		gameMap = Bitmap.createBitmap(BOARD_WIDTH, BOARD_HEIGHT, Bitmap.Config.ARGB_8888);
		gameCanvas = new Canvas(gameMap);
		scale = 1.0f;
	}
	
	/////////////////////////////
	public Canvas gameCanvas;
	public Bitmap gameMap;
	public float scale;   // Not sure I actually use this in any way
	public LinkedList<BoardObject> boardPlanets;
	
	public void flip() {
		gameCanvas.rotate(90.f, gameCanvas.getWidth()/2.f, gameCanvas.getHeight()/2.f);
	}
}
