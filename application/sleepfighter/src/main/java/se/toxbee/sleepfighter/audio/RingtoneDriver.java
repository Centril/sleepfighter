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

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import se.toxbee.sleepfighter.model.audio.AudioSource;

/**
 * RingtoneDriver is the AudioDriver for ringtones.
 * <p>Only difference from a LocalContentDriver is that it's using different
 * way to fetch names.</p>
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class RingtoneDriver extends LocalContentDriver  {
	private Ringtone ringtone;

	@Override
	public void setSource( Context context, AudioSource source ) {
		super.setSource( context, source );

		Uri ringtoneUri = Uri.parse( source.getUri() );
		this.ringtone = RingtoneManager.getRingtone( context, ringtoneUri );
	}

	@Override
	// Overridden since getting title from some ringtones doesn't seem to work
	// otherwise (DRM related?)
	public String printSourceName() {
		// Seems to happen if URI is for default ringtone, and no ringtones are
		// on the device (emulator), returns the URI instead then
		if (this.ringtone == null) {
			return getSource().getUri();
		}
		return this.ringtone.getTitle( this.getContext() );
	}
}
