package se.chalmers.dat255.sleepfighter;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimepickerPreference extends DialogPreference {
	
	private int hour;
	private int minute;
	private TimePicker tp;
	
	public TimepickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setPositiveButtonText("Okay");
		setNegativeButtonText("Cancel");
	}
	
	@Override
	protected View onCreateDialogView() {
		tp = new TimePicker(getContext());
		tp.setIs24HourView(true);
		
		tp.setCurrentHour(0);
		tp.setCurrentMinute(0);
		
		return(tp);
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			hour = tp.getCurrentHour();
			minute = tp.getCurrentMinute();
		}
		
		String time = (hour < 10 ? "0" : "") + hour + ":"
				+ (minute < 10 ? "0" : "") + minute;
		
		
		if (callChangeListener(time)) {
            persistString(time);
        }
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
}
