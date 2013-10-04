package se.chalmers.dat255.sleepfighter.android.component.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Used for laying out a 2X4(width 2, height 4) memory game
 */
public class RectangleLayout extends LinearLayout {
	public RectangleLayout( Context context ) {
		super( context );
	}

	public RectangleLayout( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
		double mScale = 0.5; // because 4/2 = 2

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