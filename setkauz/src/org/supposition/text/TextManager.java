package org.supposition.text;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.text.abstracts.PropertyLoader;
import org.supposition.utils.Constants;

public class TextManager extends PropertyLoader {
	private static Log _log = LogFactory.getLog(PropertyLoader.class);	
	
	private Map<String, Properties>  _propertiesMap = null;
	private String _currentBasename = null;
	
	public TextManager() {
		super();
	}
	
	public void setBasename(String name){
		_currentBasename = name;
		_propertiesMap = new HashMap<String, Properties>();
		_log.debug("Basename for property files now is " + _currentBasename);
	}
	
	public String getBasename() {
		return _currentBasename;
	}
	
	public void setLocale(String inLocale){
		if(!_propertiesMap.containsKey(inLocale)){
			loadPropertyFile(inLocale);
			_log.debug("Texts loaded for " + inLocale);
		}else{
			_log.debug("Texts already cashed for " + inLocale);
		}
	}
	
	public String getTextByKey(String inKey, String inLocale){
		if(!hasLocale(inLocale))loadPropertyFile(inLocale);
		
		// Build string with start & end tags
		String result = "";
		
		// HEADER TAG
		if(_propertiesMap.get(inLocale).containsKey(inKey + Constants._text_header_def)){
			result += _propertiesMap.get(inLocale).getProperty(inKey + Constants._text_header_def);
		}
			
		// ACTUAL TEXT
		if(_propertiesMap.get(inLocale).containsKey(inKey)){
			result += _propertiesMap.get(inLocale).getProperty(inKey);
		}else{
			result += "Could not find text by key = " + inKey;
		}
		
		// FOOTER TAG 
		if(_propertiesMap.get(inLocale).containsKey(inKey + Constants._text_footer_def)){
			result += _propertiesMap.get(inLocale).getProperty(inKey + Constants._text_footer_def);
		}
		
		return replaceFinalTokensInText(result); 
	}

	private String replaceFinalTokensInText(String result) {
		if(result.indexOf("INSERT_UNIQUE_DATETIME") != -1){
			result = result.replaceAll("INSERT_UNIQUE_DATETIME", Constants.getUniqueDateTime());
		}
		return result;
	}

	public boolean hasLocale(String inLocale){
		return _propertiesMap.containsKey(inLocale);
	}
	
	public boolean hasKey(String inKey, String inLocale){
		return _propertiesMap.get(inLocale).containsKey(inKey);
	}
	
	public void loadPropertyFile(String inLocale){
		String fileName = getBasename() + Constants._file_name_delimiter + inLocale;
		_propertiesMap.put(inLocale, loadProperties(fileName));
		_log.debug(String.format("Propetry file %s has %d key(s)", fileName, _propertiesMap.get(inLocale).size()));
	}
}