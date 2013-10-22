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
package se.toxbee.sleepfighter.android.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

/**
 * A DialogPreference with containing a TimePicker.
 * 
 * @author Hassel
 *
 */
public class TimepickerPreference extends DialogPreference {
	// TODO: Preview volume?
	
	
	private int hour;
	private int minute;
	private TimePicker tp;
	
	public TimepickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected View onCreateDialogView() {
		tp = new TimePicker(getContext());
		tp.setIs24HourView(true);
	
		tp.setCurrentHour(hour);
		tp.setCurrentMinute(minute);
		
		return(tp);
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			hour = tp.getCurrentHour();
			minute = tp.getCurrentMinute();
		}
		
		String time = (hour < 10 ? "0" : "") + hour + ":"
				+ (minute < 10 ? "0" : "") + minute;
		
		
		if (positiveResult && callChangeListener(time)) {
			persistString(time);
        }
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	/**
	 * @return the picked hour.
	 */
	public int getHour() {
		return hour;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	/**
	 * @return the picked minute.
	 */
	public int getMinute() {
		return minute;
	}
}
