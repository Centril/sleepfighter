package se.chalmers.dat255.sleepfighter.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import se.chalmers.dat255.sleepfighter.utils.DateTimeUtils;

/**
 * Manages all the existing alarms.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 18, 2013
 */
public class AlarmsManager {
	/**
	 * Provides information about the earliest alarm.<br/>
	 * This information contains:
	 * <ul>
	 * 	<li>occurrence in milliseconds since unix epoch</li>
	 * 	<li>index of alarm in list.</li>
	 * </ul>
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Sep 18, 2013
	 */
	public static class EarliestInfo {
		private long millis;
		private int index;

		private EarliestInfo( long millis, int index ) {
			this.millis = millis;
			this.index = index;
		}

		/**
		 * Returns true if the earliest info is real.<br/>
		 * This occurs when {@link Alarm#canHappen()} returns true for some alarm.
		 *
		 * @return true if earliest info is real.
		 */
		public boolean isReal() {
			return this.millis != Alarm.NEXT_NON_REAL;
		}

		/**
		 * Returns the difference of the time of earliest alarm and now as a calendar.
		 *
		 * @param now the current time.
		 * @return the difference.
		 */
		public Calendar getDiff( Calendar now ) {
			return DateTimeUtils.getDateDiff( now, this.getMillis() );
		}

		/**
		 * The earliest alarm in milliseconds.
		 *
		 * @return the earliest alarm in milliseconds.
		 */
		public long getMillis() {
			return this.millis;
		}

		/**
		 * The earliest alarm in index.
		 *
		 * @return the earliest alarm in index.
		 */
		public int getIndex() {
			return this.index;
		}
	}

	/** Holds the list of alarms. */
	private List<Alarm> list;

	/**
	 * Constructs the manager with no initial alarms.
	 */
	public AlarmsManager() {
		this.list = new ArrayList<Alarm>();
	}

	/**
	 * Sets the list of alarms.
	 *
	 * @param list the list of alarms to set.
	 */
	public void set( List<Alarm> list ) {
		this.list = list;
	}

	/**
	 * Returns the list of alarms.
	 *
	 * @return the list of alarms.
	 */
	public List<Alarm> get() {
		return this.list;
	}

	/**
	 * Returns info about the earliest alarm.<br/>
	 * The info contains info about milliseconds and index of alarm.
	 *
	 * @return info about the earliest alarm. 
	 */
	public EarliestInfo getEarliestInfo( Calendar now ) {
		long millis = Alarm.NEXT_NON_REAL;
		int earliestIndex = -1;

		for ( int i = 0; i < this.list.size(); i++ ) {
			long currMillis = this.list.get( i ).getNextMillis( now );
			if ( currMillis > Alarm.NEXT_NON_REAL && (millis == Alarm.NEXT_NON_REAL || millis > currMillis) ) {
				earliestIndex = i;
				millis = currMillis;
			}
		}

		if ( millis == Alarm.NEXT_NON_REAL ) {
			earliestIndex = -1;
		}

		return new EarliestInfo( millis, earliestIndex );
	}

	/**
	 * Returns info about the earliest alarm.<br/>
	 * The info contains info about milliseconds and index of alarm.
	 *
	 * @return info about the earliest alarm. 
	 */
	public EarliestInfo getEarliestInfo() {
		return this.getEarliestInfo( GregorianCalendar.getInstance() );
	}
}