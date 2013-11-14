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
import java.util.Locale;

import se.toxbee.sleepfighter.persist.PersistenceException;

/**
 * MigrationException thrown when an error occurs in migration.<br/>
 * A MigrationException is a very serious error that should be displayed to user.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 13, 2013
 */
public final class MigrationException extends Exception {
	/**
	 * Indicates the reason that the migration failed.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 13, 2013
	 */
	public enum Reason {
		SQL_FAILURE, RUNTIME_FAILURE, TOO_OLD, UNKNOWN
	}

	private static final long serialVersionUID = -6934930583715968324L;

	private final VersionMigrater vm;
	private final Reason reason;
	private int version;

	/**
	 * Constructs migration failure with message, reason and a version code.<br/>
	 * Use this for when an exception is not behind the error.
	 *
	 * @param msg the message.
	 * @param reason the reason.
	 * @param version the version.
	 */
	public MigrationException( String msg, Reason reason, int version ) {
		this( msg, null, null, reason );
		this.version = version;
	}

	/**
	 * Constructs the migration failure with message, a throwable and a version code.
	 *
	 * @param msg the message.
	 * @param cause the cause, used to deduce the reason.
	 * @param version the version.
	 */
	public MigrationException( String msg, Throwable cause, int version ) {
		this( msg, cause, null );
		this.version = version;
	}

	/**
	 * Constructs the migration failure with message, a throwable and a migrater.
	 * 
	 * @param msg the message.
	 * @param cause the cause, used to deduce the reason.
	 * @param vm the migrater, used to deduce version code.
	 */
	public MigrationException( String msg, Throwable cause, VersionMigrater vm ) {
		this(
			msg, cause, vm,
			// Deduce reason from exception.
			(
				(cause instanceof SQLException || cause instanceof PersistenceException)
			?	Reason.SQL_FAILURE
			:	(
					(cause instanceof RuntimeException)
				?	Reason.RUNTIME_FAILURE
				:	null
				)
			)
		);
	}

	private MigrationException( String msg, Throwable cause, VersionMigrater vm, Reason reason ) {
		super( msg, cause );
		this.vm = vm;
		this.reason = reason == null ? Reason.UNKNOWN : reason;
		this.version = vm.versionCode();
	}

	/**
	 * The reason that migration failed.
	 *
	 * @return the reason.
	 */
	public Reason getReason() {
		return this.reason;
	}

	/**
	 * Returns the version that the failure originated from.
	 *
	 * @return the version.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Returns the migrater that the failure originated from.
	 *
	 * @return the migrater.
	 */
	public VersionMigrater getMigrater() {
		return this.vm;
	}

	@Override
	public String toString() {
		return String.format( Locale.getDefault(),
			"Migration failed for version-code %d, with reason: %s, and message: %s",
			this.version, this.reason, this.getMessage() );
	}
}