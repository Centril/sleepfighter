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
		// TODO
	}

	@Override
	public void stop() {
		// TODO
	}
}