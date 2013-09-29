package se.chalmers.dat255.sleepfighter.challenge;

import android.content.Context;
import android.widget.ImageView;

/*
 * Represents a memory card graphically
 */
public class MemoryCardView extends ImageView {

	
	// what to show when the memory card is flipped over and hidden.
  	static final int HIDDEN =  android.R.color.white;
  	// what to show when the memory card s flipped over and shown.
  	int SHOWN;
  	
  	
  	int currentImage;
    
	
	public MemoryCardView(Context context) {
		super(context);
	}
	
	public void hide() {
		super.setImageResource(HIDDEN);
		currentImage = HIDDEN;
	}
	
	public void show() {
		super.setImageResource(SHOWN);
		currentImage = SHOWN;
	}
	
	public boolean isHidden() {
		return this.currentImage == HIDDEN;
	}
	
	public boolean isShown() {
		return !isHidden();
	}
	
	public void flip() {
		if(isHidden()) {
			this.show();
		} else
			this.hide();
	}
	
	@Override
	public void setImageResource(int image) {
		super.setImageResource(image);
		SHOWN = image;
		
		// all cards are hidden by default. 
		hide();
	}
}
