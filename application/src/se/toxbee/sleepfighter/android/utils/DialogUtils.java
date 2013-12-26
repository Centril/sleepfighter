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

import se.toxbee.sleepfighter.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

/**
 * Utility class for Android dialogs.
 */
public class DialogUtils {
	private static DialogInterface.OnClickListener NOOP_CLICK_LISTENER;

	/**
	 * Returns a lazy-created noop onClickListener.
	 *
	 * @return the listener.
	 */
	public static DialogInterface.OnClickListener getNoopClickListener() {
		if ( NOOP_CLICK_LISTENER == null ) {
			NOOP_CLICK_LISTENER = new OnClickListener() {
				@Override
				public void onClick( DialogInterface dialog, int which ) {
				}
			};
		}

		return NOOP_CLICK_LISTENER;
	}

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

	public static void showMessageDialog(String message, Context context) {
		String okString = context.getResources().getString(android.R.string.ok);

		AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message).setPositiveButton(okString, null);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/*
	 * A message box with an ok button, and a "do not show this again" checkbox. 
	 * prefsName is the name of the preference used to save whether the 
	 * checkbox has been checked, and that we therefore should not show this dialog anymore. 
	 * Every dialog of this type should have a unique preference name!
	 */
	public static void showDoNotShowAgainMessageBox(final Context context, final String title, 
			final String message, final String prefsName) {
		
		final String CHECKED = "checked";
		final String NOT_CHECKED = "not_checked";
		final String PREF_FILE_NAME = "do_not_show_again_preferences";
		
		
		// inflate dialog
	    AlertDialog.Builder adb = new AlertDialog.Builder(context);
        LayoutInflater adbInflater = LayoutInflater.from(context);
        View eulaLayout = adbInflater.inflate(R.layout.do_now_show_again_messagebox, null);
        
        // get checkbox
        final CheckBox dontShowAgain = (CheckBox) eulaLayout.findViewById(R.id.skip);
        
        adb.setView(eulaLayout);
        adb.setTitle(title);
        adb.setMessage(Html.fromHtml(message));
        
        // add ok button. 
        adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	
                String checkBoxResult = NOT_CHECKED;
                if (dontShowAgain.isChecked())
                    checkBoxResult = CHECKED;
                
                SharedPreferences settings = context.getSharedPreferences(PREF_FILE_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(prefsName, checkBoxResult);
                // Commit the edits!
                editor.commit();
                return;
            }
        });

        SharedPreferences settings = context.getSharedPreferences(PREF_FILE_NAME, 0);
        String skipMessage = settings.getString(prefsName, NOT_CHECKED);
        if (!skipMessage.equals(CHECKED))
            adb.show();
    }

}
