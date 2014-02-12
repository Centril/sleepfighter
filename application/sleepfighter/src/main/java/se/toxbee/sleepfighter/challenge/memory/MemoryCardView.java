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

package se.toxbee.sleepfighter.challenge.memory;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Represents a memory card graphically
 *
 * @author Eric Arneb�ck
 * @version 1.0
 * @since Sep 28, 2013
 */

public class MemoryCardView extends Button {

	// what to show when the memory card is flipped over and hidden.
  	private static String HIDDEN =  "";
  	// what to show when the memory card s flipped over and shown.
  	private String SHOWN;

  	private int position;

  	private String currentImage;
  	
  	
  	private float FONT_SIZE = 40;

    public MemoryCardView(Context context) {
        super(context);
        this.setTextSize(FONT_SIZE);
    }

    public MemoryCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextSize(FONT_SIZE);
    }

	public void hide() {
		currentImage = HIDDEN;
		super.setText(currentImage);
		super.setClickable(true);
	}

	public void show() {
		currentImage = SHOWN;
		super.setText(currentImage);

		// it should not be clickable when it is flipped. 
		super.setClickable(false);
	}

	public boolean isHidden() {
		return this.currentImage.equals(HIDDEN);
	}

	public boolean isShown() {
		return !isHidden();
	}

	public void flip() {
		if(isHidden()) {
			this.show();
		} else {
			this.hide();
		}
	}

	public void setImage(String image) {

		SHOWN = image;

		// all cards are hidden by default. 
		hide();
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return this.position;
	}
}