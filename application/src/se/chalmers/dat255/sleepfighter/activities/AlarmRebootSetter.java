package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.activities.AlarmService.Command;
import se.chalmers.dat255.sleepfighter.model.Alarm;
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
    	for ( Alarm alarm : SFApplication.get().getAlarms() ) {
    		AlarmService.call( context, Command.CREATE, alarm );
    	}
    }
}