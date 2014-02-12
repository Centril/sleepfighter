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

package se.toxbee.sleepfighter.android.component.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import se.toxbee.sleepfighter.R;

/**
 * Used for laying out a rectangular layout.
 */
public class RectangleLayout extends LinearLayout {
	private double mScale;

	public RectangleLayout( Context context ) {
		this( context, 1.0 );
	}

	public RectangleLayout( Context context, double scale ) {
		super( context );
		this.mScale = scale;
	}

	public RectangleLayout( Context context, AttributeSet attrs ) {
		super( context, attrs );

		// We have added a custom xml attribute "scale" to this class. This code
		// reads that attribute.
		TypedArray a = context.obtainStyledAttributes( attrs, R.styleable.RectangleLayout );
		final int N = a.getIndexCount();
		for ( int i = 0; i < N; ++i ) {
			int attr = a.getIndex( i );

			if ( attr == R.styleable.RectangleLayout_scale ) {
				this.mScale = Double.parseDouble( a.getString( attr ) );
			}
		}
		a.recycle();
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
		int width = MeasureSpec.getSize( widthMeasureSpec );
		int height = MeasureSpec.getSize( heightMeasureSpec );

		if ( width > (int) (mScale * height + 0.5) ) {
			width = (int) (mScale * height + 0.5);
		} else {
			height = (int) (width / mScale + 0.5);
		}

		super.onMeasure(
				MeasureSpec.makeMeasureSpec( width, MeasureSpec.EXACTLY ),
				MeasureSpec.makeMeasureSpec( height, MeasureSpec.EXACTLY ) );
	}
}