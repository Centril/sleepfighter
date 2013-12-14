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
package se.toxbee.sleepfighter.android.settings;

import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;

/**
 * SharedPreferenceNode is the {@link PreferenceNode} for shared preferences.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public interface SharedPreferenceNode extends PreferenceNode {
	/**
	 * Sets whether or not we are in auto-commit mode.
	 *
	 * @see #isAutoCommit()
	 * @param flag the mode.
	 * @return this.
	 */
	public SharedPreferenceNode setAutoCommit( boolean flag );
}
