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
package se.chalmers.dat255.sleepfighter.model.gps;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.sleepfighter.utils.collect.ObservableList;

/**
 * ExclusionAreaSet defines a set of ExclusionArea:s that an alarm has.<br/>
 * It is really a list, but a list of conceptually unique elements.<br/>
 * It is your responsibility to keep it so.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class ExclusionAreaSet extends ObservableList<ExcludeArea> {
	/**
	 * Default constructor.
	 */
	public ExclusionAreaSet() {
		this( new ArrayList<ExcludeArea>() );
	}

	/**
	 * Constructs the set given a list of areas to exclude.
	 *
	 * @param areas the areas.
	 */
	public ExclusionAreaSet( List<ExcludeArea> areas ) {
		this.setDelegate( areas );
	}

	/**
	 * Returns true if any of the areas bound to Alarm has the given LatLng point.<br/>
	 * Worst case: O(n).
	 *
	 * @param point a LatLng point.
	 * @return true if the set contains the point.
	 */
	public boolean contains( LatLng point ) {
		for ( ExcludeArea area : this.delegate() ) {
			if ( area.isEnabled() && area.getPolygon().contains( point ) ) {
				return true;
			}
		}

		return false;
	}
}