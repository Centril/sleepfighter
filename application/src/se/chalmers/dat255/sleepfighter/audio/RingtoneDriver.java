package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * RingtoneDriver is the AudioDriver for ringtones.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class RingtoneDriver implements AudioDriver {
	private AudioSource source;
	private Ringtone ringtone;
	private Context context;

	@Override
	public void setSource( Context context, AudioSource source ) {
		this.source = source;

		this.context = context;

		Uri ringtoneUri = Uri.parse( source.getUri() );
		this.ringtone = RingtoneManager.getRingtone( context, ringtoneUri );
	}

	@Override
	public AudioSource getSource() {
		return this.source;
	}

	@Override
	public String printSourceName() {
		return this.ringtone.getTitle( context );
	}

	@Override
	public void play( AudioConfig config ) {
		// TODO: Use volume and stuff, use MediaPlayer maybe?
		this.ringtone.setStreamType( AudioManager.STREAM_ALARM );
		this.ringtone.play();
	}

	@Override
	public void stop() {
		this.ringtone.stop();
	}
}