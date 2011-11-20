package com.JMR.PlanetGLTest;

import java.util.Random;

import android.util.Log;

public class ExplosionEffect extends ParticleEffect {

	private static final float BASE_SIZE = 10.f;
	private static final float SIZE_JITTER = 5.f;
	
	public float[] velocities;
	public float radius;
	
	public ExplosionEffect(long meanLife, long lifeJitter,
			ParticleSystem particleSystem, float[] center, float radius) {
		super(meanLife, lifeJitter, particleSystem);
		
		this.radius = radius;
		this.velocities = new float[particleSystem.numParticles * 3];
	}

	@Override
	public void start() {
		super.start();
		Log.d("ExplosionEffect.start","Start Called");
		Random r = new Random();
		float velocity;
		float theta, phi;
		
		// Create a bunch of particles:
		for (int i = 0; i < this.particleSystem.numParticles; i++)
		{
			/*
			 * Average velocity should be that which transports a particle with meanLife
			 * to radius exactly when it dies. Min velocity should be ~0 so we get a puf, and
			 * not a shell. (Shockwave will be a different effect). 
			 * 
			 * v(t) = c
			 * x(t) = ct	{roll the '-' into the constant}
			 * x(0) = 0		{In effect space, obviously it will be displaced by center}
			 * x(meanLife) = radius
			 * c*meanLife = radius
			 * c = radius/meanLife ( * rand{-1., 1.})
			 * x(t) = radius*t/meanLife
			 */
			this.particleSystem.age[i] = 0;
			this.particleSystem.life[i] = (long) (r.nextFloat() * (2.f*this.lifeJitter) + this.meanLife - this.lifeJitter);
			
			this.particleSystem.color[i*4+0] = 1.f; 	// R
			this.particleSystem.color[i*4+1] = 1.f; 	// G
			this.particleSystem.color[i*4+2] = 1.f; 	// B
			this.particleSystem.color[i*4+3] = 1.f; 	// A
			
			this.particleSystem.size[i] = r.nextFloat() * (2.f*SIZE_JITTER) + BASE_SIZE - SIZE_JITTER;
			
			this.particleSystem.pos[i*3+0] = center[0];	// X
			this.particleSystem.pos[i*3+1] = center[1];	// Y
			this.particleSystem.pos[i*3+2] = 0.f;		// Z
			
			// Instead of actual velocities, we'll just store that random part for each component.
//			velocities[i*3+0] = r.nextFloat()*2.f - 1.f;	// X
//			velocities[i*3+0] *= this.radius/((float)this.meanLife)*30.f;
//			velocities[i*3+1] = r.nextFloat()*2.f - 1.f;	// Y
//			velocities[i*3+1] *= this.radius/((float)this.meanLife)*30.f;
//			velocities[i*3+2] = r.nextFloat()*2.f - 1.f;	// Z
//			velocities[i*3+2] *= this.radius/((float)this.meanLife)*30.f;
			
			velocity = r.nextFloat();
			velocity *= this.radius/((float)this.meanLife)*30.f;
			
			theta = r.nextFloat() * 2.f * 3.1415f;
			phi = r.nextFloat() * 3.1415f;
			
			velocities[i*3+0] = (float) (velocity * Math.sin(theta) * Math.cos(phi));
			velocities[i*3+1] = (float) (velocity * Math.sin(theta) * Math.sin(phi));
			velocities[i*3+1] = (float) (velocity * Math.cos(theta));
		}
		
	}
	
	@Override
	public void update() {
//		Log.d("ExplosionEffect.update","Update Called");
		if (!this.isRunning) return;
		if (this.numLive == 0)
		{
			this.isRunning = false;
			return;
		}
		
		long currTime = System.currentTimeMillis();
		float deltaT = (currTime-prevTime)/1000.f;
		
		for (int i = 0; i < this.particleSystem.numParticles; i++)
		{
			// Skip dead particles:
			if (this.particleSystem.size[i] == 0.f) continue;
			
			// Age particles:
			this.particleSystem.age[i] += currTime-prevTime;
			
			// Kill geriatric particles:
			if (this.particleSystem.age[i] >= this.particleSystem.life[i])
			{
				this.particleSystem.size[i] = 0.f;
				this.numLive--;
				continue;
			}
			
			// Update position if the particles make it this far:
			this.particleSystem.pos[i*3+0] += this.velocities[i*3+0]*deltaT;	// X
//			Log.d("ExplosionEffect.update$velocity.x", new Float(this.velocities[i*3+0]).toString());
			this.particleSystem.pos[i*3+1] += this.velocities[i*3+1]*deltaT;	// Y
			this.particleSystem.pos[i*3+2] += this.velocities[i*3+2]*deltaT;	// Z
		}
		
		this.prevTime = currTime;
	}

}
