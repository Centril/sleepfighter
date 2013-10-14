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
package se.chalmers.dat255.sleepfighter.utils.android;

import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.content.Intent;

/**
 * IntentUtils provides some utilities for reading and writing data to intents.<br/>
 * Writing when in reading-mode and vice versa is pointless, intents are parceled.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 24, 2013
 */
public class IntentUtils {
	private static final String ALARM_EXTRA_ID = "alarm_id";
	private static final String SETTING_PRESET_ALARM = "setting_preset_alarm";
	
	private final Intent intent;

	/**
	 * Constructs a IntentUtils object.
	 *
	 * @param intent intent object to perform operations on.
	 */
	public IntentUtils( Intent intent ) {
		this.intent = intent;
	}

	/**
	 * Sets the alarm id on intent.
	 *
	 * @param alarm the alarm
	 * @return this.
	 */
	public IntentUtils setAlarmId( Alarm alarm ) {
		return this.setAlarmId( alarm.getId() );
	}

	/**
	 * Sets the alarm id on intent.<br/>
	 * If provided id == {@link Alarm#NOT_COMMITTED_ID}, nothing happens.
	 *
	 * @param id the alarm id.
	 * @return this.
	 */
	public IntentUtils setAlarmId( final int id ) {
		if ( id != Alarm.NOT_COMMITTED_ID ) {
			this.intent.putExtra( ALARM_EXTRA_ID, id );
		}

		return this;
	}
	
	// if we are about to go to the settings for the preset(default) alarm, set this to true.
	public IntentUtils setSettingPresetAlarm(final boolean settingPresetAlarm) {
		this.intent.putExtra( SETTING_PRESET_ALARM,  settingPresetAlarm);
		
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
}
