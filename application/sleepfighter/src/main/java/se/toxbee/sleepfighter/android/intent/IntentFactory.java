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
package se.toxbee.sleepfighter.android.intent;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * <p>IntentFactory provides an application wide mechanism for<br/>
 * binding a key to a class and getting an intent for that class.</p>
 *
 * <p>The raison d'etre for the class is for communication between<br/>
 * activities, services and receivers without hard-coding class objects in intents.</p>
 *
 * <p>The standard way of use is to provide a static initializer block<br/>
 * in each class that provides an intent.</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 18, 2013
 */
public class IntentFactory {
	private static final Map<String, Class<?>> mappings = Maps.newHashMap();

	/**
	 * Registers a class to a given key.
	 *
	 * @param key the key to associate with class.
	 * @param clazz the class object.
	 */
	public static final void register( String key, Class<?> clazz ) {
		mappings.put( Preconditions.checkNotNull( key ), Preconditions.checkNotNull( clazz ) );
	}

	/**
	 * Unregisters a previously registered key.
	 *
	 * @param key the key.
	 */
	public static final void unregister( String key ) {
		mappings.remove( Preconditions.checkNotNull( key ) );
	}

	/**
	 * Produces an intent object for a given key given a context.
	 *
	 * @param key the key to make intent for.
	 * @param ctx android context.
	 * @return the intent object.
	 */
	public static final Intent get( String key, Context ctx ) {
		Class<?> clazz = mappings.get( Preconditions.checkNotNull( key ) );
		if ( clazz == null ) {
			throw new IllegalArgumentException( "The provided key is not bound to any class." );
		}

		Intent intent = new Intent( ctx, clazz );
		return intent;
	}

	/**
	 * Produces an intent object for a given key given a context.
	 *
	 * @param key the key to make intent for.
	 * @param ctx android context.
	 * @param extras an extras bundle to put in intent.
	 * @return the intent object.
	 */
	public static final Intent get( String key, Context ctx, Bundle extras ) {
		Intent intent = get( key, ctx );
		intent.putExtras( extras );

		return intent;
	}
}