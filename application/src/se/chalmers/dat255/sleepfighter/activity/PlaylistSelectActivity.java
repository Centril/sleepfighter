package se.chalmers.dat255.sleepfighter.activity;

import java.util.List;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.adapter.PlaylistsAdapter;
import se.chalmers.dat255.sleepfighter.audio.playlist.Playlist;
import se.chalmers.dat255.sleepfighter.audio.playlist.PlaylistProvider;
import se.chalmers.dat255.sleepfighter.audio.playlist.PlaylistProviderFactory;
import se.chalmers.dat255.sleepfighter.audio.playlist.PlaylistProviderFactory.ProviderID;
import se.chalmers.dat255.sleepfighter.audio.playlist.Track;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.common.collect.Lists;

/**
 * PlaylistSelectActivity allows the user to select a playlist from device.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 28, 2013
 */
public class PlaylistSelectActivity extends Activity {
	public final static String SELECTED_URI_EXTRAS = "selected_uri";

	private PlaylistProviderFactory factory;

	private List<Playlist> playlists;

	private PlaylistsAdapter listAdapter;

	
	private Uri selectedUri;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.activity_playlist_select );

		this.factory = new PlaylistProviderFactory();

		this.playlists = this.fetchPlaylists();

		this.readSelectedUri();

		this.setupListView();
	}

	/**
	 * Reads the to-activity passed URI if available.
	 */
	private void readSelectedUri() {
		String uri = this.getIntent().getStringExtra( SELECTED_URI_EXTRAS );
		if ( uri != null ) {
			this.selectedUri = Uri.parse( uri );
		}
	}

	/**
	 * Sets up the list view of playlists.
	 */
	private void setupListView() {
		this.listAdapter = new PlaylistsAdapter( this, playlists );
		this.listAdapter.setSelectedUri( this.selectedUri );

		ListView listView = (ListView) this.findViewById( R.id.playlist_select_list );
		listView.setAdapter( this.listAdapter );
		listView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				playlistSelected( position );
			}
		});
	}

	/**
	 * Called when a playlist has been selected by User.
	 *
	 * @param playlistIndex the index of playlist in list.
	 */
	private void playlistSelected( int playlistIndex ) {
		Playlist pl = this.playlists.get( playlistIndex );
		Uri data = pl.getUri();

		Intent intent = new Intent();
		intent.setData( data );

		this.setResult( Activity.RESULT_OK, intent );
		this.finish();
	}

	/**
	 * Fetches the list of playlists in device.
	 *
	 * @return all playlists on device.
	 */
	private List<Playlist> fetchPlaylists() {
		List<Playlist> playlists = Lists.newArrayList();

		// Read all from local mediastore.
		PlaylistProvider localProvider = this.factory.getProvider( ProviderID.LOCAL );
		List<Playlist> localList = localProvider.fetchPlaylists( this );

		if ( localList != null ) {
			playlists.addAll( localList );
		}

		/*
		 * google play music seems not to provide a decent API or way to get tracks at all
		 * out-commented until they do or we find a fix.
		 *
		// Read all from google play music.
		PlaylistProvider googleProvider = this.factory.getProvider( ProviderID.GOOGLE );
		List<Playlist> googleList = this.readFromProvider( googleProvider );

		if ( googleList != null ) {
			if ( localList == null ) {
				playlists.addAll( googleList );
			} else {
				// Filter googleList to get "set" difference of googleList & localList.
				this.computeDifference( googleList, localList );
				playlists.addAll( googleList );
			}
		}
		*/

		return playlists;
	}

	/**
	 * Computes difference in two playlists by comparing their tracks.<br/>
	 * The difference is stored in the first list.
	 *
	 * @param first the first list of playlists.
	 * @param second the second list of playlists.
	 */
	@SuppressWarnings( "unused" )
	private void computeDifference( List<Playlist> first, List<Playlist> second ) {
		List<List<Track>> secondTracks = this.fetchTracksFor( second );
		List<List<Track>> firstTracks = this.fetchTracksFor( first );

		for ( int i = 0; i < first.size(); ++i ) {
			for ( int j = 0; j < second.size(); ++j ) {
				// If they have the same tracks, they are considered equal.
				if ( firstTracks.get( i ).containsAll( secondTracks.get( j ) ) ) {
					first.remove( i );
				}
			}
		}
	}

	/**
	 * Fetches a list of list of all tracks for all playlists in list.
	 *
	 * @param list list of playlists.
	 * @return the list of list of all tracks.
	 */
	private List<List<Track>> fetchTracksFor( List<Playlist> list ) {
		List<List<Track>> tracks = Lists.newArrayListWithCapacity( list.size() );
		for ( Playlist pl : list ) {
			tracks.add( pl.getProvider().getTracks( this, pl ) );
		}

		return null;
	}
}