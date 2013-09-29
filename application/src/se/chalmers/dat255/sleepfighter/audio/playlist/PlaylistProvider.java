package se.chalmers.dat255.sleepfighter.audio.playlist;

import java.util.List;

import se.chalmers.dat255.sleepfighter.model.IdProvider;
import android.content.Context;
import android.net.Uri;

/**
 * PlaylistProvider defines a playlist provider.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 28, 2013
 */
public interface PlaylistProvider extends IdProvider {
	/**
	 * Returns true if the given playlistUri is handled<br/>
	 * by or was provided by this PlaylistProvider.
	 *
	 * @param playlistUri the URI of the playlist.
	 * @return true if this is the provider for the playlist.
	 */
	public boolean isProviderFor( String playlistUri );

	/**
	 * Returns the URI of playlist provider.
	 *
	 * @return the URI.
	 */
	public Uri getUri();

	/**
	 * Alias of {@link #getUri()}.toString()
	 */
	public String toString();

	/**
	 * Returns the URI for a playlist.
	 *
	 * @param context android context.
	 * @param pl the playlist.
	 * @return the URI.
	 */
	public Uri getUriFor( Playlist pl );

	/**
	 * Returns a Playlist object for a given URI.
	 *
	 * @param context android context.
	 * @param playlistUri the URI of playlist.
	 * @return the playlist.
	 */
	public Playlist getPlaylistFor( Context context, String playlistUri );

	/**
	 * Returns the list of tracks for a Playlist, skipping meta information such as title.
	 *
	 * @param context android context.
	 * @param pl the playlist.
	 * @return the list of tracks.
	 */
	public List<Track> getTracksSkipMeta( Context context, Playlist pl );

	/**
	 * Returns the list of tracks for a Playlist.
	 *
	 * @param context android context.
	 * @param pl the playlist.
	 * @return the list of tracks.
	 */
	public List<Track> getTracks( Context context, Playlist pl );

	/**
	 * Returns the list of tracks for a playlist URI, skipping meta information such as title.
	 *
	 * @param context android context.
	 * @param playlistUri the playlist URI.
	 * @return the list of tracks.
	 */
	public List<Track> getTracksSkipMeta( Context context, String playlistUri );

	/**
	 * Returns the list of tracks for a playlist URI.
	 *
	 * @param context android context.
	 * @param playlistUri the playlist URI.
	 * @return the list of tracks.
	 */
	public List<Track> getTracks( Context context, String playlistUri );

	/**
	 * Fetches the list of playlists this provider provides.
	 *
	 * @param context android context.
	 * @return list of playlists.
	 */
	public List<Playlist> fetchPlaylists( Context context );
}