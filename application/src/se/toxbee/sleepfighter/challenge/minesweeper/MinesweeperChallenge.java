/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 * 
 * This file is part of SleepFighter.
 * 
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package se.toxbee.sleepfighter.challenge.minesweeper;

import java.util.List;

import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.challenge.gdx.GdxChallenge;
import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperConfig.Level;
import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperMove.Action;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;
import se.toxbee.sleepfighter.utils.geom.Dimension;
import se.toxbee.sleepfighter.utils.geom.FinalPosition;
import se.toxbee.sleepfighter.utils.geom.Position;
import android.app.Activity;
import android.os.Bundle;

import com.alex.games.minesweeper.Minesweeper;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Disposable;
import com.google.common.collect.Lists;

/**
 * MinesweeperChallenge is a challenge where the user plays minesweeper.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 4, 2013
 */
public class MinesweeperChallenge extends GdxChallenge implements ApplicationListener {
	private static final String CONFIG = "board-config";
	private static final String CONFIG_CUSTOM = "board-custom";

	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType( ChallengeType.MINESWEEPER );
		add( CONFIG, PrimitiveValueType.INTEGER, 0 );
		add( CONFIG_CUSTOM, PrimitiveValueType.STRING, "" );
	}}

	private MinesweeperConfig readConfig( ChallengeResolvedParams params ) {
		String custom = params.getString( CONFIG_CUSTOM ).trim();

		if ( custom.isEmpty() ) {
			return Level.fromInt( params.getInt( CONFIG ) ).getConfig();
		} else {
			try {
				return MinesweeperConfig.fromString( custom );
			} catch ( RuntimeException e ) {
				return Level.BEGINNER.getConfig();
			}
		}
	}

	private MinesweeperConfig config;
	private MinesweeperGame game;

	public void start( Activity activity, ChallengeResolvedParams params ) {
		super.start( activity, params );

		// Read the config.
		this.config = this.readConfig( params );

		// Setup libdx.
		this.initGdx();
	}

	@Override
	public void start( Activity activity, ChallengeResolvedParams params, Bundle state ) {
		super.start( activity, params );

		// Setup libdx.
		this.initGdx();
	}

	@Override
	public Bundle savedState() {
		return null;
	}

	private void generateGame( Position inital ) {
		// Setup a factory.
	//	MinesweeperFactory fac = new MinesweeperFactory( this.config, new SolverConfig() );
		MinesweeperFactory fac = new MinesweeperFactory( this.config, null );

		// Produce it.
		this.game = fac.produce( inital );
	}

	public void performAction( Position pos ) {
		MinesweeperMove move = new MinesweeperMove( pos, Action.NORMAL );
		this.game.acceptMove( move );
	}

	private void initGdx() {
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		cfg.useGL20 = true;

		this.initGdx( new G(), cfg );
		//this.initGdx( new Minesweeper(), cfg );
	}

	public class G extends Game {
		@Override
		public void create() {
			this.setScreen( new MinesweeperScreen( this ) );
		}
	}

	// Sizes.
	private final float WIDTH = 100;
	private final float HEIGHT = WIDTH * 1.2f;
	private final float TOOLBAR_SIZE = WIDTH * 0.2f;
	private final float GRID_SIZE = WIDTH;

	// Colors.
	private final Color bg = new Color( 0.2f, 0.2f, 0.2f, 1f );
	private final Color gridLineColor = new Color( 0.1f, 0.1f, 0.1f, 1 );
	private final Color gridBg = new Color( 0xCCCCCCFF );
	private final Color clickedBg = Color.WHITE;
	private final Color[] valueColor = new Color[] {
		new Color( 0x40bf00ff ), // hue = 100, greenish
		new Color( 0x0080bfff ), // hue = 200, turquoise.
		new Color( 0xbf0070ff ), // hue = 325, dark blue.
		new Color( 0xbf003fff ), // hue = 340, cerise.
		new Color( 0xbf0000ff ), // hue = 360, red.
		new Color( 0xbf4000ff ), // hue = 20, red-orange.
		new Color( 0xbf6000ff ), // hue = 30, orange.
		new Color( 0xbf8000ff ), // hue = 40, orange-yellow.
	};

	// Textures
	private Texture clickedTex;
	private Texture mineTex;
	private Texture gridTex;

	// Value to String Conversion.
	private final CharSequence[] valSeqMap = new CharSequence[] {
		"1", "2", "3", "4", "5", "6", "7", "8"
	};

	// Fonts.
	private BitmapFont font;
	private static final String FONT_FILE = "font/Roboto-Condensed.ttf";

	// Cameras & scene2d
	private Stage stage;

	// Tools.
	private ShapeRenderer shapeRenderer;
	private List<Disposable> disposables = Lists.newArrayList();
	private final Vector2 temp = new Vector2( 0, 0 );

	private class GridView extends Actor {
		private float cellSize;

		public GridView( float size, float x, float y ) {
			this.setBounds( x, y, size, size );
			this.cellSize = size / config.dim().highest();

			this.getColor().set( gridBg );

			final GridView self = this;
			this.addListener( new ActorGestureListener() {
				@Override
				public void tap( InputEvent event, float x, float y, int count, int button ) {
					if ( button == Buttons.LEFT ) {
						self.tap( x, y );
					}
					super.tap( event, x, y, count, button );
				}
			});
		}

		private void tap( float x, float y ) {
			// Translate pos.
			Position pos = this.translated( x, y );

			if ( game == null ) {
				generateGame( pos );
			} else {
				performAction( pos );
			}
		}

		private Position translated( float x, float y ) {
			return new FinalPosition( this.translatedCord( x, 0 ), this.translatedCord( y, 1 ) );
		}

		private int translatedCord( float c, int i ) {
			// Translate the coordinate to cell coordinate system.
			int ci = (int) (c / cellSize);

			// Clamp to bounds.
			int d = config.dim().size( i );
			return ci >= d ? d - 1 : ci;
		}

		@Override
		public void draw( SpriteBatch batch, float parentAlpha ) {
			batch.getTransformMatrix().translate( this.getX(), this.getY(), 0 );

			this.renderBg( batch );
			this.renderAllCells( batch, temp.set( Vector2.Zero ) );
			this.renderGridLines( batch, temp.set( Vector2.Zero ) );
		}

		private void renderBg( SpriteBatch batch ) {
			batch.draw( gridTex, 0, 0, this.getWidth(), this.getHeight() );
		}

		private void renderAllCells( SpriteBatch batch, Vector2 v ) {
			if ( game == null ) {
				return;
			}

			Dimension dim = config.dim();
			int w = dim.width();
			int h = dim.height();
			for ( int y = 0; y < h; ++y, v.y += cellSize, v.x = 0 ) {
				for ( int x = 0; x < w; ++x, v.x += cellSize ) {
					this.renderCell( batch, game.board().get( x, y ),  v );
				}
			}
		}

 		private void renderCell( SpriteBatch batch, MinesweeperCell cell, Vector2 v ) {
			switch ( cell.getState() ) {
			case NOT_CLICKED:
				return;

			case CLICKED:
				batch.draw( clickedTex, v.x, v.y, cellSize, cellSize );

				if ( cell.isEmpty() ) {
				} else if ( cell.isMine() ) {
					batch.draw( mineTex, v.x, v.y, cellSize, cellSize );
				} else {
					this.renderValuedCell( batch, cell, v );
				}
				break;

			case FLAG_CLICKED:
				// TODO
				break;

			case QUESTION_CLICKED:
				// TODO
				break;

			default:
				throw new AssertionError();
			}
		}

		private void renderValuedCell( SpriteBatch batch, MinesweeperCell cell, Vector2 v ) {
			BitmapFont font = font( cellSize * 0.8f );
			font.setColor( valueColor( cell ) );
			font.draw( batch, valueToSequence( cell ), v.x + cellSize / 3, v.y + cellSize );
		}

		private Color valueColor( MinesweeperCell cell ) {
			return valueColor[cell.getValue() - 1];
		}

		private CharSequence valueToSequence( MinesweeperCell cell ) {
			return valSeqMap[cell.getValue() - 1];
		}

		private void renderGridLines( SpriteBatch batch, Vector2 v ) {
			ShapeRenderer sr = shapeRenderer;
			sr.setProjectionMatrix( batch.getProjectionMatrix() );
			sr.setTransformMatrix( batch.getTransformMatrix() );

			batch.end();
			sr.begin( ShapeType.Line );
			sr.setColor( gridLineColor );

			Dimension dim = config.dim();
			int w = dim.width();
			int h = dim.height();

			float v_end = this.getHeight();
			float h_end = this.getWidth();

			// vertical.
			for ( int x = 0; x < w; x++ ) {
				v.x += cellSize;
				sr.line( v.x, v.y, v.x, v_end );
			}

			// horizontal.
			v.set( Vector2.Zero );
			for ( int y = 0; y < h; y++ ) {
				v.y += cellSize;
				sr.line( v.x, v.y, h_end, v.y );
			}

			sr.end();
			batch.begin();
		}
	}

	private final Texture makeColorTexture( Color c ) {
		Pixmap pix = new Pixmap( 1, 1, Format.RGBA8888 );
		pix.setColor( c );
		pix.fill();

		Texture tex = new Texture( pix );

		this.disposables.add( tex );

		return tex;
	}

	private BitmapFont font( float size ) {
		if ( this.font == null ) {
			FileHandle file = this.getFiles().internal( FONT_FILE );
			FreeTypeFontGenerator fg = new FreeTypeFontGenerator( file );
			this.font = fg.generateFont( (int) size );
			fg.dispose();
		}

		return this.font;
	}

	public void create() {
		// Allocate textures.
		this.gridTex = makeColorTexture( this.gridBg );
		this.clickedTex = makeColorTexture( this.clickedBg );
		this.mineTex = makeColorTexture( Color.RED );

		// Shape renderer.
		this.shapeRenderer = new ShapeRenderer();

		// Setup stage.
		this.stage = new Stage( WIDTH, HEIGHT, true );
		this.getInput().setInputProcessor( this.stage );

		// Setup grid.
		GridView gv = new GridView( GRID_SIZE, 0, 0 );
		this.stage.addActor( gv );
	}

	public void dispose() {
		this.stage.dispose();

		this.shapeRenderer.dispose();

		if ( this.font != null ) {
			this.font.dispose();
		}

		for ( Disposable d : this.disposables ) {
			d.dispose();
		}
		
	}

	public void render() {
		this.clearBackground();

		this.stage.act();
		this.stage.draw();
	}

	private void clearBackground() {
		// Set background.
		GLCommon gl = this.getGraphics().getGLCommon();
		gl.glClearColor( bg.r, bg.g, bg.b, bg.a );
		gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
	}


	@Override
	public void resize( int width, int height ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}