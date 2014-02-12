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

package se.toxbee.sleepfighter.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import se.toxbee.sleepfighter.R;

/**
 * Class for easily making and canceling notifications for SleepFighter.
 */
public class NotificationHelper {

	// Having a constant ID makes sure only one notification entry from our app
	// will be shown at a time
	public static final int NOTIFICATION_ID = 17;

	private static NotificationHelper instance;

	private Notification notification;

	private NotificationHelper() {}

	public static synchronized NotificationHelper getInstance() {
		if (instance == null) {
			instance = new NotificationHelper();
		}
		return instance;
	}

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
	public void showNotification(Context context, String title,
			String message, PendingIntent intent) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		
		// LargeIcon is shown at the left site of the notification entry,
		// App's icon is used to make it clear which app the notification is
		// from
		Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
		builder.setLargeIcon(largeIcon);

		// The standard android black/white alarm icon is shown in the status
		// bar. Android guidelines seems to be fairly strict about the
		// appearance.
		builder.setSmallIcon(R.drawable.ic_stat_alarm);

		builder.setContentTitle(title);
		builder.setContentText(message);

		// Makes it non-removable from drawer, being able to swipe it away might
		// make it seem like it disables the alarm
		builder.setOngoing(true);

		// To prevent showing time notification was created
		builder.setWhen(0);

		// Sets what will happen when the notification is clicked
		builder.setContentIntent(intent);

		this.notification = builder.build();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, this.notification);
	}

	/**
	 * Removes the notification that has been launched using this class.
	 * 
	 * @param context
	 *            the context
	 */
	public void removeNotification(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
		this.notification = null;
	}

	/**
	 * Returns the last notification shown using this that has not been
	 * canceled.
	 * 
	 * <p>
	 * Please note that it's possible that a notification remains from previous
	 * times that app has been running, and afterwards restarted, which can't be
	 * returned here.
	 * </p>
	 * 
	 * @return the last notification shown using this that has not been
	 *         canceled, null if no notification has been shown using this
	 */
	public Notification getNotification() {
		return notification;
	}
}
