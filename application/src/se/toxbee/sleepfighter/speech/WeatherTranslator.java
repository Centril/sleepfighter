package se.toxbee.sleepfighter.speech;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.utils.debug.Debug;
import se.toxbee.sleepfighter.utils.string.StringUtils;
import android.annotation.SuppressLint;
import android.content.Context;

// used for localizing weather summaries. 
class WeatherTranslator {
	
	private final Locale locale;
	
	private final Context context;

	private Map<String, String> lookupTable;
	
	private String rs(final int i) {
		return context.getResources().getString(i);		
	}
	
	private String and() {
		return context.getResources().getString(R.string.and_word);
	}
	
	public WeatherTranslator(final Locale locale, Context context) {

		this.locale = locale;
		this.context = context;
		if(TextToSpeechUtil.isEnglish(locale)) {
			// No need to translate
		} else {	
			lookupTable = new HashMap<String, String>();
		
			lookupTable.put("clear", rs(R.string.clear_weather));
			
			lookupTable.put("possible drizzle", rs(R.string.possible_very_light_rain));
			lookupTable.put("drizzle", rs(R.string.very_light_rain));
			lookupTable.put("possible light rain", rs(R.string.possible_light_rain));
			lookupTable.put("light rain", rs(R.string.light_rain));
			lookupTable.put("rain", rs(R.string.medium_rain));
			lookupTable.put("heavy rain", rs(R.string.heavy_rain));
		
			lookupTable.put("possible light sleet", rs(R.string.possible_light_sleet));
			lookupTable.put("light sleet", rs(R.string.light_sleet));
			
			lookupTable.put("sleet", rs(R.string.medium_sleet));
			lookupTable.put("heavy sleet", rs(R.string.heavy_sleet));
			lookupTable.put("possible flurries", rs(R.string.possible_very_light_snow));
			lookupTable.put("flurries", rs(R.string.very_light_snow));
			lookupTable.put("possible light snow", rs(R.string.possible_light_snow));
			lookupTable.put("light snow", rs(R.string.light_snow));
			lookupTable.put("snow", rs(R.string.medium_snow));
			lookupTable.put("heavy snow", rs(R.string.heavy_snow));

			lookupTable.put("breezy", rs(R.string.light_wind));
			lookupTable.put("windy", rs(R.string.medium_wind));
			lookupTable.put("dangerously windy", rs(R.string.heavy_wind));
			
			lookupTable.put("dry", rs(R.string.low_humidity));
			lookupTable.put("humid", rs(R.string.high_humidity));
			
			lookupTable.put("foggy", rs(R.string.fog));
			
			lookupTable.put("partly cloudy", rs(R.string.light_clouds));
			lookupTable.put("mostly cloudy", rs(R.string.medium_clouds));
			lookupTable.put("overcast", rs(R.string.heavy_clouds));
		}
		
	}
	

	@SuppressLint("DefaultLocale")
	public String translate(String weatherSummary) {
		if(TextToSpeechUtil.isEnglish(locale)) {
			// Language of forecast.io is English, so we don't need to translate it.
			return weatherSummary;
		} 
	

		// convert to lowercase.
		weatherSummary = weatherSummary.toLowerCase(Locale.ENGLISH);
		
		Debug.d("weather summary: " + weatherSummary);
		
		if(TextToSpeechUtil.isJapanese(this.locale)) {
			return translateToJapanese(weatherSummary);
		}
	
		if(!weatherSummary.contains("and")) {
			// simple weather report, we can just use the lookup table.
			return lookupTable.get(weatherSummary);
		}  else {
			// the weather report specifies two pieces of information, so we have translate "and"
			// as well.
			
			Splitter andSplitter = Splitter.on("and").trimResults();
			List<String> weatherInfos =Lists.newArrayList(andSplitter.split(weatherSummary));
			return 
					lookupTable.get(weatherInfos.get(0)) + " " + and() + " " + 
					lookupTable.get(weatherInfos.get(1));
		}

	}
	
	
	private static class JapaneseWeatherString {
		
		// the weather report in it's te-form
		public String teForm;
		// in it's polite present form
		public String politeForm;
		// we need both of these in order to produce a grammatically correct sentence.
		
		public JapaneseWeatherString(String tableValue) {
			
			// in the lookup table, both the te-form and the polite form of the weather report
			// are stored, separated by a space.
			List<String> w =  Lists.newArrayList(StringUtils.WS_SPLITTER.split(tableValue));
			this.politeForm = w.get(0);
			this.teForm = w.get(1);
		}
	}
	
	/*
	 * There exists no convenient word like the English word "and" in Japanese
	 * (instead you have to conjugate the words properly) 
	 * , so Japanese becomes a special case.
	 */
	public String translateToJapanese(String weatherSummary) {
		if(!weatherSummary.contains("and")) {
			JapaneseWeatherString jap = new JapaneseWeatherString(lookupTable.get(weatherSummary));
			return jap.politeForm;
		}  else {
			Splitter andSplitter = Splitter.on("and").trimResults();
			List<String> weatherInfos =Lists.newArrayList(andSplitter.split(weatherSummary));
			
			JapaneseWeatherString jap1 = new JapaneseWeatherString((lookupTable.get(weatherInfos.get(0))));
			JapaneseWeatherString jap2 = new JapaneseWeatherString((lookupTable.get(weatherInfos.get(1))));
		
			return jap1.teForm + ", " + jap2.politeForm;
		}
	}
}
