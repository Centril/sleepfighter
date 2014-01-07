package se.toxbee.sleepfighter.android.component.fontfit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * FontFitButton is a button whose text fits to the width of itself.
 *
 * @version 2.0
 * @since Oct 2, 2013
 */
public class FontFitButton extends Button {
	private FontFitEngine ffe;

	public FontFitButton( Context context ) {
		this( context, -2 );
	}

	public FontFitButton( Context context, int nukeBelow ) {
		super( context );
		this.ffe.setNukePaddingBelow( nukeBelow );
	}

	public FontFitButton( Context context, AttributeSet attrs ) {
		super( context, attrs );
		this.ffe().readAttrs( attrs );
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
		super.onMeasure( widthMeasureSpec, heightMeasureSpec );
		int[] d = this.ffe().performMeasure( widthMeasureSpec, heightMeasureSpec );
		this.setMeasuredDimension( d[0], d[1] );
	}

	@Override
	protected void onTextChanged( final CharSequence text, final int start, final int before, final int after ) {
		this.ffe().onTextChanged( text, start, before, after );
	}

	@Override
	protected void onSizeChanged( int w, int h, int oldw, int oldh ) {
		this.ffe().onSizeChanged( w, h, oldw, oldh );
	}

	protected FontFitEngine ffe() {
		if ( this.ffe == null ) {
			this.ffe = new FontFitEngine().initialise( this );
		}

		return this.ffe;
	}
}