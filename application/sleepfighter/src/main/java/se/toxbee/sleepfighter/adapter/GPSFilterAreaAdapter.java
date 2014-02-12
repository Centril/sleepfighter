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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.List;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.activity.ManageGPSFilterAreasActivity;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import se.toxbee.sleepfighter.model.gps.GPSFilterMode;

/**
 * GPSFilterAreaAdapter is the list adapter for {@link ManageGPSFilterAreasActivity}
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 10, 2013
 */
public class GPSFilterAreaAdapter extends ArrayAdapter<GPSFilterArea> {
	public GPSFilterAreaAdapter( Context context, List<GPSFilterArea> areas ) {
		super(context, 0, areas );
	}

	private static class ViewHolder {
		View mode;
		TextView name;
		CompoundButton activated;

		public ViewHolder( View cv ) {
			// Find all views needed.
			mode = cv.findViewById( R.id.manage_gpsfilter_area_mode_summary );
			name = (TextView) cv.findViewById( R.id.manage_gpsfilter_area_name );
			activated = (CompoundButton) cv.findViewById( R.id.manage_gpsfilter_area_enabled );
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			// A view isn't being recycled, so make a new one from definition
			convertView = LayoutInflater.from(getContext()).inflate( R.layout.gpsfilter_areas_list_item, null);

			// Make & store holder.
			holder = new ViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			// Recycle holder.
			holder = (ViewHolder) convertView.getTag();
		}

		// The area associated with the row
		final GPSFilterArea area = this.getItem( position );

		// Set properties of view elements to reflect model state
		this.setupEnabledSwitch( area, holder );
		this.setupName( area, holder );
		this.setupModeSummary( area, holder );
		
		return convertView;
	}

	private void setupModeSummary( final GPSFilterArea area, ViewHolder holder ) {
		int colorId = area.getMode() == GPSFilterMode.INCLUDE
					? R.color.gpsfilter_polygon_fill_include
					: R.color.gpsfilter_polygon_fill_exclude;

		holder.mode.setBackgroundColor( holder.mode.getResources().getColor( colorId ) );
	}

	private void setupName( final GPSFilterArea area, ViewHolder holder ) {
		holder.name.setText( area.printName() );
	}

	private void setupEnabledSwitch( final GPSFilterArea area, ViewHolder holder ) {
		// Makes sure that previous area for a recycled view won't get changed when setting initial value
		holder.activated.setOnCheckedChangeListener( null );
		holder.activated.setChecked( area.isEnabled() );
		holder.activated.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
				area.setEnabled( isChecked );
			}
		} );
	}
}