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

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;

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
