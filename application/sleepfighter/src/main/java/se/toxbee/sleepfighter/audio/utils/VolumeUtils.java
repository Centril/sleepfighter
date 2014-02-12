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

package se.toxbee.sleepfighter.audio.utils;

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
	 * <p>
	 * The scaling method used is to approximate using x^3 as described by
	 * Alexander Thomas
	 * <a href="http://www.dr-lex.be/info-stuff/volumecontrols.html">here</a>.
	 * </p>
	 * 
	 * @param volume
	 *            the volume in the range 0-100
	 * @return the scaled volume in the range 0-1
	 */
	public static float convertUIToFloatVolume(int volume) {
		float f = volume / 100f;
		float powEstimate = (float) Math.pow(f, 3);
		return powEstimate;
	}}
