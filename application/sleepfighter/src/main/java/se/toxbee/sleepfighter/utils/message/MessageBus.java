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

package se.toxbee.sleepfighter.utils.message;

import android.util.Log;
import net.engio.mbassy.IPublicationErrorHandler;
import net.engio.mbassy.PublicationError;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.MBassador;

/**
 * The message bus for handling events.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since September 19, 2013
 */
public class MessageBus<T extends Message> extends MBassador<T> {

	private static final String TAG = MessageBus.class.getSimpleName();
	
	
	/**
	 * Forward exceptions gotten during publish to Logcat. 
	 */
	private static class LogCatErrorHandler implements IPublicationErrorHandler {

		@Override
		public void handleError(PublicationError error) {
			Log.e(TAG, error.getMessage(), error.getCause());
		}
		
	}
	
	/**
	 * Constructs bus with default bus config.
	 */
	public MessageBus() {
		this( BusConfiguration.Default() );

		// Custom handler needed so that exceptions thrown during publish won't
		// be swallowed
		addErrorHandler(new LogCatErrorHandler());
	}

	/**
	 * Constructs bus with given bus config.
	 *
	 * @param config the bus config.
	 */
	public MessageBus( BusConfiguration config ) {
		super( config );
	}

	
	/**
	 * Creates a new message bus.
	 *
	 * @return the new message bus.
	 */
	public static <T extends Message> MessageBus<T> create() {
		return new MessageBus<T>();
	}

	/**
	 * Creates a new message bus with given config.
	 *
	 * @param config the bus config.
	 * @return the new message bus.
	 */
	public static <T extends Message> MessageBus<T> create( BusConfiguration config ) {
		return new MessageBus<T>( config );
	}
}
