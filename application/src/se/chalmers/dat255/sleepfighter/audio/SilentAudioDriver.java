package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;

/**
 * SilentAudioDriver is a silent driver that does nothing.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class SilentAudioDriver implements AudioDriver {
	private Context context;

	public SilentAudioDriver() {
	}

	@Override
	public void setSource( Context context, AudioSource source ) {
		this.context = context;
	}

	@Override
	public String printSourceName() {
		return context.getString( R.string.ringtone_none );
	}

	@Override
	public void play( AudioConfig config ) {
	}

	@Override
	public void stop() {
	}
}