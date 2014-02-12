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

package se.toxbee.sleepfighter.android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import se.toxbee.sleepfighter.R;

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
		TypedArray a = context.obtainStyledAttributes( attrs, R.styleable.LongTextPreference );
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
