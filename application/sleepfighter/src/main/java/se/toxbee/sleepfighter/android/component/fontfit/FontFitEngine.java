/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.android.component.fontfit;

import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.widget.TextView;

import se.toxbee.sleepfighter.R;

/**
 * FontFitEngine provides logic {@link FontFitButton} & {@link FontFitTextView}.<br/>
 * Reworked logic from from http://stackoverflow.com/questions/2617266/how-to-adjust-text-font-size-to-fit-textview
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 11, 2013
 */
public class FontFitEngine {
	private Paint mTestPaint;
	private int nukeBelow = -2;
	private TextView view;

	/**
	 * Sets the text size below which to nuke padding.<br/>
	 * Special values:
	 *  -1 = nuke padding completely.
	 *  -2 = don't nuke at all.
	 *
	 * @param nukeBelow the point below which to nuke, in pixels.
	 * @return this.
	 */
	public FontFitEngine setNukePaddingBelow( int nukeBelow ) {
		this.nukeBelow = nukeBelow;
		return this;
	}

	/**
	 * Reads {@link #setNukePaddingBelow(int)} from AttributeSet
	 *
	 * @param attrs the AttributeSet to read from.
	 * @see #setNukePaddingBelow(int)
	 * @return this.
	 */
	public FontFitEngine readAttrs( AttributeSet attrs ) {
		TypedArray a = this.view.getContext().obtainStyledAttributes( attrs, R.styleable.FontFit );
		final int N = a.getIndexCount();
		for ( int i = 0; i < N; ++i ) {
			int attr = a.getIndex( i );

			if ( attr == R.styleable.FontFit_nukePaddingTextSize ) {
				this.setNukePaddingBelow( a.getDimensionPixelSize( attr, -2 ) );
			}
		}
		a.recycle();
		return this;
	}

	/**
	 * Initializes the engine binding it to view.
	 *
	 * @param view the textview to work on.
	 * @return this.
	 */
	public FontFitEngine initialise( TextView view ) {
		this.view = view;

		mTestPaint = new Paint();
		mTestPaint.set( view.getPaint() );
		// max size defaults to the initially specified text size unless it is too small.
		return this;
	}

	protected int computeWidth( String text, int textWidth, int parentHeight ) {
		// Naively compute width.
		int[] pad_h = new int[] { view.getPaddingLeft(), view.getPaddingRight() };
		int targetWidth = textWidth - pad_h[0] - pad_h[1];

		if ( this.nukeBelow > -2 ) {
			// Nuke vertical padding if needed.
			int pad_v[] = new int[] { view.getPaddingTop(), view.getPaddingBottom() };
			int targetHeight = parentHeight - pad_v[0] - pad_v[0];
			if ( this.nukeBelow == -1 ) {
				if ( targetHeight < 1 ) {
					targetHeight = parentHeight;
					view.setPadding( pad_h[0], 0, pad_h[1], 0 );
				}
			} else if ( targetHeight < this.nukeBelow ) {
				// Take 1 pixel off each side until we're happy.
				int lr = 0;
				do {
					lr = (++lr) % 2;
					--pad_v[lr];
					--targetHeight;
				} while ( targetHeight < this.nukeBelow );
				view.setPadding( pad_h[0], pad_v[0], pad_h[1], pad_v[1] );
			}

			// Nuke horizontal padding if needed.
			if ( this.nukeBelow == -1 ) {
				if ( targetWidth < 1 ) {
					targetWidth = textWidth;
					view.setPadding( 0, pad_v[0], 0, pad_v[1] );
				}
			} else if ( targetWidth < this.nukeBelow ) {
				// Take 1 pixel off each side until we're happy.
				mTestPaint.setTextSize( this.nukeBelow );

				int lr = 0;
				do {
					lr = (++lr) % 2;
					--pad_h[lr];
					--targetWidth;
				} while ( targetWidth < textWidth && targetWidth < mTestPaint.measureText( text ) );
				view.setPadding( pad_h[0], pad_v[0], pad_h[1], pad_v[1] );
			}
		}

		return targetWidth;
	}

	/**
	 * Resize the font so the specified text fits in the
	 * text box assuming the text box is the specified width.
	 */
	public void refitText( String text, int textWidth, int parentHeight ) {
		if ( textWidth < 1 ) {
			return;
		}

		mTestPaint.set( view.getPaint() );

		int targetWidth = this.computeWidth( text, textWidth, parentHeight );

		float hi = 100, lo = 2;
		// How close we have to be.
		final float threshold = 0.5f;

		while ( (hi - lo) > threshold ) {
			float size = (hi + lo) / 2;
			mTestPaint.setTextSize( size );
			if ( mTestPaint.measureText( text ) >= targetWidth ) {
				hi = size; // too big
			} else {
				lo = size; // too small
			}
		}
		// Use lo so that we undershoot rather than overshoot
		view.setTextSize( TypedValue.COMPLEX_UNIT_PX, lo );
	}

	public int[] performMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
		int parentWidth = MeasureSpec.getSize( widthMeasureSpec );
		int parentHeight = MeasureSpec.getSize( heightMeasureSpec );
		refitText( this.view.getText().toString(), parentWidth, parentHeight );
		return new int[] { parentWidth, this.view.getMeasuredHeight() };
	}

	public void onTextChanged( final CharSequence text, final int start, final int before, final int after ) {
		int w = this.view.getWidth();
		int h = this.view.getHeight();
		this.refitText( text.toString(), w, h );
	}

	public void onSizeChanged( int w, int h, int oldw, int oldh ) {
		if ( w != oldw ) {
			this.refitText( this.view.getText().toString(), w, h );
		}
	}
}
