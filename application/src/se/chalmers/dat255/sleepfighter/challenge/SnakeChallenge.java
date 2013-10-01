package se.chalmers.dat255.sleepfighter.challenge;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;

public class SnakeChallenge implements Challenge {

	private Model model;
	private ChallengeActivity activity;
	
	private float touchX;
	private float touchY;
	
	@Override
	public void start(ChallengeActivity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		model = new Model();
		this.activity = activity;
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
			
			thread.dirChanged();
			
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
		boolean updateDir = false;
		
		public void dirChanged() {
			updateDir = true;
		}
		
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
						if (model.lost) {
							model = new Model();
						}
						else if (model.won) {
							activity.complete();
						}
						
						if (updateDir) {
							updateDir = false;
							model.updateDirection(touchX/c.getWidth(), touchY/c.getHeight());
						}
						
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
		private final int maxSegments = 6;
		
		private final int obstacleWidth = 15;
		
		private List<Segment> snakeSegments;
		private List<RectEntity> obstacles;
		private List<SphereFruit> sphereFruits;
		
		private float startX;
		private float startY;
		
		private RectEntity exit;
		
		private boolean lost;
		private boolean won;
		
		private Paint snakePaint;
		private Paint obstaclePaint;
		private Paint sphereFruitPaint;
		private Paint exitPaint;
		
		public Model() {
			snakePaint = new Paint();
			snakePaint.setColor(Color.rgb(255, 255, 0));
			
			obstaclePaint = new Paint();
			obstaclePaint.setColor(Color.rgb(200, 200, 200));
			
			sphereFruitPaint = new Paint();
			sphereFruitPaint.setColor(Color.rgb(255, 40, 0));
			
			exitPaint = new Paint();
			exitPaint.setColor(Color.rgb(30, 255, 40));
			
			startX = boardWidth/2f;
			startY = 300;
			
			snakeSegments = new ArrayList<Segment>();
			snakeSegments.add(new Segment(startX, startY));
			
			obstacles = new ArrayList<RectEntity>();
			obstacles.add(new RectEntity(0, 0, boardWidth, obstacleWidth));
			obstacles.add(new RectEntity(0, 0, obstacleWidth, boardHeight));
			obstacles.add(new RectEntity(boardWidth - obstacleWidth, 0, obstacleWidth, boardHeight));
			obstacles.add(new RectEntity(0, boardHeight - obstacleWidth, boardWidth, obstacleWidth));
			
			sphereFruits = new ArrayList<SphereFruit>();
			sphereFruits.add(new SphereFruit(40, 40));
			
			lost = false;
			won = false;
		}
		
		private long lastUpdate = System.nanoTime();
		
		public void draw(Canvas c) {
			float scaleX = c.getWidth()*1f/boardWidth;
			float scaleY = c.getHeight()*1f/boardHeight;
			
			for (int i = 0; i < obstacles.size(); i++) {
				RectEntity o = obstacles.get(i);
				
				c.drawRect(
						o.getX() * scaleX,
						o.getY() * scaleY,
						o.getX() * scaleX + o.getWidth() * scaleX,
						o.getY() * scaleY + o.getHeight() * scaleY, obstaclePaint);
			}
			
			if (exit != null) {
				c.drawRect(
						exit.getX() * scaleX,
						exit.getY() * scaleY,
						exit.getX() * scaleX + exit.getWidth() * scaleX,
						exit.getY() * scaleY + exit.getHeight() * scaleY, exitPaint);
			}
			
			for (int i = 0; i < sphereFruits.size(); i++) {
				SphereFruit f = sphereFruits.get(i);
				
				c.drawOval(new RectF(
						f.getX() * scaleX - f.getWidth() * scaleX,
						f.getY() * scaleY - f.getHeight() * scaleY,
						f.getX() * scaleX + f.getWidth() * scaleX,
						f.getY() * scaleY + f.getHeight() * scaleY), sphereFruitPaint);
			}
			
			for (int i = 0; i < snakeSegments.size(); i++) {
				Segment s = snakeSegments.get(i);
				
				c.drawCircle(
						(s.getX()*scaleX),
						(s.getY()*scaleY),
						(s.getHeight()*scaleY), snakePaint);
				
				c.drawOval(new RectF(
						(s.getX() * scaleX - s.getWidth() * scaleX),
						(s.getY() * scaleY - s.getHeight() * scaleY),
						(s.getX() * scaleX + s.getWidth() * scaleX),
						(s.getY() * scaleY + s.getHeight() * scaleY)), snakePaint);
			}
			
			c.drawText("Fps: " + 1000/((System.nanoTime() - lastUpdate)/1000000L), 10, 10, snakePaint);
			c.drawText("Segments: " + snakeSegments.size(), 10, 25, snakePaint);
			c.drawText("Pos: " + (int)snakeSegments.get(0).x + ", " + (int)snakeSegments.get(0).y, 10, 40, snakePaint);
			c.drawText("LastSeg: " + (int)snakeSegments.get(snakeSegments.size()-1).x + ", " + (int)snakeSegments.get(snakeSegments.size()-1).y, 10, 55, snakePaint);
			c.drawText("Fruits: " + sphereFruits.size(), 10, 70, snakePaint);
			
			lastUpdate = System.nanoTime();
		}
		
		private float dx;
		private float dy;
		
		public void updateDirection(float touchX, float touchY) {
			float segX = snakeSegments.get(0).getX();
			float segY = snakeSegments.get(0).getY();
			
			float nextX = touchX * boardWidth;
			float nextY = touchY * boardHeight;
			
			float relativeX = nextX - segX;
			float relativeY = nextY - segY;
			
			float hypotenuse = (float) Math.sqrt(relativeX*relativeX + relativeY*relativeY);
			
			if (hypotenuse != 0) {
				dx = relativeX/hypotenuse;
				dy = relativeY/hypotenuse;
			}
		}
		
		public void update(float touchX, float touchY) {
				
				snakeSegments.get(0).setX(snakeSegments.get(0).getX() + dx);
				snakeSegments.get(0).setY(snakeSegments.get(0).getY() + dy);
				
				snakeSegments.get(0).getXList().add(snakeSegments.get(0).getX());
				snakeSegments.get(0).getYList().add(snakeSegments.get(0).getY());
				
				
				for (int i = 1; i < snakeSegments.size(); i++) {
					snakeSegments.get(i).setX(snakeSegments.get(0).getXList().get(snakeSegments.get(i).iter));
					snakeSegments.get(i).setY(snakeSegments.get(0).getYList().get(snakeSegments.get(i).iter));
						
					snakeSegments.get(i).iter++;
				}
				
				snakeSegments.get(0).iter++;
				
				if (snakeSegments.size() < maxSegments && snakeSegments.get(0).iter % (snakeSegments.get(0).getWidth()*2) == 0) {
					snakeSegments.add(new Segment(startX, startY));
				}
			
			
			checkCollision();
		}
		
		public void checkCollision() {
			Segment head = snakeSegments.get(0);
			
			if (snakeSegments.get(snakeSegments.size()-1).getY() <= 0) {
				won = true;
			}
			
			for (int i = 1; i < snakeSegments.size(); i++) {
				if (Math.abs((snakeSegments.get(i).getX() - snakeSegments.get(0).getX())) <= head.getWidth()/2
						&& Math.abs((snakeSegments.get(i).getY() - snakeSegments.get(0).getY())) <= head.getWidth()/2) {
					lost = true;
					break;
				}
			}
			
			
			if (exit != null
					&& 0 < head.getX() - head.getWidth() - exit.getX() 
					&& 0 < exit.getX() + exit.getWidth() - head.getWidth() - head.getX()
					&& 0 < head.getY() + head.getHeight() - exit.getY()
					&& 0 < exit.getY() + exit.getHeight() + head.getHeight() - head.getY()) {
				
			}
			else {
				for (int i = 0; i < obstacles.size(); i++) {
					RectEntity o = obstacles.get(i);
					if (0 < head.getX() + head.getWidth() - o.getX() 
							&& 0 < o.getX() + o.getWidth() + head.getWidth() - head.getX()
							&& 0 < head.getY() + head.getHeight() - o.getY()
							&& 0 < o.getY() + o.getHeight() + head.getHeight() - head.getY()) {
						lost = true;
						break;
					}
				}
			}
			
			for (int i = 0; i < sphereFruits.size(); i++) {
				if (Math.abs(sphereFruits.get(i).getX() - head.getX()) <= head.getWidth()
						&& Math.abs(sphereFruits.get(i).getY() - head.getY()) <= head.getWidth()) {
					sphereFruits.remove(i);
					break;
				}
			}
			
			if (sphereFruits.isEmpty() && exit == null) {
				exit = new RectEntity(boardWidth/2, 0, 50, 30);
			}
		}
	}

	private class SphereFruit { // Healthy!!
		private float x;
		private float y;
		
		private float width;
		
		public SphereFruit(float x, float y) {
			this.x = x;
			this.y = y;
			
			this.width = 10;
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
			return width;
		}
	}
	
	private class RectEntity {
		private float x;
		private float y;
		private float width;
		private float height;
		
		public RectEntity(float x, float y, float width, float height) {
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
		
		private float width;
		
		public Segment(float x, float y) {
			this.x = x;
			this.y = y;
			
			iter = 0;
			
			xList = new ArrayList<Float>();
			yList = new ArrayList<Float>();
			
			this.width = 15;
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
		
		public float getWidth() {
			return width;
		}
		
		public float getHeight() {
			return width;
		}
	}
}