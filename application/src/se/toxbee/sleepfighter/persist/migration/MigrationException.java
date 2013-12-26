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
package se.toxbee.sleepfighter.persist.migration;

import java.sql.SQLException;

import se.toxbee.sleepfighter.utils.migration.IMigrationException;

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