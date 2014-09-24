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

package se.toxbee.sleepfighter.persist;

import android.content.Context;

import se.toxbee.ormlite.BaseOrmHelper;
import se.toxbee.ormlite.CacheEnabler;
import se.toxbee.ormlite.migration.MigrationExecutor;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.SnoozeConfig;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfig;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.model.challenge.ChallengeParam;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;

/**
 * Provides an OrmLiteSqliteOpenHelper for persistence layer.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
public class OrmHelper extends BaseOrmHelper {
	// Name of the database file in application.
	private static final String DATABASE_NAME = "sleep_fighter.db";

	// Current database version, change when database structure changes.
	private static final int DATABASE_VERSION = 27;

	// List of all classes that is managed by helper.
	private static final Class<?>[] CLASSES = new Class<?>[] {
		Alarm.class,

		AudioSource.class,
		AudioConfig.class,

		SnoozeConfig.class,

		ChallengeConfigSet.class,
		ChallengeConfig.class,
		ChallengeParam.class,

		GPSFilterArea.class
	};
	{
		initWith( Alarm.class, new CacheEnabler<Alarm>() );
	}

	@Override
	protected Class<?>[] classes() {
		return CLASSES;
	}

	@Override
	protected MigrationExecutor migrationExecutor() {
		return new UpgradeExecutor();
	}

	/**
	 * Constructs the helper from a given context.
	 *
	 * @param context the context to use.
	 */
	public OrmHelper( Context context ) {
		super( context, DATABASE_NAME, null, DATABASE_VERSION );
	}
}