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

package se.toxbee.sleepfighter.android.component.secondpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TimePicker;

import se.toxbee.sleepfighter.R;

/**
 * A dialog that prompts the user for the time of day using a {@link TimePicker}.
 */
public class SecondTimePickerDialog extends AlertDialog implements OnClickListener {
    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {
        /**
         * @param view The view associated with this listener.
         * @param hourOfDay The hour that was set.
         * @param minute The minute that was set.
         */
        void onTimeSet( SecondTimePicker view, int hourOfDay, int minute, int seconds );
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECONDS = "seconds";
    private static final String IS_24_HOUR = "is24hour";
    
    private final SecondTimePicker mTimePicker;
    private final OnTimeSetListener mCallback;
        
    int mInitialHourOfDay;
    int mInitialMinute;
    int mInitialSeconds;    
    boolean mIs24HourView;

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     * @param hourOfDay The initial hour.
     * @param minute The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public SecondTimePickerDialog(Context context,
            OnTimeSetListener callBack,
            int hourOfDay, int minute, int seconds, boolean is24HourView) {
    	
        this(context, 0,
                callBack, hourOfDay, minute, seconds, is24HourView);
    }

    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     * @param hourOfDay The initial hour.
     * @param minute The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
	public SecondTimePickerDialog( Context context, int theme,
			OnTimeSetListener callBack, int hourOfDay, int minute, int seconds,
			boolean is24HourView ) {
		super( context, theme );
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		mCallback = callBack;
		mInitialHourOfDay = hourOfDay;
		mInitialMinute = minute;
		mInitialSeconds = seconds;
		mIs24HourView = is24HourView;

		setTitle( R.string.second_time_picker_title );

		// get date_time_done if available.
		int res_date_time_done = Resources.getSystem().getIdentifier( "date_time_done", "string", "android" );
		if ( res_date_time_done == 0 ) {
			res_date_time_done = android.R.string.ok;
		}

		setButton( DialogInterface.BUTTON_POSITIVE, context.getText( res_date_time_done ), this );
		setButton( DialogInterface.BUTTON_NEGATIVE, context.getText( android.R.string.cancel ), (OnClickListener) null );
		// setIcon(android.R.drawable.ic_dialog_time);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View view = inflater.inflate( R.layout.second_time_picker_dialog, null );
		setView( view );
		mTimePicker = (SecondTimePicker) view.findViewById( R.id.timePicker );

		// initialize state
		mTimePicker.setCurrentHour( mInitialHourOfDay );
		mTimePicker.setCurrentMinute( mInitialMinute );
		mTimePicker.setCurrentSecond( mInitialSeconds );
		mTimePicker.setIs24HourView( mIs24HourView );
	}
    
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(), 
                    mTimePicker.getCurrentMinute(), mTimePicker.getCurrentSeconds());
        }
    }
    
    public void updateTime(int hourOfDay, int minutOfHour, int seconds) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minutOfHour);
        mTimePicker.setCurrentSecond(seconds);
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putInt(SECONDS, mTimePicker.getCurrentSeconds());
        state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
        return state;
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        int seconds = savedInstanceState.getInt(SECONDS);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setCurrentSecond(seconds);
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
    }
}
