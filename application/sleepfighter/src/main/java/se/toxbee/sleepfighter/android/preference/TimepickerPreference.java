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
import android.widget.TimePicker;

import se.toxbee.commons.string.StringUtils;

/**
 * A DialogPreference with containing a TimePicker.
 *
 * @author Hassel
 */
public class TimepickerPreference extends DialogPreference {
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

		String time = StringUtils.joinTime( this.hour, this.minute );

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

	public void show() {
		this.showDialog( null );
	}
}
