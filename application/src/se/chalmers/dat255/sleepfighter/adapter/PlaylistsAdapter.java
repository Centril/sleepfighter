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
package se.chalmers.dat255.sleepfighter.adapter;

import java.util.List;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.audio.playlist.Playlist;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.common.base.Objects;

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
