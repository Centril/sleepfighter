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
package se.toxbee.sleepfighter.audio.playlist;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.common.collect.Lists;

public abstract class BasePlaylistProvider implements PlaylistProvider {
	private final static String TAG = BasePlaylistProvider.class.getSimpleName();

	/**
	 * <p>The default behavior is to check if the playlistUri starts with {@link #getUri()}.</p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean isProviderFor( String playlistUri ) {
		return playlistUri.startsWith( this.getUri().toString() );
	}

	@Override
	public List<Playlist> fetchPlaylists( Context context ) {
		return this.fetchPlaylistsImpl( this.getPlaylistCursor( context, null ) );
	}

	/**
	 * Implementation for {@link #fetchPlaylists(Context)}, allows for providing a different cursor.
	 *
	 * @param cursor the cursor to use.
	 * @return the list of playlists.
	 */
	protected List<Playlist> fetchPlaylistsImpl( Cursor cursor ) {
		if ( cursor == null ) {
			return null;
		}

		// Log a list of the playlists.
		Log.i( TAG, "Playlists for provider: " + this.getUri() );

		List<Playlist> list = Lists.newArrayList();
		for ( boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext() ) {
			int id = cursor.getInt( cursor.getColumnIndex( this.getIdColumn() ) );
			String name = cursor.getString( cursor.getColumnIndex( this.getNameColumn() ) );

			Playlist pl = new Playlist( id, name, this );
			list.add( pl );
			Log.i( TAG, pl.toString() );
		}

		return list;
	}

	public String toString() {
		return this.getUri().toString();
	}

	/**
	 * Returns the ID column in the SQL query used in {@link #getPlaylistCursor(Context)}.
	 *
	 * @return the ID column
	 */
	protected abstract String getIdColumn();

	/**
	 * Returns the name column in the SQL query used in {@link #getPlaylistCursor(Context)}.
	 *
	 * @return the name column
	 */
	protected abstract String getNameColumn();

	/**
	 * <p>Returns the WHERE clause (the WHERE itself excluded)<br/>
	 * in the SQL query used in {@link #getPlaylistCursor(Context)}.</p>
	 * <p>The default behavior is not to provide a WHERE-clause (null).</p>
	 *
	 * @return the WHERE clause.
	 */
	protected String getWhereClause() {
		return null;
	}

	/**
	 * Returns the cursor used to fetch the playlists.
	 *
	 * @param context android context.
	 * @param whereClause inline WHERE clause, overrides {@link #getWhereClause()}
	 * @return the cursor.
	 */
	protected Cursor getPlaylistCursor( Context context, String whereClause ) {
		String[] columns = new String[] { this.getIdColumn(), this.getNameColumn() };
		final ContentResolver resolver = context.getContentResolver();
		return resolver.query( this.getUri(), columns, whereClause == null ? this.getWhereClause() : whereClause, null, null );
	}
}
