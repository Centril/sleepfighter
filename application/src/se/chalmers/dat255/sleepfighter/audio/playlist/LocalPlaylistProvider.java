package se.chalmers.dat255.sleepfighter.audio.playlist;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Playlists;
import android.util.Log;

import com.google.common.collect.Lists;

public class LocalPlaylistProvider extends BasePlaylistProvider {
	private final static String TAG = LocalPlaylistProvider.class.getSimpleName();

	private static final int ID = 1;

	private static final Uri URI = Playlists.EXTERNAL_CONTENT_URI;
	private static final String ID_COLUMN = Playlists._ID;
	private static final String NAME_COLUMN = Playlists.NAME;

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public Uri getUri() {
		return URI;
	}

	@Override
	protected String getIdColumn() {
		return ID_COLUMN;
	}

	@Override
	protected String getNameColumn() {
		return NAME_COLUMN;
	}

	@Override
	public Uri getUriFor( Playlist pl ) {
		return Playlists.Members.getContentUri( "external", pl.getId() );
	}

	@Override
	public Playlist getPlaylistFor( Context context, String playlistUri ) {
		// The playlist has the following URI format: URI/ID/members
		String value = playlistUri.substring( 0, playlistUri.lastIndexOf( '/' ) );
		value = value.substring( value.lastIndexOf( '/' ) + 1 );

		Log.d( TAG, "getPlaylistFor is given ID: " + value );

		int id = Integer.parseInt( value );

		List<Playlist> list = this.fetchPlaylistsImpl( this.getPlaylistCursor( context, ID_COLUMN + " = " + id ) );
		if ( list == null || list.size() != 1) {
			return null;
		}

		Playlist pl = list.get( 0 );
		pl.setUri( Uri.parse( playlistUri ) );
		return pl;
	}

	@Override
	public List<Track> getTracks( Context context, Playlist pl ) {
		return this.getTracksImpl( context, pl.getUri(), true );
	}

	@Override
	public List<Track> getTracksSkipMeta( Context context, Playlist pl ) {
		return this.getTracksImpl( context, pl.getUri(), false );
	}

	@Override
	public List<Track> getTracks( Context context, String playlistUri ) {
		return this.getTracksImpl( context, Uri.parse( playlistUri ), true );
	}

	@Override
	public List<Track> getTracksSkipMeta( Context context, String playlistUri ) {
		return this.getTracksImpl( context, Uri.parse( playlistUri ), false );
	}

	private List<Track> getTracksImpl( Context context, Uri playlistUri, boolean meta ) {
		if ( playlistUri == null ) {
			Log.e( TAG, "Encountered null Playlist Uri" );
		}

		String[] columns;

		if ( meta ) {
			columns = new String[] {
				Playlists.Members.AUDIO_ID,
				Playlists.Members.DATA,
				Playlists.Members.TITLE,
			};
		} else {
			columns = new String[] {
				Playlists.Members.AUDIO_ID,
				Playlists.Members.DATA,
			};
		}

		Cursor cursor = context.getContentResolver().query( playlistUri, columns, null, null, null );
		if ( cursor == null ) {
			return null;
		}

		int idColumn = cursor.getColumnIndex( Playlists.Members.AUDIO_ID );
		int dataColumn = cursor.getColumnIndex( Playlists.Members.DATA );
		int titleColumn = meta ? cursor.getColumnIndex( Playlists.Members.TITLE ) : -1;

		Log.i( TAG, "playlist: " + playlistUri );

		List<Track> tracks = Lists.newArrayListWithCapacity( cursor.getCount() );

		if ( cursor.getCount() == 0 ) {
			Log.i( TAG, "no tracks for playlist_id: " + playlistUri );
		} else {
			for ( boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext() ) {
				int id = cursor.getInt( idColumn );
				String data = cursor.getString( dataColumn );
				String title = meta ? cursor.getString( titleColumn ) : null;

				Track track = new Track( id, title, data, this );
				tracks.add( track );

				Log.i( TAG, track.toString() );
			}
		}

		return tracks;
	}
}