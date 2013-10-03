package se.chalmers.dat255.sleepfighter.challenge.fluidsnake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.graphics.Paint;

//All model classes are below here

	/**
	 * The model of the fluid Snake game.
	 * 
	 * @author Hassel
	 *
	 */
	public class Model {
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
			snakeSegments.add(new Segment(startX, startY, snakeWidth, snakePaint));
			
			obstacles = new ArrayList<RectEntity>();
			obstacles.add(new RectEntity(0, 0, boardWidth, obstacleWidth, obstaclePaint));
			obstacles.add(new RectEntity(0, 0, obstacleWidth, boardHeight, obstaclePaint));
			obstacles.add(new RectEntity(boardWidth - obstacleWidth, 0, obstacleWidth, boardHeight, obstaclePaint));
			obstacles.add(new RectEntity(0, boardHeight - obstacleWidth, boardWidth, obstacleWidth, obstaclePaint));
			
			sphereFruits = new ArrayList<CircleEntity>();
			Random rand = new Random();
			
			for (int i = 0; i < nbrOfSphereFruits; i++) {
				sphereFruits.add(new CircleEntity(
						rand.nextInt(boardWidth - 2*(obstacleWidth + sphereFruitWidth)) + obstacleWidth + sphereFruitWidth,
						rand.nextInt(boardHeight - 2*(obstacleWidth + sphereFruitWidth)) + obstacleWidth + sphereFruitWidth,
						sphereFruitWidth, sphereFruitPaint));
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
					snakeSegments.get(i).setX(snakeSegments.get(0).getXList().get(snakeSegments.get(i).getIter()));
					snakeSegments.get(i).setY(snakeSegments.get(0).getYList().get(snakeSegments.get(i).getIter()));
						
					snakeSegments.get(i).iter();
				}
				
				snakeSegments.get(0).iter();
				
				if (snakeSegments.size() < maxSegments) {
					modelTime += delta;
					
					if (modelTime >= (snakeSegments.get(0).getWidth()*2)) {
						snakeSegments.add(new Segment(startX, startY, snakeWidth, snakePaint));
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
				exit = new RectEntity(boardWidth/2, 0, 50, 30, exitPaint);
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