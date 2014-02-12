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
