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

package se.toxbee.sleepfighter.model.gps;

import java.util.ArrayList;
import java.util.List;

import se.toxbee.sleepfighter.utils.collect.IdObservableList;

/**
 * GPSFilterAreaSet defines a set of GPSFilterArea:s that an alarm has.<br/>
 * It is really a list, but a list of conceptually unique elements.<br/>
 * It is your responsibility to keep it so.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class GPSFilterAreaSet extends IdObservableList<GPSFilterArea> {
	/**
	 * Default constructor.
	 */
	public GPSFilterAreaSet() {
		this( new ArrayList<GPSFilterArea>() );
	}

	/**
	 * Constructs the set given a list of areas to filter.
	 *
	 * @param areas the areas.
	 */
	public GPSFilterAreaSet( List<GPSFilterArea> areas ) {
		this.setDelegate( areas );
	}

	/**
	 * Returns whether or not there's an area that is both valid and enabled.
	 *
	 * @return true if there's one.
	 */
	public boolean hasEnabledAndValid() {
		for ( GPSFilterArea area : this ) {
			if ( area.isEnabled() && area.isValid() ) {
				return true;
			}
		}

		return false;
	}
}