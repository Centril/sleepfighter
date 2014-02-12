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
package se.toxbee.sleepfighter.utils.model;

import se.toxbee.sleepfighter.utils.factory.FactoryInstantiator;

/**
 * A {@link CodifiableLong} is an object that can be converted to and from a long code.<br/>
 * The object must honor the following conditions:
 * <ul>
 *	<li>It must define at least one public default {@link Factory}.<br/>
 *	<li>Given a {@link Factory} of type C and instance c,<br/>
 *		<code>C.produce( c.toCode() ).equals( c ) == true</code>
 *	</li>
 * </ul>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 16, 2013
 */
public interface CodifiableLong {
	/**
	 * Converts the object to a long representation.
	 *
	 * @return the long code.
	 */
	public long toCodeLong();

	public static interface Factory extends FactoryInstantiator<Long, CodifiableLong> {
	}
}
