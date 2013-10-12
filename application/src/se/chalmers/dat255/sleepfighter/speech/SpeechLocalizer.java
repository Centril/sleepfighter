package se.chalmers.dat255.sleepfighter.speech;

import java.util.Locale;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.R;

import android.content.Context;
import android.speech.tts.TextToSpeech;

// localizes the text read out by the speech feature. 
public class SpeechLocalizer {
	
	private Locale locale;
	private Context context;

	public SpeechLocalizer(TextToSpeech tts, Context context) {
		this.locale = TextToSpeechUtil.getBestLanguage(tts, context);
		this.context = context;
	}
		
	private String getCurrentTime() {
		DateTime time =  new DateTime();
		int min = time.getMinuteOfHour();
		int hour = time.getHourOfDay();
		return new TimeFormatter(locale).formatTime(hour, min);
	}

	
	public String getWakeUp() {
		return context.getResources().getString(R.string.speech_wake_up_format);
	}
	
	private String getTime() {
		String timeFormat = context.getResources().getString(R.string.speech_time_format);
		return String.format(timeFormat, getCurrentTime());
	}
	
	private String getWeather(WeatherDataFetcher weather) {
		String weatherFormat = context.getResources().getString(R.string.speech_weather_format);
		//Locale current = getResources().getConfiguration().locale;
		//String localizedWeatherStr = new WeatherTranslator(current).translate(weatherStr); 
		return String.format(weatherFormat, weather.getSummary());
			
	}
	
	// the text to be read out. Both the time and weather is read out. 
	public String getSpeech(WeatherDataFetcher weather) {
		return getWakeUp()  + " " +  getTime() + " " + getWeather(weather);
	}

	// the text to be read out. The time is only read out, not the time.
	public String getSpeech() {
		return getWakeUp()  + " " +  getTime();	
	}
}
