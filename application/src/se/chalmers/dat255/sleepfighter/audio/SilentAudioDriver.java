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

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;

/**
 * SilentAudioDriver is a silent driver that does nothing.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class SilentAudioDriver implements AudioDriver {
	private Context context;

	public SilentAudioDriver() {
	}

	@Override
	public void setSource( Context context, AudioSource source ) {
		this.context = context;
	}

	public AudioSource getSource() {
		return null;
	}

	@Override
	public String printSourceName() {
		return context.getString( R.string.alarm_audiosource_summary_name_none );
	}

	@Override
	public void play( AudioConfig config ) {
	}

	@Override
	public boolean isPlaying() {
		return false;
	}

	@Override
	public void stop() {
	}

	@Override
	public void toggle( AudioConfig config ) {
	}

	@Override
	public void setVolume(int volume) {
	}

	@Override
	public int getVolume() {
		return 0;
	}

}
