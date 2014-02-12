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