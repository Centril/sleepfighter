package se.chalmers.dat255.sleepfighter.android.component.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Layout for squares.
 * Copied from: http://stackoverflow.com/questions/2948212/android-layout-with-sqare-buttons?lq=1
 *
 * @version 1.0
 * @since Oct 2, 2013
 */
public class SquareLayout extends LinearLayout {
	public SquareLayout( Context context ) {
		super( context );
	}

	public SquareLayout( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
		double mScale = 1;

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