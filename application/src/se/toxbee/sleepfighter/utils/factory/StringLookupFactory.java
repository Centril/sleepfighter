/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
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
 ******************************************************************************/
package se.toxbee.sleepfighter.utils.factory;

import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;
import se.toxbee.sleepfighter.utils.string.StringUtils;

import com.google.common.reflect.TypeToken;

/**
 * StringLookupFactory adds some fallback mechanisms,<br/>
 * based on reflection on top of AbstractFactory<String, V>.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 2
 * @since Nov 14, 2013
 */
public abstract class StringLookupFactory<V> extends AbstractFactory<String, V> {
	@SuppressWarnings( { "serial", "unchecked" } )
	private Class<V> clazz = (Class<V>) (new TypeToken<V>( this.getClass() ) {}).getRawType();

	/**
	 * Returns the default package to use when non-fully-qualified fallback.
	 *
	 * @return the default package, fully qualified.
	 */
	protected abstract String getDefaultPackage();

	/**
	 * When a key not found is added via reflection, this method returns an instantiator for it.
	 *
	 * @param clazz the clazz.
	 * @return the instantiator.
	 */
	protected FactoryClassInstantiator<String, V> makeFoundInstantiator( Class<? extends V> clazz ) {
		return new FactoryClassInstantiator<String, V>( clazz );
	}

	public V produce( String key ) {
		// Take "" as being null key, meaning default instantiator.
		if ( key != null && key.length() == 0 ) {
			key = null;
		}

		// Produce the value.
		V val = super.produce( key );

		/*
		 * Not found -> try to use reflection to recover.
		 * If not fully qualified, qualify!
		 * If not found for some reason, we're toast.
		 */
		if ( val == null ) {
			String[] keys;
			String qualifiedKey;
			boolean unqualified = key.indexOf( '.' ) == -1;

			// Qualify if needed.
			if ( unqualified ) {
				qualifiedKey = StringUtils.QUALIFIER_JOINER.join( this.getDefaultPackage(), key );
				keys = new String[] { key, qualifiedKey };
			} else {
				qualifiedKey = key;
				keys = new String[] { key };
			}

			Class<? extends V> valClazz = null;

			// Find the Class.
			try {
				valClazz = ReflectionUtil.classForName( qualifiedKey, clazz );
			} catch ( ClassNotFoundException e ) {
				ROJava6Exception.reThrow( e );
			}

			// Add classes.
			FactoryClassInstantiator<String, V> ins = this.makeFoundInstantiator( valClazz );
			for ( String addKey : keys ) {
				this.add( addKey, ins );
			}

			// Produce again.
			val = ins.get( key );
		}

		return val;
	}
}