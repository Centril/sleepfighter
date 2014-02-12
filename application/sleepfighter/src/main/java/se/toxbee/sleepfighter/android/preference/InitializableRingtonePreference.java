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

package se.toxbee.sleepfighter.android.preference;

import android.content.Context;
import android.net.Uri;
import android.preference.RingtonePreference;
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

	@Override
	protected Uri onRestoreRingtone() {	
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
