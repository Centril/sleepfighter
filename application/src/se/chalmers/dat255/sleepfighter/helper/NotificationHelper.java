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
package se.chalmers.dat255.sleepfighter.helper;

import se.chalmers.dat255.sleepfighter.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Class for easily making and canceling notifications for SleepFighter.
 */
public class NotificationHelper {

	// Having a constant ID makes sure only one notification entry from our app
	// will be shown at a time
	private static final int ID = 1;

	/**
	 * Prevent instantiation.
	 */
	private NotificationHelper() {}

	/**
	 * Shows a notification, with specified content, for SleepFighter.
	 * 
	 * When using this, any previous notification will be replaced with the new
	 * one.
	 * 
	 * @param context
	 *            the context
	 * @param title
	 *            the title of the notification
	 * @param message
	 *            the message for the notification, shown below the tidle
	 * @param intent
	 *            the PendingIntent to be launched when a user click
	 */
	public static void showNotification(Context context, String title,
			String message, PendingIntent intent) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(title);
		builder.setContentText(message);
		// Makes it non-removable from drawer
		builder.setOngoing(true);
		// To prevent showing time notification was created
		builder.setWhen(0);
		builder.setContentIntent(intent);

		Notification notification = builder.build();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(ID, notification);
	}

	/**
	 * Removes the notification that has been launched using this class.
	 * 
	 * @param context
	 *            the context
	 */
	public static void removeNotification(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(ID);
	}
}
