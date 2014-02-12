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
