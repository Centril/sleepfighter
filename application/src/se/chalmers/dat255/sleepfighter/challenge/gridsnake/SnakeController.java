package Snake;

import java.util.Random;

import Game.GameController;
import Game.GameOverException;
import Geometry.Direction;
import Geometry.QuadrantDirectionKeyMapper;
import MVC.KeypressDelegator;
import MVC.KeypressReceiver;
import MVC.UpdateDelegator;
import MVC.UpdateReceiver;

public class SnakeController extends GameController implements KeypressReceiver, UpdateReceiver {
	/** Random Number Generator (RNG) */
	private final Random rng;

	/**
	 * Constructs the controller.
	 */
	public SnakeController() {
		super();

		this.rng = new Random();

		this.view = new SnakeView();

		// Add delegators for keypress & interval updates.
		this.addDelegator( new KeypressDelegator( this, this.getView() ) );
		this.addDelegator( new UpdateDelegator( this ) );
	}

	@Override
	public long updateSpeed() {
		return SnakeConstants.getUpdateSpeed();
	}

	@Override
	protected void init() {
		this.model = new SnakeModel( SnakeConstants.getGameSize(), QuadrantDirectionKeyMapper.random( this.rng ), this.rng );
	}

	@Override
	public void update( UpdateDelegator delegator ) {
		try {
			SnakeModel model = (SnakeModel) this.model;
			model.tickUpdate();
		} catch ( GameOverException e ) {
			this.stopGame();
		}
	}

	@Override
	public void update( int key ) {
		Direction dir = QuadrantDirectionKeyMapper.get( key );

		if ( dir != Direction.NONE ) {
			((SnakeModel) this.model).updateDirection( dir );
		}
	}
}