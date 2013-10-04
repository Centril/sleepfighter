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

import java.util.Map;

import android.net.Uri;

import com.google.common.collect.Maps;

/**
 * PlaylistProviderFactory is a factory for PlaylistProvider
 *
 * @see PlaylistProvider
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 28, 2013
 */
public class PlaylistProviderFactory {
	public enum ProviderID {
		LOCAL
	}

	private Map<ProviderID, PlaylistProvider> entries;

	/**
	 * <p>Returns a PlaylistProvider that is a valid provider for a given playlistUri.<br/>
	 * This has the unfortunate downside of calling {@link #getAll()} internally,<br/>
	 * thus lazy loading all providers available.</p>
	 *
	 * <p>The first provider that is a match is returned.</p>
	 *
	 * @param playlistUri the URI of a playlist.
	 * @return the provider or null if none was found.
	 */
	public PlaylistProvider getProvider( String playlistUri ) {
		PlaylistProvider[] all = this.getAll();

		for ( PlaylistProvider provider : all ) {
			if ( provider.isProviderFor( playlistUri ) ) {
				return provider;
			}
		}

		return null;
	}

	/**
	 * <p>Returns a PlaylistProvider that matches the given URI.<br/>
	 * This has the unfortunate downside of calling {@link #getAll()} internally,<br/>
	 * thus lazy loading all providers available.</p>
	 *
	 * <p>The first provider that is a match is returned.</p>
	 *
	 * @param uri the URI of a the provider.
	 * @return the provider or null if none was found.
	 */
	public PlaylistProvider getProvider( Uri uri ) {
		PlaylistProvider[] all = this.getAll();

		for ( PlaylistProvider provider : all ) {
			if ( provider.getUri().equals( uri ) ) {
				return provider;
			}
		}

		return null;
	}

	/**
	 * Returns a PlaylistProvider that matches the ProviderID.<br/>
	 * This is lazy loaded.
	 *
	 * @param id the ProviderID.
	 * @return the provider.
	 */
	public PlaylistProvider getProvider( ProviderID id ) {
		if ( this.entries == null ) {
			this.entries = Maps.newEnumMap( ProviderID.class );
		}

		PlaylistProvider provider = this.entries.get( id );

		if ( provider == null ) {
			switch ( id ) {
			case LOCAL:
				provider = new LocalPlaylistProvider();
				break;
			}

			this.entries.put( id, provider );
		}

		return provider;
	}

	/**
	 * Returns all available PlaylistProvider:s.
	 *
	 * @return all available PlaylistProvider:s.
	 */
	public PlaylistProvider[] getAll() {
		ProviderID[] ids = ProviderID.values();
		PlaylistProvider[] providers = new PlaylistProvider[ids.length];

		for ( int i = 0; i < ids.length; ++i ) {
			providers[i] = this.getProvider( ids[i] );
		}

		return providers;
	}
}
