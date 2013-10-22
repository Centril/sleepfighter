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
package se.toxbee.sleepfighter.android.resources;

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
	public static int getResourceStringId( String name, Context context ) {
		return context.getResources().getIdentifier( name, "string", context.getApplicationInfo().packageName );
	}

	/**
	 * Fetches a string from resources with identifier given by name.<br/>
	 * Throws exception if name wasn't found.
	 *
	 * @param name the name/identifier given in XML or anywhere else.
	 * @param context android context.
	 * @return the string
	 */
	public static String getResourceStringCheck( String name, Context context ) {
		String str = getResourceString( name, context );
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
	public static String getResourceString( String name, Context context ) {
		int nameResourceID = getResourceStringId( name, context );
		return nameResourceID == 0 ? null : context.getString( nameResourceID );
	}
}