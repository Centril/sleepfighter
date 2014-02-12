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

package se.toxbee.sleepfighter.android.intent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;

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