package se.chalmers.dat255.sleepfighter.challenge;

import java.util.Deque;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;

public class SnakeChallenge extends Thread implements Challenge {

	
	
	private long delay = 1000000000L / 25;
	  private long beforeTime = 0;
	  private long afterTime = 0;
	  private long timeDiff = 0;
	  private long sleepTime;
	  private long overSleepTime = 0;
	  private long excess = 0;
	
	
	
	
	
	private boolean playing;
	private Model model;
	private SurfaceView view;
	private Activity activity;
	
	private Paint p;
	
	@Override
	public void start(ChallengeActivity activity) {
		
		playing = true;
		this.activity = activity;
		Display display = activity.getWindowManager().getDefaultDisplay();
		
		final int width = display.getWidth();
		final int height = display.getHeight();
		
		model = new Model();
		
		p = new Paint();
		Random r = new Random();
		p.setColor(new Color().rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
		
		view = new SurfaceView(activity) implements SurfaceHolder.Callback {
			
			@Override
			public void surfaceCreated (SurfaceHolder holder) {
		       start();
		    }
		};
	}
	
	@Override
	public void run() {
		while (playing) {
			Canvas c = null;
		     try {
		           //lock canvas so nothing else can use it
		           c = view.getHolder().lockCanvas(null);
		           synchronized (view.getHolder()) {
		                //clear the screen with the black painter.
		        	   c.drawCircle(model.exit.x, model.exit.y, 50, p);
		                //This is where we draw the game engine.
		         }
		}
		     finally {
	         // do this in a finally so that if an exception is thrown
	         // during the above, we don't leave the Surface in an
	         // inconsistent state
	         if (c != null) {
	             view.getHolder().unlockCanvasAndPost(c);
	         }
		     }
	}}
	
	private class Position {
		private int x;
		private int y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void setX(int x) {
			this.x = x;
		}
		
		public int getX() {
			return x;
		}
		
		public void setY(int y) {
			this.y = y;
		}
		
		public int getY() {
			return y;
		}
	}
	
	private class Model {
		
		private final int START_LENGTH = 5;
		
		private Deque<Position> snakePos;
		private List<Position> emptyPos;
		private List<Position> obstaclePos;
		private Position exit;
		
		public Model() {
			exit = new Position(3, 3);
		}
		
		public void update() {
			exit.setX(exit.getX() + 1);
		}	
	}
	
	private class DerpView extends SurfaceView implements Callback {

		public DerpView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
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
		
	}
}
