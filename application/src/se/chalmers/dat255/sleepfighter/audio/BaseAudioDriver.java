package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;

/**
 * BaseAudioDriver is the abstract base class for interface AudioDriver.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 29, 2013
 */
public abstract class BaseAudioDriver implements AudioDriver {
	private AudioSource source;
	private Context context;

	private boolean isPlaying;

	@Override
	public void setSource( Context context, AudioSource source ) {
		this.source = source;
		this.context = context;
	}

	protected Context getContext() {
		return this.context;
	}

	@Override
	public AudioSource getSource() {
		return this.source;
	}

	@Override
	public boolean isPlaying() {
		return this.isPlaying;
	}

	@Override
	public void play( AudioConfig config ) {
		this.isPlaying = true;
	}

	@Override
	public void stop() {
		this.isPlaying = false;
	}

	@Override
	public void toggle( AudioConfig config ) {
		if ( this.isPlaying() ) {
			this.stop();
		} else {
			this.play( config );
		}
	}
}