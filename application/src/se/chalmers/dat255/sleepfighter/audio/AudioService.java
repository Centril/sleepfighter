package se.chalmers.dat255.sleepfighter.audio;

import java.io.IOException;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.IBinder;

/**
 * Service for handling audio playback using a MediaPlayer.
 */
public class AudioService extends Service implements OnPreparedListener,
		OnErrorListener {

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
		this.state = State.Stopped;
		this.player.setOnPreparedListener(this);
		this.player.setOnErrorListener(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getAction();
		if (ACTION_PLAY.equals(action)) {
			// TODO null checks etc
			String uriString = intent.getStringExtra(BUNDLE_URI);
			Uri uri = Uri.parse(uriString);
			play(uri);
		} else if (ACTION_STOP.equals(action)) {
			stop();
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
		// TODO handle exception differently
		try {
			player.setDataSource(this, uri);
			player.prepareAsync();
		} catch (IllegalArgumentException e) {
			Debug.e(e);
		} catch (SecurityException e) {
			Debug.e(e);
		} catch (IllegalStateException e) {
			Debug.e(e);
		} catch (IOException e) {
			Debug.e(e);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (state == State.Preparing) {
			state = State.Playing;
			mp.setLooping(true);
			mp.start();
		}
	}

	/**
	 * Stop any ongoing audio playback.
	 */
	private void stop() {
		if (state == State.Playing) {
			player.stop();
		}
		player.reset();
		state = State.Stopped;
		stopSelf();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		stop();
		return true;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
