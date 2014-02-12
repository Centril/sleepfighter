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

/**
 * FromPresetAlarmFactory creates an Alarm from a preset alarm object.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 4, 2013
 */
public class FromPresetAlarmFactory implements AlarmFactory {
	private Alarm preset;

	/**
	 * Constructs a FromPresetAlarmFactory given a preset.
	 *
	 * @param preset the preset to use.
	 */
	public FromPresetAlarmFactory( Alarm preset ) {
		if ( !preset.isPresetAlarm() ) {
			throw new IllegalArgumentException( "Alarm provided is not a preset." );
		}

		this.preset = preset;
	}

	@Override
	public Alarm createAlarm() {
		Alarm alarm = new Alarm( this.preset );
		alarm.setIsPresetAlarm( false );

		return alarm;
	}

	/**
	 * Returns the preset if need be.
	 *
	 * @return the preset.
	 */
	public Alarm getPreset() {
		return this.preset;
	}
}