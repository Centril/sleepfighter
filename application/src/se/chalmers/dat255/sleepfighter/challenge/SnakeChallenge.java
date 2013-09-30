package se.chalmers.dat255.sleepfighter.challenge;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;

public class SnakeChallenge implements Challenge {

	private Model model;
	
	@Override
	public void start(ChallengeActivity activity) {
		model = new Model();
		activity.setContentView(new GameView(activity));
	}

	public class GameView extends SurfaceView implements Callback {
		// used to keep track of time between updates and amount of time to
		// sleep for
		long lastUpdate = 0;
		long sleepTime = 0;

		// objects which house info about the screen
		SurfaceHolder surfaceHolder;
		Context context;

		// our Thread class which houses the game loop
		private GameThread thread;

		// initialization code
		void InitView() {
			// initialize our screen holder
			SurfaceHolder holder = getHolder();
			holder.addCallback(this);

			// initialize our Thread class. A call will be made to start it
			// later
			thread = new GameThread(holder, context, new Handler());
			setFocusable(true);
		}

		// class constructors
		public GameView(Context context) {
			super(context);
			this.context = context;
			InitView();
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			boolean retry = true;
			// code to end gameloop
			thread.setRunning(false);
			while (retry) {
				try {
					// code to kill Thread
					thread.join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}

		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			if (thread.isRunning()) {
				// When game is opened again in the Android OS
				thread = new GameThread(getHolder(), context, new Handler());
				thread.start();
			} else {
				// creating the game Thread for the first time
				thread.start();
			}
		}
	}

	private class GameThread extends Thread {
		private SurfaceHolder mSurfaceHolder;
		private Paint mLinePaint;
		private Paint blackPaint;

		// for consistent rendering
		private long sleepTime;
		// amount of time to sleep for (in milliseconds)
		private long delay = 70;

		private boolean isRunning = true;

		public GameThread(SurfaceHolder surfaceHolder, Context context,
				Handler handler) {

			// data about the screen
			mSurfaceHolder = surfaceHolder;

			// standard game painter. Used to draw on the canvas
			mLinePaint = new Paint();
			mLinePaint.setARGB(255, 0, 255, 0);
			// black painter below to clear the screen before the game is
			// rendered
			blackPaint = new Paint();
			blackPaint.setARGB(255, 0, 0, 0);
			// mLinePaint.setAntiAlias(true);

		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}
		
		public boolean isRunning() {
			return isRunning;
		}
		
		@Override
		public void run() {
			while (isRunning) {
				long beforeTime = System.nanoTime();
				model.update();
				Canvas c = null;
				try {
					c = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) {
						c.drawRect(0, 0, c.getWidth(), c.getHeight(),
								blackPaint);
						model.draw(c);
					}
				} finally {
					if (c != null) {
						mSurfaceHolder.unlockCanvasAndPost(c);
					}
				}

				this.sleepTime = delay - ((System.nanoTime() - beforeTime) / 1000000L);

				try {
					if (sleepTime > 0) {
						this.sleep(sleepTime);
					}
				} catch (InterruptedException ex) {
					Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}
	
	private class Model {
		private int x;
		private int y;
		
		private Paint snakePaint = new Paint();
		
		public Model() {
			x = 20;
			y = 200;
			
			snakePaint.setColor(Color.rgb(255, 255, 0));
		}
		
		public void draw(Canvas c) {
			c.drawCircle(x, y, 50, snakePaint);
		}
		
		public void update() {
			x += 20;
		}
	}
}
