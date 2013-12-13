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
package se.toxbee.sleepfighter.app;

import java.util.Locale;
import java.util.Map;

import se.toxbee.sleepfighter.utils.model.LocalizationProvider;
import android.content.Context;

import com.google.common.collect.Maps;

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
