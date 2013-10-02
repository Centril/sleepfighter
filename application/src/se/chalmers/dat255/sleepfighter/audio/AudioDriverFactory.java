package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.audio.playlist.PlaylistDriver;
import se.chalmers.dat255.sleepfighter.audio.playlist.PlaylistProviderFactory;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * AudioDriverFactory produces AudioDriver:s.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class AudioDriverFactory {
	/**
	 * Constructs a AudioDriverFactory.
	 */
	public AudioDriverFactory() {
	}

	/**
	 * Produces an AudioDriver given a context and an AudioSource.
	 *
	 * @param context android context.
	 * @param source audio source.
	 * @return the produced AudioDriver.
	 */
	public AudioDriver produce( Context context, AudioSource source ) {
		AudioDriver driver = null;

		if ( source == null ) {
			driver = new SilentAudioDriver();
		} else {
			Log.d( "AudioDriverFactory", source.toString() );

			switch ( source.getType() ) {
			case RINGTONE:
				driver = new RingtoneDriver();
				break;

			case PLAYLIST:
				driver = new PlaylistDriver( new PlaylistProviderFactory() );
				break;
			case LOCAL_CONTENT_URI:
				driver = new LocalContentDriver();
				break;
			case INTERNET_STREAM:
			case SPOTIFY:
				Toast.makeText( context, "NOT IMPLEMENTED YET!", Toast.LENGTH_LONG ).show();
				driver = new RingtoneDriver();
				break;
			}
		}

		driver.setSource( context, source );
		return driver;
	}
}