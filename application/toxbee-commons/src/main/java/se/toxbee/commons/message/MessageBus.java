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

package se.toxbee.commons.message;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.config.IBusConfiguration;

/**
 * The message bus for handling events.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since September 19, 2013
 */
public class MessageBus<T extends Message> extends MBassador<T> {
	private static final String TAG = MessageBus.class.getSimpleName();

	private static IBusConfiguration makeConfig() {
		return new BusConfiguration()
				.addFeature( Feature.SyncPubSub.Default() )
				.addFeature( Feature.AsynchronousHandlerInvocation.Default() )
				.addFeature( Feature.AsynchronousMessageDispatch.Default() );
	}

	/**
	 * Constructs bus with default bus config.
	 */
	public MessageBus() {
		this( makeConfig() );

	}

	/**
	 * Constructs bus with given bus config.
	 *
	 * @param config the bus config.
	 */
	public MessageBus( IBusConfiguration config ) {
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
