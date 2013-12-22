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
package se.toxbee.sleepfighter.adapter;

import java.util.List;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.activity.ManageGPSFilterAreasActivity;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import se.toxbee.sleepfighter.model.gps.GPSFilterMode;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

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