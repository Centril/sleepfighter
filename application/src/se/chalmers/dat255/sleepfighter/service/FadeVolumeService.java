package se.chalmers.dat255.sleepfighter.service;

import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.audio.AudioDriver;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

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
		
		 Bundle extras = intent.getExtras(); 
		final int alarmVolume = (Integer)extras.get("alarm_volume");
		 
		increaseVol = new Runnable(){
			public void run(){
				
				
				final AudioDriver d = SFApplication.get().getAudioDriver();
				int curVol = d.getVolume();

				int origVolume = alarmVolume;

				if(curVol == origVolume) {
					FadeVolumeService.this.stopSelf();
				}
				
				if(curVol < origVolume){
					curVol += 1;
					Debug.d("volume: " + curVol);

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
