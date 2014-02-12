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
