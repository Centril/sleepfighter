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
