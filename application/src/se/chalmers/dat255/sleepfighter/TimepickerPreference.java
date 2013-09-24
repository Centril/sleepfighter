package se.chalmers.dat255.sleepfighter;

import se.chalmers.dat255.sleepfighter.debug.Debug;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

/**
 * A DialogPreference with containing a TimePicker.
 * 
 * @author Hassel
 *
 */
public class TimepickerPreference extends DialogPreference {
	
	private int hour;
	private int minute;
	private TimePicker tp;
	
	private boolean hasBeenCreated = false;
	private int lastHour;
	private int lastMinute;
	
	public TimepickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected View onCreateDialogView() {

		if(!hasBeenCreated) {
			Debug.d("created time picker preference view");
			tp = new TimePicker(getContext());
			tp.setIs24HourView(true);
			tp.setCurrentHour(0);
			tp.setCurrentMinute(0);
		} else {
			tp = new TimePicker(getContext());
			tp.setIs24HourView(true);

			tp.setCurrentHour(lastHour);
			tp.setCurrentMinute(lastMinute);
			// else, the picker remembers the time from the last time
		}
		hasBeenCreated = true;
		
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
		
		
		if (positiveResult && callChangeListener(time)) {
			lastHour = hour;
			lastMinute = minute;
			
            persistString(time);
        }
	}
	
	/**
	 * @return the picked hour.
	 */
	public int getHour() {
		return hour;
	}
	
	/**
	 * @return the picked minute.
	 */
	public int getMinute() {
		return minute;
	}
}
