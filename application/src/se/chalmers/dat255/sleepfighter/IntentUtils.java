package se.chalmers.dat255.sleepfighter;

import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.content.Intent;
import android.net.Uri;

/**
 * IntentUtils provides some utilities for reading and writing data to intents.<br/>
 * Writing when in reading-mode and vice versa is pointless, intents are parceled.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 24, 2013
 */
public class IntentUtils {
	private static final String SCHEME = "sleepfighter";
	private static final String ID_SSP = "alarm/id";

	private final Intent intent;

	/**
	 * Constructs a IntentUtils object.
	 *
	 * @param intent intent object to perform operations on.
	 */
	public IntentUtils( Intent intent ) {
		this.intent = intent;
	}

	/**
	 * Sets the alarm id on intent.
	 *
	 * @param alarm the alarm
	 * @return this.
	 */
	public IntentUtils setAlarmId( Alarm alarm ) {
		return this.setAlarmId( alarm.getId() );
	}

	/**
	 * Sets the alarm id on intent.
	 *
	 * @param id the alarm id.
	 * @return this.
	 */
	public IntentUtils setAlarmId( final int id ) {
		if ( id == Alarm.NOT_COMMITTED_ID ) {
			throw new IllegalArgumentException( "The provided id is not legal to bind to an intent." );
		}

		this.intent.setData( Uri.fromParts( SCHEME, ID_SSP, Integer.toString( id ) ) );

		return this;
	}

	/**
	 * Returns the ID of the alarm.
	 *
	 * @return the ID of the alarm.
	 */
	public int getAlarmId() {
		Uri data = this.intent.getData();
		if ( data == null) {
			this.failNoId();
		}

		String fragment = data.getFragment();
		if ( fragment == null ) {
			this.failNoId();
		}

		try {
			int id = Integer.parseInt( fragment );
			if ( id < 1 ) {
				throw new IllegalArgumentException( "ID is outside of valid range, only positive integers allowed." );
			}
			return id;
		} catch ( NumberFormatException e ) {
			throw new IllegalArgumentException( "ID providided is not a number" );
		}
	}

	private void failNoId() {
		throw new IllegalArgumentException( "No arguments provided, ID needed." );
	}
}