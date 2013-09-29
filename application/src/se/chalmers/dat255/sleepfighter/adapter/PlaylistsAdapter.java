package se.chalmers.dat255.sleepfighter.adapter;

import java.util.List;

import com.google.common.base.Objects;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.audio.playlist.Playlist;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * PlaylistsAdapter displays items for PlaylistSelectActivity.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 29, 2013
 */
public class PlaylistsAdapter extends ArrayAdapter<Playlist> {
	private Uri selected;

	public PlaylistsAdapter(Context context, List<Playlist> playlists) {
		super(context, 0, playlists);
	}

	public void setSelectedUri( Uri uri ) {
		this.selected = uri;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// A view isn't being recycled, so make a new one from definition
			convertView = LayoutInflater.from(getContext()).inflate( R.layout.playlist_select_item, null);
		}

		final Playlist pl = this.getItem( position );

		if ( Objects.equal( this.selected, pl.getUri() ) ) {
			convertView.setBackgroundColor( this.getContext().getResources().getColor( R.color.background_holo_dark ) );
		}

		final TextView timeTextView = (TextView) convertView.findViewById( R.id.playlist_select_item_title );
		timeTextView.setText( pl.getName() );
		
		return convertView;
	}
}