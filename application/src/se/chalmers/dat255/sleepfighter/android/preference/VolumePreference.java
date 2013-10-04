package se.chalmers.dat255.sleepfighter.android.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

public class VolumePreference extends DialogPreference {

	private final int maxNbr = 7;
	
	SeekBar slider;
	private int volume;
	
	public VolumePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public View onCreateDialogView() {
		slider = new SeekBar(getContext());
		slider.setMax(maxNbr);
		
		slider.setProgress(volume);
		
		return slider;
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			volume = slider.getProgress();
			
			if (callChangeListener(100*volume/maxNbr)) {
				persistInt(volume);
			}
		}
	}
	
	/**
	 * @param volume the volume (0-100)
	 */
	public void setVolume(int volume) {
		this.volume = (int) Math.round((volume*maxNbr)/100.0);
	}
	
	/**
	 * @return the volume
	 */
	public int getVolume() {
		return volume;
	}
}
