package com.JMR.PlanetTest;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public class GameState extends Drawable implements Animatable {
	public static final int STATE_PLAYER1_TURN = 1;
	public static final int STATE_PLAYER2_TURN = 2;
	public static final int STATE_SIMULATING = 0;
	public static final int STATE_GAME_OVER = -1;
	
	public static GameState instance;
	
	public static GameState getInstance() {
		if (instance == null)
			instance = new GameState();
		return instance;
	}
	
	private GameState(){
		landscape = false;
		turn = -1;
	}
	
	/////////////////////////////
	public Canvas gameCanvas;
	public Bitmap gameMap;
	public boolean landscape;
	public LinkedList<BoardObject> boardObjects;
	public Base player1Base;
	public Base player2Base;
	public int player1Score;
	public int player2Score;
	public Background boardBackground;
	public int turn;
	
	public void flip() {  // TODO This is a horrible way to handle this, need to change.
		gameCanvas.rotate(90.f, gameCanvas.getWidth()/2.f, gameCanvas.getHeight()/2.f);
	}

	public void createBoard(int width, int height) {
		gameMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		gameCanvas = new Canvas(gameMap);
		boardObjects = new LinkedList<BoardObject>();
		boardBackground = new Background(gameCanvas);
		if (width > height) landscape = true;
		else landscape = false;
		
		if (landscape) {
			player1Base = new Base(width, height/2);
			player2Base = new Base(0, height/2);
		}
	}
	
	public void createBoard(int width, int height, int numPlanets, int numStars, int numBlackHoles) {
		createBoard(width, height);
		
		// Add the requested amount of content:
		for (int i = 0; i < numPlanets; i++)
		{
			boardObjects.add(PlanetFactory.getRandomPlanet());
		}
		for (int i = 0; i < numStars; i++)
		{
			boardObjects.add((BoardObject)StarFactory.getRandomStar());
		}
		for (int i = 0; i < numBlackHoles; i++)
		{
			boardObjects.add((BoardObject)BlackHoleFactory.getRandomBlackHole());
		}
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

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (canvas != null)
		{
			boardBackground.draw(canvas);
		}
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
	
	
}
