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

package se.toxbee.sleepfighter.model.gps;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.toxbee.sleepfighter.utils.message.Message;
import se.toxbee.sleepfighter.utils.message.MessageBus;
import se.toxbee.sleepfighter.utils.message.MessageBusHolder;
import se.toxbee.sleepfighter.utils.model.IdProvider;
import se.toxbee.sleepfighter.utils.model.LocalizationProvider;
import se.toxbee.sleepfighter.utils.string.StringUtils;

/**
 * GPSFilterArea defines an exclusion area.<br/>
 * It is made up of a {@link GPSFilterPolygon}, name and id.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
@DatabaseTable(tableName = "gpsfilter_area")
public class GPSFilterArea implements IdProvider, MessageBusHolder {
	/* --------------------------------
	 * Defined Events.
	 * --------------------------------
	 */

	/**
	 * Field enumerates the defined model fields in {@link GPSFilterArea}
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Oct 6, 2013
	 */
	public enum Field {
		NAME, ENABLED, MODE, POLYGON
	}

	/**
	 * ChangeEvent is triggered when a model change occurs in a {@link GPSFilterArea}.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Oct 6, 2013
	 */
	public static class ChangeEvent implements Message {
		private GPSFilterArea area;
		private Field field;
		private Object old;

		/**
		 * Constructs a ChangeEvent.
		 */
		public ChangeEvent( GPSFilterArea area, Field modifiedField, Object oldValue ) {
			this.area = area;
			this.field = modifiedField;
			this.old = oldValue;
		}

		/**
		 * Returns the GPSFilterArea that triggered event.
		 *
		 * @return the area.
		 */
		public GPSFilterArea getArea() {
			return area;
		}

		/**
		 * Returns the Field instance describing what field was changed.
		 *
		 * @return the modified Field.
		 */
		public Field getModifiedField() {
			return field;
		}

		/**
		 * Returns the old value.
		 *
		 * @return the old value.
		 */
		public Object getOld() {
			return old;
		}
	}

	/* --------------------------------
	 * Fields.
	 * --------------------------------
	 */

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String name;

	@DatabaseField
	private boolean enabled;

	@DatabaseField
	private GPSFilterMode mode;

	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private GPSFilterPolygon poly;

	private MessageBus<Message> bus;

	/* --------------------------------
	 * Constructors.
	 * --------------------------------
	 */

	/**
	 * Default constructor
	 */
	public GPSFilterArea() {
	}

	/**
	 * Constructs an GPSFilterArea with name & enabled/disabled.<br/>
	 * The polygon will be null.
	 *
	 * @param name the user defined name of area.
	 * @param enabled whether or not the area is enabled.
	 */
	public GPSFilterArea( String name, boolean enabled, GPSFilterMode mode ) {
		this( name, enabled, mode, null );
	}

	/**
	 * Constructs an GPSFilterArea with name, enabled/disabled and a polygon.
	 *
	 * @param name the user defined name of area.
	 * @param enabled whether or not the area is enabled.
	 * @param poly the polygon to use.
	 */
	public GPSFilterArea( String name, boolean enabled, GPSFilterMode mode, GPSFilterPolygon poly ) {
		this.setName( name );
		this.setEnabled( enabled );
		this.setMode( mode );
		this.setPolygon( poly );
	}

	/* --------------------------------
	 * Public interface.
	 * --------------------------------
	 */

	/**
	 * Returns whether or not the area is valid, it is when it has a polygon.
	 *
	 * @return true if {@link #getPolygon()} != null.
	 */
	public boolean isValid() {
		return this.poly != null && this.poly.isValid();
	}

	/**
	 * Returns Whether or not the polygon contains the given GPSLatLng point.<br/>
	 * {@link #getPolygon()} may not return null before a call to {@link #contains(GPSLatLng)}.
	 *
	 * @see GPSFilterArea#contains(GPSLatLng)
	 * @param pos the point to check for.
	 * @return true if it contains the point, otherwise false.
	 */
	public boolean contains( GPSLatLng pos ) {
		return this.getPolygon().contains( pos );
	}

	@Override
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the user defined name of this area.
	 *
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * "Prints" the name of the Alarm - formatted, using unnamedFormat if name is empty.
	 *
	 * @param unnamedFormat the format to use when empty.
	 * @return the formatted name.
	 */
	public String printName( String unnamedFormat ) {
		return name == null || name.trim().equals( "" ) ? unnamedFormat : name;
	}

	/**
	 * "Prints" the name of the area using the format given by using the format key {@link Field#NAME} with {@link #getLocalizationProvider()}.
	 *
	 * @return the formatted name.
	 */
	public String printName() {
		return this.printName( this.getLocalizationProvider().format( Field.NAME ) );
	}

	private static LocalizationProvider localizationProvider;

	/**
	 * Sets the {@link LocalizationProvider} to use.
	 *
	 * @param provider the provider to use.
	 */
	public static void setLocalizationProvider( LocalizationProvider provider ) {
		localizationProvider = Preconditions.checkNotNull( provider );
	}

	public LocalizationProvider getLocalizationProvider() {
		return localizationProvider;
	}

	public GPSFilterMode getMode() {
		return this.mode;
	}

	/**
	 * Returns the polygon that defines this area.
	 *
	 * @return the polygon, or null if not in memory yet.
	 */
	public GPSFilterPolygon getPolygon() {
		return this.poly;
	}

	/**
	 * Returns the points that defines this area, an empty list if {@link #getPolygon()} yields null.
	 *
	 * @return a list of points, changes to this list will not be reflected in the area.
	 */
	public List<GPSLatLng> getPoints() {
		GPSFilterPolygon poly = this.getPolygon();
		return poly == null ? new ArrayList<GPSLatLng>() : poly.getPoints();
	}

	/**
	 * Returns whether or not the area is enabled.
	 *
	 * @return true if it is enabled.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Sets the name of the area.
	 *
	 * @param name the name.
	 */
	public void setName( String name ) {
		if ( Objects.equal(  this.name, name ) ) {
			return;
		}

		String old = name;
		this.name = name;

		this.publish( new ChangeEvent( this, Field.NAME, old ) );
	}

	/**
	 * Sets whether or not the area is enabled.
	 *
	 * @param enabled true if it should be enabled, false otherwise.
	 */
	public void setEnabled( boolean enabled ) {
		if ( this.enabled == enabled ) {
			return;
		}

		boolean old = this.enabled;
		this.enabled = enabled;

		this.publish( new ChangeEvent( this, Field.ENABLED, old ) );
	}

	/**
	 * Changes the GPSMode the area operates with.
	 *
	 * @param mode
	 * @return true if the mode was change, or false.
	 */
	public boolean setMode( GPSFilterMode mode ) {
		if ( this.mode == mode ) {
			return false;
		}

		GPSFilterMode old = this.mode;
		this.mode = mode;

		this.publish( new ChangeEvent( this, Field.MODE, old ) );

		return true;
	}

	/**
	 * Sets the GPSFilterPolygon of this area.<br/>
	 * Note: Don't modify the GPSFilterPolygon returned by {@link #getPolygon()} if events are to be handled.
	 *
	 * @param poly the polygon.
	 */
	public void setPolygon( GPSFilterPolygon poly ) {
		if ( Objects.equal( this.poly, poly ) ) {
			return;
		}

		GPSFilterPolygon old = this.poly;
		this.poly = poly;

		this.publish( new ChangeEvent( this, Field.POLYGON, old ) );
	}

	/**
	 * Sets the message bus, if not set, no events will be received.
	 *
	 * @param messageBus the buss that receives events.
	 */
	public void setMessageBus( MessageBus<Message> messageBus ) {
		this.bus = messageBus;
	}

	/**
	 * Returns the current message-bus if any.
	 *
	 * @return the bus, or null.
	 */
	public MessageBus<Message> getMessageBus() {
		return this.bus;
	}

	public String toString() {
		final Map<String, String> prop = Maps.newHashMap();
		prop.put( "id", Integer.toString( this.id ) );
		prop.put( "name", this.name );
		prop.put( "enabled", Boolean.toString( this.enabled ) );
		prop.put( "mode", this.mode.toString() );
		prop.put( "polygon", this.poly == null ? null : this.poly.toString() );

		return "GPSFilterArea[" + StringUtils.PROPERTY_MAP_JOINER.join( prop ) + "]";
	}

	/* --------------------------------
	 * Private Methods.
	 * --------------------------------
	 */

	/**
	 * Publishes an event to event bus.
	 *
	 * @param event the event to publish.
	 */
	private void publish( ChangeEvent event ) {
		if ( this.bus == null ) {
			return;
		}

		this.bus.publish( event );
	}

}