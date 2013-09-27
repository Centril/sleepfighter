package se.chalmers.dat255.sleepfighter.preference;

import se.chalmers.dat255.sleepfighter.activity.RingerSettingsActivity;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.content.Context;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.util.AttributeSet;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class AlarmRingtonePreference extends RingtonePreference {
	@Override
	protected Uri onRestoreRingtone() {
		RingerSettingsActivity activity = (RingerSettingsActivity) this.getContext();
		Alarm alarm = activity.getAlarm();

		// TODO fetch from Alarm, for now return null.
		
		return null;
	}

	/**
	 * @see RingtonePreference#RingtonePreference(Context)
	 */
	public AlarmRingtonePreference( Context context ) {
		super( context );
	}

	/**
	 * @see RingtonePreference#RingtonePreference(Context, AttributeSet)
	 */
	public AlarmRingtonePreference( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}

	/**
	 * @see RingtonePreference#RingtonePreference(Context, AttributeSet, int)
	 */
	public AlarmRingtonePreference( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
	}
}