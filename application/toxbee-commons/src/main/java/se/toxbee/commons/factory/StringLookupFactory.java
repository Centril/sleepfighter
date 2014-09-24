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

package se.toxbee.commons.factory;

import se.toxbee.commons.reflect.ReflectionUtil;
import se.toxbee.commons.string.StringUtils;

/**
 * StringLookupFactory adds some fallback mechanisms,<br/>
 * based on reflection on top of AbstractFactory<String, V>.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 2
 * @since Nov 14, 2013
 */
public abstract class StringLookupFactory<V> extends AbstractFactory<String, V> {
	private Class<V> clazz = ReflectionUtil.genericType( this );

	/**
	 * Returns the default package to use when non-fully-qualified fallback.
	 *
	 * @return the default package, fully qualified.
	 */
	protected abstract String getDefaultPackage();

	/**
	 * When a key not found is added via reflection, this method returns an instantiator for it.<br/>
	 * This gives subclasses an opportunity to use a different instantiator.
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
			valClazz = ReflectionUtil.classForName( qualifiedKey, clazz );

			// Add classes, this caches them.
			FactoryClassInstantiator<String, V> ins = this.makeFoundInstantiator( valClazz );
			for ( String addKey : keys ) {
				this.add( addKey, ins );
			}

			// Produce again.
			val = ins.produce( key );
		}

		return val;
	}
}