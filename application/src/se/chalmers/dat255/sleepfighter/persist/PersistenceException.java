package se.chalmers.dat255.sleepfighter.persist;

/**
 * <p>PersitenceError indicates an error related to persistence.<br/>
 * This is a serious, but can be recovered from.</p>
 *
 * <p>While this is a RuntimeException, it should be checked for and handled.</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 22, 2013
 */
public class PersistenceException extends RuntimeException {
	private static final long serialVersionUID = -5914772855383078239L;

	/**
	 * Constructs a PersitenceException with detailed message.<br/>
	 * This should not be used under normal circumstances,<br/>
	 * rather, a Throwable should be passed to constructor.
	 *
	 * @param detailMessage the message to pass.
	 */
	public PersistenceException( String detailMessage ) {
		super( detailMessage );
	}

	/**
	 * Wraps a throwable in a PersitenceException.
	 *
	 * @param throwable the throwable to wrap in.
	 */
	public PersistenceException( Throwable throwable ) {
		super( throwable );
	}

	/**
	 * Wraps a throwable in a PersitenceException and adds a detailed message.
	 *
	 * @param detailMessage the message to pass.
	 * @param throwable the throwable to wrap in.
	 */
	public PersistenceException( String detailMessage, Throwable throwable ) {
		super( detailMessage, throwable );
	}
}