package se.chalmers.dat255.sleepfighter.activities;

import java.util.List;
import java.util.Locale;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.utils.DateTextUtils;
import se.chalmers.dat255.sleepfighter.utils.StringUtils;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmAdapter extends ArrayAdapter<Alarm> {

	public AlarmAdapter(Context context, List<Alarm> alarms) {
		super(context, 0, alarms);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// A view isn't being recycled, so make a new one from definition
			convertView = LayoutInflater.from(getContext()).inflate( R.layout.alarm_list_item, null);
		}


		// The alarm associated with the row
		final Alarm alarm = getItem(position);

		// Set properties of view elements to reflect model state
		this.setupActivatedSwitch( alarm, convertView );

		this.setupTimeText( alarm, convertView );

		this.setupName( alarm, convertView );

		this.setupWeekdays( alarm, convertView );
		
		return convertView;
	}

	private void setupTimeText( final Alarm alarm, View convertView ) {
		final TextView timeTextView = (TextView) convertView.findViewById( R.id.time_view );

		timeTextView.setText( alarm.getTimeString() );
		timeTextView.setOnClickListener( new OnClickListener() {

			public void onClick( View v ) {
				
				OnTimeSetListener onTimePickerSet = new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						alarm.setTime(hourOfDay, minute);
						timeTextView.setText( alarm.getTimeString() );
					}
				};
				// TODO take am/pm into account
				// And possibly use some way that doesn't make the dialog close
				// on rotate
				TimePickerDialog tpd = new TimePickerDialog(getContext(),
						onTimePickerSet, alarm.getHour(), alarm.getMinute(),
						true);
				tpd.show();
			}
		});
	}

	private void setupName( final Alarm alarm, View convertView ) {
		TextView nameTextView = (TextView) convertView.findViewById(R.id.name_view);
		String name = alarm.getName();
		nameTextView.setText(name);
	}

	private void setupWeekdays( final Alarm alarm, View convertView ) {
		TextView view = (TextView) convertView.findViewById(R.id.weekdaysText);

		// Compute weekday names & join.
		final int indiceLength = 2;
		String[] names = DateTextUtils.getWeekdayNames( indiceLength, Locale.getDefault() );

		SpannableString text = new SpannableString( StringUtils.WS_JOINER.join( names ) );

		// Create spans for enabled days.
		boolean[] enabledDays = alarm.getEnabledDays();

		if ( enabledDays.length != names.length || names.length != 7 ) {
			throw new AssertionError("A week has 7 days, wrong array lengths!");
		}

		int start = 0;
		for ( int i = 0; i < enabledDays.length; i++ ) {
			boolean enabled = enabledDays[i];
			int length = names[i].length();

			if ( enabled ) {
				text.setSpan( new ForegroundColorSpan( Color.WHITE ), start, start + length, 0 );
			}

			start += length + 1;
		}

		view.setText( text );
	}

	private void setupActivatedSwitch( final Alarm alarm, View convertView ) {
		CompoundButton activatedSwitch = (CompoundButton) convertView.findViewById(R.id.activated);

		activatedSwitch.setChecked(alarm.isActivated());

		activatedSwitch.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
				alarm.setActivated( isChecked );
			}
		} );
	}
}