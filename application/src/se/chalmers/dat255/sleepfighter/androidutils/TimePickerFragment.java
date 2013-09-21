package se.chalmers.dat255.sleepfighter.androidutils;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.google.common.base.Preconditions;

/**
 * TimePickerFragment is an utility class for showing a time picker dialog.
 *
 * @see http://developer.android.com/guide/topics/ui/controls/pickers.html
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	private Alarm alarm;
	private OnTimeSetListener relayCallback;
	private boolean useCurrentTime = false;

	/**
	 * Sets the alarm the picker is used for, null is not allowed.
	 *
	 * @param alarm the alarm to use for time, unless bypassed by 
	 */
	public void setAlarm( Alarm alarm ) {
		this.alarm = Preconditions.checkNotNull( alarm );
	}

	/**
	 * Returns the alarm the picker is used for.
	 *
	 * @return the alarm.
	 */
	public Alarm getAlarm() {
		return this.alarm;
	}

	/**
	 * Sets whether or not to use alarm time, or current time.<br/>
	 * Default is false.
	 *
	 * @param useCurrentTime true if current time should be used instead.
	 */
	public void setUseCurrentTime( boolean useCurrentTime ) {
		this.useCurrentTime = useCurrentTime;
	}

	/**
	 * Returns whether or not to use alarm time, or current time.<br/>
	 * Default is false.
	 *
	 * @return true if current time should be used instead.
	 */
	public boolean isUsingCurrentTime() {
		return this.useCurrentTime;
	}

	public void setRelayCallback( TimePickerDialog.OnTimeSetListener callback ) {
		this.relayCallback = callback;
	}


	@Override
	public Dialog onCreateDialog( Bundle savedInstanceState ) {
		int hour, minute;

		if ( this.isUsingCurrentTime() ) {
			DateTime now = new DateTime();
			hour = now.getHourOfDay();
			minute = now.getMinuteOfHour();
		} else {
			hour = alarm.getHour();
			minute = alarm.getMinute();
		}

		return new TimePickerDialog( getActivity(), this, hour, minute, DateFormat.is24HourFormat( this.getActivity() ) );
	}

	public void onTimeSet( TimePicker view, int hour, int minute ) {
		this.alarm.setTime( hour, minute );
		if ( this.relayCallback != null ) {
			this.relayCallback.onTimeSet( view, hour, minute );
		}
	}

	/**
	 * Issues an alarm, this is the preferred way to use this class.
	 *
	 * @param alarm the alarm the picker is used for
	 * @param activity the activity to get FragmentManager from.
	 * @param callback an additional callback to relay to when onTimeSet is called.
	 */
	public static void issue( Alarm alarm, FragmentActivity activity, TimePickerDialog.OnTimeSetListener callback ) {
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setAlarm( alarm );
		fragment.setRelayCallback( callback );
		FragmentManager fm = activity.getSupportFragmentManager();
		fragment.show( fm, "timePicker" );
	}
}