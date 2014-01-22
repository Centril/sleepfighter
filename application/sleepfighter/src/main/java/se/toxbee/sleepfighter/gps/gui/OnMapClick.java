/*
 * Copyright (c) 2014. See AUTHORS file.
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
 */
package se.toxbee.sleepfighter.gps.gui;

import se.toxbee.sleepfighter.model.gps.GPSLatLng;

/**
 * Interface for map click listeners.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan, 15, 2014
 */
public interface OnMapClick {
	/**
	 * Invoked when the map is clicked.
	 *
	 * @param loc the location that was clicked in lat long coordinates.
	 * @return true if the click was handled.
	 */
	public boolean onMapClick( GPSLatLng loc );
}
