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
