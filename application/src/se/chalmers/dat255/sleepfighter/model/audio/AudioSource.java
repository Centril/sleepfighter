package se.chalmers.dat255.sleepfighter.model.audio;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 26, 2013
 */
public interface AudioSource {
	/**
	 * Returns the type of this audio source.
	 *
	 * @return the type.
	 */
	public AudioSourceType getType();

	/**
	 * Returns a representation of the source as an implementation specific URI.<br/>
	 * Has varying meaning depending on {@link #getType()}.
	 *
	 * @return URI representation of source.
	 */
	public String toUri();

	/**
	 * Alias of {@link #toUri()}.
	 *
	 * @return URI representation of source.
	 */
	public String toString();
}