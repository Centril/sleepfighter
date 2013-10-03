package se.chalmers.dat255.sleepfighter.challenge.fluidsnake;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

//The view that will be render the Graphics
	public class GameView extends SurfaceView implements Callback {

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
			RectEntity exit = model.getExit();
			List<CircleEntity> sphereFruits = model.getSphereFruits();
			List<Segment> snakeSegments = model.getSnakeSegments();
			
			float scaleX = c.getWidth()*1f/model.boardWidth;
			float scaleY = c.getHeight()*1f/model.boardHeight;

			for (int i = 0; i < obstacles.size(); i++) {
				RectEntity o = obstacles.get(i);

				c.drawRect(
						o.getX() * scaleX,
						o.getY() * scaleY,
						o.getX() * scaleX + o.getWidth() * scaleX,
						o.getY() * scaleY + o.getHeight() * scaleY, o.getPaint());
			}

			if (exit != null) {
				c.drawRect(
						exit.getX() * scaleX,
						exit.getY() * scaleY,
						exit.getX() * scaleX + exit.getWidth() * scaleX,
						exit.getY() * scaleY + exit.getHeight() * scaleY, exit.getPaint());
			}

			for (int i = 0; i < sphereFruits.size(); i++) {
				CircleEntity f = sphereFruits.get(i);

				c.drawOval(new RectF(
						f.getX() * scaleX - f.getWidth() * scaleX,
						f.getY() * scaleY - f.getHeight() * scaleY,
						f.getX() * scaleX + f.getWidth() * scaleX,
						f.getY() * scaleY + f.getHeight() * scaleY), f.getPaint());
			}

			for (int i = 0; i < snakeSegments.size(); i++) {
				Segment s = snakeSegments.get(i);

				c.drawOval(new RectF(
						(s.getX() * scaleX - s.getWidth() * scaleX),
						(s.getY() * scaleY - s.getHeight() * scaleY),
						(s.getX() * scaleX + s.getWidth() * scaleX),
						(s.getY() * scaleY + s.getHeight() * scaleY)), s.getPaint());
			}
		}
	}