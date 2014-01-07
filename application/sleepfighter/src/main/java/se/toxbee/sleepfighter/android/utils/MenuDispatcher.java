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
package se.toxbee.sleepfighter.android.utils;

import se.toxbee.sleepfighter.utils.collect.ReflectiveIdDispatcher;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

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