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
package se.chalmers.dat255.sleepfighter.model.audio;

import se.chalmers.dat255.sleepfighter.model.IdProvider;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * AudioConfig models data per Alarm such as volume, etc.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
@DatabaseTable(tableName = "audio_config")
public class AudioConfig implements IdProvider {
	public static final String ID_COLUMN = "id";

	@DatabaseField(generatedId = true, columnName = ID_COLUMN)
	private int id;

	@DatabaseField
	private int volume;
	
	@DatabaseField
	private boolean vibrationEnabled;
	
	// TODO: REMOVE when real fields are added, NEEDED 'cause SQLite crashes otherwise.
	@DatabaseField
	private String temp;

	/**
	 * Constructs an AudioConfig, for DB purposes only.
	 */
	public AudioConfig() {
	}

	/**
	 * Constructs an AudioConfig
	 * 
	 * @param volume the volume (0-100)
	 */
	public AudioConfig(int volume, boolean vibrationEnabled) {
		Debug.d("AudioConfig created! vibration = " + vibrationEnabled);
		
		this.volume = volume;
		this.vibrationEnabled = vibrationEnabled;
	}
	
	/**
	 * Copy constructor.
	 *
	 * @param audioConfig the config to copy from.
	 */
	public AudioConfig( AudioConfig rhs ) {
		this.volume = rhs.getVolume();
		this.vibrationEnabled = rhs.getVibrationEnabled();
	}


	/**
	 * Returns the id of the AudioConfig (in DB).
	 *
	 * @return the id.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * @return the volume of this AudioConfig (0-100)
	 */
	public int getVolume() {
		return volume;
	}
	
	/**
	 * @param volume the volume of this AudioConfig (0-100)
	 */
	public void setVolume(int volume) {
		if (volume < 0 || volume > 100) {
			throw new IllegalArgumentException();
		}
		this.volume = volume;
	}
	
	public boolean getVibrationEnabled() {
		return this.vibrationEnabled;
	}

	public void setVibrationEnabled(boolean vibrationEnabled) {
		this.vibrationEnabled = vibrationEnabled;
	}

}
