package com.JMR.PlanetGLTest;

public abstract class ParticleEffect {
	public long meanLife;
	public long lifeJitter;
	public String particleTexture;
	public long timeStarted;
	public long prevTime;
	public boolean isRunning;
	
	public ParticleEffect(long meanLife, long lifeJitter, String particleTexture) {
		this.meanLife = meanLife;
		this.lifeJitter = lifeJitter;
		this.particleTexture = particleTexture;
		this.isRunning = false;
	}
	
	public void start() {
		this.timeStarted = System.currentTimeMillis();
		this.prevTime = this.timeStarted;
		this.isRunning = true;
	}
	public void stop() {
		this.timeStarted = -1l;
		this.prevTime = -1l;
		this.isRunning = false;
	}
	public abstract void update();
}
