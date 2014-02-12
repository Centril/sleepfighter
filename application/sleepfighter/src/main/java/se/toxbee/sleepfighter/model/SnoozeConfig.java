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

package se.toxbee.sleepfighter.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import se.toxbee.sleepfighter.utils.message.Message;
import se.toxbee.sleepfighter.utils.message.MessageBus;
import se.toxbee.sleepfighter.utils.message.MessageBusHolder;
import se.toxbee.sleepfighter.utils.model.IdProvider;

/**
 * The snooze configuration for an alarm.
 */
@DatabaseTable(tableName = "snooze_config")
public class SnoozeConfig implements IdProvider, MessageBusHolder {

	/**
	 * Field constants for ChangeEvent.
	 */
	public static enum Field {
		SNOOZE_ENABLED, SNOOZE_TIME
	}

	/**
	 * An message bus message for a change in SnoozeConfig.
	 */
	public static class ChangeEvent implements Message {

		private SnoozeConfig snoozeConfig;
		private Field field;
		private Object oldValue;

		public ChangeEvent(SnoozeConfig snoozeConfig, Field field,
				Object oldValue) {
			this.snoozeConfig = snoozeConfig;
			this.field = field;
			this.oldValue = oldValue;
		}

		public SnoozeConfig getSnoozeConfig() {
			return snoozeConfig;
		}

		public Field getField() {
			return field;
		}

		public Object getOldValue() {
			return oldValue;
		}
	}

	public static final String ID_COLUMN = "id";

	@DatabaseField(generatedId = true, columnName = ID_COLUMN)
	private int id;

	@DatabaseField
	private int snoozeTime;

	@DatabaseField
	private boolean snoozeEnabled;

	private MessageBus<Message> bus;

	/**
	 * Empty constructor, for database use only.
	 */
	public SnoozeConfig() {}

	/**
	 * Constructs a SnoozeConfig.
	 * 
	 * @param snoozeEnabled
	 *            whether snooze is enabled or not
	 * @param snoozeTime
	 *            the time to snooze, in minutes
	 */
	public SnoozeConfig(boolean snoozeEnabled, int snoozeTime) {
		this.snoozeEnabled = snoozeEnabled;
		this.snoozeTime = snoozeTime;
	}

	public SnoozeConfig(SnoozeConfig snoozeConfig) {
		this.snoozeEnabled = snoozeConfig.isSnoozeEnabled();
		this.snoozeTime = snoozeConfig.getSnoozeTime();
	}

	@Override
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the configured snooze time, in minutes.
	 * 
	 * @return the snooze time in minutes
	 */
	public int getSnoozeTime() {
		return this.snoozeTime;
	}

	/**
	 * Sets the snooze time, in minutes
	 * 
	 * @param snoozeTime
	 *            the snooze time in minutes
	 */
	public void setSnoozeTime(int snoozeTime) {
		if (this.snoozeTime == snoozeTime) {
			return;
		}
		int old = snoozeTime;
		this.snoozeTime = snoozeTime;
		publish(new ChangeEvent(this, Field.SNOOZE_TIME, old));
	}

	/**
	 * Checks if snooze is enabled
	 * 
	 * @return if snooze is enabled
	 */
	public boolean isSnoozeEnabled() {
		return snoozeEnabled;
	}

	/**
	 * Set whether snooze is enabled or not.
	 * 
	 * @param snoozeEnabled
	 *            whether snooze is enabled or not
	 */
	public void setSnoozeEnabled(boolean snoozeEnabled) {
		if (this.snoozeEnabled == snoozeEnabled) {
			return;
		}
		boolean old = this.snoozeEnabled;
		this.snoozeEnabled = snoozeEnabled;
		publish(new ChangeEvent(this, Field.SNOOZE_ENABLED, old));
	}

	/**
	 * Sets the message bus.
	 * 
	 * @param bus
	 *            the message bus
	 */
	public void setMessageBus(MessageBus<Message> bus) {
		this.bus = bus;
	}

	@Override
	public MessageBus<Message> getMessageBus() {
		return this.bus;
	}

	/**
	 * Publish an event to the message bus.
	 * 
	 * @param event
	 *            the ChangeEvent
	 */
	private void publish(ChangeEvent event) {
		if (this.bus == null) {
			return;
		}
		this.bus.publish(event);
	}

}
