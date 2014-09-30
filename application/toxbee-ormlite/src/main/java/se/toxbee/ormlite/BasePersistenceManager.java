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

package se.toxbee.ormlite;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Feb, 19, 2014
 */
public abstract class BasePersistenceManager {
	protected volatile BaseOrmHelper ormHelper = null;
	protected Context context;

	/**
	 * Returns the orm helper class.
	 *
	 * @return the helper class.
	 */
	abstract protected Class<? extends BaseOrmHelper> helperClazz();

	/**
	 * Initialization code goes here.
	 */
	abstract protected void init();

	/**
	 * Constructs the PersistenceManager.
	 *
	 * @param context android context.
	 */
	public BasePersistenceManager( Context context ) {
		this.context = context;
	}

	/**
	 * Rebuilds all data-structures. Any data is lost.
	 */
	public void cleanStart() {
		this.getHelper().rebuild();
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
	 * @return the helper.
	 */
	protected BaseOrmHelper getHelper() {
		if ( this.ormHelper == null ) {
			this.ormHelper = OpenHelperManager.getHelper( this.context, this.helperClazz() );
			this.init();
		}

		return this.ormHelper;
	}

	/**
	 * Facade: returns DAO from {@link #getHelper()}.
	 *
	 * @param clazz the {@link Class} to get DAO for.
	 * @return the DAO.
	 */
	public <T, I> PersistenceExceptionDao<T, I> dao( Class<T> clazz ) {
		return this.getHelper().dao( clazz );
	}

	/**
	 * Facade: returns DAO from {@link #getHelper()}.
	 *
	 * @param clazz the {@link Class} to get DAO for.
	 * @return the DAO.
	 */
	public <T> PersistenceExceptionDao<T, String> dao_s( Class<T> clazz ) {
		return this.getHelper().dao_s( clazz );
	}

	/**
	 * Facade: returns DAO from {@link #getHelper()}.
	 *
	 * @param clazz the {@link Class} to get DAO for.
	 * @return the DAO.
	 */
	public <T> PersistenceExceptionDao<T, Integer> dao_i( Class<T> clazz ) {
		return this.getHelper().dao_i( clazz );
	}
}
