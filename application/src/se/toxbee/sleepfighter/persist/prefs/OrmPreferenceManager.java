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
package se.toxbee.sleepfighter.persist.prefs;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.Callable;

import se.toxbee.sleepfighter.persist.dao.PersistenceException;
import se.toxbee.sleepfighter.persist.dao.PersistenceExceptionDao;
import se.toxbee.sleepfighter.utils.factory.IFactory;
import se.toxbee.sleepfighter.utils.prefs.MapBasePreferenceManager;
import se.toxbee.sleepfighter.utils.prefs.PreferenceManager;
import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;
import se.toxbee.sleepfighter.utils.prefs.SerializablePreference;

import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.stmt.PreparedQuery;

/**
 * {@link OrmPreferenceManager} works like android OS SharedPreferences<br/>
 * but uses the ORM persistence instead.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public class OrmPreferenceManager<P extends OrmPreference> extends MapBasePreferenceManager {
	private Map<String, Serializable> entries;
	private PersistenceExceptionDao<P, String> dao;
	private boolean isApplying = false;
	private Factory<P> factory;

	public static <P extends OrmPreference> OrmPreferenceManager<P> make( PersistenceExceptionDao<P, String> dao, Factory<P> factory ) {
		return new OrmPreferenceManager<P>( dao, factory );
	}

	/**
	 * {@link Factory} is the factory for {@link OrmPreferenceManager} when setting to DB is needed.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Dec 15, 2013
	 */
	public static interface Factory<FT extends OrmPreference> extends IFactory<SerializablePreference, FT> {
	}

	/**
	 * Constructs the manager given the corresponding DAO.
	 *
	 * @param dao the DAO.
	 * @param factory the P type object factory.
	 */
	public OrmPreferenceManager( PersistenceExceptionDao<P, String> dao, Factory<P> factory ) {
		this.dao = dao;
		this.factory = factory;
	}

	/**
	 * Loads all available preferences into memory.
	 *
	 * @return this.
	 */
	public OrmPreferenceManager<P> load() {
		return this.load( this.dao.getWrappedIterable() );
	}

	/**
	 * Loads all available preferences from a prepared query.
	 *
	 * @param query the prepared query to use.
	 * @return this.
	 */
	public OrmPreferenceManager<P> load( PreparedQuery<P> query ) {
		return this.load( this.dao.getWrappedIterable( query ) );
	}

	protected OrmPreferenceManager<P> load( CloseableWrappedIterable<P> i ) {
		try {
			this.load( this.dao );
		} finally {
			try {
				i.close();
			} catch ( SQLException e ) {
				throw new PersistenceException( e );
			}
		}

		return this;
	}

	@Override
	protected Map<String, Serializable> memory() {
		return this.entries;
	}

	@Override
	protected <U extends Serializable> void backendSet( String key, U value ) {
		this.dao.createOrUpdate( this.factory.produce( OrmPreference.anon( key, value ) ) );
	}

	@Override
	protected <U extends Serializable> void backendRemove( String key ) {
		this.dao.deleteById( key );
	}

	@Override
	public PreferenceManager _apply( final PreferenceNode node, final PreferenceEditCallback cb ) {
		this._apply_impl( node, cb );
		return this;
	}

	@Override
	public boolean _applyForResult( PreferenceNode node, PreferenceEditCallback cb ) {
		try {
			this._apply_impl( node, cb );
			return true;
		} catch ( Exception e ) {
			return false;
		} finally {
			this.isApplying = false;
		}
	}

	private void _apply_impl( final PreferenceNode node, final PreferenceEditCallback cb ) {
		this.isApplying = true;
		this.dao.callBatchTasks( new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				cb.editPreference( node );
				return null;
			}
		} );
		this.isApplying = false;
	}

	@Override
	public boolean isApplying() {
		return this.isApplying;
	}
}
