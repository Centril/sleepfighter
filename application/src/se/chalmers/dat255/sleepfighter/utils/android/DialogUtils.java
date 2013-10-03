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
package se.chalmers.dat255.sleepfighter.utils.android;

import se.chalmers.dat255.sleepfighter.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * Utility class for Android dialogs.
 */
public class DialogUtils {
	
	/**
	 * Prevent instantiation.
	 */
	private DialogUtils() {}
	
	/**
	 * Shows a yes/no dialogue, with the specified message, to the user.
	 * 
	 * @param message
	 *            the message
	 * @param context
	 *            the current context
	 * @param yesAction
	 *            the callback to be run when the user answers yes
	 */
	public static void showConfirmationDialog(String message, Context context,
			DialogInterface.OnClickListener yesAction) {
		showConfirmationDialog(message, context, yesAction, null);
	}

	/**
	 * Shows a yes/no dialogue, with the specified message, to the user.
	 * 
	 * @param message
	 *            the message
	 * @param context
	 *            the current context
	 * @param yesAction
	 *            the callback to be run when the user answers yes
	 * @param yesAction
	 *            the callback to be run when the user answers no
	 */
	public static void showConfirmationDialog(String message, Context context,
			DialogInterface.OnClickListener yesAction, OnClickListener noAction) {
		String yesString = context.getResources().getString(R.string.yes);
		String noString = context.getResources().getString(R.string.no);

		AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message)
				.setPositiveButton(yesString, yesAction)
				.setNegativeButton(noString, noAction);
		AlertDialog alert = builder.create();
		alert.show();
	}
}
