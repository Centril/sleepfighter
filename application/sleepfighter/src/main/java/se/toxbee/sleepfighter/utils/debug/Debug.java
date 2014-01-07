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
package se.toxbee.sleepfighter.utils.debug;

import android.util.Log;

/*
 * If you use this class instead of android.util.Log, then all log messages from our app will have a
 * tag Sleepfighter. This makes it easy to find log messages from our app in logcat. 
 */
public final class Debug{ 

	private static String sDebugTag = "Sleepfighter";
	private static DebugLevel sDebugLevel = DebugLevel.VERBOSE;

	private Debug() {}

	public static String getDebugTag() {
		return Debug.sDebugTag;
	}
	
	
	public static void setDebugTag(final String pDebugTag) {
		Debug.sDebugTag = pDebugTag;
	}

	public static DebugLevel getDebugLevel() {
		return Debug.sDebugLevel;
	}
	
	public static void setDebugLevel(final DebugLevel pDebugLevel) {
		if(pDebugLevel == null) {
			throw new IllegalArgumentException("pDebugLevel must not be null!");
		}
		Debug.sDebugLevel = pDebugLevel;
	}

	public static void v(final String pMessage) {
		Debug.v(pMessage, null);
	}

	public static void v(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.VERBOSE)) {
			Log.v(sDebugTag, pMessage, pThrowable);
		}
	}

	public static void d(final String pMessage) {
		Debug.d(pMessage, null);
	}

	public static void d(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.DEBUG)) {
			Log.d(sDebugTag, pMessage, pThrowable);
		}
	}

	public static void i(final String pMessage) {
		Debug.i(pMessage, null);
	}

	public static void i(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.INFO)) {
			Log.i(sDebugTag, pMessage, pThrowable);
		}
	}

	public static void w(final String pMessage) {
		Debug.w(pMessage, null);
	}

	public static void w(final Throwable pThrowable) {
		Debug.w("", pThrowable);
	}

	public static void w(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.WARNING)) {
			if(pThrowable == null) {
				Log.w(sDebugTag, pMessage, new Exception());
			} else {
				Log.w(sDebugTag, pMessage, pThrowable);
			}
		}
	}

	public static void e(final String pMessage) {
		Debug.e(pMessage, null);
	}

	public static void e(final Throwable pThrowable) {
		Debug.e(sDebugTag, pThrowable);
	}

	public static void e(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.ERROR)) {
			if(pThrowable == null) {
				Log.e(sDebugTag, pMessage, new Exception());
			} else {
				Log.e(sDebugTag, pMessage, pThrowable);
			}
		}
		System.exit(1);
	}
	
	public static enum DebugLevel implements Comparable<DebugLevel> {
		NONE,
		ERROR,
		WARNING,
		INFO,
		DEBUG,
		VERBOSE;

		public static final DebugLevel ALL = DebugLevel.VERBOSE;

		private boolean isSameOrLessThan(final DebugLevel pDebugLevel) {
			return this.compareTo(pDebugLevel) >= 0;
		}
	}
}
