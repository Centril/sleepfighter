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
package se.chalmers.dat255.sleepfighter.audio;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * Service for handling audio playback using a MediaPlayer.
 */
public class AudioService extends Service implements OnPreparedListener,
		OnErrorListener {

	private static final String TAG = "AudioService";

	public static final String ACTION_PLAY = "se.chalmers.dat255.sleepfighter.audio.AudioService.PLAY";
	public static final String ACTION_STOP = "se.chalmers.dat255.sleepfighter.audio.AudioService.STOP";
	public static final String BUNDLE_URI = "audio_uri";

	public enum State {
		Preparing,
		Playing,
		Stopped
	}

	private MediaPlayer player;
	private State state;

	@Override
	public void onCreate() {
		super.onCreate();

		this.player = new MediaPlayer();
		this.player.setAudioStreamType(AudioManager.STREAM_ALARM);
		this.player.setLooping(true);
		this.player.setOnPreparedListener(this);
		this.player.setOnErrorListener(this);

		// Makes MediaPlayer hold a wake lock while playing
		this.player.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
		
		this.state = State.Stopped;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getAction();
		Log.d(TAG, "start action " + action);

		if (ACTION_PLAY.equals(action)) {
			Object o = intent.getParcelableExtra(BUNDLE_URI);
			if(!(o instanceof Uri)) {
				throw new IllegalArgumentException("No Uri bundled with PLAY action");
			}
			Uri uri = (Uri) o;
			play(uri);
		} else if (ACTION_STOP.equals(action)) {
			stopPlayback();
			stopSelf();
		}
		return START_NOT_STICKY;
	}

	/**
	 * Starts playback of audio referenced by the given uri.
	 * 
	 * @param uri
	 *            the uri
	 */
	private void play(Uri uri) {
		// Stop if previously playing
		if (state != State.Stopped) {
			stopPlayback();
		}

		try {
			player.setDataSource(this, uri);
			player.prepareAsync();
			state = State.Preparing;
		} catch (IllegalArgumentException e) {
			handleException(e);
		} catch (SecurityException e) {
			handleException(e);
		} catch (IllegalStateException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (state == State.Preparing) {
			mp.start();
			state = State.Playing;
		}
	}

	/**
	 * Stops any ongoing audio playback.
	 */
	private void stopPlayback() {
		if (state == State.Playing) {
			player.stop();
		}
		player.reset();
		state = State.Stopped;
	}

	@Override
	public void onDestroy() {
		stopPlayback();
		player.release();
		super.onDestroy();
	}

	/**
	 * Handle an exception by stopping and logging.
	 * 
	 * @param e the exception
	 */
	private void handleException(Exception e) {
		stopPlayback();
		Log.e(TAG, "AudioService exception", e);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.e(TAG, "MediaPlayer error = (" + what + ", " + extra + ")");
		stopPlayback();
		return true;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
