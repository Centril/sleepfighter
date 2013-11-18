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
package se.toxbee.sleepfighter.android.component.textview;

import se.toxbee.sleepfighter.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Text view that allows changing the letter spacing of the text.
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @author Pedro Barros (pedrobarros.dev at gmail.com)
 * @since Nov 17, 2013
 */
public class LetterSpacingTextView extends TextView {
	private float letterSpacing = LetterSpacing.NORMAL;
	private CharSequence originalText = "";

	public LetterSpacingTextView( Context context ) {
		super( context );
	}

	public LetterSpacingTextView( Context context, AttributeSet attrs ) {
		super( context, attrs, 0 );
	}

	public LetterSpacingTextView( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
		this.readAttrs( context, attrs );
	}

	public void readAttrs( Context context, AttributeSet attrs ) {
		TypedArray a = context.obtainStyledAttributes( attrs, R.styleable.LetterSpacing );
		final int N = a.getIndexCount();
		for ( int i = 0; i < N; ++i ) {
			int attr = a.getIndex( i );

			if ( attr == R.styleable.LetterSpacing_letterSpacing ) {
				this.setLetterSpacing( a.getFloat( attr, LetterSpacing.NORMAL ) );
			}
		}
		a.recycle();
	}

	public float getLetterSpacing() {
		return letterSpacing;
	}

	public void setLetterSpacing( float letterSpacing ) {
		this.letterSpacing = letterSpacing;
		applyLetterSpacing();
	}

	@Override
	public void setText( CharSequence text, BufferType type ) {
		originalText = text;
		applyLetterSpacing();
	}

	@Override
	public CharSequence getText() {
		return originalText;
	}

	private void applyLetterSpacing() {
		StringBuilder builder = new StringBuilder();
		for ( int i = 0; i < originalText.length(); i++ ) {
			builder.append( originalText.charAt( i ) );
			if ( i + 1 < originalText.length() ) {
				builder.append( "\u00A0" );
			}
		}
		SpannableString finalText = new SpannableString( builder.toString() );
		if ( builder.toString().length() > 1 ) {
			for ( int i = 1; i < builder.toString().length(); i += 2 ) {
				finalText.setSpan( new ScaleXSpan( (letterSpacing + 1) / 10 ),
						i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
			}
		}
		super.setText( finalText, BufferType.SPANNABLE );
	}

	public class LetterSpacing {
		public final static float NORMAL = 0;
	}
}