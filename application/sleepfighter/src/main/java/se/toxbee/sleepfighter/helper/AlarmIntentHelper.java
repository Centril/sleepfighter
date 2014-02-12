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

package se.toxbee.sleepfighter.helper;

import android.app.Activity;
import android.content.Intent;

import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.model.Alarm;

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

	/**
	 * Indicate that we want preset alarm.
	 *
	 * @param isPreset true if it is the preset alarm.
	 * @return this.
	 */
	public AlarmIntentHelper setSettingPresetAlarm( final boolean isPreset ) {
		this.intent.putExtra( SETTING_PRESET_ALARM, isPreset );
		
		return this;
	}

	/**
	 * Returns the intent we're working on.
	 *
	 * @return the intent.
	 */
	public Intent intent() {
		return this.intent;
	}

	/**
	 * Sets the alarm on intent.
	 *
	 * @param alarm an alarm.
	 * @return this.
	 */
	public AlarmIntentHelper setAlarm( Alarm alarm ) {
		if ( alarm.isPresetAlarm() ) {
			this.setSettingPresetAlarm( true );
		} else {
			this.setAlarmId( alarm );
		}

		return this;
	}

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