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
package se.toxbee.sleepfighter.preference;

import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;

/**
 * {@link WeatherPreferences} contains info about weather.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public class WeatherPreferences extends AppPreferenceNode {
	protected WeatherPreferences( PreferenceNode b ) {
		super( b, "weather" );
	}

	/**
	 * Used to temporarily store the weather. Some seconds before the app starts, the weather is fetched.
	 * This preference is used to temporarily store the weather info. 
	 *
	 * @param weather
	 */
	public void setTemp( String weather ) {
		p.setString( "temp", weather );
	}

	/**
	 * Returns the weather.
	 *
	 * @see #setTemp(String)
	 * @return the weather.
	 */
	public String getWeather() {
		return p.getString( "temp", null );
	}
}
