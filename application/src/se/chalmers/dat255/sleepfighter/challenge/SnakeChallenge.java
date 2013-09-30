package se.chalmers.dat255.sleepfighter.challenge;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
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
		private final int snakeWidth = 10;
		
		private List<Segment> snakeSegments;
		
		private float lastX;
		private float lastY;
		
		private Paint snakePaint;
		
		public Model() {
			snakePaint = new Paint();
			snakePaint.setColor(Color.rgb(255, 255, 0));
			
			lastX = 0;
			lastY = 0;
			
			snakeSegments = new ArrayList<Segment>();
			snakeSegments.add(new Segment(lastX, lastY));
		}
		
		public void draw(Canvas c) {
			for (int i = 0; i < snakeSegments.size(); i++) {
				c.drawCircle((snakeSegments.get(i).getX()*c.getWidth()/boardWidth),
						(snakeSegments.get(i).getY()*c.getHeight()/boardHeight),
						(snakeWidth*c.getHeight()/boardHeight), snakePaint);
				
				c.drawOval(new RectF((snakeSegments.get(i).getX()*c.getWidth()/boardWidth - snakeWidth*c.getWidth()/boardWidth),
						(snakeSegments.get(i).getY()*c.getHeight()/boardHeight - snakeWidth*c.getHeight()/boardHeight),
						(snakeSegments.get(i).getX()*c.getWidth()/boardWidth + snakeWidth*c.getWidth()/boardWidth),
						(snakeSegments.get(i).getY()*c.getHeight()/boardHeight + snakeWidth*c.getHeight()/boardHeight)), snakePaint);
			}
			
			c.drawText("Segments: " + snakeSegments.size(), 10, 10, snakePaint);
			c.drawText("Pos: " + (int)snakeSegments.get(0).x + ", " + (int)snakeSegments.get(0).y, 10, 25, snakePaint);
			c.drawText("LastSeg: " + snakeSegments.get(snakeSegments.size()-1).x + ", " + snakeSegments.get(snakeSegments.size()-1).y, 10, 40, snakePaint);
		}
		
		public void update(float touchX, float touchY) {

			float segX = snakeSegments.get(0).getX();
			float segY = snakeSegments.get(0).getY();
			
			float nextX = touchX * boardWidth;
			float nextY = touchY * boardHeight;
			
			float relativeX = nextX - segX;
			float relativeY = nextY - segY;
			
			float hypotenuse = (float) Math.sqrt(relativeX*relativeX + relativeY*relativeY);
			
			if (hypotenuse != 0) {
				snakeSegments.get(0).xList.add(segX);
				snakeSegments.get(0).yList.add(segY);
				
				snakeSegments.get(0).setX(snakeSegments.get(0).getX() + relativeX/hypotenuse);
				snakeSegments.get(0).setY(snakeSegments.get(0).getY() + relativeY/hypotenuse);
				
				for (int i = 1; i < snakeSegments.size(); i++) {
					snakeSegments.get(i).setX(snakeSegments.get(0).xList.get(snakeSegments.get(i).iter));
					snakeSegments.get(i).setY(snakeSegments.get(0).yList.get(snakeSegments.get(i).iter));
						
					snakeSegments.get(i).iter++;
				}
				
				snakeSegments.get(0).iter++;
				
				if (snakeSegments.size() <= 5 && snakeSegments.get(0).iter % (snakeWidth*2) == 0) {
					snakeSegments.add(new Segment(lastX, lastY));
				}
			}
		}
	}
	
	private class Segment {
		float x;
		float y;
		
		List<Float> xList;
		List<Float> yList;
		
		private int iter;
		
		public Segment(float x, float y) {
			this.x = x;
			this.y = y;
			
			iter = 0;
			
			xList = new ArrayList<Float>();
			yList = new ArrayList<Float>();
		}
		
		public float getX() {
			return x;
		}
		
		public void setX(float x) {
			this.x = x;
		}
		
		public float getY() {
			return y;
		}
		
		public void setY(float y) {
			this.y = y;
		}
		
		public List<Float> getXList() {
			return xList;
		}
		
		public void setDx(List<Float> xList) {
			this.xList = xList;
		}
		
		public List<Float> getYList() {
			return yList;
		}
		
		public void setDy(List<Float> yList) {
			this.yList = yList;
		}
	}
}
