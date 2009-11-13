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
import org.dwr.xml.ParseXMLString;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.Utils;

public class WeatherService implements Runnable {
	private static Log _log = LogFactory.getLog(WeatherService.class);
	private String weatherServiceURL = null;
	private Map<String, String> weatherData = new HashMap<String, String>();
	
	public static final String key4ParsedHtml = "HTML";
	public static final String key4ExpiredTime = "expiredTime";
	public static final String key4ExpiredTimeFormat = "yyyyMMddHHmm";
	
	/**
	 * @return the weatherData
	 */
	public Map<String, String> getWeatherData() {
		return weatherData;
	}

	@Override
	public void run() {
		if(getWeatherServiceURL() == null){
			weatherData.put(key4ParsedHtml, "url is null");
			return;
		}
		
		// Get data from weather service
		URLConnection weatherServiceConnection = null;

		try {
			URL weatherURL = new URL(getWeatherServiceURL());
			
			_log.debug("try to open connectio with: " + getWeatherServiceURL());
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
			setWeatherData(parseXml(result));
            setWeatherDataExpireTime(3);
            _log.debug("set next update time:" + weatherData.get("nextUpdate"));
			
			
		} catch (MalformedURLException e) {
			setWeatherErrorData(e.getMessage());
		} catch (IOException e) {
			setWeatherErrorData(e.getMessage());
		}		
	}

	private String parseXml(String inXMLString) {
		
		ParseXMLString parser = new ParseXMLString();
		return parser.parse(inXMLString);
	}

	private void setWeatherDataExpireTime(int inHours) {
		 weatherData.put(key4ExpiredTime, getExpireTime(inHours));		
	}

	public static String getExpireTime(int inHours) {
		return Utils.GetFormatedDate(key4ExpiredTimeFormat,DBUtils.dateAfterInHours(inHours));
	}

	public void setWeatherServiceURL(String weatherServiceURL) {
		_log.debug("weatherServiceURL set to " + weatherServiceURL);
		this.weatherServiceURL = weatherServiceURL;
	}

	public String getWeatherServiceURL() {
		return weatherServiceURL;
	}
	
	private void setWeatherData(String inData){
		weatherData.put(key4ParsedHtml, inData);
	}
	
	private void setWeatherErrorData(String inData){
		weatherData.put(key4ParsedHtml, formateErrorData(inData));
	}

	public static String formateErrorData(String inData) {
		return String.format("<div class=\"error\">%s</div>", inData);
	}

}
