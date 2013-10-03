package se.chalmers.dat255.sleepfighter.challenge.gridsnake.geometry;

/**
 * Implementation of an immutable dimension class.
 * Extends java.awt.Dimension and forbids setSize().
 */
public class Dimension {
	private static final long serialVersionUID = -1845270057681504248L;

	private int width, height;
	
	/**
	 * Creates an instance of <code>Dimension</code>
	 * with a width of zero and a height of zero.
	 */
	public Dimension() {
		this( 0, 0 );
	}

	/**
	 * Creates an instance of <code>Dimension</code> whose width
	 * and height are the same as for the specified dimension.
	 *
	 * @param d The specified dimension for the <code>width</code> and <code>height</code> values
	 */
	public Dimension( Dimension d ) {
		this( d.getWidth(), d.getHeight() );
	}

	/**
	 * Constructs a <code>Dimension</code> and initializes
	 * it to the specified width and specified height.
	 *
	 * @param width the specified width
	 * @param height the specified height
	 */
	public Dimension( int width, int height ) {
		this.width = width;
		this.height = height;
	}

	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
}
