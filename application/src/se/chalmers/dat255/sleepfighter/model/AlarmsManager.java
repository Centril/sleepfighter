package se.chalmers.dat255.sleepfighter.model;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.sleepfighter.utils.collect.ObservableList;
import se.chalmers.dat255.sleepfighter.utils.message.Message;
import se.chalmers.dat255.sleepfighter.utils.message.MessageBus;

/**
 * Manages all the existing alarms.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 18, 2013
 */
public class AlarmsManager extends ObservableList<Alarm> {
	/**
	 * Constructs the manager with no initial alarms.
	 */
	public AlarmsManager() {
		this( new ArrayList<Alarm>() );
	}

	/**
	 * Constructs the manager starting with given alarms.
	 *
	 * @param alarms list of given alarms. Don't modify this list directly.
	 */
	public AlarmsManager( List<Alarm> alarms ) {
		this.setDelegate( alarms );
	}

	/**
	 * Sets the list of alarms.
	 *
	 * @param delegate list of given alarms. Don't modify this list directly.
	 */
	@Override
	public void setDelegate( List<Alarm> delegate ) {
		super.setDelegate( delegate );
	}

	@Override
	protected void fireEvent( Event e ) {
		// Intercept add/update events and inject message bus.
		if ( e.operation() == Operation.ADD || e.operation() == Operation.UPDATE ) {
			for ( Object obj : e.elements() ) {
				((Alarm) obj).setMessageBus( this.getMessageBus() );
			}
		}

		super.fireEvent( e );
	}

	/**
	 * Sets the message bus, if not set, no events will be received.
	 *
	 * @param messageBus the buss that receives events.
	 */
	public void setMessageBus( MessageBus<Message> messageBus ) {
		super.setMessageBus( messageBus );

		for ( Alarm alarm : this ) {
			alarm.setMessageBus( messageBus );
		}
	}

	/**
	 * Returns info about the earliest alarm.<br/>
	 * The info contains info about milliseconds and the alarm.
	 *
	 * @param now current time in unix epoch timestamp.
	 * @return info about the earliest alarm. 
	 */
	public AlarmTimestamp getEarliestAlarm( long now ) {
		Long millis = null;
		int earliestIndex = -1;

		for ( int i = 0; i < this.size(); i++ ) {
			Long currMillis = this.get( i ).getNextMillis( now );
			if ( currMillis != Alarm.NEXT_NON_REAL && (millis == Alarm.NEXT_NON_REAL || millis > currMillis) ) {
				earliestIndex = i;
				millis = currMillis;
			}
		}

		return earliestIndex == -1 ? AlarmTimestamp.INVALID : new AlarmTimestamp( millis, this.get( earliestIndex) );
	}
}