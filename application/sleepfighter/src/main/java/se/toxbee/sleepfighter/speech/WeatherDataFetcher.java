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

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

// gets the current weather from forecast.io given the current coordinates. 
public class WeatherDataFetcher {
	private static final String KEY = "cb8a0d4b48c35b562d1b427b3f77552d";
	private static final String TAG = WeatherDataFetcher.class.getSimpleName();

	// the json weather data.
	private String jsonData;

	// build the url used to access the weather data, given latitudes and longitudes.
	public WeatherDataFetcher(double lat, double lon) throws IOException {
		// fetch the json data form forecast.io
		Log.d( TAG, "about to fetch json data" );

		jsonData = httpGET(buildUrl(lat, lon));

		Log.d( TAG, "done fetching json data");
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
		
		return (String) jsonData.subSequence(i, endI-1);
	}

	private String buildUrl(double lat, double lon) {
		String s = "https://api.forecast.io/forecast/" + KEY + "/" +
				doubleToString(lat) + "," + doubleToString(lon);
		return s;
	}

	private static String doubleToString(double d) {
		return String.valueOf(d).replace(",", ".");
	}

	private String httpGET(String requestURL) throws IOException {

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
			
			// the data we need is in the beginning, and it is rather short, so we only 
			// need about the first 200 characters. 
			// this speeds things up a bit. 
			// but for some reason it gets stuck when we comment out this line. 
			//connection.setRequestProperty("Content-Length", "" + 200);
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
		}finally {	
			if(connection != null)
				connection.disconnect();
		}

		return response;
	}
}
