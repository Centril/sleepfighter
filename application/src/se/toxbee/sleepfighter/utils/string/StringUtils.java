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
package se.toxbee.sleepfighter.utils.string;

import java.io.File;
import java.util.Locale;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Splitter;

/**
 * Static utilities for string operations.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @since 19 apr 2013
 * @version 1.0
 */
public class StringUtils {
	/** Splitter for whitespace. */
	public static final Splitter WS_SPLITTER = Splitter.onPattern( "\\s" ).omitEmptyStrings().trimResults();

	/** Splitter for directory paths. */
	public static final Splitter COMMA_SPLITTER = Splitter.on( ',' ).omitEmptyStrings().trimResults();

	/** Splitter for directory paths. */
	public static final Splitter DIRECTORY_SPLITTER = Splitter.on( File.separatorChar );

	/** Pair splitter for directory paths. */
	public static final Splitter PAIR_DIRECTORY_SPLITTER = DIRECTORY_SPLITTER.limit( 2 );

	public static final MapJoiner PROPERTY_MAP_JOINER = Joiner.on(", ").withKeyValueSeparator(": ").useForNull( "null" );

	/** Joiner for whitespace. */
	public static final Joiner WS_JOINER = Joiner.on( ' ' ).skipNulls();

	/** Joiner for directory paths. */
	public static final Joiner DIRECTORY_JOINER = Joiner.on( File.separatorChar ).skipNulls();

	/** Joiner for slugs. */
	public static final Joiner SLUG_JOINER = Joiner.on( '_' ).skipNulls();

	/** Joiner for slugs. */
	public static final char PACKAGE_SEPARATOR = '.';

	/** Joiner for slugs. */
	public static final Joiner QUALIFIER_JOINER = Joiner.on( PACKAGE_SEPARATOR ).skipNulls();

	/**
	 * "Casts" a string to uppercase.
	 *
	 * @param string the string to uppercase.
	 * @return the string, uppercased.
	 */
	public static String castUpper( String string ) {
		return string.toUpperCase( Locale.getDefault() );
	}

	/**
	 * "Casts" a string to lowercase.
	 *
	 * @param string the string to lowercase.
	 * @return the string, lowercase.
	 */
	public static String castLower( String string ) {
		return string.toLowerCase( Locale.getDefault() );
	}

	/**
	 * Capitalizes the first letter of a string.
	 *
	 * @param string the string to capitalize.
	 * @return the capitalized string.
	 */
	public static String capitalizeFirst( String string ) {
		return string.substring( 0, 1 ).toUpperCase( Locale.getDefault() ) + string.substring( 1 ).toLowerCase( Locale.getDefault());
	}

	/**
	 * Reads a hex-value-string, into a natural number, removes 0x / # prefixes.
	 *
	 * @param value the value string.
	 * @return natural read number.
	 */
	public static Integer readHexString( String value ) {
		if ( value.charAt( 0 ) == '#' ) {
			value = value.substring( 1 );
		} else if ( value.startsWith( "0x" ) ) {
			value = value.substring( 2 );
		}

		return Integer.parseInt( value, 16 );
	}

	/** Joiner for time, the separator is ":". */
	public static final Joiner TIME_JOINER = Joiner.on( ':' ).skipNulls();

	/**
	 * Joins time parts together according to {@link #TIME_JOINER}.<br/>
	 * All the parts are padded with 1 leading zero before joining.
	 *
	 * @param parts the time parts to join together.
	 * @return the time parts joined together.
	 */
	public static final String joinTime( int... parts ) {
		String[] paddedParts = new String[parts.length];
		for ( int i = 0; i < parts.length; ++i ) {
			paddedParts[i] = Strings.padStart( Integer.toString( parts[i] ), 2, '0' );
		}

		return TIME_JOINER.join( paddedParts );
	}
}