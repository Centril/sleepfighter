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
 * a UNIX epoch timestamp comparison on {@link Alarm#getNextMillis(long)}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 13, 2013
 */
public class TimestampComparator implements AlarmComparator {
	@Override
	public int compare( Alarm lhs, Alarm rhs ) {
		Long lv_boxed = lhs.scheduledTimestamp();
		Long rv_boxed = rhs.scheduledTimestamp();
		long lv = lv_boxed == null ? Long.MAX_VALUE : lv_boxed;
		long rv = rv_boxed == null ? Long.MAX_VALUE : rv_boxed;
		return (int) (lv - rv);
	}
}
