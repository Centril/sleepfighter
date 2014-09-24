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

package se.toxbee.sleepfighter.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.audio.AudioDriver;

/**
 * Used to fade in the volume of the ringtone once the speech is over.
 */
public class FadeVolumeService extends Service {
	private static final String TAG = FadeVolumeService.class.getSimpleName();
	private Runnable increaseVol;
	private Handler h;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		h = new Handler();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d( TAG, "on start" );

		if(intent == null) {
			this.stopSelf();
			return Service.START_STICKY;
		}

		Bundle extras = intent.getExtras(); 

		if(extras.get("alarm_volume") == null) {
			this.stopSelf();
			return Service.START_STICKY;
		} 
		
		
		final int alarmVolume = (Integer)extras.get("alarm_volume");

		increaseVol = new Runnable(){
			public void run(){
				final AudioDriver d = SFApplication.get().getAudioDriver();
				
				// if the alarm has been turned of then d is null. 
				if(d == null) {
					FadeVolumeService.this.stopSelf();
					return;
				}
				
				int curVol = d.getVolume();

				int origVolume = alarmVolume;

				if(curVol == origVolume) {
					FadeVolumeService.this.stopSelf();
				}

				if(curVol < origVolume){
					curVol += 1;
					Log.d( TAG, "volume: " + curVol);

					// wait 0.02seconds. 
					h.postDelayed(increaseVol, 20);
					d.setVolume(curVol);
				} 
			}
		};
		h.post(increaseVol);


		return Service.START_STICKY;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
