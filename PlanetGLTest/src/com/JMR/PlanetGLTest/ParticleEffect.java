package com.JMR.PlanetGLTest;

import android.util.Log;

public abstract class ParticleEffect {
	public long meanLife;
	public long lifeJitter;
	public ParticleSystem particleSystem;
	public long timeStarted;
	public long prevTime;
	public boolean isRunning;
	public long numLive;

	public float[] center;
	
	public ParticleEffect(long meanLife, long lifeJitter, ParticleSystem particleSystem) {
		this.meanLife = meanLife;
		this.lifeJitter = lifeJitter;
		this.particleSystem = particleSystem;
		this.isRunning = false;
		this.numLive = 0l;
		this.center = new float[2];
	}
	
	public void start() {
		this.timeStarted = System.currentTimeMillis();
		this.prevTime = this.timeStarted;
		this.isRunning = true;
		this.numLive = this.particleSystem.numParticles;
	}
	public void stop() {
		this.timeStarted = -1l;
		this.prevTime = -1l;
		this.numLive = 0;
		this.isRunning = false;
		
		// Make sure the particles don't render:
		for (int i = 0; i < this.particleSystem.numParticles; i++)
		{
			this.particleSystem.size[i] = 0.f;
		}
	}
	public void setCenter(float x, float y)
	{
		this.center[0] = -(x-((float)GameBoard.Instance.width)/2.f)/100.f;
		this.center[1] = -(y-((float)GameBoard.Instance.height)/2.f)/100.f;
		Log.d("ParticleEffect.setCenter$x", new Float(x).toString());
		Log.d("ParticleEffect.setCenter$center[0]", new Float(this.center[0]).toString());

		Log.d("ParticleEffect.setCenter$y", new Float(y).toString());
		Log.d("ParticleEffect.setCenter$center[1]", new Float(this.center[1]).toString());

	}
	public abstract void update();
}
