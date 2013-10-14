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

package se.chalmers.dat255.sleepfighter.android.preference;

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
	private static final int maxNbr = 7;
	
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
