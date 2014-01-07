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