package se.chalmers.dat255.sleepfighter.android.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

public class VolumePreference extends DialogPreference {

	SeekBar slider;
	
	public VolumePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		slider = new SeekBar(context);
		
	}
	
	@Override
	public View onCreateDialogView() {
		return slider;
	}
}
