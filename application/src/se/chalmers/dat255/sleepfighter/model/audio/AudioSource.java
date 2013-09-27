package se.chalmers.dat255.sleepfighter.model.audio;

/**
 * <p>AudioSource is the model for knowing where to look for audio and with what to read it and get metadata</p>
 *
 * <p>It has 2 properties, one accessed by {@link #getType()}, which tells us what to read source with.<br/>
 * The next property is the URI which is implementation specfic depending on {@link #getType()}.</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 26, 2013
 */
public class AudioSource {
	private AudioSourceType type;
	private String uri;

	/**s
	 * Constructs an AudioSource given type & URI.
	 *
	 * @param type the type.
	 * @param uri the URI.
	 */
	public AudioSource( AudioSourceType type, String uri ) {
		this.set( type, uri );
	}

	/**
	 * Sets the type and the URI.
	 *
	 * @param type the type.
	 * @param uri the URI.
	 */
	public void set( AudioSourceType type, String uri ) {
		this.type = type;
		this.uri = uri;
	}

	/**
	 * Returns the type of this audio source.
	 *
	 * @return the type.
	 */
	public AudioSourceType getType() {
		return this.type;
	}

	/**
	 * Returns a representation of the source as an implementation specific URI.<br/>
	 * Has varying meaning depending on {@link #getType()}.
	 *
	 * @return URI representation of source.
	 */
	public String getUri() {
		return this.uri;
	}
}