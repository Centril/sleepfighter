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

package se.toxbee.sleepfighter.utils.geom;

/**
 * Dimension represents an object that contains the integer sizes of various dimensions.<br/>
 * The dimension is at least 3-dimensional.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 4, 2013
 */
public interface Dimension3 extends Dimension {
	/**
	 * Returns the 2:th dimension size, AKA z-axis size.<br/>
	 * The same as {@link #size(int)} with 2.
	 *
	 * @return the depth.
	 */
	public int depth();

	/**
	 * Alias of {@link #depth()}.
	 */
	public int getDepth();
}
