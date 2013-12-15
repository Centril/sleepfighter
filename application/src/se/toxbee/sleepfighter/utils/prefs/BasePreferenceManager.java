package se.toxbee.sleepfighter.utils.prefs;

/**
 * {@link BasePreferenceManager} is the base class for all {@link PreferenceManager}s.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public abstract class BasePreferenceManager implements PreferenceManager {
	@Override
	public PreferenceNode apply( PreferenceEditCallback cb ) {
		return this._apply( this, cb );
	}

	@Override
	public boolean applyForResult( PreferenceEditCallback cb ) {
		return this._applyForResult( this, cb );
	}

	@Override
	public PreferenceNode sub( String ns ) {
		return new ChildPreferenceNode( this, ns );
	}

	@Override
	public PreferenceNode parent() {
		return this;
	}
}