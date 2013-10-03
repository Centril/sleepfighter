package se.chalmers.dat255.sleepfighter.challenge.gridsnake;

import java.awt.Color;
import java.awt.Graphics;

import Geometry.Position;
import Tiled.GameTile;
import Tiled.RectangularTile;
import Tiled.RoundTile;
import Tiled.TiledGameView;

/**
 * The view class responsible for rendering a single position in the game Snake
 */
public class SnakeView extends TiledGameView {
	/**
	 * Renders the snake game.
	 *
	 * @param g Graphics object.
	 */
	@Override
	public void paintComponent( final Graphics g ) {
		super.paintComponent( g );

		if ( this.getModel() == null ) {
			return;
		}

		this.render( g );
	}

	/**
	 * Constructs the SnakeView.
	 */
	public SnakeView() {
		// Set size & tile size.
		super( SnakeConstants.getGameSize(), SnakeConstants.getTileSize() );
	}

	private static final long serialVersionUID = 3374654405637655816L;

	/** Graphical representation of snake-food. */
	private static final GameTile FOOD_TILE = new RoundTile( Color.BLACK, SnakeConstants.getFoodColor(), 2.0 );

	/** Graphical representation of the snake. */
	private static final GameTile SNAKE_TILE = new RectangularTile( SnakeConstants.getSnakeColor() );

	/** Graphical representation of the snakes head. */
	private static final GameTile SNAKE_HEAD_TILE = new RectangularTile( SnakeConstants.getSnakeHeadColor() );

	protected Color bodyColor;
	protected Color headColor;

	/**
	 * Tests if the head will be accented.
	 *
	 * @return true if the head will be accented.
	 */
	public boolean isAccentedHead() {
		return !( this.bodyColor == this.headColor || bodyColor.equals( this.headColor ) );
	}

	/**
	 * Sets the color used for head of snake.
	 * A null parameter value means indicates the head won't be differentiated.
	 *
	 * @param color The color used for head of snake.
	 */
	public void setHeadColor( final Color color ) {
		this.headColor = color;
	}

	/**
	 * Returns the color used for head of snake.
	 * A null parameter value means indicates the head won't be differentiated.
	 *
	 * @return The color used for head of snake.
	 */
	public Color getHeadColor() {
		return this.headColor;
	}

	/**
	 * Sets the color used for body of snake.
	 *
	 * @param color The color used for body of snake.
	 */
	public void setBodyColor( final Color color ) {
		this.bodyColor = color;
	}

	/**
	 * Returns the color used for body of snake.
	 *
	 * @return The color used for body of snake.
	 */
	public Color getBodyColor() {
		return this.bodyColor;
	}

	/**
	 * Returns a tile used for the head.
	 *
	 * @return A tile used for the head.
	 */
	protected GameTile getHeadTile() {
		return this.getHeadColor() == null ? SNAKE_HEAD_TILE : new RectangularTile( this.getHeadColor() );
	}

	/**
	 * Returns a tile used for the body.
	 *
	 * @return A tile used for the body.
	 */
	protected GameTile getBodyTile() {
		return this.getBodyColor() == null ? SNAKE_TILE : new RectangularTile( this.getBodyColor() );
	}

	/**
	 * Do the actual rendering.
	 *
	 * @param g Graphics object.
	 */
	protected void render( final Graphics g ) {
		SnakeModel model = (SnakeModel) this.getModel();

		// Paint the food.
		this.paintTile( g, model.getFoodPosition(), FOOD_TILE );

		// Paint all snake parts.
		for ( Position pos : model.getSnakePositions() ) {
			this.paintTile( g, pos, this.isAccentedHead() && model.isPositionHead( pos ) ? this.getHeadTile() : this.getBodyTile() );
		}
	}
}