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
	
	private JSONObject jObject;
	
	
	// use this instead of the constuctor; the constructor does nothing.
	// build the url used to access the weather data, given latitudes and longitudes. 
	public synchronized void init(double lat, double lon) {
		try {
			// fetch the json data form forecast.io
			String json = httpGET(buildUrl(lat, lon));
			
			
			Debug.d("json: " + json);
			
		   jObject = new JSONObject(json);
		   
		   JSONObject currently = jObject.getJSONObject("currently");
		   String summary = currently.getString("summary");
		   Debug.d("summary :" + summary);
		   
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// get a summary of the weather condition
	public synchronized String getSummary() {
		String summary = "no data";
		try {
			JSONObject currently = jObject.getJSONObject("currently");
			summary = currently.getString("summary");
		} catch (JSONException e) {
			Debug.e(e);
		}
		return summary;
	}
	
	private synchronized String buildUrl(double lat, double lon) {
		String s =  new String("https://api.forecast.io/forecast/") + KEY + "/" +
				doubleToString(lat) + "," + doubleToString(lon);
		return s;
	}
	
	private synchronized static String doubleToString(double d) {
		return String.valueOf(d).replace(",", ".");
	}
	

	private synchronized String httpGET(String requestURL) {

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
