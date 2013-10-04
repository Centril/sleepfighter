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

import se.chalmers.dat255.sleepfighter.audio.BaseAudioDriver;
import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;

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
	}

	@Override
	public void stop() {
		super.stop();
	}
}
