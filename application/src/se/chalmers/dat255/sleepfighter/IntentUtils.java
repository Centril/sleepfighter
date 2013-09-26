package se.chalmers.dat255.sleepfighter;

import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.content.Intent;

/**
 * IntentUtils provides some utilities for reading and writing data to intents.<br/>
 * Writing when in reading-mode and vice versa is pointless, intents are parceled.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 24, 2013
 */
public class IntentUtils {
	private final String ALARM_EXTRA_ID = "alarm_id";

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
	 * Sets the alarm id on intent.<br/>
	 * If provided id == {@link Alarm#NOT_COMMITTED_ID}, nothing happens.
	 *
	 * @param id the alarm id.
	 * @return this.
	 */
	public IntentUtils setAlarmId( final int id ) {
		if ( id != Alarm.NOT_COMMITTED_ID ) {
			this.intent.putExtra( ALARM_EXTRA_ID, id );
		}

		return this;
	}

	/**
	 * Returns the ID of the alarm.
	 *
	 * @return the ID of the alarm.
	 */
	public int getAlarmId() {
		int id = this.intent.getIntExtra( ALARM_EXTRA_ID, Alarm.NOT_COMMITTED_ID );
		if ( id == Alarm.NOT_COMMITTED_ID ) {
			throw new IllegalArgumentException( "ID is outside of valid range, only positive integers allowed." );
		}

		return id;
	}
}