package se.toxbee.sleepfighter.utils.migration;

import java.sql.SQLException;
import java.util.Locale;

/**
 * MigrationException thrown when an error occurs in {@link IMigration}<br/>
 * or somewhere during migration.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 22, 2013
 */
public class IMigrationException extends Exception {
	/**
	 * Indicates the reason that the migration failed.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Dec 22, 2013
	 */
	public interface Reason {
		public String toString();
	}

	/**
	 * Enumeration of the common reasons.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Dec 22, 2013
	 */
	public enum StdReason implements Reason {
		SQL_FAILURE, RUNTIME_FAILURE, TOO_OLD, UNKNOWN;

		public static Reason deduceFromCause( Throwable c  ) {
			if ( c == null ) {
				return null;
			} else if ( c instanceof SQLException ) {
				return SQL_FAILURE;
			} else if ( c instanceof RuntimeException ) {
				return RUNTIME_FAILURE;
			} else {
				return null;
			}
		}
	}

	/**
	 * Throws an exception if originVersion < lowestAllowed with {@link StdReason#TOO_OLD}.
	 *
	 * @param originVersion
	 * @param lowestAllowed
	 * @throws IMigrationException
	 */
	public static final void tooOld( int originVersion, int lowestAllowed ) throws IMigrationException {
		if ( originVersion < lowestAllowed ) {
			throw new IMigrationException( "The version is too old", StdReason.TOO_OLD, originVersion );
		}
	}

	/**
	 * Constructs migration failure with message, reason and a version code.<br/>
	 * Use this for when an exception is not behind the error.
	 *
	 * @param msg the message.
	 * @param reason the reason.
	 * @param version the version.
	 */
	public IMigrationException( String msg, Reason reason, int version ) {
		this( msg, version, reason, null, null );
	}

	/**
	 * Constructs the migration failure with message, a throwable and a version code.
	 *
	 * @param msg the message.
	 * @param cause the cause, used to deduce the reason.
	 * @param version the version.
	 */
	public IMigrationException( String msg, Throwable cause, int version ) {
		this( msg, version, StdReason.deduceFromCause( cause ), cause, null );
	}

	/**
	 * Constructs the migration failure with message, a throwable and a migrater.
	 * 
	 * @param msg the message.
	 * @param cause the cause, used to deduce the reason.
	 * @param vm the migrater, used to deduce version code.
	 */
	public IMigrationException( String msg, Throwable cause, IMigration<?> vm ) {
		this( msg, vm.versionCode(), StdReason.deduceFromCause( cause ), cause, vm );
	}

	protected IMigrationException( String msg, int version, Reason reason, Throwable cause, IMigration<?> vm ) {
		super( msg, cause );
		this.vm = vm;
		this.reason = reason == null ? StdReason.UNKNOWN : reason;
		this.version = version;
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
	public IMigration<?> getMigrater() {
		return this.vm;
	}

	@Override
	public String toString() {
		return String.format( Locale.getDefault(),
			"Migration failed for version-code %d, with reason: %s, and message: %s",
			this.version, this.reason, this.getMessage() );
	}

	private final IMigration<?> vm;
	private final Reason reason;
	private int version;
	private static final long serialVersionUID = 449411709351977927L;
}