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
import android.widget.Toast;

/**
 * This is not one of those you make bread delicious with,<br/>
 * it is an utility for showing toasts to the user.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 13, 2013
 */
public class Toaster {
	private static final int DEFAULT_DURATION = Toast.LENGTH_LONG;

	public static void out( Context ctx, String msg, int dur ) {
		Toast.makeText( ctx, msg, dur ).show();
	}

	public static void out( Context ctx, String msg ) {
		Toast.makeText( ctx, msg, DEFAULT_DURATION ).show();
	}

	public static void out( Context ctx, int res_id, int dur ) {
		out( ctx, ctx.getResources().getString( res_id ), dur );
	}

	public static void out( Context ctx, int res_id ) {
		out( ctx, ctx.getResources().getString( res_id ) );
	}
}