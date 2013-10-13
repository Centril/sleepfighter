package se.chalmers.dat255.sleepfighter.speech;

import java.util.Locale;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;

// localizes the text read out by the speech feature. 
public class SpeechLocalizer {
	
	private Locale originalDeviceLocale;
	private Activity activity;
	private Locale ttsLocale;
	private TextToSpeech tts;
	

	public SpeechLocalizer(TextToSpeech tts, Activity activity) {
		this.activity = activity;
		this.ttsLocale = tts.getLanguage();
		this.tts = tts;
	}
		
	private String getCurrentTime() {
		DateTime time =  new DateTime();
		int min = time.getMinuteOfHour();
		int hour = time.getHourOfDay();
		return new TimeFormatter(ttsLocale).formatTime(hour, min);
	}
	
	public String getString(int id) {
		return activity.getResources().getString(id);
	}

	public String getWakeUp() {
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
		
		String w = new WeatherTranslator(ttsLocale, activity).translate(weather);
		return String.format(weatherFormat, w);		
	}
	
	// switch to the tts locale.
	public void switchToBestLocale() {
		// If there is no voice for the current language of the user, switch to English. 
		
		originalDeviceLocale = Locale.getDefault();
		
		if(TextToSpeechUtil.languageHasVoice(originalDeviceLocale, tts, activity)) {
			return;
			// we already have a voice, we don't need to switch locale. 
		}
			
		Configuration conf = activity.getResources().getConfiguration();
		conf.locale = Locale.ENGLISH;
		DisplayMetrics metrics = new DisplayMetrics();
		
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		new Resources(activity.getAssets(), metrics, conf);
	}

	// now we restore the original locale used by the device.
	public void restoreLocale() {
		
		if(TextToSpeechUtil.languageHasVoice(originalDeviceLocale, tts, activity)) {
			return;
		}
		
		
		Configuration conf = activity.getResources().getConfiguration();
		conf.locale = originalDeviceLocale;
		DisplayMetrics metrics = new DisplayMetrics();
		
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		new Resources(activity.getAssets(), metrics, conf);
	}
	
	// the text to be read out. Both the time and weather is read out. 
	public String getSpeech(WeatherDataFetcher weather) {
		switchToBestLocale();
		String s = getWakeUp()  + " " +  getTime() + " " + getWeather(weather);
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
