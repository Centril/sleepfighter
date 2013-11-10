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
package se.toxbee.sleepfighter.helper;

import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.SFApplication;
import android.app.Activity;
import android.content.Intent;

/**
 * AlarmIntentHelper provides some utilities for reading and writing data to intents regarding alarms.<br/>
 * Writing when in reading-mode and vice versa is pointless, intents are parceled.<br/>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 24, 2013
 */
public class AlarmIntentHelper {
	private static final String ALARM_EXTRA_ID = "alarm_id";
	private static final String SETTING_PRESET_ALARM = "setting_preset_alarm";
	
	private final Intent intent;

	/**
	 * Constructs a IntentUtils object.
	 *
	 * @param intent intent object to perform operations on.
	 */
	public AlarmIntentHelper( Intent intent ) {
		this.intent = intent;
	}

	/**
	 * Sets the alarm id on intent.
	 *
	 * @param alarm the alarm
	 * @return this.
	 */
	public AlarmIntentHelper setAlarmId( Alarm alarm ) {
		return this.setAlarmId( alarm.getId() );
	}

	/**
	 * Sets the alarm id on intent.<br/>
	 * If provided id == {@link Alarm#NOT_COMMITTED_ID}, nothing happens.
	 *
	 * @param id the alarm id.
	 * @return this.
	 */
	public AlarmIntentHelper setAlarmId( final int id ) {
		if ( id != Alarm.NOT_COMMITTED_ID ) {
			this.intent.putExtra( ALARM_EXTRA_ID, id );
		}

		return this;
	}

	// if we are about to go to the settings for the preset(default) alarm, set this to true.
	public AlarmIntentHelper setSettingPresetAlarm(final boolean settingPresetAlarm) {
		this.intent.putExtra( SETTING_PRESET_ALARM,  settingPresetAlarm);
		
		return this;
	}

	/**
	 * Sets an alarm on intent.
	 *
	 * @param alarm the alarm.
	 * @return this.
	 */
	public AlarmIntentHelper setAlarm( final Alarm alarm ) {
		if ( alarm.isPresetAlarm() ) {
			this.setSettingPresetAlarm( true );
		} else {
			this.setAlarmId( alarm );
		}

		return this;
	}

	/**
	 * Returns true if the alarm is a preset one.
	 *
	 * @return true if preset.
	 */
	public boolean isSettingPresetAlarm() {
		return this.intent.getBooleanExtra(SETTING_PRESET_ALARM, false);
	}

	/**
	 * Returns the ID of the alarm.
	 *
	 * @return the ID of the alarm.
	 */
	public int getAlarmId() {
		int id = this.intent.getIntExtra( ALARM_EXTRA_ID, Alarm.NOT_COMMITTED_ID );
		if ( id == Alarm.NOT_COMMITTED_ID ) {
			throw new IllegalArgumentException( "ID is outside of valid range, only positive integers allowed." );
		}

		return id;
	}

	/**
	 * Fetches an alarm from intent taken from activity or the preset if that is the intent.
	 *
	 * @param activity the activity to get intent from.
	 * @return the alarm.
	 */
	public static Alarm fetchAlarmOrPreset( Activity activity ) {
		SFApplication app = SFApplication.get();

		AlarmIntentHelper intentUtils = new AlarmIntentHelper( activity.getIntent() );
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

		AlarmIntentHelper intentUtils = new AlarmIntentHelper( activity.getIntent() );
		Alarm alarm = app.getAlarms().getById( intentUtils.getAlarmId() );
		return alarm;
	}
}