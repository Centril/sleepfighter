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
