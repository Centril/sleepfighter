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

package se.toxbee.sleepfighter.android.utils;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import se.toxbee.commons.collect.ReflectiveIdDispatcher;

/**
 * {@link MenuDispatcher} is a simple utility for quick menu inflating and dispatching.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 23, 2013
 */
public class MenuDispatcher extends ReflectiveIdDispatcher<Activity, MenuDispatcher> {
	private int menuId;

	/**
	 * Makes a dispatcher.
	 *
	 * @param act the activity
	 * @param menuId the id of the menu xml to inflate.
	 * @return the dispatcher.
	 */
	public static MenuDispatcher make( Activity act, int menuId ) {
		return new MenuDispatcher( act, menuId );
	}

	/**
	 * Constructs a dispatcher.
	 *
	 * @param act the activity
	 * @param menuId the id of the menu xml to inflate.
	 */
	public MenuDispatcher( Activity act, int menuId ) {
		super( act );
		this.menuId = menuId;
	}

	/**
	 * One-shot calls {@link #inflate(Menu)} and {@link #bindInflated(Menu)}.
	 *
	 * @param m the menu to use in calls.
	 * @return true.
	 */
	public boolean bind( Menu m ) {
		this.inflate( m );
		this.bindInflated( m );
		return true;
	}

	/**
	 * Inflates the menu with XML.
	 *
	 * @param m the menu.
	 * @return true.
	 */
	public boolean inflate( Menu m ) {
		this.receiver.getMenuInflater().inflate( this.menuId, m );
		return true;
	}

	/**
	 * Binds all inflated menu items to their id(menu_action) -> method("menu_action").
	 *
	 * @param m the menu.
	 * @return this.
	 */
	public MenuDispatcher bindInflated( Menu m ) {
		this.entries.clear();

		int s = m.size();
		for ( int i = 0; i < s; ++i ) {
			int id = m.getItem( i ).getItemId();
			String name = this.receiver.getResources().getResourceEntryName( id );
			this.bind( id, name );
		}

		return this;
	}

	/**
	 * Binds all ids given as: id(R.id.ABC) -> method(ABC)
	 *
	 * @param ids all ids to bind.
	 * @return this.
	 */
	public MenuDispatcher bind( String... ids ) {
		for ( String id : ids ) {
			this.bind( id, (Object[]) null );
		}

		return this;
	}

	/**
	 * Binds id(R.id.action) -> method("action")
	 *
	 * @param action the action.
	 * @param a optional arguments.
	 * @return this.
	 */
	public MenuDispatcher bind( String action, Object... a ) {
		return (MenuDispatcher) this.bind( ResourcesDynamicUtil.getId( action, this.receiver ), action, a );
	}

	/**
	 * Dispatches from item m to a bound runnable.
	 *
	 * @param m the item.
	 * @return true if there was a runnable for {@link MenuItem#getItemId()}.
	 */
	public boolean dispatch( MenuItem m ) {
		return this.dispatch( m.getItemId() );
	}
}