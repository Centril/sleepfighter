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

package se.toxbee.sleepfighter.android.utils;

import android.content.Context;

/**
 * ResourcesDynamicUtil provides utilities for dealing with resources in a dynamic way.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class ResourcesDynamicUtil {
	/**
	 * Returns an address in memory for name identifier given by a string.
	 *
	 * @param name the name/identifier given in XML or anywhere else.
	 * @param context android context.
	 * @return address in memory.
	 */
	public static int getStringId( String name, Context context ) {
		return getId( name, context, "string" );
	}

	/**
	 * Fetches a string from resources with identifier given by name.<br/>
	 * Throws exception if name wasn't found.
	 *
	 * @param name the name/identifier given in XML or anywhere else.
	 * @param context android context.
	 * @return the string
	 */
	public static String getStringCheck( String name, Context context ) {
		String str = getString( name, context );
		if ( str == null ) {
			throw new IllegalArgumentException( "No resource string found with name " + name );
		}
		return str;
	}

	/**
	 * Fetches a string from resources with identifier given by name.
	 *
	 * @param name the name/identifier given in XML or anywhere else.
	 * @param context android context.
	 * @return the string, or null if not found.
	 */
	public static String getString( String name, Context context ) {
		int nameResourceID = getStringId( name, context );
		return nameResourceID == 0 ? null : context.getString( nameResourceID );
	}

	/**
	 * Returns an address in memory for name identifier given by a string.
	 *
	 * @param name the name/identifier given in XML or anywhere else.
	 * @param context android context.
	 * @return address in memory.
	 */
	public static int getId( String name, Context context ) {
		return getId( name, context, "id" );
	}

	/**
	 * Returns an address in memory for name identifier given by a string.
	 *
	 * @param name the name/identifier given in XML or anywhere else.
	 * @param context android context.
	 * @param type the type of the identifier.
	 * @return address in memory.
	 */
	public static int getId( String name, Context context, String type ) {
		return context.getResources().getIdentifier( name, type, context.getApplicationInfo().packageName );
	}
}