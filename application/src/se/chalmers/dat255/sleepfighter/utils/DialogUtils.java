package se.chalmers.dat255.sleepfighter.utils;

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
