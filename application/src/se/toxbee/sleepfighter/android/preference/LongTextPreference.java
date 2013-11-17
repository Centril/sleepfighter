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
package se.toxbee.sleepfighter.android.preference;

import se.toxbee.sleepfighter.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * If the summary of a preference is too long, it may get cut off.
 * {@link LongTextPreference} class prevents this.
 */
public class LongTextPreference extends Preference {
	private int maxLines;

	public LongTextPreference( Context context ) {
		this( context, 10 );
	}

	public LongTextPreference( Context context, int maxLines ) {
		super( context );
		this.maxLines = maxLines;
	}

	public LongTextPreference( Context context, AttributeSet attrs ) {
		this( context, attrs, 0 );
	}

	public LongTextPreference( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
		this.readAttrs( context, attrs );
	}

	public void readAttrs( Context context, AttributeSet attrs ) {
		TypedArray a = context.obtainStyledAttributes( attrs, R.styleable.FontFit );
		final int N = a.getIndexCount();
		for ( int i = 0; i < N; ++i ) {
			int attr = a.getIndex( i );

			if ( attr == R.styleable.LongTextPreference_maxLines ) {
				this.maxLines = a.getInt( attr, 10 );
			}
		}
		a.recycle();
	}

	@Override
	protected void onBindView( View view ) {
		super.onBindView( view );

		TextView summaryView = (TextView) view.findViewById( android.R.id.summary );
		summaryView.setMaxLines( this.maxLines );
	}
}
