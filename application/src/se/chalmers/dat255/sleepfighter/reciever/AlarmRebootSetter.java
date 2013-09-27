package se.chalmers.dat255.sleepfighter.reciever;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.service.AlarmPlannerService;
import se.chalmers.dat255.sleepfighter.service.AlarmPlannerService.Command;
import se.chalmers.dat255.sleepfighter.model.AlarmList;
import se.chalmers.dat255.sleepfighter.model.AlarmTimestamp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * AlarmRebootSetter is responsible for setting alarms on reboot.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 26, 2013
 */
public class AlarmRebootSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	SFApplication app = SFApplication.get();
    	AlarmList list = app.getAlarms();

		AlarmTimestamp at = list.getEarliestAlarm( new DateTime().getMillis() );
		if ( at != null ) {
    		AlarmPlannerService.call( app, Command.CREATE, at.getAlarm().getId() );
		}
    }
}