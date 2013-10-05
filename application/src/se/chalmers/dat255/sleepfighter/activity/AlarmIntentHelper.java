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
package se.chalmers.dat255.sleepfighter.activity;

import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.utils.android.IntentUtils;
import android.app.Activity;

/**
 * AlarmIntentHelper helps activities with fetching alarms from intents.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class AlarmIntentHelper {
	/**
	 * Fetches an alarm from intent taken from activity or the preset if that is the intent.
	 *
	 * @param activity the activity to get intent from.
	 * @return the alarm.
	 */
	public static Alarm fetchAlarmOrPreset( Activity activity ) {
		SFApplication app = SFApplication.get();

		IntentUtils intentUtils = new IntentUtils( activity.getIntent() );
		Alarm alarm = intentUtils.isSettingPresetAlarm() ? app.getFromPresetFactory().getPreset() : app.getAlarms().getById( intentUtils.getAlarmId() );
		return alarm;
	}

	/**
	 * Fetches an alarm from intent without checking for preset.
	 *
	 * @param activity the activity to get intent from.
	 * @return the alarm.
	 */
	public static Alarm fetchAlarm( Activity activity ) {
		SFApplication app = SFApplication.get();

		IntentUtils intentUtils = new IntentUtils( activity.getIntent() );
		Alarm alarm = app.getAlarms().getById( intentUtils.getAlarmId() );
		return alarm;
	}
}