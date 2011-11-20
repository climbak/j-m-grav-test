package com.JMR.PlanetGLTest;

public abstract class ParticleEffect {
	public long meanLife;
	public long lifeJitter;
	public ParticleSystem particleSystem;
	public long timeStarted;
	public long prevTime;
	public boolean isRunning;
	public long numLive;
	
	public ParticleEffect(long meanLife, long lifeJitter, ParticleSystem particleSystem) {
		this.meanLife = meanLife;
		this.lifeJitter = lifeJitter;
		this.particleSystem = particleSystem;
		this.isRunning = false;
		this.numLive = 0l;
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
