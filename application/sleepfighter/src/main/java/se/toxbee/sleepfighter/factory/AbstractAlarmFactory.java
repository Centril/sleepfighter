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

import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.SnoozeConfig;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.model.time.ExactTime;

/**
 * AbstractAlarmFactory is the abstract factory implementation of AlarmFactory. 
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 4, 2013
 */
public abstract class AbstractAlarmFactory implements AlarmFactory {
	@Override
	public Alarm createAlarm() {
		Alarm alarm = this.instantiateAlarm();

		// Set meta properties.
		alarm.setName( this.createName() );
		alarm.setIsPresetAlarm( this.createIsPresetFlag() );

		// Set scheduling properties.
		alarm.setActivated( this.createIsActivated() );
		alarm.setEnabledDays( this.createEnabledDays() );
		alarm.setTime( this.createTime() );

		// Set other basic non-foreign properties.
		alarm.setSpeech(this.createIsSpeech());
		alarm.setFlash( this.createIsFlashEnabled() );

		// Set foreign objects.
		alarm.setAudioSource( this.createAudioSource() );
		alarm.setFetched( this.createAudioConfig() );
		alarm.setChallenges( this.createChallenges() );
		alarm.setFetched( createSnoozeConfig() );

		return alarm;
	}

	protected Alarm instantiateAlarm() {
		return new Alarm();
	}

	protected abstract ExactTime createTime();

	protected abstract String createName();

	protected abstract boolean createIsPresetFlag();

	protected abstract boolean createIsActivated();

	protected abstract boolean[] createEnabledDays();

	protected abstract AudioSource createAudioSource();

	protected abstract AudioConfig createAudioConfig();

	protected abstract SnoozeConfig createSnoozeConfig();

	protected abstract boolean createVibrationFlag();

	protected abstract ChallengeConfigSet createChallenges();
	
	protected abstract boolean createIsFlashEnabled();

	protected abstract boolean createIsSpeech();
}