package com.JMR.PlanetTest;

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
	public static final double MEAN_PLANET_SIZE = 0.03; // Expressed as percent of canvas size
	public static final double PLANET_SIZE_JITTER = 0.01;
		
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
		
		private Background theBackground;
		private Planet somePlanets[];
		private int numPlanets;
		private GameState state;
		
		public TestThread(SurfaceHolder holder, Context context, Handler handler){
			int rectCX,rectCY;
			int size;
			
			mHandler = handler;
			mHolder = holder;
			mContext = context;
			numPlanets = (int)(Math.random()*20);
			
			state = GameState.getInstance();
			theBackground = new Background(state.gameCanvas);
			
			// Create some planets:
			somePlanets = new Planet[numPlanets];
			for (int i = 0; i < numPlanets; i++)
			{
				somePlanets[i] = new CirclePlanet();
				
				// Make some random center:
				rectCX = (int) (Math.random()*state.gameMap.getWidth());
				rectCY = (int) (Math.random()*state.gameMap.getHeight());
				
				// Make some random size:
				size = (int) ((Math.random()*PLANET_SIZE_JITTER-(PLANET_SIZE_JITTER/2.0)+MEAN_PLANET_SIZE)*
						state.gameMap.getWidth());
				
				somePlanets[i].setBounds(new Rect((int)(rectCX - (size/2.0)), (int)(rectCY - (size/2.0)),
										(int)(rectCX + (size/2.0)), (int)(rectCY + (size/2.0))));
			}
		}
		
		@Override
		public void run() {
			while (running) { // <<-- MAIN LOOP
				Canvas c = null;
				try {
					c = mHolder.lockCanvas(null);
					synchronized (mHolder) {
						// doLogic()
						// doPhysics()
						doDraw(c);
					}
				} finally {
					if (c != null) {
						mHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}

		private void doDraw(Canvas c) {
			if (myCanvas == null) myCanvas = c;
			if (c != null) {
				// Draw background to game canvas:
				if (theBackground == null){
					Log.d("TestView.onDraw", "BACKGROUND OBJECT IS NULL!");
				}
				else if (state.gameCanvas == null){
					Log.d("TestView.onDraw", "GAME CANVAS OBJECT IS NULL!");
				}
				theBackground.draw(state.gameCanvas);
				
				// Draw the planets to the game canvas:
				for (int i = 0; i < numPlanets; i++)
				{
					somePlanets[i].draw(state.gameCanvas);
				}
				
				// Draw the bitmap from the game canvas to the screen surface, with the transform matrix:
				c.drawBitmap(state.gameMap, new Matrix(), null);
				//c.drawBitmap(state.gameMap, state.gameCanvas.getMatrix(), null);
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
				doZoom(event.getX(0),startX1,event.getY(0),startY1,
						event.getX(1),startX2,event.getY(1),startY2);
				startX1 = event.getX(0);
				startY1 = event.getY(0);
				startX2 = event.getX(1);
				startY2 = event.getY(1);
			}
			
		}
		
		// We consumed the event:
		return true;
	}

	private void doZoom(float x1, float historicalX1, float y1, float historicalY1,
			float x2, float historicalX2, float y2, float historicalY2) {
		GameState state = GameState.getInstance();
		float dist1, dist2;
		
		dist1 = (float) Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
		dist2 = (float) Math.sqrt((historicalX2-historicalX1)*(historicalX2-historicalX1)+
				(historicalY2-historicalY1)*(historicalY2-historicalY1));
		
		float deltaDist = (float) (dist1-dist2);
		float ratio = deltaDist/dist1;
		state.gameCanvas.scale(1.0f-ratio, 1.0f-ratio, (x1+x2)/2, (y1+y2)/2);
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
			state.gameCanvas.translate(points[2], 0.0f);
		}
		
		if (points[1] > 0.0f)
		{
			state.gameCanvas.translate(0.0f, -points[1]);
		}
		else if (points[3] < myCanvas.getHeight())
		{
			state.gameCanvas.translate(0.0f, points[3]);
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
