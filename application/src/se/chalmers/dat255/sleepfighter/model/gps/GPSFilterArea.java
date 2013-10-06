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
package se.chalmers.dat255.sleepfighter.model.gps;

import se.chalmers.dat255.sleepfighter.model.IdProvider;
import se.chalmers.dat255.sleepfighter.utils.message.Message;
import se.chalmers.dat255.sleepfighter.utils.message.MessageBus;

import com.google.common.base.Objects;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * GPSFilterArea defines an exclusion area.<br/>
 * It is made up of a {@link GPSFilterPolygon}, name and id.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
@DatabaseTable(tableName = "gpsfilter_area")
public class GPSFilterArea implements IdProvider {
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
		public Field getField() {
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
	 * Returns Whether or not the polygon contains the given GPSLatLng point.<br/>
	 * {@link #getPolygon()} may not return null before a call to {@link #contains(GPSGPSLatLng)}.
	 *
	 * @see GPSFilterArea#contains(GPSGPSLatLng)
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