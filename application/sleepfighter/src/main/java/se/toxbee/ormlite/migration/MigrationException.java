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

package se.toxbee.ormlite.migration;

import java.sql.SQLException;

import se.toxbee.commons.migration.IMigrationException;

/**
 * MigrationException thrown when an error occurs in migration.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 13, 2013
 */
public final class MigrationException extends IMigrationException {
	public static MigrationException fail( SQLException e, Migrater m ) throws MigrationException {
		throw new MigrationException( "Migration v" + m.versionCode() + " failed.", e, m );
	}

	private MigrationException( String msg, Throwable cause, Migrater vm ) {
		super( msg, cause, vm );
	}

	private static final long serialVersionUID = -6934930583715968324L;
}