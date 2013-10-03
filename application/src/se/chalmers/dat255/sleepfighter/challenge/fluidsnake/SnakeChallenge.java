/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 * 
 * This file is part of SleepFighter.
 * 
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package se.chalmers.dat255.sleepfighter.challenge.fluidsnake;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;

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
}