package org.supposition.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.supposition.text.TextManager;

public final class MessagesManager {
	public static Log _log = LogFactory.getLog("org.supposition.utils.MessageFactory");
	private static final String _beans_text_manager_id = "textManager";
	private static final String _defaultBaseName = "messages";
	private static final boolean _isTomcatContext = Utils.isWebContext();

	private static TextManager _textManager = null;

	public static void changeLocale(String inLocale) {
		checkInitTextManager();
		// Set locale
		SessionManager.setSessionLocale(inLocale);
	}

	private static void checkInitTextManager() {
		if (_textManager == null) {
			// Get TextManager bean from spring framework
			if(_isTomcatContext){
				_textManager = (TextManager) ContextLoader
					.getCurrentWebApplicationContext().getBean(_beans_text_manager_id);
			}else{
				_textManager = new TextManager();
				_textManager.setBasename(_defaultBaseName);
			}
		}

	}
	
	public static String errorDataNotSaveMessage() {
		return  errorPrefix() + getText("message.data.NOT.saved");
	}	
	
	public static String errorPrefix(){
		return  getDefault("web.error.result.prefix");
	}

	public static String getDefault(String inKey) {
		checkInitTextManager();
		return _textManager.getDefaultByKey(inKey);
	}
	
	public static Properties getDefaults() {
		return  _textManager.getDefaults();
	}

	public static String getLocale() {
		return SessionManager.getSessionLocale();
	}

	public static int getSessionIntValue(String inKey) {
		return SessionManager.getSessionIntValue(inKey);
	}

	// *** TEXT MANAGER part ****
	public static String getText(String inKey) {
		checkInitTextManager();
		return _textManager.getTextByKey(inKey, getLocale());
	}

	// *** TEXT MANAGER part  # 2****
	public static Map<String, String> getText2(String inKey) {
		checkInitTextManager();
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("eval", null);
		result.put("text", String.format("Could not find any text by key = %s", inKey));
		
		// Get Eval part
		if(_textManager.hasKey(inKey + ".eval", getLocale())){
			_log.debug("Found EVAL part of " + inKey + ".eval");
			result.put("eval",_textManager.getTextByKey(inKey + ".eval", getLocale()));
		}

		// Get Text part
		if(_textManager.hasKey(inKey, getLocale())){
			_log.debug("Found TEXT part of " + inKey);
			result.put("text",_textManager.getTextByKey(inKey, getLocale()));
		}			
		return result;
	}

	public static boolean hasDefaultByKey(String inKey) {
		checkInitTextManager();
		return _textManager.hasDefaultKey(inKey);
	}
	
	// *** MESSAGE SENDER part ****
	public static boolean hasMessageByKey(String inKey) {
		checkInitTextManager();
		return _textManager.hasKey(inKey, getLocale());
	}
	
	public static String okDataSaveMessage() {
		return  okPrefix() + getText("message.data.saved");
	}

	public static String okPrefix(){
		return  getDefault("web.ok.result.prefix");
	}

	public static String getWeather(String urlCode) {
		String result = _textManager.getWeatherByCityCode(urlCode);
		String[] replaceStrings={"text.Forecast.for",
								 "text.at",
								 "text.temperature",
								 "text.pressure.measure",
								 "text.pressure",
								 "text.relwet",
								 "text.cloudiness.0",
								 "text.cloudiness.1",
								 "text.cloudiness.2",
								 "text.cloudiness.3",
								 "text.precipitation.4",
								 "text.precipitation.5",
								 "text.precipitation.6",
								 "text.precipitation.7",
								 "text.precipitation.8",
								 "text.tod.0",								 
								 "text.tod.1",								 
								 "text.tod.2",								 
								 "text.tod.3"								 
								 };
		
		for (String rString : replaceStrings) {
			result = result.replaceAll(rString, MessagesManager.getText(rString));
		}
		
		return result + MessagesManager.getText("text.link.to.weather.service");
	}
	


}
