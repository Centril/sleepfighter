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

import android.net.Uri;

import com.google.common.base.Objects;

import se.toxbee.sleepfighter.utils.model.IdProvider;

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
