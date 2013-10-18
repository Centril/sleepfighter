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
package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

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
