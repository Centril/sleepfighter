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
package se.toxbee.sleepfighter.text;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import android.content.res.Resources;

/**
 * Text utilities for GPSFilter feature.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 7, 2013
 */
public class GPSFilterTextUtils {
	/**
	 * Prints the name of a {@link GPSFilterArea} to human readable form.
	 *
	 * @param res android resources.
	 * @param name the name of the area.
	 * @return the name, printed.
	 */
	public static final String printName( Resources res, String name ) {
		return name == null || name.trim().equals( "" ) ? res.getString( R.string.edit_gpsfilter_area_unnamed ) : name;
	}
}