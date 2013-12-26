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
package se.toxbee.sleepfighter.utils.prefs;

/**
 * <p>{@link PreferenceManager} is the<br/>
 * root node for {@link PreferenceNode}s.</p>
 *
 * <p>Client API should not rely on this interface.<br/>
 * It is meant for children of the root.</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public interface PreferenceManager extends PreferenceNode {
	/**
	 * <strong>NOTE: Only for children of {@link PreferenceManager} and internal use.</strong>
	 *
	 * @param node the node to pass to callback.
	 * @param cb the callback to apply atomically.
	 * @return true for success.
	 */
	public PreferenceManager _apply( PreferenceNode node, PreferenceEditCallback cb );

	/**
	 * <strong>NOTE: Only for children of {@link PreferenceManager} and internal use.</strong>
	 *
	 * @param node the node to pass to callback.
	 * @param cb the callback to apply atomically.
	 * @return this.
	 */
	public boolean _applyForResult( PreferenceNode node, PreferenceEditCallback cb );
}
