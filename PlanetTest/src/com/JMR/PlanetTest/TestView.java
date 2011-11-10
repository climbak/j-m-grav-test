package com.JMR.PlanetTest;

import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TestView extends SurfaceView implements SurfaceHolder.Callback {
		
	private Thread mThread;

	private float sensitivity = 0.75f;
	
	private SurfaceHolder surfaceHolder;
	private float startX1;
	private float startY1;
	private float startX2;
	private float startY2;
	public Canvas myCanvas;
	
	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
				
		// Do surface bureaucracy: 
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
				
		mThread = new TestThread(surfaceHolder, context, new Handler());
		
		setFocusable(true);
	}
	
	// Here's the thread that does the stuff:
	public class TestThread extends Thread {
		private SurfaceHolder mHolder;
		private Context mContext;
		private Handler mHandler;
		public boolean running = true;
		public long prevTime;
		
		private GameState theGame;
		
		public TestThread(SurfaceHolder holder, Context context, Handler handler){
			mHandler = handler;
			mHolder = holder;
			mContext = context;
			
			theGame = GameState.getInstance();
			
		}
		
		@Override
		public void run() {
			long t;
			while (running) { // <<-- MAIN LOOP
				Canvas c = null;
				if (theGame.projectile == null && theGame.gameCanvas != null)
				{
					Log.d("TestView.run","SPAWNING PROJECTILE");

					theGame.projectile = new Projectile((float)(Math.random()*theGame.gameCanvas.getWidth()),
							(float)(Math.random()*theGame.gameCanvas.getHeight()),
							(float)(Math.random()*5),
							(float)(Math.random()*5));
				
				}
				try {
					c = mHolder.lockCanvas(null);
					synchronized (mHolder) {
						// doLogic()
						doPhysics();
						doDraw(c);
					}
				} finally {
					if (c != null) {
						mHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
		
		private void doPhysics() {
			// Setup timestep:
			long currTime = System.currentTimeMillis();
			long deltaT = currTime - prevTime;
			double workingT = ((double)deltaT) / 1000.;
			
			// Is there anything to simulate?:
			if (theGame.projectile != null)
			{	
				// Create some stuff to do the sim:
				double ax, ay; // Accumulated accelerations
				double vecX, vecY;
				double dist;
				Projectile theProjectile;
				Iterator gravityObjects;
				BoardObject currObject;
				
				// Do simulation:
				ax = ay = 0;
				theProjectile = theGame.projectile;
				
				gravityObjects = theGame.boardObjects.iterator();
				
				// Accumulate all accelerations:
				while (gravityObjects.hasNext())
				{
					currObject = (BoardObject)gravityObjects.next();
					
					// Get a vector from the projectile to the current object:
					vecX = -(currObject.getBounds().exactCenterX() - theProjectile.x);
					vecY = -(currObject.getBounds().exactCenterY() - theProjectile.y);
					
					// Calculate the magnitude of the vector:
					dist = Math.sqrt(vecX*vecX + vecY*vecY);
					
					// Normalize the vector:
					vecX /= dist;
					vecY /= dist;
					
					// Divide twice more, because the acceleration should drop by the square of the distance:
					vecX /= dist / dist;
					vecY /= dist / dist;
					
					// Now multiply by the objects intrinsic acceleration:
					ax += vecX * currObject.g;
					ay += vecY * currObject.g;
				}
			
				// Now update the projectile's position:
				theProjectile.x += theProjectile.vx*workingT + 0.5*ax*workingT*workingT;
				theProjectile.y += theProjectile.vy*workingT + 0.5*ay*workingT*workingT;
				
				// Do some collision stuff:
				if (theProjectile.x < 0 || theProjectile.y < 0 ||
						theProjectile.x > theGame.gameCanvas.getWidth() ||
						theProjectile.y > theGame.gameCanvas.getHeight())
					theGame.projectile = null;
				
				gravityObjects = theGame.boardObjects.iterator();
				
				// Accumulate all accelerations:
				while (gravityObjects.hasNext())
				{
					if (((BoardObject)gravityObjects.next()).handleImpactXY(theProjectile.x,theProjectile.y))
						theGame.projectile = null;
				}
			}
			// Flip current time into prevTime:
			prevTime = currTime;
		}

		private void doDraw(Canvas c) {
			// Get a copy of the canvas, for screen dimension stuff:
			if (myCanvas == null) myCanvas = c;
						
			// Make sure not to try drawing to a canvas should we be called before we get one:
			if (c != null) {
				if (theGame.gameCanvas == null) theGame.createBoard(c.getWidth(), c.getHeight(), 2, 2, 1);
				theGame.draw(c);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// We only care if there is movement (for this test)
		Log.d("TestView.onTouchEvent", "TOUCH EVENT");
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			
			startX1 = event.getX(0);
			startY1 = event.getY(0);
			if (event.getPointerCount() > 1)
			{
				startX2 = event.getX(1);
				startY2 = event.getY(1);
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			Log.d("TestView.onTouchEvent", "MOVE EVENT");
			// If there is only one pointer, we're scrolling, otherwise zooming:
			if (event.getPointerCount() == 1)
			{
				doTranslate(event.getX(0),startX1,event.getY(0),startY1);
				startX1 = event.getX(0);
				startY1 = event.getY(0);				
			}
			else
			{
				doZoom(event.getX(0),event.getY(0),
						event.getX(1),event.getY(1));
				startX1 = event.getX(0);
				startY1 = event.getY(0);
				startX2 = event.getX(1);
				startY2 = event.getY(1);
			}
			
		}
		
		// We consumed the event:
		return true;
	}

	private void doZoom(float x1, float y1,
			float x2, float y2) {
		GameState state = GameState.getInstance();
		float dist1, dist2;
		
		dist1 = (float) Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
		dist2 = (float) Math.sqrt((startX2-startX1)*(startX2-startX1)+
				(startY2-startY1)*(startY2-startY1));
		
		float deltaDist = (float) (dist2-dist1);
		float ratio = deltaDist/dist1;
		
		// Get the apparent width and height of the canvas:
		float workingHeight = state.gameCanvas.getMatrix().mapRadius(state.gameCanvas.getHeight());
		float workingWidth = state.gameCanvas.getMatrix().mapRadius(state.gameCanvas.getWidth());
		
		// See if the scale will make us smaller than the screen:
		if ((1.0f-ratio) * workingHeight <= myCanvas.getHeight())
		{
			state.gameCanvas.setMatrix(new Matrix());
			return;
		}
		if ((1.0f-ratio) * workingWidth <= myCanvas.getWidth())
		{
			state.gameCanvas.setMatrix(new Matrix());
			return;
		}
		
		// Do the scale
		state.gameCanvas.scale(1.0f-ratio, 1.0f-ratio, (x1+x2)/2, (y1+y2)/2);
		
		// Check to see if we're off the side:
		Matrix currMat = state.gameCanvas.getMatrix();
		
		float[] points = new float[4];
		points[0] = 0.0f;	// Top left
		points[1] = 0.0f;
	 
		points[2] = state.gameCanvas.getWidth(); // Bottom right
		points[3] = state.gameCanvas.getHeight();
		
		currMat.mapPoints(points);
		
		// Check to see if we've scrolled our bitmap outside the screen rect:
		if (points[0] > 0.0f)
		{
			state.gameCanvas.translate(-points[0], 0.0f);
		}
		else if (points[2] < myCanvas.getWidth())
		{
			state.gameCanvas.translate(myCanvas.getWidth()-points[2], 0.0f);
		}
		
		if (points[1] > 0.0f)
		{
			state.gameCanvas.translate(0.0f, -points[1]);
		}
		else if (points[3] < myCanvas.getHeight())
		{
			state.gameCanvas.translate(0.0f, myCanvas.getHeight()-points[3]);
		}
	}

	private void doTranslate(float x, float historicalX, float y,
			float historicalY) {
		GameState state = GameState.getInstance();
		float dx, dy;
		
		dx = x - startX1;
		dy = y - startY1;
		
		state.gameCanvas.translate(dx*sensitivity, dy*sensitivity);
		
		Matrix currMat = state.gameCanvas.getMatrix();
		
		float[] points = new float[4];
		points[0] = 0.0f;	// Top left
		points[1] = 0.0f;
	 
		points[2] = state.gameCanvas.getWidth(); // Bottom right
		points[3] = state.gameCanvas.getHeight();
		
		currMat.mapPoints(points);
		
		// Check to see if we've scrolled our bitmap outside the screen rect:
		if (points[0] > 0.0f)
		{
			state.gameCanvas.translate(-points[0], 0.0f);
		}
		else if (points[2] < myCanvas.getWidth())
		{
			state.gameCanvas.translate(myCanvas.getWidth()-points[2], 0.0f);
		}
		
		if (points[1] > 0.0f)
		{
			state.gameCanvas.translate(0.0f, -points[1]);
		}
		else if (points[3] < myCanvas.getHeight())
		{
			state.gameCanvas.translate(0.0f, myCanvas.getHeight()-points[3]);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public Thread getThread() {
		return mThread;
	}

}
