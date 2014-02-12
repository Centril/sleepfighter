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

package se.toxbee.sleepfighter.audio.playlist;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.audio.BaseAudioDriver;
import se.toxbee.sleepfighter.audio.utils.VolumeUtils;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import se.toxbee.sleepfighter.service.AudioService;

/**
 * PlaylistDriver is the AudioDriver for playlists.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 29, 2013
 */
public class PlaylistDriver extends BaseAudioDriver {
	private PlaylistProviderFactory factory;
	private PlaylistProvider provider;
	private Playlist playlist;
	private int  volume;

	/**
	 * Constructs a PlaylistDriver given a PlaylistProviderFactory.
	 *
	 * @param factory a PlaylistProviderFactory object.
	 */
	public PlaylistDriver( PlaylistProviderFactory factory ) {
		this.factory = factory;
	}

	@Override
	public void setSource( Context context, AudioSource source ) {
		super.setSource( context, source );

		this.playlist = null;
		this.provider = null;
	}

	@Override
	public String printSourceName() {
		if ( this.playlist == null ) {
			this.playlist = this.getProvider().getPlaylistFor( this.getContext(), this.getSource().getUri() );
		}
		// If null after lazy load, the playlist no longer exists on the device
		if (this.playlist == null) {
			// May be better if handled some other way, and somewhere other than
			// here
			return getContext().getString(R.string.pref_playlist_not_found);
		}

		return this.playlist.getName();
	}

	private PlaylistProvider getProvider() {
		if ( this.provider == null ) {
			this.provider = this.factory.getProvider( this.getSource().getUri() );
		}

		return this.provider;
	}

	@Override
	public void play( AudioConfig config ) {
		super.play( config );
		this.volume = config.getVolume();

		float bundleVol = VolumeUtils
				.convertUIToFloatVolume(config.getVolume());

		Intent intent = new Intent(AudioService.ACTION_PLAY_PLAYLIST);

		List<Track> tracks = getProvider().getTracks(getContext(),
				getSource().getUri());

		if (tracks.isEmpty()) {
			// Play nothing if playlist empty
			return;
		}

		// Assemble String array with paths to bundle
		String[] data = new String[tracks.size()];
		for(int i = 0; i < data.length; i++) {
			data[i] = tracks.get(i).getData();
		}

		intent.putExtra(AudioService.BUNDLE_PLAYLIST, data);
		intent.putExtra(AudioService.BUNDLE_FLOAT_VOLUME, bundleVol);
		getContext().startService(intent);
	}

	@Override
	public void stop() {
		super.stop();
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

	@Override
	public int getVolume() {
		return this.volume;
	}
	
	
}
