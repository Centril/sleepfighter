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
package se.toxbee.sleepfighter.factory;

import se.toxbee.sleepfighter.model.SnoozeConfig;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import se.toxbee.sleepfighter.model.audio.AudioSourceType;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfig;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;
import se.toxbee.sleepfighter.model.time.ExactTime;
import se.toxbee.sleepfighter.utils.collect.PrimitiveArrays;
import android.provider.Settings;

/**
 * PresetAlarmFactory is responsible for creating our preset.<br/>
 * Define the preset on app initialization here.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 4, 2013
 */
public class PresetAlarmFactory extends AbstractAlarmFactory {
	@Override
	protected ExactTime createTime() {
		return new ExactTime( 8, 0 );
	}

	@Override
	protected String createName() {
		return null;
	}

	@Override
	protected boolean createIsPresetFlag() {
		return true;
	}

	@Override
	protected boolean createIsActivated() {
		return false;
	}

	@Override
	protected boolean[] createEnabledDays() {
		return PrimitiveArrays.filled( true, 7 );
	}

	@Override
	protected AudioSource createAudioSource() {
		return new AudioSource(
				AudioSourceType.RINGTONE,
				Settings.System.DEFAULT_ALARM_ALERT_URI.toString() );
	}

	protected AudioConfig createAudioConfig() {
		return new AudioConfig(100, createVibrationFlag());
	}

	@Override
	protected SnoozeConfig createSnoozeConfig() {
		return new SnoozeConfig(true, 9);
	}

	@Override
	protected boolean createVibrationFlag() {
		return true;
	}

	@Override
	protected ChallengeConfigSet createChallenges() {
		ChallengeConfigSet set = new ChallengeConfigSet( true );

		for ( ChallengeType type : ChallengeType.values() ) {
			set.putChallenge( new ChallengeConfig( type, true ) );
		}

		return set;
	}

	@Override
	protected boolean createIsSpeech() {
		return false;
	}
	
	@Override
	protected boolean createIsFlashEnabled(){
		return false;
	}
}