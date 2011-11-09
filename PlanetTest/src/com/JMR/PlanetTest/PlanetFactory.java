package com.JMR.PlanetTest;

import java.util.Random;

import android.util.Log;

public class PlanetFactory {
	public static BoardObject getRandomPlanet() {
		Random r = new Random();
		
		
		// TODO stub
		CirclePlanet circ = new CirclePlanet();
		int rad = 50 + r.nextInt(100);
		int x = 20 + r.nextInt(1000);
		int y = 20 + r.nextInt(700);
		circ.setBounds(x, y, x + rad, y + rad);
		circ.radius = rad;
		circ.Create();
		return circ;
	}
}
