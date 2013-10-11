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
package se.chalmers.dat255.sleepfighter.speech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

public class TextToSpeechUtil {
	
	private TextToSpeechUtil() {	
	}

	// set some miscellaneous settings for tts.
	public static void config(TextToSpeech tts) {
/*		tts.setSpeechRate(1.8f);
		tts.setPitch(1.2f);*/
	}
	
	public static final int CHECK_TTS_DATA_REQUEST_CODE = 2;

	// check whether we have TTS data. 
	public static void checkTextToSpeech(Activity activity) {
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		activity.startActivityForResult(checkIntent, CHECK_TTS_DATA_REQUEST_CODE);
	}
	
	/*
	 * Returns whether there exists an installed voice for the locale on the current device. 
	 */
	/*public boolean localeHasLanguage(Locale locale) {
		
	}
	
	private static Locale getBestLanguage() {
		
	}*/
	
	
	public static void setBestLanguage(TextToSpeech tts, Context context) {
		
		Locale[] locales = Locale.getAvailableLocales();
		List<Locale> localeList = new ArrayList<Locale>();
		for (Locale locale : locales) {
		    int res = tts.isLanguageAvailable(locale);
		    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
		        localeList.add(locale);
		//        Debug.d("locale: " + locale.toString());
		    }
		}
		// localeList now contains the available locales for tts. 
		
		Locale current = context.getResources().getConfiguration().locale;
		Debug.d("current locale: " + current);
		
		// exact match?
		for(Locale locale : localeList) {
			if(locale.equals(current)) {
				tts.setLanguage(locale);
				return;
			}
		}
		
		
		// different countries, but same language?
		for(Locale locale : localeList) {	
			if(locale.getLanguage() == current.getLanguage()) {
				tts.setLanguage(locale);
				return;
			}
		}
		
		// if we found no match, we'll have to default to English. 
		tts.setLanguage(Locale.ENGLISH);
	}
	
	// time and in the temperate
	
	// get the time string to read out. 
	public static String getCurrentTime() {
		DateTime time =  new DateTime();
		int min = time.getMinuteOfHour();
		int hour = time.getHourOfDay();
		return TimeFormatter.formatTime(hour, min);
	}

}
