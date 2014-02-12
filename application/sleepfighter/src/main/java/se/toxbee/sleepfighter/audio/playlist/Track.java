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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import se.toxbee.sleepfighter.utils.model.IdProvider;

/**
 * Track models a track in a Playlist.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 28, 2013
 */
public class Track implements IdProvider {
	private final int id;
	private final String title;
	private final String data;
	private final PlaylistProvider provider;

	/**
	 * Constructs a track.
	 *
	 * @param id the id (AUDIO_ID).
	 * @param title the human readable title
	 * @param data the data.
	 */
	public Track( int id, String title, String data, PlaylistProvider provider ) {
		this.id = id;
		this.title = Preconditions.checkNotNull( title );
		this.data = Preconditions.checkNotNull( data );
		this.provider = Preconditions.checkNotNull( provider );
	}

	@Override
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the human readable title of this track.
	 *
	 * @return the title.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Returns the data of this track.<br/>
	 * The data can be set to a MediaPlayer via<br/>
	 * <code>MediaPlayer.setDataSource({@link #getData()})</code>
	 *
	 * @return the data.
	 */
	public String getData() {
		return this.data;
	}

	/**
	 * Returns the provider of this track.
	 *
	 * @return the provider.
	 */
	public PlaylistProvider getProvider() {
		return this.provider;
	}

	public String toString() {
		return Objects.toStringHelper( this.getClass() )
			.add( "id", this.id )
			.add( "title", this.title )
			.add( "data", this.data )
			.add( "provider", this.provider ).toString();
	}

	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}

		if ( obj == null || obj.getClass() != this.getClass() ) {
			return false;
		}

		Track rhs = (Track) obj;
		return this.getData().equals( rhs.getData() );
	}
}
