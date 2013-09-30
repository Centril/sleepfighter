package se.chalmers.dat255.sleepfighter.challenge;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;

public class SnakeChallenge implements Challenge {

	private Model model;
	
	private float touchX;
	private float touchY;
	
	private int screenWidth;
	private int screenHeight;
	
	@Override
	public void start(ChallengeActivity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Display display = activity.getWindowManager().getDefaultDisplay(); 
		screenWidth = display.getWidth();  // deprecated
		screenHeight = display.getHeight();  // deprecated
		
		model = new Model();
		activity.setContentView(new GameView(activity));
	}

	private class GameView extends SurfaceView implements Callback {
		Context context;

		private GameThread thread;

		void initView() {
			SurfaceHolder holder = getHolder();
			holder.addCallback(this);
			thread = new GameThread(holder, context, new Handler());
			setFocusable(true);
		}

		// class constructors
		public GameView(Context context) {
			super(context);
			this.context = context;
			initView();
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			boolean retry = true;
			
			thread.setRunning(false);
			while (retry) {
				try {
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
		
		@Override
		public boolean onTouchEvent(MotionEvent e) {
			touchX = e.getX();
			touchY = e.getY();
			
			return true;
		}
	}

	private class GameThread extends Thread {
		private SurfaceHolder holder;
		private Paint blackPaint;

		// for consistent rendering
		private long sleepTime;
		// amount of time to sleep for (in milliseconds)
		private long delay = 1000/60;

		private boolean isRunning = true;

		public GameThread(SurfaceHolder surfaceHolder, Context context,
				Handler handler) {

			// data about the screen
			holder = surfaceHolder;

			blackPaint = new Paint();
			blackPaint.setARGB(255, 0, 0, 0);
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
				Canvas c = null;
				try {
					c = holder.lockCanvas(null);
					model.update(touchX/c.getWidth(), touchY/c.getHeight());
					synchronized (holder) {
						c.drawRect(0, 0, c.getWidth(), c.getHeight(), blackPaint);
						model.draw(c);
					}
				} finally {
					if (c != null) {
						holder.unlockCanvasAndPost(c);
					}
				}

				this.sleepTime = delay - ((System.nanoTime() - beforeTime) / 1000000L);

				try {
					if (sleepTime > 0) {
						this.sleep(sleepTime);
					}
				} catch (InterruptedException ex) {
					Debug.d("GameThread (SnakeChallenge) sleep interrupted!");
				}
			}
		}
	}
	
	private class Model {
		private final int boardWidth = 200;
		private final int boardHeight = 400;
		
		private float x;
		private float y;
		
		private Paint snakePaint;
		
		public Model() {
			x = 0;
			y = 10;
			
			snakePaint = new Paint();
			snakePaint.setColor(Color.rgb(255, 255, 0));
		}
		
		public void draw(Canvas c) {
			c.drawCircle((c.getWidth()*x/boardWidth), (c.getHeight()*y/boardHeight), 50, snakePaint);
		}
		
		public void update(float touchX, float touchY) {
			float derpX = touchX * boardWidth;
			float derpY = touchY * boardHeight;
			
			float relativeX = derpX - x; 
			float relativeY = derpY - y;
			
			float hypotenusa = (float) Math.sqrt(relativeX*relativeX + relativeY*relativeY);
			
			if (hypotenusa != 0) {
				x += relativeX/hypotenusa;
				y += relativeY/hypotenusa;
			}
		}
	}
}
