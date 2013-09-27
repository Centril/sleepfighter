package se.chalmers.dat255.sleepfighter.preference;

import android.content.Context;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.util.AttributeSet;

/**
 * InitializableRingtonePreference provides {@link #setInitialUri(Uri)}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class InitializableRingtonePreference extends RingtonePreference {
	private Uri initialUri;

	/**
	 * Sets the initial URI to use upon inflation.
	 *
	 * @param uri the initial URI.
	 */
	public void setInitialUri( Uri uri ) {
		this.initialUri = uri;
	}

	@Override
	protected Uri onRestoreRingtone() {
		return this.initialUri;
	}

	/**
	 * @see RingtonePreference#RingtonePreference(Context)
	 */
	public InitializableRingtonePreference( Context context ) {
		super( context );
	}

	/**
	 * @see RingtonePreference#RingtonePreference(Context, AttributeSet)
	 */
	public InitializableRingtonePreference( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}

	/**
	 * @see RingtonePreference#RingtonePreference(Context, AttributeSet, int)
	 */
	public InitializableRingtonePreference( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
	}
}