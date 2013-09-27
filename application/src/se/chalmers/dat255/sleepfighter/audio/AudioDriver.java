package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;

/**
 * AudioDriver is the responsible for providing playing audio<br/>
 * and providing metadata from AudioSource.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public interface AudioDriver {
	/**
	 * Sets the source the driver should handle.
	 *
	 * @param context android context, use to save or w/e is needed for driver.
	 * @param source the AudioSource to handle.
	 */
	public void setSource( Context context, AudioSource source );

	/**
	 * Returns the source the driver handles.
	 *
	 * @return the source.
	 */
	public AudioSource getSource();

	/**
	 * Prints the name of source in a human readable format.<br/>
	 * This could for example be a songs title.
	 *
	 * @return name of source.
	 */
	public String printSourceName();

	/**
	 * Starts playing audio source.
	 *
	 * @param config audio configuration.
	 */
	public void play( AudioConfig config );

	/**
	 * Stops playing audio source.
	 */
	public void stop();
}