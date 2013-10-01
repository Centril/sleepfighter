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
					synchronized (holder) {
						model.update(touchX/c.getWidth(), touchY/c.getHeight());
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
		private final int maxSegments = 6;
		
		private final int obstacleWidth = 15;
		
		private List<Segment> snakeSegments;
		private List<Obstacle> obstacles;
		
		
		private float startX;
		private float startY;
		
		private Paint snakePaint;
		private Paint obstaclePaint;
		
		public Model() {
			snakePaint = new Paint();
			snakePaint.setColor(Color.rgb(255, 255, 0));
			
			obstaclePaint = new Paint();
			obstaclePaint.setColor(Color.rgb(200, 200, 200));
			
			startX = 0;
			startY = 60;
			
			snakeSegments = new ArrayList<Segment>();
			snakeSegments.add(new Segment(startX, startY));
			
			obstacles = new ArrayList<Obstacle>();
			obstacles.add(new Obstacle(0, 0, boardWidth, obstacleWidth));
			obstacles.add(new Obstacle(0, 0, obstacleWidth, boardHeight));
			obstacles.add(new Obstacle(boardWidth - obstacleWidth, 0, obstacleWidth, boardHeight));
			obstacles.add(new Obstacle(0, boardHeight - obstacleWidth, boardWidth, obstacleWidth));
		}
		
		private long lastUpdate = System.nanoTime();
		
		public void draw(Canvas c) {
			float scaleX = c.getWidth()*1f/boardWidth;
			float scaleY = c.getHeight()*1f/boardHeight;
			
			for (int i = 0; i < obstacles.size(); i++) {
				c.drawRect(
						obstacles.get(i).x * scaleX,
						obstacles.get(i).y * scaleY,
						obstacles.get(i).x * scaleX + obstacles.get(i).width * scaleX,
						obstacles.get(i).y * scaleY + obstacles.get(i).height * scaleY, obstaclePaint);
			}
			
			for (int i = 0; i < snakeSegments.size(); i++) {
				c.drawCircle(
						(snakeSegments.get(i).getX()*scaleX),
						(snakeSegments.get(i).getY()*scaleY),
						(snakeWidth*scaleY), snakePaint);
				
				c.drawOval(new RectF(
						(snakeSegments.get(i).getX()*scaleX - snakeWidth*scaleX),
						(snakeSegments.get(i).getY()*scaleY - snakeWidth*scaleY),
						(snakeSegments.get(i).getX()*scaleX + snakeWidth*scaleX),
						(snakeSegments.get(i).getY()*scaleY + snakeWidth*scaleY)), snakePaint);
			}
			
			c.drawText("Fps: " + 1000/((System.nanoTime() - lastUpdate)/1000000L), 10, 10, snakePaint);
			c.drawText("Segments: " + snakeSegments.size(), 10, 25, snakePaint);
			c.drawText("Pos: " + (int)snakeSegments.get(0).x + ", " + (int)snakeSegments.get(0).y, 10, 40, snakePaint);
			c.drawText("LastSeg: " + (int)snakeSegments.get(snakeSegments.size()-1).x + ", " + (int)snakeSegments.get(snakeSegments.size()-1).y, 10, 55, snakePaint);
			
			lastUpdate = System.nanoTime();
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
				snakeSegments.get(0).getXList().add(segX);
				snakeSegments.get(0).getYList().add(segY);
				
				snakeSegments.get(0).setX(snakeSegments.get(0).getX() + relativeX/hypotenuse);
				snakeSegments.get(0).setY(snakeSegments.get(0).getY() + relativeY/hypotenuse);
				
				for (int i = 1; i < snakeSegments.size(); i++) {
					snakeSegments.get(i).setX(snakeSegments.get(0).getXList().get(snakeSegments.get(i).iter));
					snakeSegments.get(i).setY(snakeSegments.get(0).getYList().get(snakeSegments.get(i).iter));
						
					snakeSegments.get(i).iter++;
				}
				
				snakeSegments.get(0).iter++;
				
				if (snakeSegments.size() < maxSegments && snakeSegments.get(0).iter % (snakeWidth*2) == 0) {
					snakeSegments.add(new Segment(startX, startY));
				}
			}
			
			checkCollision();
		}
		
		public void checkCollision() {
			Segment head = snakeSegments.get(0);
			
			for (int i = 1; i < snakeSegments.size(); i++) {
				if (Math.abs((snakeSegments.get(i).getX() - snakeSegments.get(0).getX())) <= 5
						&& Math.abs((snakeSegments.get(i).getY() - snakeSegments.get(0).getY())) <= 5) {
					Debug.d("COLLISION");
				}
			}
			
			for (int i = 0; i < obstacles.size(); i++) {
				Obstacle o = obstacles.get(i);
				if (snakeWidth > o.getX() - head.getX()
						&& snakeWidth > o.getX() - o.getWidth() + head.getX()
						&& snakeWidth > o.getY() - head.getY()
						&& snakeWidth > o.getY() - o.getHeight() + head.getY()) {
					Debug.d("OBSTACLE COLLISION");
				}
			}
		}
	}
	
	private class Obstacle {
		private float x;
		private float y;
		private float width;
		private float height;
		
		public Obstacle(float x, float y, float width, float height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public float getX() {
			return x;
		}
		
		public float getY() {
			return y;
		}
		
		public float getWidth() {
			return width;
		}
		
		public float getHeight() {
			return height;
		}
	}
	
	private class Segment {
		private float x;
		private float y;
		
		private final List<Float> xList;
		private final List<Float> yList;
		
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

		public List<Float> getYList() {
			return yList;
		}
	}
}
