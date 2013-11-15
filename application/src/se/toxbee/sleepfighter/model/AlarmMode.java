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
package se.toxbee.sleepfighter.model;

import se.toxbee.sleepfighter.utils.model.Codifiable;

/**
 * AlarmMode enumerates the exclusive modes an alarm can be in.<br/>
 * These modes decide what {@link Alarm#getTime()} means.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 14, 2013
 */
public enum AlarmMode implements Codifiable {
	NORMAL, REPEATING, COUNTDOWN;

	@Override
	public int toCode() {
		switch ( this ) {
		case NORMAL:
		default:
			return 0;

		case REPEATING:
			return 1;

		case COUNTDOWN:
			return 2;
		}
	}

	public static class Factory implements Codifiable.Factory {
		@Override
		public AlarmMode produce( Integer key ) {
			switch ( key ) {
			case 0:
			default:
				return NORMAL;

			case 1:
				return REPEATING;

			case 2:
				return COUNTDOWN;
			}
		}
	}
}
