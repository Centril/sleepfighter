package se.chalmers.dat255.sleepfighter.android.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class VolumePreference extends DialogPreference implements OnSeekBarChangeListener {

	SeekBar slider;
	private int volume;
	
	public VolumePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public View onCreateDialogView() {
		slider = new SeekBar(getContext());
		slider.setMax(7);
		slider.setOnSeekBarChangeListener(this);
		
		slider.setProgress(volume);
		
		return slider;
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if (positiveResult && callChangeListener(100*volume/slider.getMax())) {
			persistInt(volume);
		}
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public int getVolume() {
		return volume;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		this.volume = seekBar.getProgress();
	}
}
