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

package se.toxbee.sleepfighter.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.common.base.Objects;

import java.util.List;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.audio.playlist.Playlist;

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
