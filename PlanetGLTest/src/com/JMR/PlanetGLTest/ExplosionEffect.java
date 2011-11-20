package com.JMR.PlanetGLTest;

import java.util.Random;

public class ExplosionEffect extends ParticleEffect {

	private static final float BASE_SIZE = 4.f;
	private static final float SIZE_JITTER = 1.f;
	
	public float[] velocities;
	public float[] center;
	public float radius;
	
	public ExplosionEffect(long meanLife, long lifeJitter,
			ParticleSystem particleSystem, float[] center, float radius) {
		super(meanLife, lifeJitter, particleSystem);
		
		this.velocities = new float[particleSystem.numParticles * 3];
	}

	@Override
	public void start() {
		super.start();
		
		Random r = new Random();
				
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
			this.particleSystem.pos[i*3+2] = center[2];	// Z
			
			// Instead of actual velocities, we'll just store that random part for each component.
			velocities[i*3+0] = r.nextFloat()*2.f - 1.f;	// X
			velocities[i*3+0] *= this.radius/((float)this.meanLife);
			velocities[i*3+1] = r.nextFloat()*2.f - 1.f;	// Y
			velocities[i*3+1] *= this.radius/((float)this.meanLife);
			velocities[i*3+2] = r.nextFloat()*2.f - 1.f;	// Z
			velocities[i*3+2] *= this.radius/((float)this.meanLife);
		}
		
	}
	
	@Override
	public void update() {
		if (!this.isRunning) return;
		if (this.numLive == 0)
		{
			this.isRunning = false;
			return;
		}
		
		long currTime = System.currentTimeMillis();
		
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
			this.particleSystem.pos[i*3+0] += this.velocities[i*3+0];	// X
			this.particleSystem.pos[i*3+1] += this.velocities[i*3+1];	// Y
			this.particleSystem.pos[i*3+2] += this.velocities[i*3+2];	// Z
		}
		
		this.prevTime = currTime;
	}

}
