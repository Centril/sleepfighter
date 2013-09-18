package se.chalmers.dat255.sleepfighter.activities;

import java.util.List;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
		TextView timeTextView = (TextView) convertView
				.findViewById(R.id.time_view);

		// The alarm associated with the row
		Alarm alarm = getItem(position);

		// Set properties of view elements to reflect model state
		checkBox.setChecked(alarm.isActivated());

		String timeText = String.format("%02d", alarm.getHour()) + ":"
				+ String.format("%02d", alarm.getMinute());
		timeTextView.setText(timeText);

		// TODO show more properties of Alarm

		return convertView;
	}

}
