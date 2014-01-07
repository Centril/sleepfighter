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

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * ContextUtils provides utilities for {@link Context}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 16, 2013
 */
public class ContextUtils {
	/**
	 * Returns the version code set by manifest.
	 *
	 * @param ctx android os context.
	 * @return the code, or -1 if error.
	 */
	public static int versionCode( Context ctx ) {
		try {
			return ctx.getPackageManager().getPackageInfo( ctx.getPackageName(), 0 ).versionCode;
		} catch ( NameNotFoundException e ) {
			e.printStackTrace();
			return -1;
		}
	}
}
