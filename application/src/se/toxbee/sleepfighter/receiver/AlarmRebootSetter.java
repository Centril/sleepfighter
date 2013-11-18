/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 * 
 * This file is part of SleepFighter.
 * 
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package se.toxbee.sleepfighter.receiver;

import org.joda.time.DateTime;

import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.model.AlarmList;
import se.toxbee.sleepfighter.model.AlarmTimestamp;
import se.toxbee.sleepfighter.service.AlarmPlannerService;
import se.toxbee.sleepfighter.service.AlarmPlannerService.Command;
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
