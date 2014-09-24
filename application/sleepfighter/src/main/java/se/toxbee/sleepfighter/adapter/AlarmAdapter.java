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

package se.toxbee.sleepfighter.adapter;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.List;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.component.secondpicker.SecondTimePicker;
import se.toxbee.sleepfighter.android.component.secondpicker.SecondTimePickerDialog;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.time.AlarmTime;
import se.toxbee.sleepfighter.model.time.CountdownTime;
import se.toxbee.sleepfighter.model.time.ExactTime;
import se.toxbee.sleepfighter.text.DateTextUtils;
import se.toxbee.commons.string.StringUtils;

public class AlarmAdapter extends ArrayAdapter<Alarm> {
	public AlarmAdapter( Context context, List<Alarm> alarms ) {
		super( context, 0, alarms );
	}

	private static class ViewHolder {
		TextView name;
		View timeContainer;
		TextView time;
		TextView seconds;
		TextView weekdays;
		CompoundButton activatedSwitch;
		View activatedBackground;

		public ViewHolder( View cv ) {
			// Find all views needed.
			name = (TextView) cv.findViewById( R.id.name_view );
			timeContainer = cv.findViewById( R.id.time_view_container );
			time = (TextView) cv.findViewById( R.id.time_view );
			seconds = (TextView) cv.findViewById( R.id.time_view_seconds );
			weekdays = (TextView) cv.findViewById( R.id.weekdaysText );
			activatedSwitch = (CompoundButton) cv.findViewById( R.id.activated );
			activatedBackground = cv.findViewById( R.id.activated_background );
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			// A view isn't being recycled, so make a new one from definition
			convertView = LayoutInflater.from( getContext() ).inflate( R.layout.alarm_list_item_htctheme, parent, false );

			// Make & store holder.
			holder = new ViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			// Recycle holder.
			holder = (ViewHolder) convertView.getTag();
		}

		// Get alarm associated with the row.
		final Alarm alarm = this.getItem( position );

		// Setup the view.
		this.makeTimeTextViewHitboxBigger( holder );
		this.setupActivatedSwitch( alarm, holder );
		this.setupTimeText( alarm, holder );
		this.setupName( alarm, holder );
		this.setupWeekdays( alarm, holder );

		return convertView;
	}

	private void makeTimeTextViewHitboxBigger( final ViewHolder holder ) {
		holder.timeContainer.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				holder.time.performClick();
			}
		} );
	}

	public void pickTime( final Alarm alarm ) {
		if ( alarm.isCountdown() ) {
			this.pickCountdownTime( alarm );
		} else {
			this.pickNormalTime( alarm );
		}
	}

	public void pickNormalTime( final Alarm alarm ) {
		OnTimeSetListener onTimePickerSet = new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int h, int m ) {
				alarm.setTime( new ExactTime( h, m ) );
			}
		};

		// TODO possibly use some way that doesn't make the dialog close on rotate
		AlarmTime time = alarm.getTime();
		TimePickerDialog tpd = new TimePickerDialog(
			getContext(), onTimePickerSet,
			time.getHour(), time.getMinute(),
			true
		);

		tpd.show();
	}

	public void pickCountdownTime( final Alarm alarm ) {
		SecondTimePickerDialog.OnTimeSetListener onTimePickerSet = new SecondTimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet( SecondTimePicker view, int h, int m, int s ) {
				alarm.setTime( new CountdownTime( h, m, s ) );
			}
		};

		// TODO possibly use some way that doesn't make the dialog close on rotate
		AlarmTime time = alarm.getTime();
		time.refresh();

		SecondTimePickerDialog tpd = new SecondTimePickerDialog(
			getContext(), onTimePickerSet,
			time.getHour(), time.getMinute(), time.getSecond(),
			true
		);

		tpd.show();
	}

	private void setupTimeText( final Alarm alarm, ViewHolder holder ) {
		AlarmTime time = alarm.getTime();
		time.refresh();

		// Set countdown if needed.
		if ( alarm.isCountdown() ) {
			holder.seconds.setVisibility( View.VISIBLE );
			holder.seconds.setText( StringUtils.joinTime( time.getSecond() ) + "\"" );
		} else {
			holder.seconds.setVisibility( View.INVISIBLE );
		}

		holder.time.setText( time.getTimeString() );
		holder.time.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				pickTime( alarm );
			}
		});
	}

	private void setupName( final Alarm alarm, ViewHolder holder ) {
		holder.name.setText( alarm.printName() );
		holder.name.setEnabled( alarm.isActivated() );
	}

	private void setupWeekdays( final Alarm alarm, ViewHolder holder ) {
		holder.weekdays.setText( DateTextUtils.makeEnabledDaysText( alarm ) );
	}

	private void setupActivatedSwitch( final Alarm alarm, final ViewHolder holder ) {
		final CompoundButton activated = holder.activatedSwitch;

		// Makes sure that previous alarm for a recycled view won't get changed
		// when setting initial value
		activated.setOnCheckedChangeListener( null );
		activated.setChecked( alarm.isActivated() );
		activated.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
				alarm.setActivated( isChecked );
			}
		} );

		// Allow pressing in area next to checkbox/switch to toggle
		holder.activatedBackground.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activated.toggle();
			}
		});
	}
}
