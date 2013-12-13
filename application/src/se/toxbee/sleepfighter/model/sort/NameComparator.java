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
package se.toxbee.sleepfighter.model.sort;

import se.toxbee.sleepfighter.model.Alarm;

/**
 * NameComparator implements {@link AlarmComparator} performing<br/>
 * a lexicographical comparison on {@link Alarm#printName()}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 12, 2013
 */
public class NameComparator implements AlarmComparator {
	@Override
	public int compare( Alarm lhs, Alarm rhs ) {
		String lName = lhs.printName();
		String rName = rhs.printName();

		return lName.compareTo( rName );
	}
}
