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

import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;

/**
 * BaseAudioDriver is the abstract base class for interface AudioDriver.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 29, 2013
 */
public abstract class BaseAudioDriver implements AudioDriver {
	private AudioSource source;
	private Context context;

	private boolean isPlaying;

	@Override
	public void setSource( Context context, AudioSource source ) {
		this.source = source;
		this.context = context;
	}

	protected Context getContext() {
		return this.context;
	}

	@Override
	public AudioSource getSource() {
		return this.source;
	}

	@Override
	public boolean isPlaying() {
		return this.isPlaying;
	}

	@Override
	public void play( AudioConfig config ) {
		this.isPlaying = true;
	}

	@Override
	public void stop() {
		this.isPlaying = false;
	}

	@Override
	public void toggle( AudioConfig config ) {
		if ( this.isPlaying() ) {
			this.stop();
		} else {
			this.play( config );
		}
	}
}
