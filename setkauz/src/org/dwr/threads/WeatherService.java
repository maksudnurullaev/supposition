package org.dwr.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.text.abstracts.PropertyLoader;
import org.supposition.utils.DBUtils;
import org.supposition.utils.Utils;

public class WeatherService implements Runnable {
	private static Log _log = LogFactory.getLog(PropertyLoader.class);
	private String weatherServiceURL = null;
	private Map<String, String> weatherData = new HashMap<String, String>();
	
	/**
	 * @return the weatherData
	 */
	public Map<String, String> getWeatherData() {
		return weatherData;
	}

	@Override
	public void run() {
		if(getWeatherServiceURL() == null){
			weatherData.put("error", "url is null");
			return;
		}
		
		// Get data from weather service
		URLConnection weatherServiceConnection = null;

		try {
			URL weatherURL = new URL(getWeatherServiceURL());
			
			_log.debug("try to open connectio with " + getWeatherServiceURL());
			weatherServiceConnection = weatherURL.openConnection();
			
			_log.debug("try to open BufferedReader(in)");
			BufferedReader in = new BufferedReader( 
		             new InputStreamReader(weatherServiceConnection.getInputStream())); 
			
			String result = "", inputLine;
			
			_log.debug("read lines of BufferedReader(in)");
            while ((inputLine = in.readLine()) != null) { 
            	result += inputLine; 
            } 
            
            in.close(); 
			
			_log.debug("set to xml key result:" + result);            
            weatherData.put("xml", result);
            weatherData.put("nextUpdate", Utils.GetFormatedDate("yyyyMMddHHmm",DBUtils.dateAfterInHours(1)));
            _log.debug("set next update time:" + weatherData.get("nextUpdate"));
			
			
		} catch (MalformedURLException e) {
			weatherData.put("error", e.getMessage());
		} catch (IOException e) {
			weatherData.put("error", e.getMessage());
		}		
	}

	public void setWeatherServiceURL(String weatherServiceURL) {
		_log.debug("weatherServiceURL set to " + weatherServiceURL);
		this.weatherServiceURL = weatherServiceURL;
	}

	public String getWeatherServiceURL() {
		return weatherServiceURL;
	}
	

}
