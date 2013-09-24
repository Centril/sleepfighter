package se.chalmers.dat255.sleepfighter;

import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.content.Intent;
import android.os.Bundle;

/**
 * IntentUtils provides some utilities for reading and writing data to intents.<br/>
 * Writing when in reading-mode and vice versa is pointless, intents are parceled.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 24, 2013
 */
public class IntentUtils {
	private static final String EXTRA_ALARM_ID = "alarm_id";

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
		this.intent.putExtra( EXTRA_ALARM_ID, id );

		return this;
	}

	/**
	 * Returns the ID of the alarm.
	 *
	 * @return the ID of the alarm.
	 */
	public int getAlarmId() {
		Bundle extras = this.intent.getExtras();

		if ( extras == null) {
			throw new IllegalArgumentException( "No arguments provided, ID needed." );
		}

		int id = extras.getInt( EXTRA_ALARM_ID );

		if ( id < 1 ) {
			throw new IllegalArgumentException( "ID is outside of valid range, only positive integers allowed." );
		}

		return id;
	}
}