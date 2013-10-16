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
package se.chalmers.dat255.sleepfighter.audio.playlist;

import java.util.List;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.audio.AudioService;
import se.chalmers.dat255.sleepfighter.audio.BaseAudioDriver;
import se.chalmers.dat255.sleepfighter.audio.utils.VolumeUtils;
import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;
import android.content.Intent;

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
