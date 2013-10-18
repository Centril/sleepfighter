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
package se.chalmers.dat255.sleepfighter.android.preference;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.provider.Settings;
import android.util.AttributeSet;

/**
 * InitializableRingtonePreference provides {@link #setInitialUri(Uri)}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class InitializableRingtonePreference extends RingtonePreference {
	private Uri initialUri;

	/**
	 * Sets the initial URI to use upon inflation.
	 *
	 * @param uri the initial URI.
	 */
	public void setInitialUri( Uri uri ) {
		this.initialUri = uri;
	}

	Uri[] getAllRingtones() {
		
		RingtoneManager ringtoneMgr = new RingtoneManager(this.getContext());
		ringtoneMgr.setType(RingtoneManager.TYPE_ALL);
		Cursor alarmsCursor = ringtoneMgr.getCursor();
		int alarmsCount = alarmsCursor.getCount();
		if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
			return null;
		}
		Uri[] alarms = new Uri[alarmsCount];
		while(!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
			int currentPosition = alarmsCursor.getPosition();
			alarms[currentPosition] = ringtoneMgr.getRingtoneUri(currentPosition);
			//Debug.d("sound uri " +alarms[currentPosition]);
			
			Debug.d("compare: " + Settings.System.DEFAULT_ALARM_ALERT_URI.compareTo(alarms[currentPosition]));
		}
		alarmsCursor.close();
		return alarms;
	}

	@Override
	protected Uri onRestoreRingtone() {
		
		//Debug.d("current uri " + this.initialUri);
		
		//getAllRingtones();
		
		return this.initialUri;

	}

	/**
	 * @see RingtonePreference#RingtonePreference(Context)
	 */
	public InitializableRingtonePreference( Context context ) {
		super( context );
	}

	/**
	 * @see RingtonePreference#RingtonePreference(Context, AttributeSet)
	 */
	public InitializableRingtonePreference( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}

	/**
	 * @see RingtonePreference#RingtonePreference(Context, AttributeSet, int)
	 */
	public InitializableRingtonePreference( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
	}
}
