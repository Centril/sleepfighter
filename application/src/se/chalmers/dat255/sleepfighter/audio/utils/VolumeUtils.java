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
package se.chalmers.dat255.sleepfighter.audio.utils;

import android.media.MediaPlayer;

/**
 * Class with utility methods for volume calculations.
 */
public class VolumeUtils {

	/**
	 * Prevent instantiation.
	 */
	private VolumeUtils() {

	}
	
	/**
	 * Convert volume from 0-100 integer, which corresponds to what a user sees
	 * in the UI, to a 0-1 float, which MediaPlayer uses 
	 * ({@link MediaPlayer#setVolume(float, float)}).
	 * 
	 * @param volume
	 *            the volume in the range 0-100
	 * @return the volume in the range 0-1
	 */
	public static float convertUIToFloatVolume(int volume) {
		// TODO perhaps to logarithmic conversion here
		return (float) volume / 100;
	}}
