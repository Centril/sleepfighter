package se.chalmers.dat255.sleepfighter.audio.playlist;

import se.chalmers.dat255.sleepfighter.model.IdProvider;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

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