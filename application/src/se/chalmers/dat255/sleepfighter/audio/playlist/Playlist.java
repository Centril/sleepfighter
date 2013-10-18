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

import se.chalmers.dat255.sleepfighter.utils.model.IdProvider;
import android.net.Uri;

import com.google.common.base.Objects;

/**
 * Playlist models playlist information.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 28, 2013
 */
public class Playlist implements IdProvider {
	private final int id;
	private final String name;
	private final PlaylistProvider provider;
	private Uri uri;

	public Playlist( int id, String name, PlaylistProvider provider ) {
		this.id = id;
		this.name = name;
		this.provider = provider;
	}

	public int getId() {
		return this.id;
	}

	/**
	 * Returns the human readable name of the playlist.
	 *
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the URI of the playlist.
	 *
	 * @return the URI.
	 */
	public Uri getUri() {
		if ( this.uri == null ) {
			this.uri = this.getProvider().getUriFor( this );
		}

		return this.uri;
	}

	/**
	 * Sets the URI of the playlist.
	 *
	 * @param uri the URI to set.
	 */
	public void setUri( Uri uri ) {
		this.uri = uri;
	}

	/**
	 * Returns the PlaylistProvider of this playlist.
	 *
	 * @return the provider.
	 */
	public PlaylistProvider getProvider() {
		return this.provider;
	}

	public String toString() {
		return Objects.toStringHelper( this.getClass() )
			.add( "id", this.id )
			.add( "name", this.name )
			.add( "provider", this.provider ).toString();
	}

	/**
	 * <p>Two playlists are equal if {@link #getUri()} is equal for both.</p>
	 * {@inheritDoc}
	 */
	public boolean equals( Object obj ) {
		return this == obj || (obj != null && obj.getClass() == this.getClass() && this.getUri().equals( ((Playlist) obj).getUri() ) );
	}
}
