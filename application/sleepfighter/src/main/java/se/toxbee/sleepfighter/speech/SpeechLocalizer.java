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

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.joda.time.DateTime;

import java.util.Locale;

import se.toxbee.sleepfighter.R;

// localizes the text read out by the speech feature. 
public class SpeechLocalizer {
	
	private Locale originalDeviceLocale;
	private Context context;
	private Locale ttsLocale;
	private TextToSpeech tts;

	public SpeechLocalizer(TextToSpeech tts, Context context) {
		this.context = context;
		this.ttsLocale = tts.getLanguage();
		this.tts = tts;
	}
		
	private String getCurrentTime() {
		DateTime time =  new DateTime();
		int min = time.getMinuteOfHour();
		int hour = time.getHourOfDay();
		return new TimeFormatter(ttsLocale).formatTime(hour, min);
	}
	
	private String getString(int id) {
		return context.getResources().getString(id);
	}

	private String getWakeUp() {
		return getString(R.string.speech_wake_up_format);
	}
	
	private String getTime() {
		String timeFormat = getString(R.string.speech_time_format);
		return String.format(timeFormat, getCurrentTime());
	}
	
	private String getWeather(WeatherDataFetcher weather) {
		return getWeather(weather.getSummary());
	}
	
	private String getWeather(String weather) {
		String weatherFormat =getString(R.string.speech_weather_format);
		
		String w = new WeatherTranslator(ttsLocale, context).translate(weather);
		return String.format(weatherFormat, w);		
	}
	
	private void switchToBestLocale() {
		// If there is no voice for the current language of the user, switch to English. 
		
		originalDeviceLocale = Locale.getDefault();
		
		if(TextToSpeechUtil.languageHasVoice(originalDeviceLocale, tts, context)) {
			return;
			// we already have a voice, we don't need to switch locale. 
		}
			
		Configuration conf = context.getResources().getConfiguration();
		conf.locale = Locale.ENGLISH;
		DisplayMetrics metrics = new DisplayMetrics();
		
		WindowManager windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		new Resources(context.getAssets(), metrics, conf);
	}

	// now we restore the original locale used by the device.
	private void restoreLocale() {
		
		if(TextToSpeechUtil.languageHasVoice(originalDeviceLocale, tts, context)) {
			return;
		}
		
		
		Configuration conf = context.getResources().getConfiguration();
		conf.locale = originalDeviceLocale;
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);

		windowManager.getDefaultDisplay().getMetrics(metrics);
		new Resources(context.getAssets(), metrics, conf);
	}
	
	// the text to be read out. Both the time and weather is read out. 
	public String getSpeech(WeatherDataFetcher weather) {
		switchToBestLocale();
		String s = getWakeUp()  + " " +  getTime() + " " + getWeather(weather);
		restoreLocale();
		return s;
	}
	
	public String getSpeech() {
		switchToBestLocale();
		String s = getWakeUp()  + " " +  getTime();
		restoreLocale();
		return s;
	}


	// the text to be read out. Both the time and weather is read out. 
	public String getSpeech(String weather) {
		switchToBestLocale();
		String s = getWakeUp()  + " " +  getTime() + " " + getWeather(weather);
		restoreLocale();
		return s;
	}
}
