package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.debug.Debug;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * Responsible for managing the audio of the alarms. 
 * Although right now it only plays a single tune.
 */
public class AlarmAudioManager {

	private static AlarmAudioManager instance = null;

	protected AlarmAudioManager() {
	}

	/*
	 * The class is a singleton for now. We'll probably fix this later. 
	 */
	public static AlarmAudioManager getInstance() {
		if (instance == null) {
			instance = new AlarmAudioManager();
		}
		return instance;
	}
	
	private MediaPlayer player;
	
	public void setup(Context context) {
		try {
			 AssetFileDescriptor afd = context.getAssets().openFd("on_time.ogg");
			 player = new MediaPlayer();
			 player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			 player.prepare();
			 player.setLooping(true);
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
	public void play() {
		try {
		player.start(); 
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	public void stop() {
		try {
			player.stop();
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
}
