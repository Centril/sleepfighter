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

import se.chalmers.dat255.sleepfighter.audio.utils.VolumeUtils;
import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.service.AudioService;
import android.content.Intent;
import android.net.Uri;

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
