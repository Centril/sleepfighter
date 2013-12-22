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
import android.util.Log;

/**
 * {@link UpgradeExecutor} runs upgrades for the entire app.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 16, 2013
 */
public class UpgradeExecutor extends IMigrationExecutor<PreferenceNode, Migrater> {
	private static final String TAG = UpgradeExecutor.class.getSimpleName();

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

		try {
			p.applyForResult( new PreferenceEditCallback() {
				@Override
				public void editPreference( PreferenceNode pref ) {
					try {
						apply( p, ovc, nvc );
					} catch ( IMigrationException e ) {
						throw new RuntimeException( e );
					}
				}
			} );
		} catch ( RuntimeException e ) {
			return fail( e );
		}

		return true;
	}

	@Override
	protected boolean fail( Throwable e ) {
		Log.e( TAG, "Error during migration.", e );
		return super.fail( e );
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
