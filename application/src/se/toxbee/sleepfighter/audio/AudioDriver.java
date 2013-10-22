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
package se.toxbee.sleepfighter.audio;

import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import android.content.Context;

/**
 * AudioDriver is the responsible for providing playing audio<br/>
 * and providing metadata from AudioSource.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public interface AudioDriver {
	/**
	 * Sets the source the driver should handle.
	 *
	 * @param context android context, use to save or w/e is needed for driver.
	 * @param source the AudioSource to handle.
	 */
	public void setSource( Context context, AudioSource source );

	/**
	 * Returns the source the driver handles.
	 *
	 * @return the source.
	 */
	public AudioSource getSource();

	/**
	 * Prints the name of source in a human readable format.<br/>
	 * This could for example be a songs title.
	 *
	 * @return name of source.
	 */
	public String printSourceName();

	/**
	 * Starts playing audio source.
	 *
	 * @param config audio configuration.
	 */
	public void play( AudioConfig config );

	/**
	 * Returns true if the driver is currently playing.
	 *
	 * @return true if playing.
	 */
	public boolean isPlaying();

	/**
	 * Stops playing audio source.
	 */
	public void stop();

	/**
	 * Toggles playing or pausing depending on {@link #isPlaying()}.
	 *
	 * @param config audio configuration.
	 */
	public void toggle( AudioConfig config );

	/**
	 * Sets the volume of audio played through the driver.
	 * 
	 * @param volume
	 *            the volume (0-100)
	 */
	public void setVolume(int volume);
	
	public int getVolume();
}
