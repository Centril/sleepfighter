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

package se.toxbee.sleepfighter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;

import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.model.AlarmList;
import se.toxbee.sleepfighter.model.AlarmTimestamp;
import se.toxbee.sleepfighter.service.AlarmPlannerService;
import se.toxbee.sleepfighter.service.AlarmPlannerService.Command;

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
