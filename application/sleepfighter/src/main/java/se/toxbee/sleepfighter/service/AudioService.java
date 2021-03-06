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

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import se.toxbee.sleepfighter.helper.NotificationHelper;

/**
 * Service for handling local audio playback using a MediaPlayer.
 * 
 * <p>
 * Audio played will go through {@link AudioManager#STREAM_ALARM} and played
 * until a {@link #ACTION_STOP} is sent to the service. Single tracks
 * playlists, and remote URI:s can be played through the service,
 * see {@link #ACTION_PLAY_TRACK}, {@link #ACTION_PLAY_PLAYLIST}, {@link #ACTION_PLAY_REMOTE} for details.
 * </p>
 */
public class AudioService extends Service implements OnPreparedListener,
		OnErrorListener, OnCompletionListener {

	private static final String TAG = "AudioService";

	/**
	 * Action for starting playback of a single audio track.
	 * <p>Required extras:</p>
	 * <ul>
	 * <li>A {@link Uri}, using key defined by {@link #BUNDLE_URI} - the Uri for
	 * the track to be played</li>
	 * <li>A float (0-1), using key defined by {@link #BUNDLE_FLOAT_VOLUME} -
	 * the initial volume</li>
	 * </ul>
	 */
	public static final String ACTION_PLAY_TRACK = "se.toxbee.sleepfighter.service.AudioService.PLAY_TRACK";

	/**
	 * Action for starting playback streaming from a remote URI source.
	 * <p>Required extras:</p>
	 * <ul>
	 * <li>A {@link Uri}, using key defined by {@link #BUNDLE_URI} - the Uri for
	 * the remote source to be played.</li>
	 * <li>A float (0-1), using key defined by {@link #BUNDLE_FLOAT_VOLUME} - the initial volume</li>
	 * </ul>
	 */
	public static final String ACTION_PLAY_REMOTE = "se.toxbee.sleepfighter.service.AudioService.PLAY_REMOTE";

	/**
	 * Action for stopping any playback.
	 */
	public static final String ACTION_STOP = "se.toxbee.sleepfighter.service.AudioService.STOP";

	/**
	 * Action for modifying the volume of what's currently playing. 
	 * <p>Required extras:</p>
	 * <ul>
	 * <li>A float, using key defined by {@link #BUNDLE_FLOAT_VOLUME} - the
	 * volume (0-1) for the playing audio</li>
	 * </ul>
	 */
	public static final String ACTION_VOLUME = "se.toxbee.sleepfighter.service.AudioService.VOLUME";

	/**
	 * Action for starting playback of a playlist.
	 * <p>Required extras:</p>
	 * <ul>
	 * <li>A {@code String[]}, using key defined by {@link #BUNDLE_PLAYLIST} -
	 * paths to the tracks to be played</li>
	 * <li>A float (0-1), using key defined by {@link #BUNDLE_FLOAT_VOLUME} -
	 * the initial volume</li>
	 * </ul>
	 */
	public static final String ACTION_PLAY_PLAYLIST = "se.toxbee.sleepfighter.service.AudioService.PLAY_PLAYLIST";

	public static final String BUNDLE_URI = "audio_uri";
	public static final String BUNDLE_FLOAT_VOLUME = "audio_volume";
	public static final String BUNDLE_PLAYLIST = "audio_playlist";

	/**
	 * The states the MediaPlayer can be in.
	 */
	private enum State {
		PREPARING,
		PLAYING,
		STOPPED
	}

	/**
	 * The available sources this can play from.
	 */
	private enum SourceType {
		SINGLE,
		PLAYLIST,
		REMOTE
	}

	private MediaPlayer player;
	private State state;
	private SourceType sourceType;

	// Variables only relevant when source is playlist
	// Might be better if handled elsewhere
	private String[] tracks;
	private int track;

	@Override
	public void onCreate() {
		super.onCreate();
		this.player = new MediaPlayer();
		this.player.setAudioStreamType(AudioManager.STREAM_ALARM);
		this.player.setOnPreparedListener(this);
		this.player.setOnErrorListener(this);
		this.player.setOnCompletionListener(this);

		// Makes MediaPlayer hold a wake lock while playing
		this.player.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
		
		this.state = State.STOPPED;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getAction();
		Log.d(TAG, "start action " + action);

		if (ACTION_PLAY_TRACK.equals(action)) {
			handleVolumeAction(intent);
			handlePlayAction(intent);
		} else if (ACTION_STOP.equals(action)) {
			handleStopAction();
		} else if (ACTION_VOLUME.equals(action)) {
			handleVolumeAction(intent);
		} else if (ACTION_PLAY_PLAYLIST.equals(action)) {
			handleVolumeAction(intent);
			handlePlaylistPlay(intent);
		} else if (ACTION_PLAY_REMOTE.equals(action) ) {
			handleVolumeAction(intent);
			handleRemotePlay(intent);
		}

		return START_NOT_STICKY;
	}

	private void handleRemotePlay( Intent intent ) {
		Uri uri = this.getBundledUri( intent );

		this.stopIfPlaying();

		setSourceType(SourceType.REMOTE);

		// Loop if only one track
		this.player.setLooping(true);
		try {
			this.player.setDataSource(uri.toString());
		} catch (Exception e) {
			handleException(e);
			return;
		}
		prepareAndPlay();
	}

	private void handlePlayAction(Intent intent) {
		Uri uri = this.getBundledUri( intent );

		this.stopIfPlaying();

		setSourceType(SourceType.SINGLE);

		// Loop if only one track
		this.player.setLooping(true);
		try {
			this.player.setDataSource(this, uri);
		} catch (Exception e) {
			handleException(e);
			return;
		}
		prepareAndPlay();
	}

	private Uri getBundledUri( Intent intent ) {
		Object o = intent.getParcelableExtra(BUNDLE_URI);
		if (!(o instanceof Uri)) {
			throw new IllegalArgumentException(
					"No Uri bundled with PLAY action");
		}
		Uri uri = (Uri) o;
		return uri;
	}

	private void handlePlaylistPlay(Intent intent) {
		String[] tracks = intent.getStringArrayExtra(BUNDLE_PLAYLIST);
		if (tracks == null) {
			throw new IllegalArgumentException(
					"No String[] bundled with PLAY_PLAYLIST action");
		}
		if (tracks.length == 0) {
			throw new IllegalArgumentException(
					"Empty String[] bundled with PLAY_PLAYLIST action");
		}

		this.stopIfPlaying();

		setSourceType(SourceType.PLAYLIST);
		this.tracks = tracks;

		// Don't let player loop, handle ourselves in onCompletion
		this.player.setLooping(false);

		setPlaylistDataSource();
		prepareAndPlay();
	}

	private void stopIfPlaying() {
		// Stop if previously playing
		if (this.state != State.STOPPED) {
			stopPlayback();
		}
	}

	private void handleStopAction() {
		stopPlayback();
		stopSelf();
	}

	private void handleVolumeAction(Intent intent) {
		setMaxAlarmStreamVolume();
		float volume = intent.getFloatExtra(BUNDLE_FLOAT_VOLUME, -1f);
		setVolume(volume);
	}

	/**
	 * Sets the alarm stream volume to its maximum, used since this controls its
	 * volume through the MediaPlayer which offers more fine-tuning.
	 */
	private void setMaxAlarmStreamVolume() {
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
		audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVol, 0);
	}

	/**
	 * Prepares playback of the data source set on MediaPlayer asynchronously
	 * and starts playback when prepared.
	 */
	private void prepareAndPlay() {
		try {
			this.player.prepareAsync();
			this.state = State.PREPARING;
		} catch (Exception e) {
			handleException(e);
		}

		// Without using startForeground, audio playback will stop if the user
		// closes the app from the app drawer.
		// Doing this requires the service to supply a notification. If a
		// notification is shown using NotificationHelper, that one is used.
		Notification notification = NotificationHelper.getInstance()
				.getNotification();
		if(notification != null) {
			startForeground(NotificationHelper.NOTIFICATION_ID, notification);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// Don't start if state no longer is preparing
		if (this.state == State.PREPARING) {
			mp.start();
			this.state = State.PLAYING;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		this.state = State.STOPPED;
		mp.reset();
		if (this.sourceType == SourceType.PLAYLIST) {
			incTrack();
			prepareAndPlay();
		} else {
			throw new RuntimeException("Non playlist complete");
		}
	}

	/**
	 * Increases the playlist track.
	 */
	private void incTrack() {
		if (this.sourceType != SourceType.PLAYLIST) {
			throw new RuntimeException(
					"Non playlist source type can not use incTrack");
		}
		// Increase track by one and wrap around to first if at last
		this.track = (this.track + 1) % this.tracks.length;
		setPlaylistDataSource();
	}

	/**
	 * Sets the data source of the player to the current track in the playlist,
	 * defined by {@link #track}.
	 */
	private void setPlaylistDataSource() {
		try {
			this.player.setDataSource(this.tracks[this.track]);
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Stops any ongoing audio playback.
	 */
	private void stopPlayback() {
		if (this.state == State.PLAYING) {
			this.player.stop();
		}
		this.player.reset();

		if (this.sourceType == SourceType.PLAYLIST) {
			// Reset playlist-only variables
			this.tracks = null;
			this.track = 0;
		}

		this.state = State.STOPPED;

		setSourceType(null);

		// Make sure audio service no longer keeps the app alive
		stopForeground(false);
	}

	/**
	 * Sets the source type for the player.
	 * 
	 * @param sourceType
	 *            the source type
	 */
	private void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * Sets the volume for the audio played through the MediaPlayer.
	 * 
	 * @param volume
	 *            the volume as a float 0-1
	 */
	private void setVolume(float volume) {
		if (volume > 1 || volume < 0) {
			throw new IllegalArgumentException("No valid volume bundled");
		}

		this.player.setVolume(volume, volume);
	}

	@Override
	public void onDestroy() {
		stopPlayback();
		this.player.release();
		super.onDestroy();
	}

	/**
	 * Handle an exception by stopping and logging.
	 * 
	 * @param exception
	 *            the exception
	 */
	private void handleException(Exception exception) {
		stopPlayback();
		Log.e(TAG, "AudioService exception", exception);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		stopPlayback();
		Log.e(TAG, "MediaPlayer error = (" + what + ", " + extra + ")");
		return true;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
