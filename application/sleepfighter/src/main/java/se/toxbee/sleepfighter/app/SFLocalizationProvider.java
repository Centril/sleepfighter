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

package se.toxbee.sleepfighter.app;

import android.content.Context;

import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;

import se.toxbee.commons.model.LocalizationProvider;

/**
 * SFLocalizationProvider is the LocalizationProvider for SFApplication.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 13, 2013
 */
public class SFLocalizationProvider implements LocalizationProvider {
	private Map<Object, String> formats = Maps.newHashMap();

	private Context context;

	/**
	 * Constructs the SFLocalizationProvider given a context.
	 *
	 * @param ctx android os context.
	 */
	public SFLocalizationProvider( Context ctx ) {
		this.context = ctx;
	}

	@Override
	public long now() {
		return System.currentTimeMillis();
	}

	@Override
	public Locale locale() {
		return Locale.getDefault();
	}

	@Override
	public String format( Object key ) {
		return this.formats.get( key );
	}

	public void setFormat( Object key, int id ) {
		this.formats.put( key, this.context.getString( id ) );
	}
}
