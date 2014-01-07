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

/**
 * MeasureUtil provides utilities for measurement.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 11, 2013
 */
public class MeasureUtil {
	public static float dpFromPx( Context ctx, float px ) {
		return px / ctx.getResources().getDisplayMetrics().density;
	}

	public static float pxFromDp( Context ctx, float dp ) {
		return dp * ctx.getResources().getDisplayMetrics().density;
	}

	public static float pxToSp( Context ctx, float px ) {
	    return px / ctx.getResources().getDisplayMetrics().scaledDensity;
	}
}
