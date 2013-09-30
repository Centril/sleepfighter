package se.chalmers.dat255.sleepfighter.model;

/**
 * The IdProvider interface indicates that a model provides an integer ID.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 28, 2013
 */
public interface IdProvider {
	/**
	 * Returns the ID of model/object.
	 *
	 * @return the ID.
	 */
	public int getId();
}