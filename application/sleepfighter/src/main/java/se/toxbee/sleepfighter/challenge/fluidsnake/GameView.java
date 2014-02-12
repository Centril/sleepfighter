/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.challenge.fluidsnake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.util.List;

import se.toxbee.sleepfighter.challenge.snake.CircleEntity;
import se.toxbee.sleepfighter.challenge.snake.RectEntity;
import se.toxbee.sleepfighter.challenge.snake.Segment;

/**
 * The view that will contain the game graphics.
 * 
 * @author Hassel
 * 
 */
public class GameView extends SurfaceView implements Callback {

	private boolean isSurfaceCreated;
	private Model model;

	// paint which should be used to clear the canvas
	Paint clearPaint = new Paint();

	private GameView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param model
	 *            the model this View should fetch from
	 */
	public GameView(Context context, Model model) {
		this(context);

		isSurfaceCreated = false;
		this.model = model;
		clearPaint.setARGB(255, 0, 0, 0);

		getHolder().addCallback(this);
		setFocusable(true);
	}

	/**
	 * @param model
	 *            the model this view should fetch from
	 */
	public void setModel(Model model) {
		this.model = model;
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

	/**
	 * Will try to render the model on the canvas provided
	 */
	public void render() {
		if (isSurfaceCreated) {
			Canvas c = null;

			c = getHolder().lockCanvas();

			synchronized (getHolder()) {
				
				if (c != null) {
					// clear the canvas
					c.drawRect(0, 0, c.getWidth(), c.getHeight(), clearPaint);

					// Fetch entities from the model
					List<RectEntity> obstacles = model.getObstacles();
					RectEntity exit = model.getExit();
					List<CircleEntity> sphereFruits = model.getSphereFruits();
					List<Segment> snakeSegments = model.getSnakeSegments();

					float scaleX = c.getWidth() * 1f / SnakeConstants.BOARD_WIDTH;
					float scaleY = c.getHeight() * 1f / SnakeConstants.BOARD_HEIGHT;

					// draw obstacles
					for (int i = 0; i < obstacles.size(); i++) {
						RectEntity o = obstacles.get(i);

						c.drawRect(o.getX() * scaleX, o.getY() * scaleY,
								o.getX() * scaleX + o.getWidth() * scaleX,
								o.getY() * scaleY + o.getHeight() * scaleY,
								o.getPaint());
					}

					// draw exit over obstacles
					if (exit != null) {
						c.drawRect(
								exit.getX() * scaleX,
								exit.getY() * scaleY,
								exit.getX() * scaleX + exit.getWidth() * scaleX,
								exit.getY() * scaleY + exit.getHeight()
										* scaleY, exit.getPaint());
					}

					// draw the spherical fruits
					for (int i = 0; i < sphereFruits.size(); i++) {
						CircleEntity f = sphereFruits.get(i);

						c.drawOval(new RectF(f.getX() * scaleX - f.getRadius()
								* scaleX, f.getY() * scaleY - f.getRadius()
								* scaleY, f.getX() * scaleX + f.getRadius()
								* scaleX, f.getY() * scaleY + f.getRadius()
								* scaleY), f.getPaint());
					}

					// draw the snake above everything
					for (int i = 0; i < snakeSegments.size(); i++) {
						Segment s = snakeSegments.get(i);

						c.drawOval(new RectF((s.getX() * scaleX - s.getRadius()
								* scaleX), (s.getY() * scaleY - s.getRadius()
								* scaleY), (s.getX() * scaleX + s.getRadius()
								* scaleX), (s.getY() * scaleY + s.getRadius()
								* scaleY)), s.getPaint());
					}

					// update the view with the updated canvas
					getHolder().unlockCanvasAndPost(c);
				}
			}
		}
	}
}