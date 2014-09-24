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

package se.toxbee.sleepfighter.factory;

import android.provider.Settings;

import se.toxbee.sleepfighter.model.SnoozeConfig;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import se.toxbee.sleepfighter.model.audio.AudioSourceType;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfig;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;
import se.toxbee.sleepfighter.model.time.ExactTime;
import se.toxbee.commons.collect.PrimitiveArrays;

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