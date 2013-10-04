package se.chalmers.dat255.sleepfighter.android.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

public class VolumePreference extends DialogPreference {

	SeekBar slider;
	private int volume;
	
	public VolumePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public View onCreateDialogView() {
		slider = new SeekBar(getContext());
		slider.setMax(7);
		
		slider.setProgress(volume);
		
		return slider;
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			volume = slider.getProgress();
			
			if (callChangeListener(100*volume/slider.getMax())) {
				persistInt(volume);
			}
		}
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public int getVolume() {
		return volume;
	}
}
