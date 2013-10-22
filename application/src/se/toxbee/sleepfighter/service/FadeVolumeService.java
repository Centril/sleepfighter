package se.toxbee.sleepfighter.service;

import se.toxbee.sleepfighter.audio.AudioDriver;
import se.toxbee.sleepfighter.utils.debug.Debug;
import se.toxbee.sleepfighter.SFApplication;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

/**
 * Used to fade in the volume of the ringtone once the speech is over.
 */
public class FadeVolumeService extends Service {

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
		Debug.d("on start");

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
					Debug.d("volume: " + curVol);

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
