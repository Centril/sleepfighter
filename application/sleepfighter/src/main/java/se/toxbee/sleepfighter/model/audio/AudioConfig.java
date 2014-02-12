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

package se.toxbee.sleepfighter.model.audio;

import se.toxbee.sleepfighter.utils.message.Message;
import se.toxbee.sleepfighter.utils.message.MessageBus;
import se.toxbee.sleepfighter.utils.message.MessageBusHolder;
import se.toxbee.sleepfighter.utils.model.IdProvider;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * AudioConfig models data per Alarm such as volume, etc.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
@DatabaseTable(tableName = "audio_config")
public class AudioConfig implements IdProvider, MessageBusHolder {
	public static final String ID_COLUMN = "id";

	@DatabaseField(generatedId = true, columnName = ID_COLUMN)
	private int id;

	@DatabaseField
	private int volume;
	
	@DatabaseField
	private boolean vibrationEnabled;
	
	// TODO: REMOVE when real fields are added, NEEDED 'cause SQLite crashes otherwise.
	@DatabaseField
	private String temp;

	private MessageBus<Message> messageBus;
	
	/**
	 * Constructs an AudioConfig, for DB purposes only.
	 */
	public AudioConfig() {
	}

	/**
	 * Constructs an AudioConfig
	 * 
	 * @param volume the volume (0-100)
	 */
	public AudioConfig(int volume, boolean vibrationEnabled) {
		this.volume = volume;
		this.vibrationEnabled = vibrationEnabled;
	}
	
	/**
	 * Copy constructor.
	 *
	 * @param audioConfig the config to copy from.
	 */
	public AudioConfig( AudioConfig rhs ) {
		this.volume = rhs.getVolume();
		this.vibrationEnabled = rhs.getVibrationEnabled();
	}

	/**
	 * Sets the message bus to publish events to.<br/>
	 *
	 * @param messageBus the message bus, or null if no messages should be received.
	 */
	public void setMessageBus( MessageBus<Message> messageBus ) {
		this.messageBus = messageBus;
	}
	
	/**
	 * Returns the currently used message bus or null if none.
	 *
	 * @return the message bus.
	 */
	public MessageBus<Message> getMessageBus() {
		return this.messageBus;
	}
	
	/**
	 * Returns the id of the AudioConfig (in DB).
	 *
	 * @return the id.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * @return the volume of this AudioConfig (0-100)
	 */
	public int getVolume() {
		return volume;
	}
	
	/**
	 * @param volume the volume of this AudioConfig (0-100)
	 */
	public void setVolume(int volume) {
		int old = this.volume;
		if (volume < 0 || volume > 100) {
			throw new IllegalArgumentException();
		}
		this.volume = volume;
		
		if (old != volume) {
			publish(new ChangeEvent(this, Field.VOLUME, old));
		}
	}
	
	/**
	 * @return true if vibration is enabled, false otherwise
	 */
	public boolean getVibrationEnabled() {
		return this.vibrationEnabled;
	}

	/**
	 * 
	 * @param vibrationEnabled true to enable vibration
	 */
	public void setVibrationEnabled(boolean vibrationEnabled) {
		boolean old = this.vibrationEnabled;
		this.vibrationEnabled = vibrationEnabled;
		
		if (old != vibrationEnabled) {
			publish(new ChangeEvent(this, Field.VIBRATION, old));
		}
	}
	
	/**
	 * Publishes an event to event bus.
	 *
	 * @param event the event to publish.
	 */
	private void publish( ChangeEvent event ) {
		if ( this.messageBus == null ) {
			return;
		}

		this.messageBus.publish( event );
	}
	
	/**
	 * Enum of the fields in AudioConfig
	 * 
	 * @author Hassel
	 *
	 */
	private enum Field {
		VIBRATION, VOLUME
	}
	
	/**
	 * All changes in {@link AudioConfig} will be published with this event
	 * 
	 * @author Hassel
	 *
	 */
	public static class ChangeEvent implements Message {
		private Field field;
		private AudioConfig audioConfig;
		private Object oldValue;

		private ChangeEvent(AudioConfig audioConfig, Field field, Object oldValue ) {
			this.audioConfig = audioConfig;
			this.field = field;
			this.oldValue = oldValue;
		}

		public AudioConfig getAudioConfig() {
			return this.audioConfig;
		}

		public Field getModifiedField() {
			return this.field;
		}

		public Object getOldValue() {
			return this.oldValue;
		}
	}
}
