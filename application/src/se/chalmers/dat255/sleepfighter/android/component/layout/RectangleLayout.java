package se.chalmers.dat255.sleepfighter.android.component.layout;

import se.chalmers.dat255.sleepfighter.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Used for laying out a 2X4(width 2, height 4) memory game
 */
public class RectangleLayout extends LinearLayout {
	public RectangleLayout( Context context ) {
		super( context );
	}
	
	private double mScale;


	public RectangleLayout( Context context, AttributeSet attrs ) {
		super( context, attrs );


		// We have added a custom xml attribute "scale" to this class. This code
		// reads that attribute.
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.RectangleLayout);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i)
		{
			int attr = a.getIndex(i);
			switch (attr)
			{
			case R.styleable.RectangleLayout_scale:
				this.mScale = Double.parseDouble(a.getString(attr));
				break;

			}
		}
		a.recycle();
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
//		double mScale = 0.5; // because 4/2 = 2

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