package se.chalmers.dat255.sleepfighter.activity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;

// gets the current weather from forecast.io given the current coordinates. 
public class WeatherUtil {

	private final String KEY = "cb8a0d4b48c35b562d1b427b3f77552d";
	
	// the json weather data.
	String jsonData;

	
	// build the url used to access the weather data, given latitudes and longitudes. 
	public WeatherUtil(double lat, double lon) {
			// fetch the json data form forecast.io
			jsonData = httpGET(buildUrl(lat, lon));
			
			//Debug.d("json: " + json);
			
		   //jObject = new JSONObject(json);
		
	}
	
	// get a summary of the weather condition
	public String getSummary() {
		
		// For some reason JSONObject threw an exception when parsing the data. 
		// But we are really only interested in one piece of information in this json data.
		// That is, the value of the field "summary" in the object "currently".
		// See: https://developer.forecast.io/docs/v2
		// So we'll extract the value using some elementary string methods. 
		
		// find the currently object
		int currentlyIndex = jsonData.indexOf("\"currently\"");
		
		// now find the "summary" field of the "currently" object
		int summaryIndex = jsonData.indexOf("\"summary\"", currentlyIndex);
		
		// now skip until the value of "summary"
		int i = summaryIndex+1;
		i = jsonData.indexOf("\"", i) + 1;
		i = jsonData.indexOf("\"", i) + 1;
		
		// find the end of the data.
		int endI = jsonData.indexOf("\"", i) + 1;
		
		return (String) jsonData.subSequence(i, endI);
		
		/*
		String summary = "no data";
		try {
			JSONObject currently = jObject.getJSONObject("currently");
			summary = currently.getString("summary");
		} catch (JSONException e) {
			Debug.e(e);
		}
		return summary;*/
	}
	
	private String buildUrl(double lat, double lon) {
		String s =  new String("https://api.forecast.io/forecast/") + KEY + "/" +
				doubleToString(lat) + "," + doubleToString(lon);
		return s;
	}
	
	private static String doubleToString(double d) {
		return String.valueOf(d).replace(",", ".");
	}
	

	private String httpGET(String requestURL) {

		//Variables
		URL request = null;
		HttpURLConnection connection = null;
		Scanner scanner = null;
		String response = "";

		try {
			request = new URL(requestURL);
			connection = (HttpURLConnection) request.openConnection();

			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(false);
			connection.connect();
			if(connection.getResponseCode() == 400){
				System.out.println("Bad Responde. Maybe an invalid location was provided.\n");
				return null;
			}
			else {
				scanner = new Scanner(request.openStream());
				response = scanner.useDelimiter("\\Z").next();
				scanner.close();
			}
		} catch (IOException e) {
			Debug.e(e);		
			response = null;
		} finally {		
			connection.disconnect();
		}

		return response;
	}//httpGET - end

	
}
