package se.chalmers.dat255.sleepfighter.persist;

import java.sql.SQLException;

import net.engio.mbassy.listener.Handler;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.Alarm.AlarmEvent;
import se.chalmers.dat255.sleepfighter.model.AlarmList;
import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.table.TableUtils;

/**
 * Handles all reads and writes to persistence.<br/>
 * There should be no reason to keep more than 1 instance of this object.
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
public class PersistenceManager {

	private volatile OrmHelper ormHelper = null;

	private Context context;

	private static boolean init = false;

	/**
	 * Handles changes in alarm-list (the list itself, additions, deletions, etc).
	 *
	 * @param evt the event.
	 */
	@Handler
	public void handleListChange( AlarmList.Event evt ) {
		switch ( evt.operation() ) {
		case CLEAR:
			this.clearAlarms();
			break;

		case ADD:
			for ( Object elem : evt.elements() ) {
				this.addAlarm( (Alarm) elem );
			}
			break;

		case REMOVE:
			for ( Object elem : evt.elements() ) {
				this.removeAlarm( (Alarm) elem );
			}
			break;

		case UPDATE:
			Alarm old = (Alarm) evt.elements().iterator().next();
			this.removeAlarm( old );
			this.addAlarm( evt.source().get( evt.index() ) );
			break;
		}
	}

	/**
	 * Handles a change in an alarm.
	 *
	 * @param evt the event.
	 */
	@Handler
	public void handleAlarmChange( AlarmEvent evt ) {
		this.updateAlarm( evt.getAlarm() );
	}

	/**
	 * Constructs the PersistenceManager.
	 *
	 * @param context android context.
	 */
	public PersistenceManager( Context context ) {
		this.setContext( context );
	}

	/**
	 * Sets the context to use.
	 *
	 * @param context android context.
	 */
	public void setContext( Context context ) {
		this.context = context;
	}

	/**
	 * Rebuilds all data-structures. Any data is lost.
	 *
	 * @param context android context.
	 */
	public void cleanStart() {
		this.getHelper().rebuild();
	}

	/**
	 * Clears the list of all alarms.
	 *
	 * @throws PersistenceException if some SQL error happens.
	 */
	public void clearAlarms() throws PersistenceException {
		try {
			TableUtils.clearTable( this.getHelper().getConnectionSource(), Alarm.class );
		} catch ( SQLException e ) {
			throw new PersistenceException( e );
		}
	}

	/**
	 * Fetches an AlarmsManager from database, it is sorted on ID.
	 *
	 * @param context android context.
	 * @return the fetched AlarmsManager.
	 * @throws PersistenceException if some SQL error happens.
	 */
	public AlarmList fetchAlarms() throws PersistenceException {
		OrmHelper helper = this.getHelper();
		PersistenceExceptionDao<Alarm, Integer> dao = helper.getAlarmDao();
		return new AlarmList( dao.queryForAll() );
	}

	/**
	 * Fetches an AlarmsManager from database, it is sorted on names.
	 *
	 * @param context android context.
	 * @return the fetched AlarmsManager.
	 * @throws PersistenceException if some SQL error happens.
	 */
	public AlarmList fetchAlarmsSortedNames() throws PersistenceException {
		OrmHelper helper = this.getHelper();
		PersistenceExceptionDao<Alarm, Integer> dao = helper.getAlarmDao();

		try {
			return new AlarmList( dao.queryBuilder().orderBy( "name", true ).query() );
		} catch ( SQLException e ) {
			throw new PersistenceException( e );
		}
	}

	/**
	 * Fetches a single alarm from database by its id.
	 *
	 * @param context android context.
	 * @param id the ID of the alarm to fetch.
	 * @return the fetched Alarm.
	 * @throws PersistenceException if some SQL error happens.
	 */
	public Alarm fetchAlarmById( int id ) throws PersistenceException {
		OrmHelper helper = this.getHelper();
		PersistenceExceptionDao<Alarm, Integer> dao = helper.getAlarmDao();
		return dao.queryForId( id );
	}

	/**
	 * Updates an alarm to database.
	 *
	 * @param context android context
	 * @param alarm the alarm to update.
	 * @throws PersistenceException if some SQL error happens.
	 */
	public void updateAlarm( Alarm alarm ) throws PersistenceException {
		OrmHelper helper = this.getHelper();
		PersistenceExceptionDao<Alarm, Integer> dao = helper.getAlarmDao();
		dao.update( alarm );
	}

	/**
	 * Stores/adds an alarm to database.
	 *
	 * @param context android context.
	 * @param alarm the alarm to store.
	 * @throws PersistenceException if some SQL error happens.
	 */
	public void addAlarm( Alarm alarm ) throws PersistenceException {
		OrmHelper helper = this.getHelper();
		PersistenceExceptionDao<Alarm, Integer> dao = helper.getAlarmDao();
		dao.create( alarm );
	}

	/**
	 * Removes an alarm from database.
	 *
	 * @param context android context.
	 * @param alarm the alarm to remove.
	 * @throws PersistenceException if some SQL error happens.
	 */
	public void removeAlarm( Alarm alarm ) throws PersistenceException {
		OrmHelper helper = this.getHelper();
		PersistenceExceptionDao<Alarm, Integer> dao = helper.getAlarmDao();
		dao.delete( alarm );
	}

	/**
	 * Releases any resources held such as the OrmHelper.
	 */
	public void release() {
		if ( this.ormHelper != null ) {
			OpenHelperManager.releaseHelper();
			this.ormHelper = null;
		}
	}

	/**
	 * Returns the OrmHelper.
	 *
	 * @param context the context to use to get helper.
	 * @return the helper.
	 */
	public OrmHelper getHelper() {
		if ( this.ormHelper == null ) {
			if ( this.context == null ) {
				throw new PersistenceException( "There is no helper set and context == null" );
			}

			this.loadHelper();
		}

		return this.ormHelper;
	}

	/**
	 * Loads the OrmHelper.
	 */
	private void loadHelper() {
		this.ormHelper = OpenHelperManager.getHelper( this.context, OrmHelper.class );

		this.init();
	}

	/**
	 * Initialization code goes here.
	 */
	private void init() {
		// Run Once guard.
		if ( init ) {
			return;
		}
		init = true;

		// Initialization code goes here.
		DataPersisterManager.registerDataPersisters( BooleanArrayType.getSingleton() );
	}
}