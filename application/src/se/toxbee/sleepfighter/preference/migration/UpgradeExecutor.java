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
package se.toxbee.sleepfighter.preference.migration;

import se.toxbee.sleepfighter.android.utils.ContextUtils;
import se.toxbee.sleepfighter.preference.AppPreferenceManager;
import se.toxbee.sleepfighter.utils.migration.IMigrationException;
import se.toxbee.sleepfighter.utils.migration.IMigrationExecutor;
import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;
import se.toxbee.sleepfighter.utils.prefs.PreferenceNode.PreferenceEditCallback;
import android.content.Context;

/**
 * {@link UpgradeExecutor} runs upgrades for the entire app.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 16, 2013
 */
public class UpgradeExecutor extends IMigrationExecutor<PreferenceNode, Migrater> {
	public boolean execute( Context ctx, AppPreferenceManager prefs ) {
		int ovc = prefs.versionCode();
		int nvc = ContextUtils.versionCode( ctx );

		if ( ovc == nvc ) {
			return true;
		} else if ( ovc > nvc || !this.executeImpl( ctx, prefs, ovc, nvc ) ) {
			return false;
		}

		prefs.versionCode( nvc );
		return true;
	}

	private boolean executeImpl( Context ctx, AppPreferenceManager prefs, final int ovc, final int nvc ) {
		final PreferenceNode p = prefs.backend().parent();

		p.apply( new PreferenceEditCallback() {
			@Override
			public void editPreference( PreferenceNode pref ) {
				edit( p, ovc, nvc );
			}
		} );

		return true;
	}

	protected void edit( PreferenceNode p, int ovc, int nvc ) {
		try {
			this.apply( p, ovc, nvc );
		} catch ( IMigrationException e ) {
			p.clear();
			fail( e );
		}
	}

	@Override
	protected Class<Migrater> clazz() {
		return Migrater.class;
	}

	@Override
	protected Class<?>[] definedMigrations() {
		return new Class<?>[] { Version8.class };
	}
}
