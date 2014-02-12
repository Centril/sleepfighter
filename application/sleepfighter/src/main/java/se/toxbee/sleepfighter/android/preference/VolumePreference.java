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

package se.toxbee.sleepfighter.android.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

/**
 * A preference designed to change volume for a specific alarm
 * 
 * @author Hassel
 *
 */
public class VolumePreference extends DialogPreference {

	// maximum number of points the slider can stick to
	private static final int maxNbr = 100;

	private SeekBar slider;
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
			
			if (callChangeListener(volume)) {
				persistInt(volume);
			}
		}
	}
	
	/**
	 * @param volume the volume (0-100)
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	/**
	 * @return the volume
	 */
	public int getVolume() {
		return volume;
	}
}
