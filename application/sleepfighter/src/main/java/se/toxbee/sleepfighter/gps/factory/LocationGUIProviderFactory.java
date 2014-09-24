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

package se.toxbee.sleepfighter.gps.factory;

import android.support.v4.app.FragmentActivity;

import se.toxbee.fimpl.ImplementationFactory;
import se.toxbee.fimpl.ImplementationFinder;
import se.toxbee.fimpl.ImplementationResultSet;
import se.toxbee.fimpl.impl.ImplementationFactoryImpl;
import se.toxbee.fimpl.metainf.MetainfReader;
import se.toxbee.sleepfighter.gps.gui.LocationGUIHandler;
import se.toxbee.sleepfighter.gps.gui.LocationGUIProvider;
import se.toxbee.sleepfighter.gps.gui.OnMapClick;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import se.toxbee.commons.factory.IFactory;
import se.toxbee.commons.reflect.ReflectionUtil;

/**
 * LocationGUIProviderFactory is the factory for LocationGUIProvider.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan, 16, 2014
 */
public final class LocationGUIProviderFactory implements IFactory<Void, LocationGUIProvider> {
	private final LocationGUIHandler handler;

	/**
	 * Constructs the factory given activity, area, and clickListener.
	 *
	 * @param activity the activity that the map GUI attaches itself to.
	 * @param area the location filter area.
	 * @param clickListener a OnMapClick listener.
	 */
	public LocationGUIProviderFactory( FragmentActivity activity, GPSFilterArea area, OnMapClick clickListener ) {
		this.handler = new LocationGUIHandler( activity, area, clickListener );
	}

	/**
	 * Returns the handler that the factory created.
	 *
	 * @return the handler.
	 */
	public LocationGUIHandler getHandler() {
		return this.handler;
	}

	@Override
	public LocationGUIProvider produce( Void key ) {
		ImplementationFactory p = new ImplementationFactoryImpl( new MetainfReader() );
		ImplementationFinder f = new ImplementationFinder( p );
		ImplementationResultSet.Impl<LocationGUIProvider> set = f.find( LocationGUIProvider.class );

		for ( Class<? extends LocationGUIProvider> clazz : set.loadingIterable() ) {
			LocationGUIProvider provider = ReflectionUtil.newInstance( clazz );
			provider.bind( this.getHandler() );

			if ( provider.isAvailable( true ) ) {
				return provider;
			}
		}

		return null;
	}
}
