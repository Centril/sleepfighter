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

package se.toxbee.sleepfighter.speech;

import java.util.Locale;

// Utility for formatting times to strings to be read out by the speech feature:
// For example, 9:45 will be formatted to "quarter to 10" if the Locale is English.
public class TimeFormatter {
	
	private final Locale locale;
	private final String am;
	private final String pm;
	private final String past;
	private final String to;
	private final String quarter;
	private final String halfPast;
	
	
	public TimeFormatter(final Locale locale) {
		this.locale = locale;
		
		if(TextToSpeechUtil.isSwedish(locale)) {
			this.am = "på förmiddagen";
			this.pm = "på eftermiddagen";	
			to = "i";
			past = "över";
			quarter = "kvart";
			halfPast = "halv";
		} else if(TextToSpeechUtil.isJapanese(locale)) {
			// These are not needed in the Japanese version. 
			this.am = null;
			this.pm = null;	
			to = null;
			past = null;
			quarter = null;
			halfPast = null;
		}   else {
			// we default to using English. 
			this.am = "a.m. ";
			this.pm = "p.m. ";
			past = "past";
			to = "to";
			quarter = "quarter";
			halfPast = "half past";
		}
	}

	// round the minutes to its nearest five minutes
	// For example, 2 should be rounded to 0, 23 to 25, and 22 to 20
	private int roundMinute(int min) {
		
		//hämta entalssiffran.
		int m = min % 10;
		// tiotalssiffran
		int n = min - m;
		
		if(m== 0)  {
			// doesn't need rounding 
			return n;
		}else if(m == 1 || m == 2) {
			return n; 
		} else if(m == 3 || m==4) {
			return n + 5;
		} else if(m == 5) {
			// doesn't need rounding
			return min;
		} else if(m == 6 || m == 7) {
			return n + 5;
		} else if(m == 8 || m == 9) {
			return n + 10;
		} else {
			throw new IllegalArgumentException("this should not happen");
		}
	}
		
	private String minuteToStringUtil(int min) {
		if(min == 5) {
			return "5";
		} else if(min == 10) {
			return "10";
		} else if(min == 15) {
			return quarter;
		} else if(min == 20) {
			return "20";
		} else if(min == 25) {
			return "25";
		} else {
			throw new IllegalArgumentException("this should not happen");
		}
	}
	
	private String minuteToString(int min) {
		if(min == 30) {
			return halfPast;
		}
		

		if(TextToSpeechUtil.isSwedish(locale)) {

			// because for example "25 past 3" becomes "5 i half fyra" in Swedish. 
			if(min == 25) {
				return "5 " + to + " " + halfPast; 
			} else if(min == 35) {
				return "5 " + past + " " + halfPast;
			}
		}
	
		String pastOrTo;
		if(min > 30) {
			pastOrTo =to;
			min = 60 -min;
		} else {
			pastOrTo = past;
		}
		
		return minuteToStringUtil(min) + " " + pastOrTo;
	}	
	
	// returns a string formatted the way humans read it.
	// if the language is English, then for 10:30 it will return "half past 10" and
	// for 6:10 it will return "10 past 6"
	public String formatTime(int hour, int originalMin) {
	
		int min = roundMinute(originalMin);
		
		// if we rounded to the next hour
		if(min == 60 && originalMin > 55) {
			hour = (hour + 1) % 24;
			min = 0;
		}
		
		if(TextToSpeechUtil.isJapanese(locale)) {
			if(min == 0) {
				return String.format("%1$s時", hour);
			} else {
				return String.format("%1$s時%2$s分", hour, min);
			}
		}
				
		String ampm;
		// we use 12-hour clock
		// http://en.wikipedia.org/wiki/12-hour_clock
		if(hour < 12) {
			if(hour < 1)  {
				hour = 12;
			}
			ampm = am;
		} else {
			
			if(hour < 13) {
				// do nothing
			} else {
				hour  = hour - 12;
			}
			ampm = pm;
		}
		
		String hourStr;
		if(min == 0)  {
			hourStr = Integer.toString(hour);
		} else {
			
			int h;	
			/*
			 * "half past" and "halv" works differently. For example,
			 * "Half past ten" becomes "halv elva"
			 */
			if(TextToSpeechUtil.isSwedish(locale)) {
				
				if(min < 30 &&
						// because for example "25 past 3" becomes "5 i half fyra" in Swedish. 
						min != 25) {
					h = hour;
				} else {
					h = (1+hour);
				}
			} else {
				if(min <= 30) {
					h = hour;
				} else {
					h = (1+hour);
				}
			}
	
			if(h == 13) {
				h = 1;
			}
			
			hourStr = Integer.toString(h);
		}
		if(min == 0) {
			// minutes are 0, so we only need to tell the hours. 
			return hourStr + " " + ampm;
		}
		String minStr = minuteToString(min);
		return minStr +" "+ hourStr + " " + ampm ;
	}
}
