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

package se.toxbee.sleepfighter.preference.migration;

import android.content.Context;
import android.util.Log;

import se.toxbee.sleepfighter.android.utils.ContextUtils;
import se.toxbee.sleepfighter.preference.AppPreferenceManager;
import se.toxbee.commons.migration.IMigrationException;
import se.toxbee.commons.migration.IMigrationExecutor;
import se.toxbee.commons.prefs.PreferenceNode;
import se.toxbee.commons.prefs.PreferenceNode.PreferenceEditCallback;

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
