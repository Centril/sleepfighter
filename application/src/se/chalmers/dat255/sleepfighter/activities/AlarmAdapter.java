package se.chalmers.dat255.sleepfighter.activities;

import java.util.List;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class AlarmAdapter extends ArrayAdapter<Alarm> {

	public AlarmAdapter(Context context, List<Alarm> alarms) {
		super(context, 0, alarms);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// A view isn't being recycled, so make a new one from definition
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.alarm_list_item, null);
		}

		TextView timeTextView = (TextView) convertView
				.findViewById(R.id.time_view);
		TextView nameTextView = (TextView) convertView
				.findViewById(R.id.name_view);

		// The alarm associated with the row
		final Alarm alarm = getItem(position);

		// Set properties of view elements to reflect model state
		this.setupActivatedSwitch( alarm, convertView );

		String timeText = alarm.getTimeString();
		timeTextView.setText(timeText);

		String name = alarm.getName();
		nameTextView.setText(name);

		// TODO show more properties of Alarm

		return convertView;
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
