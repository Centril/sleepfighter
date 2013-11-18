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
package se.toxbee.sleepfighter.android.utils;

import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.view.ViewConfiguration;

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
