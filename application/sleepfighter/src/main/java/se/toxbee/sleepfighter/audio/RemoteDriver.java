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

import android.content.Intent;
import android.net.Uri;

import se.toxbee.sleepfighter.audio.utils.VolumeUtils;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.service.AudioService;

/**
 * RemoteDriver is the audio driver for Internet streams / remote URIs.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 18, 2013
 */
public class RemoteDriver extends BaseAudioDriver {
	private int volume = 0;

	@Override
	public String printSourceName() {
		return this.getSource().getUri();
	}

	@Override
	public void play(AudioConfig config) {
		super.play(config);
		this.volume = config.getVolume();

		float bundleVol = VolumeUtils.convertUIToFloatVolume(config.getVolume());

		Intent i = new Intent(AudioService.ACTION_PLAY_REMOTE);
		i.putExtra(AudioService.BUNDLE_URI, Uri.parse( this.getSource().getUri() ) );
		i.putExtra(AudioService.BUNDLE_FLOAT_VOLUME, bundleVol);
		this.getContext().startService(i);
	}

	@Override
	public void stop() {
		Intent i = new Intent(AudioService.ACTION_STOP);
		getContext().startService(i);
		super.stop();
	}

	@Override
	public void setVolume(int volume) {
		this.volume = volume;
		float bundleVol = VolumeUtils.convertUIToFloatVolume(volume);

		Intent i = new Intent(AudioService.ACTION_VOLUME);
		i.putExtra(AudioService.BUNDLE_FLOAT_VOLUME, bundleVol);
		getContext().startService(i);
	}
	
	public int getVolume() {
		return this.volume;
	}
}
