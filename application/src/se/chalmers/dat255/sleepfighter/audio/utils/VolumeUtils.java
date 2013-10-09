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
