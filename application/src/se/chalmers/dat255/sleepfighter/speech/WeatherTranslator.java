package se.chalmers.dat255.sleepfighter.speech;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import se.chalmers.dat255.sleepfighter.R;

import android.content.Context;

// used for localizing weather summaries. 
class WeatherTranslator {
	
	private final Locale locale;
	private final Context context;
	
	private final static Locale SWEDISH = new Locale("sv", "SE");
	
	private Map<String, String> lookupTable;
	

	public String rs(final int i) {
		return context.getResources().getString(i
				
			/*	R.string.speech_wake_up_format*/);		
	}
	
	public WeatherTranslator(final Locale locale, Context context) {
		this.locale = locale;
		this.context = context;
		if(locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
			// No need to translate
		} else {	
			lookupTable = new HashMap<String, String>();
		
			lookupTable.put("clear", rs(R.string.clear));
			
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

	
	public String translate(String weatherSummary) {
		if(locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
			// Language of forecast.io is English, so we don't need to translate it.
			return weatherSummary;
		} 
		
		
		// convert to lowercase.
		weatherSummary = Character.toLowerCase(weatherSummary.charAt(0)) + weatherSummary.substring(1);
		
		// now use lookup table if no "and" can be found. 
		
		return "";
	}
}
