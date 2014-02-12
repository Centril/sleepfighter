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

package se.toxbee.sleepfighter.android.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

/**
 * ActivityUtils provides utilities for activities.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 13, 2013
 */
public class ActivityUtils {
	/**
	 * Show the triple-dot action bar overflow icon even on devices with a dedicated menu button.
	 * 
	 * <p> Solution from stackoverflow post found 
	 * <a href="http://stackoverflow.com/questions/9286822/how-to-force-use-of-overflow-menu-on-devices-with-menu-button/11438245#11438245">here</a>.
	 * </p>
	 */
	public static void forceActionBarOverflow( Application app ) {
	    try {
			ViewConfiguration config = ViewConfiguration.get( app );
			Field menuKeyField = ViewConfiguration.class.getDeclaredField( "sHasPermanentMenuKey" );
			if ( menuKeyField != null ) {
				menuKeyField.setAccessible( true );
				menuKeyField.setBoolean( config, false );
			}
	    } catch (Exception ex) {
	        // Ignore
	    }
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static void setupStandardActionBar(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			activity.getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
}
