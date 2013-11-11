/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.toxbee.sleepfighter.libgdx;

import com.badlogic.gdx.math.MathUtils;

/**
 * Extra color utilities. Using the android source.
 * 
 * @version 1.0
 * @since Nov 10, 2013
 */
public class ColorUtils {
	/**
	 * Return the alpha component of a color int. This is the same as saying
	 * color >>> 24
	 */
	public static int alpha( int color ) {
		return color >>> 24;
	}

	/**
	 * Return the red component of a color int. This is the same as saying
	 * (color >> 16) & 0xFF
	 */
	public static int red( int color ) {
		return (color >> 16) & 0xFF;
	}

	/**
	 * Return the green component of a color int. This is the same as saying
	 * (color >> 8) & 0xFF
	 */
	public static int green( int color ) {
		return (color >> 8) & 0xFF;
	}

	/**
	 * Return the blue component of a color int. This is the same as saying
	 * color & 0xFF
	 */
	public static int blue( int color ) {
		return color & 0xFF;
	}

	/**
	 * Returns the hue component of a color int.
	 * 
	 * @return A value between 0.0f and 1.0f
	 */
	public static float hue( int color ) {
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;

		int V = Math.max( b, Math.max( r, g ) );
		int temp = Math.min( b, Math.min( r, g ) );

		float H;

		if ( V == temp ) {
			H = 0;
		} else {
			final float vtemp = (float) (V - temp);
			final float cr = (V - r) / vtemp;
			final float cg = (V - g) / vtemp;
			final float cb = (V - b) / vtemp;

			if ( r == V ) {
				H = cb - cg;
			} else if ( g == V ) {
				H = 2 + cr - cb;
			} else {
				H = 4 + cg - cr;
			}

			H /= 6.f;
			if ( H < 0 ) {
				H++;
			}
		}

		return H;
	}

	/**
	 * Returns the saturation component of a color int.
	 * 
	 * @return A value between 0.0f and 1.0f
	 */
	public static float saturation( int color ) {
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;

		int V = Math.max( b, Math.max( r, g ) );
		int temp = Math.min( b, Math.min( r, g ) );

		float S;

		if ( V == temp ) {
			S = 0;
		} else {
			S = (V - temp) / (float) V;
		}

		return S;
	}

	/**
	 * Returns the brightness component of a color int.
	 * 
	 * @return A value between 0.0f and 1.0f
	 */
	public static float brightness( int color ) {
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;

		int V = Math.max( b, Math.max( r, g ) );

		return (V / 255.f);
	}

	/**
	 * Convert HSB components to an ARGB color. Alpha set to 0xFF. hsv[0] is Hue
	 * [0 .. 1) hsv[1] is Saturation [0...1] hsv[2] is Value [0...1] If hsv
	 * values are out of range, they are pinned.
	 * 
	 * @param hsb 3 element array which holds the input HSB components.
	 * @return the resulting argb color
	 * @hide Pending API council
	 */
	public static int HSBtoColor( float[] hsb ) {
		return HSBtoColor( hsb[0], hsb[1], hsb[2] );
	}

	/**
	 * Convert HSB components to an ARGB color. Alpha set to 0xFF. hsv[0] is Hue
	 * [0 .. 1) hsv[1] is Saturation [0...1] hsv[2] is Value [0...1] If hsv
	 * values are out of range, they are pinned.
	 * 
	 * @param h Hue component
	 * @param s Saturation component
	 * @param b Brightness component
	 * @return the resulting argb color
	 * @hide Pending API council
	 */
	public static int HSBtoColor( float h, float s, float b ) {
		h = MathUtils.clamp( h, 0.0f, 1.0f );
		s = MathUtils.clamp( s, 0.0f, 1.0f );
		b = MathUtils.clamp( b, 0.0f, 1.0f );

		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;

		final float hf = (h - (int) h) * 6.0f;
		final int ihf = (int) hf;
		final float f = hf - ihf;
		final float pv = b * (1.0f - s);
		final float qv = b * (1.0f - s * f);
		final float tv = b * (1.0f - s * (1.0f - f));

		switch ( ihf ) {
		case 0: // Red is the dominant color
			red = b;
			green = tv;
			blue = pv;
			break;
		case 1: // Green is the dominant color
			red = qv;
			green = b;
			blue = pv;
			break;
		case 2:
			red = pv;
			green = b;
			blue = tv;
			break;
		case 3: // Blue is the dominant color
			red = pv;
			green = qv;
			blue = b;
			break;
		case 4:
			red = tv;
			green = pv;
			blue = b;
			break;
		case 5: // Red is the dominant color
			red = b;
			green = pv;
			blue = qv;
			break;
		}

		return 0xFF000000 | (((int) (red * 255.0f)) << 16)
				| (((int) (green * 255.0f)) << 8) | ((int) (blue * 255.0f));
	}
}
