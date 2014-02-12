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
import se.toxbee.sleepfighter.utils.factory.IFactory;
import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;

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
