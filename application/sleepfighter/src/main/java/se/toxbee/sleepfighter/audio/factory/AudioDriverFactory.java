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

package se.toxbee.sleepfighter.audio.factory;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import se.toxbee.sleepfighter.audio.AudioDriver;
import se.toxbee.sleepfighter.audio.LocalContentDriver;
import se.toxbee.sleepfighter.audio.RemoteDriver;
import se.toxbee.sleepfighter.audio.RingtoneDriver;
import se.toxbee.sleepfighter.audio.SilentAudioDriver;
import se.toxbee.sleepfighter.audio.playlist.PlaylistDriver;
import se.toxbee.sleepfighter.audio.playlist.PlaylistProviderFactory;
import se.toxbee.sleepfighter.model.audio.AudioSource;

/**
 * AudioDriverFactory produces AudioDriver:s.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class AudioDriverFactory {
	/**
	 * Constructs a AudioDriverFactory.
	 */
	public AudioDriverFactory() {
	}

	/**
	 * Produces an AudioDriver given a context and an AudioSource.
	 *
	 * @param context android context.
	 * @param source audio source.
	 * @return the produced AudioDriver.
	 */
	public AudioDriver produce( Context context, AudioSource source ) {
		AudioDriver driver = null;

		if ( source == null ) {
			driver = new SilentAudioDriver();
		} else {
			Log.d( "AudioDriverFactory", source.toString() );

			switch ( source.getType() ) {
			case RINGTONE:
				driver = new RingtoneDriver();
				break;
			case PLAYLIST:
				driver = new PlaylistDriver( new PlaylistProviderFactory() );
				break;
			case LOCAL_CONTENT_URI:
				driver = new LocalContentDriver();
				break;
			case INTERNET_STREAM:
				driver = new RemoteDriver();
				break;

			case SPOTIFY:
				Toast.makeText( context, "NOT IMPLEMENTED YET!", Toast.LENGTH_LONG ).show();
				driver = new SilentAudioDriver();
				break;
			default:
				throw new IllegalArgumentException("This should not happen");
			}
		}

		driver.setSource( context, source );
		return driver;
	}
}
