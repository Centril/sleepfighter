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

import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * RingtoneDriver is the AudioDriver for ringtones.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class RingtoneDriver extends BaseAudioDriver  {
	private Ringtone ringtone;

	@Override
	public void setSource( Context context, AudioSource source ) {
		super.setSource( context, source );

		Uri ringtoneUri = Uri.parse( source.getUri() );
		this.ringtone = RingtoneManager.getRingtone( context, ringtoneUri );
	}

	@Override
	public String printSourceName() {
		return this.ringtone.getTitle( this.getContext() );
	}

	@Override
	public void play( AudioConfig config ) {
		super.play( config );

		// TODO: Use volume and stuff, use MediaPlayer maybe?
		this.ringtone.setStreamType( AudioManager.STREAM_ALARM );
		this.ringtone.play();
	}

	@Override
	public void stop() {
		this.ringtone.stop();
		super.stop();
	}

	@Override
	public void setVolume(int volume) {
		// TODO Auto-generated method stub	
	}
}
