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
	
	private SurfaceHolder surfaceHolder;
	
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
				//c.drawBitmap(state.gameMap, new Matrix(), null);
				c.drawBitmap(state.gameMap, state.gameCanvas.getMatrix(), null);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// We only care if there is movement (for this test)
		Log.d("TestVeiw.onTouchEvent", new Integer(MotionEvent.ACTION_DOWN).toString());
		Log.d("TestView.onTouchEvent", "TOUCH EVENT");
		if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			Log.d("TestView.onTouchEvent", "MOVE EVENT");
			// If there is only one pointer, we're scrolling, otherwise zooming:
			if (event.getPointerCount() == 1)
			{
				doTranslate(event.getX(0),event.getHistoricalX(0,1),event.getY(0),event.getHistoricalY(0,1));
			}
			else
			{
				doZoom(event.getX(0),event.getHistoricalX(0,1),event.getY(0),event.getHistoricalY(0,1),
						event.getX(1),event.getHistoricalX(1,1),event.getY(1),event.getHistoricalY(1,1));
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
		
		dx = x - historicalX;
		dy = y - historicalY;
		
		state.gameCanvas.translate(dx, dy);
		
		// TODO IMPLEMENT PREVENTION OF SCROLLING OFF SCREEN!!!
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
