package se.chalmers.dat255.sleepfighter.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;

public class SnakeChallenge implements Challenge, OnTouchListener {

	private Model model;
	private Thread thread;
	private GameView view;
	private ChallengeActivity activity;
	
	private float touchX;
	private float touchY;
	private boolean updateDir;
	
	public final int targetFPS = 60;
	
	@Override
	public void start(ChallengeActivity activity) {
		this.activity = activity;
		model = new Model();
		view = new GameView(activity, model);
		
		view.setOnTouchListener(this);
		activity.setContentView(view);
		
		thread = new Thread() {
			@Override
			public void run() {
				long lastTime = 0;
				
				while (!model.won()) {
					if (model.lost()) {
						restart();
					}
					
					long now = System.currentTimeMillis();
					float delta = (lastTime > 0 ? now - lastTime : 1000/targetFPS)/(1000f/targetFPS);
					lastTime = now;
					
					if (view.isSurfaceCreated()) {
						Canvas c = null;

						try {
							c = view.getHolder().lockCanvas();
							
							if (updateDir) {
								model.updateDirection(touchX/c.getWidth(), touchY/c.getHeight());
								updateDir = false;
							}
							model.update(delta);
							
							synchronized(view.getHolder()) {
								
								view.render(c);
							}
						}
						finally {
							if (c != null) {
								view.getHolder().unlockCanvasAndPost(c);
							}
						}
					}
					try {
						Thread.sleep(1000/targetFPS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				SnakeChallenge.this.activity.complete();
			}
		};
		thread.start();
	}
	
	public void restart() {
		model = new Model();
		view.setModel(model);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		touchX = event.getX();
		touchY = event.getY();
		
		updateDir = true;
		
		return true;
	}
	
	// The view that will be render the Graphics
	private class GameView extends SurfaceView implements Callback {

		private boolean isSurfaceCreated;
		private Model model;
		
		Paint clearPaint = new Paint();
		
		private GameView(Context context) {
			super(context);
		}
		
		public void setModel(Model model) {
			this.model = model;
		}
		
		public GameView(Context context, Model model) {
			this(context);
			
			isSurfaceCreated = false;
			this.model = model;
			clearPaint.setARGB(255, 0, 0, 0);
			
			getHolder().addCallback(this);
			setFocusable(true);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			isSurfaceCreated = true;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}
		
		public boolean isSurfaceCreated() {
			return isSurfaceCreated;
		}
		
		public void render(Canvas c) {
			c.drawRect(0, 0, c.getWidth(), c.getHeight(), clearPaint);
			
			List<RectEntity> obstacles = model.getObstacles();
			Paint obstaclePaint = model.obstaclePaint;
			RectEntity exit = model.getExit();
			Paint exitPaint = model.exitPaint;
			List<CircleEntity> sphereFruits = model.getSphereFruits();
			Paint sphereFruitPaint = model.sphereFruitPaint;
			List<Segment> snakeSegments = model.getSnakeSegments();
			Paint snakePaint = model.snakePaint;
			
			float scaleX = c.getWidth()*1f/model.boardWidth;
			float scaleY = c.getHeight()*1f/model.boardHeight;

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
				CircleEntity f = sphereFruits.get(i);

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
		}
	}
	
	// All model classes are below here
	
	/**
	 * The model of the fluid Snake game.
	 * 
	 * @author Hassel
	 *
	 */
	private class Model {
		public final int boardWidth = 200;
		public final int boardHeight = 400;
		
		public final float speedFactor = 2;
		
		// Snake length
		public final int maxSegments = 6;
		public final int snakeWidth = 15;
		
		public final int obstacleWidth = 15;
		// 
		public final int obstacleMargin = 8;
		
		public final int sphereFruitWidth = 10;
		
		public final int nbrOfSphereFruits = 3;
		
		private List<Segment> snakeSegments;
		private List<RectEntity> obstacles;
		private List<CircleEntity> sphereFruits;
		
		private float startX;
		private float startY;
		
		private RectEntity exit;
		
		private boolean started;
		private boolean lost;
		private boolean won;
		
		private Paint snakePaint;
		private Paint obstaclePaint;
		private Paint sphereFruitPaint;
		private Paint exitPaint;
		
		private float dx;
		private float dy;
		
		private float modelTime;
		
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
			
			modelTime = 0;
			
			snakeSegments = new ArrayList<Segment>();
			snakeSegments.add(new Segment(startX, startY, snakeWidth));
			
			obstacles = new ArrayList<RectEntity>();
			obstacles.add(new RectEntity(0, 0, boardWidth, obstacleWidth));
			obstacles.add(new RectEntity(0, 0, obstacleWidth, boardHeight));
			obstacles.add(new RectEntity(boardWidth - obstacleWidth, 0, obstacleWidth, boardHeight));
			obstacles.add(new RectEntity(0, boardHeight - obstacleWidth, boardWidth, obstacleWidth));
			
			sphereFruits = new ArrayList<CircleEntity>();
			Random rand = new Random();
			
			for (int i = 0; i < nbrOfSphereFruits; i++) {
				sphereFruits.add(new CircleEntity(
						rand.nextInt(boardWidth - 2*(obstacleWidth + sphereFruitWidth)) + obstacleWidth + sphereFruitWidth,
						rand.nextInt(boardHeight - 2*(obstacleWidth + sphereFruitWidth)) + obstacleWidth + sphereFruitWidth,
						sphereFruitWidth));
			}
			
			started = false;
			lost = false;
			won = false;
		}
		
		public boolean won() {
			return won;
		}
		
		public boolean lost() {
			return lost;
		}

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
				
				started = true;
			}
		}
		
		public void update(float delta) {
			if (started) {
				if (delta >= 5) {
					delta = 5;
				}
				delta *= speedFactor;
				
				snakeSegments.get(0).setX(snakeSegments.get(0).getX() + dx*delta);
				snakeSegments.get(0).setY(snakeSegments.get(0).getY() + dy*delta);
				
				snakeSegments.get(0).getXList().add(snakeSegments.get(0).getX());
				snakeSegments.get(0).getYList().add(snakeSegments.get(0).getY());
				
				
				for (int i = 1; i < snakeSegments.size(); i++) {
					snakeSegments.get(i).setX(snakeSegments.get(0).getXList().get(snakeSegments.get(i).iter));
					snakeSegments.get(i).setY(snakeSegments.get(0).getYList().get(snakeSegments.get(i).iter));
						
					snakeSegments.get(i).iter++;
				}
				
				snakeSegments.get(0).iter++;
				
				if (snakeSegments.size() < maxSegments) {
					modelTime += delta;
					
					if (modelTime >= (snakeSegments.get(0).getWidth()*2)) {
						snakeSegments.add(new Segment(startX, startY, snakeWidth));
						modelTime -= snakeSegments.get(0).getWidth()*2;
					}
				}
				
				checkCollision();
			}
		}
		
		public void checkCollision() {
			Segment head = snakeSegments.get(0);
			
			if (snakeSegments.get(snakeSegments.size()-1).getY() + snakeWidth <= 0) {
				won = true;
			}
			
			for (int i = 1; i < snakeSegments.size(); i++) {
				if (Math.abs((snakeSegments.get(i).getX() - snakeSegments.get(0).getX())) <= head.getWidth()/2
						&& Math.abs((snakeSegments.get(i).getY() - snakeSegments.get(0).getY())) <= head.getWidth()/2) {
					lost = true;
					break;
				}
			}
			
			if (!(exit != null
					&& 0 < head.getX() - head.getWidth() - exit.getX() 
					&& 0 < exit.getX() + exit.getWidth() - head.getWidth() - head.getX()
					&& 0 < head.getY() + head.getHeight() - exit.getY()
					&& 0 < exit.getY() + exit.getHeight() + head.getHeight() - head.getY())) {
				
				for (int i = 0; i < obstacles.size(); i++) {
					RectEntity o = obstacles.get(i);
					if (obstacleMargin < head.getX() + head.getWidth() - o.getX() 
							&& obstacleMargin < o.getX() + o.getWidth() + head.getWidth() - head.getX()
							&& obstacleMargin < head.getY() + head.getHeight() - o.getY()
							&& obstacleMargin < o.getY() + o.getHeight() + head.getHeight() - head.getY()) {
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
		
		public List<CircleEntity> getSphereFruits() {
			return sphereFruits;
		}
		
		public List<RectEntity> getObstacles() {
			return obstacles;
		}
		
		public List<Segment> getSnakeSegments() {
			return snakeSegments;
		}
		
		public RectEntity getExit() {
			return exit;
		}
	}

	private class CircleEntity extends Entity { // Healthy!!
		private int width;
		
		public CircleEntity(float x, float y, int width) {
			super(x, y);
			
			this.width = width;
		}

		public float getWidth() {
			return width;
		}
		
		public float getHeight() {
			return width;
		}
	}
	
	private class RectEntity extends Entity {
		private float width;
		private float height;
		
		public RectEntity(float x, float y, float width, float height) {
			super(x, y);
			this.width = width;
			this.height = height;
		}
		
		public float getWidth() {
			return width;
		}
		
		public float getHeight() {
			return height;
		}
	}
	
	private class Segment extends CircleEntity {
		private final List<Float> xList;
		private final List<Float> yList;
		
		private int iter;
		
		public Segment(float x, float y, int width) {
			super(x, y, width);
			
			iter = 0;
			
			xList = new ArrayList<Float>();
			yList = new ArrayList<Float>();
		}

		public List<Float> getXList() {
			return xList;
		}

		public List<Float> getYList() {
			return yList;
		}
	}
	
	private abstract class Entity {
		private float x;
		private float y;
		
		public Entity(float x, float y) {
			this.x = x;
			this.y = y;
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
	}
}