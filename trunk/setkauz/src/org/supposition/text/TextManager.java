package org.supposition.text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dwr.threads.WeatherService;
import org.supposition.text.abstracts.PropertyLoader;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;
import org.supposition.utils.Utils;

public class TextManager extends PropertyLoader {
	private static final String DEFAULTS_KEY = "defaults";
	private static final String DEFAULTS_FILE_NAME = "defaults.properties";
	private static Log _log = LogFactory.getLog(TextManager.class);

	private static final Map<String, Properties> _propertiesMap = new HashMap<String, Properties>();

	private static final Map<String, Map<String,String>> _weatherMap = new HashMap<String, Map<String,String>>();
	
	
	private String _currentBasename = null;
	public TextManager() {
		super();
	}

	public void setBasename(String name) {
		_currentBasename = name;
	}

	public String getBasename() {
		return _currentBasename;
	}

	public String getDefaultByKey(String inKey) {
		_log.debug("getDefaultByKey for inKey:" + inKey);
		
		if (!hasDefaultKey(inKey)){
			_log.warn("Could not find DEFAULT by key = " + inKey);
			return "Could not find DEFAULT by key = " + inKey;
		}

		// Build string with start & end tags
		String result = "";

		// ACTUAL TEXT
		if (_propertiesMap.get(DEFAULTS_KEY).containsKey(inKey)) {
			result += _propertiesMap.get(DEFAULTS_KEY).getProperty(inKey);
		} else {
			result += "Could not find text by key = " + inKey;
		}

		return result;
	}

	public void setDefaultByKey(String inKey, String inValue) {
		_log.debug("setDefaultByKey for inKey:" + inKey + " value:" + inValue);
		
		_propertiesMap.get(DEFAULTS_KEY).put(inKey, inValue);
		
	}	
	
	public String getTextByKey(String inKey, String inLocale) {
		_log.debug("getTextByKey for key " + inKey);
		if (!hasKey(inKey, inLocale)){
			_log.warn("Could not find text by key = " + inKey);
			return "Could not find text by key = " + inKey;
		}
		
		// Get list of roles
		List<String> roles = SessionManager.getUserRoles();

		// Build string with start & end tags
		String result = "";

		// 1. HEADER TAG
		result += tryToGetText(inLocale,inKey + MessagesManager.getDefault("text.header.def"));
		
		// 2. ACTUAL TEXT
		result += _propertiesMap.get(inLocale).getProperty(inKey);


		// 3. Get parts by User roles
		for(String role:roles)result += tryToGetText(inLocale,inKey + "." + role);
		
		// 4. FOOTER TAG
		result += tryToGetText(inLocale,inKey + MessagesManager.getDefault("text.footer.def"));

		_log.debug("Try to replace finale tokens for -> " + result);
		return replaceFinalTokensInText(result);
	}

	private String tryToGetText(String inLocale, String inKey){
		_log.debug("tryToGetText for key " + inKey);
		if (_propertiesMap.get(inLocale).containsKey(inKey)) {
			_log.debug("tryToGetText for key " + inKey + " FOUND");
			return _propertiesMap.get(inLocale).getProperty(inKey);
		}		
		return "";
	}
	
	private String replaceFinalTokensInText(String result) {
		_log.debug("replaceFinalTokensInText --> START");
		if (result.indexOf("INSERT_UNIQUE_DATETIME") != -1) {
			result = result.replaceAll("INSERT_UNIQUE_DATETIME", Utils
					.getUniqueDateTime());
			_log.debug("replaceFinalTokensInText --> FOUND --> INSERT_UNIQUE_DATETIME");
		}
		if (result.indexOf("INSTERT_SYSTEM_DEFAULTS") != -1) {
			result = result.replaceAll("INSTERT_SYSTEM_DEFAULTS",  SessionManager.getSystemDefaultsAsHTMLMgmTable());
			_log.debug("replaceFinalTokensInText --> FOUND --> INSTERT_SYSTEM_DEFAULTS");
		}
		if (result.indexOf("CGROUPS_AS_SELECT4ADS") != -1) {
			result = result.replaceAll("CGROUPS_AS_SELECT4ADS",  DBUtils.getGroupsAsHTMLSelect("ads"));
			_log.debug("replaceFinalTokensInText --> FOUND --> CGROUPS_AS_SELECT4ADS");
		}
		if (result.indexOf("CGROUPS_AS_SELECT4COMPANY") != -1) {
			result = result.replaceAll("CGROUPS_AS_SELECT4COMPANY",  DBUtils.getGroupsAsHTMLSelect("company"));
			_log.debug("replaceFinalTokensInText --> FOUND -->CGROUPS_AS_SELECT4COMPANY");
		}		
		if (result.indexOf("CGROUPS_AS_SELECT4CGROUPS") != -1) {
			result = result.replaceAll("CGROUPS_AS_SELECT4CGROUPS",  DBUtils.getGroupsAsHTMLSelect("cgroup"));
			_log.debug("replaceFinalTokensInText --> FOUND -->CGROUPS_AS_SELECT4CGROUPS");
		}		
		if (result.indexOf("CGROUPS_AS_HTML_ADMIN") != -1) {
			result = result.replaceAll("CGROUPS_AS_HTML_ADMIN",  DBUtils.getGroupsAsHTML(true));
			_log.debug("replaceFinalTokensInText --> FOUND --> CGROUPS_AS_HTML_ADMIN");
		}
		if (result.indexOf("CGROUPS_AS_HTML") != -1) {
			result = result.replaceAll("CGROUPS_AS_HTML",  DBUtils.getGroupsAsHTML(false));
			_log.debug("replaceFinalTokensInText --> FOUND --> CGROUPS_AS_HTML");
		}
		if (result.indexOf("HTML_SELECT_CITIES4ADS") != -1) {
			result = result.replaceAll("HTML_SELECT_CITIES4ADS", Utils.getHTMLSelectCity4("ads"));
			_log.debug("replaceFinalTokensInText --> FOUND --> HTML_SELECT_CITIES4ADS");
		}	
		if (result.indexOf("HTML_SELECT_WEATHER_CITIES") != -1) {
			result = result.replaceAll("HTML_SELECT_WEATHER_CITIES", MessagesManager.getText("html.select.weather.cities"));
			_log.debug("replaceFinalTokensInText --> FOUND --> HTML_SELECT_WEATHER_CITIES");
		}
		if (result.indexOf("HTML_DEFAULT_WEATHER") != -1) {
			result = result.replaceAll("HTML_DEFAULT_WEATHER", MessagesManager.getWeather(WeatherService.WEATHER_DEFAULT_URL));
			_log.debug("replaceFinalTokensInText --> FOUND --> HTML_DEFAULT_WEATHER");
		}		
		if (result.indexOf("HTML_SELECT_CITIES4COMPANY") != -1) {
			result = result.replaceAll("HTML_SELECT_CITIES4COMPANY", Utils.getHTMLSelectCity4("company"));
			_log.debug("replaceFinalTokensInText --> FOUND --> HTML_SELECT_CITIES4COMPANY");
		}
		if (result.indexOf("HTML_DEFAULT_COMPANIES") != -1) {
			result = result.replaceAll("HTML_DEFAULT_COMPANIES", Utils.getHTMLDefaultCompanies());
			_log.debug("replaceFinalTokensInText --> FOUND --> HTML_DEFAULT_COMPANIES");
		}		
		if (result.indexOf("INITIAL_NAV") != -1) {
			result = result.replaceAll("INITIAL_NAV", MessagesManager.getText("INITIAL_NAV"));
			_log.debug("replaceFinalTokensInText --> FOUND --> INITIAL_NAV");
		}		
		if (result.indexOf("INITIAL_CONTEXT") != -1) {
			result = result.replaceAll("INITIAL_CONTEXT", MessagesManager.getText("INITIAL_CONTEXT"));
			_log.debug("replaceFinalTokensInText --> FOUND --> INITIAL_CONTEXT");
		}		
		if (result.indexOf("GLOBAL_AGREEMENT") != -1) {
			result = result.replaceAll("GLOBAL_AGREEMENT", 
					loadFile(MessagesManager.getDefault("default.agreement.path"), 
							Thread.currentThread().getContextClassLoader()));
			_log.debug("replaceFinalTokensInText --> FOUND --> GLOBAL_AGREEMENT");
		}
		
		_log.debug("replaceFinalTokensInText --> END");
		return result;
	}

	public boolean hasLocale(String inLocale) {
		return _propertiesMap.containsKey(inLocale);
	}

	public boolean hasKey(String inKey, String inLocale) {
		if (!hasLocale(inLocale))
			tryToLoadPropertyFile(inLocale);
		return _propertiesMap.get(inLocale).containsKey(inKey);
	}

	public boolean hasDefaultKey(String inKey) {
		if (!hasLocale(DEFAULTS_KEY))
			tryToLoadDefaultsFile();
		return _propertiesMap.get(DEFAULTS_KEY).containsKey(inKey);
	}

	public synchronized void tryToLoadPropertyFile(String inLocale) {
		if (!_propertiesMap.containsKey(inLocale)) {

			String fileName = getBasename()
					+ MessagesManager
							.getDefault("properties.file.name.delimiter")
					+ inLocale;
			_propertiesMap.put(inLocale, loadProperties(fileName));

			int i = 1;
			_log.debug("### start ###");

			for (String key : _propertiesMap.keySet()) {
				_log.debug("" + i + ". " + key);
				i++;
			}
			_log.debug("### end ###");
		}
	}

	private synchronized void tryToLoadDefaultsFile() {
		_propertiesMap.put(DEFAULTS_KEY, loadProperties(DEFAULTS_FILE_NAME));
	}
	
	public Properties getDefaults() {
		//TODO Shuld be inplimented later return _propertiesMap.get(DEFAULTS_KEY);
		return null;		
	}

	public String getWeatherByCityCode(String urlCode) {
		if (!_weatherMap.containsKey(urlCode) 
				|| !_weatherMap.get(urlCode).containsKey(WeatherService.key4ParsedHtml)){
			_log.debug("Could not found weather data for " + urlCode);
			_log.debug("Try to reload weather data");
			tryToLoadWeatherForCity(urlCode);	
		}else{
			_log.debug("Weather data for " + urlCode + " already exist");
			
			// Check expared time key existance
			if(!_weatherMap.get(urlCode).containsKey(WeatherService.key4ExpiredTime)){
				_log.debug("Could not found key4ExpiredTime for " + urlCode);
				_log.debug("Try to reload weather data");
				tryToLoadWeatherForCity(urlCode);				
			}else{
				if(_weatherMap.get(urlCode).get(WeatherService.key4ExpiredTime).compareTo(
						Utils.GetCurrentDateTime(WeatherService.key4ExpiredTimeFormat)) > 0){
					_log.debug(String.format("%s > %s",
							_weatherMap.get(urlCode).get(WeatherService.key4ExpiredTime),
							Utils.GetCurrentDateTime(WeatherService.key4ExpiredTimeFormat)));
					_log.debug("Weather infomation still actual for " + urlCode);
				}else{
					_log.debug(String.format("%s <= %s",
							_weatherMap.get(urlCode).get(WeatherService.key4ExpiredTime),
							Utils.GetCurrentDateTime(WeatherService.key4ExpiredTimeFormat)));
					_log.debug("Weather infomation not actual for " + urlCode);
					_log.debug("Try to reload weather data");
					tryToLoadWeatherForCity(urlCode);					
				}
			}
		}
		
		if(_weatherMap.containsKey(urlCode)){
			_log.debug("Found weather data for " + urlCode);			
			if(_weatherMap.get(urlCode).containsKey(WeatherService.key4ParsedHtml)){
				_log.debug("Weather data has key:" + WeatherService.key4ParsedHtml);
				return _weatherMap.get(urlCode).get(WeatherService.key4ParsedHtml);
			}else{
				_log.warn("Weather data has UNKNOWN data");
				return WeatherService.formateErrorData(MessagesManager.getText("errors.service.inaccessible"));
			}
		}
		
		_log.warn("errors.unmatched.data.objects");
		return WeatherService.formateErrorData(MessagesManager.getText("errors.unmatched.data.objects"));
	}

	private void tryToLoadWeatherForCity(String urlCode) {
		Map<String, String> weatherData = new HashMap<String, String>();
		
		// Initial thread variables
		WeatherService weatherService = new WeatherService();
		weatherService.setWeatherServiceURL(urlCode);
		
		Thread weatherServiceThread = new Thread(weatherService);
		
		weatherServiceThread.start();
		
		_log.debug("Waiting for weatherServiceThread to finish");
		int count = 0;
		int maxCount = 3;
		while(weatherServiceThread.isAlive()){
			_log.debug("Still waiting...");
			
			// Debug thread state
			debugThreadState(weatherServiceThread);

			try {
				_log.debug("Waiting for thread at more 1 second");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				weatherData.put(WeatherService.key4ParsedHtml, WeatherService.formateErrorData(e.getMessage()));
				weatherData.put(WeatherService.key4ExpiredTime, WeatherService.getExpireTime(5));
			}
			
			if(!weatherServiceThread.isAlive()){
				_log.debug("weatherServiceThread Finished & Dead!");				
				break;
			}
			
			if(++count <  maxCount){
				_log.warn("Thread waiting time is OUT!");
				continue;
			}else{
				weatherData.put(WeatherService.key4ParsedHtml, MessagesManager.getText("errors.service.inaccessible"));
				weatherData.put(WeatherService.key4ExpiredTime, WeatherService.getExpireTime(5));
				weatherServiceThread.interrupt();				
			}
		}
		
		weatherData = weatherService.getWeatherData();
		
		// Debug thread state
		debugThreadState(weatherServiceThread);
		
		_log.debug("weatherServiceThread finished...");
		
		// Finish
		_weatherMap.put(urlCode, weatherData);
	}

	private void debugThreadState(Thread inThread) {
		if(inThread.getState() == Thread.State.BLOCKED){
			_log.debug("Thread.State = BLOCKED");
		}else if(inThread.getState() == Thread.State.NEW){
			_log.debug("Thread.State = NEW");
		}else if(inThread.getState() == Thread.State.RUNNABLE){
			_log.debug("Thread.State = RUNNABLE");
		} else if(inThread.getState() == Thread.State.TERMINATED){
			_log.debug("Thread.State = TERMINATED");
		} else if(inThread.getState() == Thread.State.TIMED_WAITING){
			_log.debug("Thread.State = TIMED_WAITING");
		} else if(inThread.getState() == Thread.State.WAITING){
			_log.debug("Thread.State = WAITING");
		} else{
			_log.debug("Thread.State = UNKNOWN");
		} 	
	}



}
