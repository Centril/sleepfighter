/*
 * Copyright (c) 2014. See AUTHORS file.
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
 */
package se.toxbee.sleepfighter.gps.osmdroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan, 12, 2014
 */
public class MyLocationButton extends Hotspot {
	protected final Bitmap bitmap;

	public MyLocationButton( Context ctx ) {
		this( new DefaultResourceProxyImpl( ctx ) );
	}

	public MyLocationButton( ResourceProxy pResourceProxy ) {
		super( pResourceProxy );

		this.bitmap = mResourceProxy.getBitmap( ResourceProxy.bitmap.person );

		float l = 0, t = 0;
		float r = l + this.bitmap.getWidth();
		float b = t + this.bitmap.getHeight();
		this.setBounds( new RectF( l, t, r, b ) );
	}

	@Override
	protected void draw( Canvas c, MapView osmv, boolean shadow ) {
		if ( shadow ) {
			return;
		}

		c.drawBitmap( this.bitmap, null, this.getBounds(), null );
	}
}
