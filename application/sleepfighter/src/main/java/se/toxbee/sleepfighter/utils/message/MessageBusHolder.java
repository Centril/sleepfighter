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

/**
 * MessageBusHolder is an object to which you can set and get a MessageBus from.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 18, 2013
 */
public interface MessageBusHolder {
	/**
	 * Sets the message bus, if not set, no events will be received.
	 *
	 * @param bus the bus that receives events.
	 */
	public void setMessageBus( MessageBus<Message> bus );

	/**
	 * Returns the message bus, or null if not set.
	 *
	 * @return the message bus.
	 */
	public MessageBus<Message> getMessageBus();
}