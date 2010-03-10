package org.hydra.text;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hydra.text.abstracts.PropertyLoader;
import org.hydra.utils.Constants;

public class TextManager extends PropertyLoader {
	private static Log _log = LogFactory.getLog(org.hydra.executors.Executor.class);	
	
	private Properties _property = null;
	private String _basename = null;
	private String _locale = null;
	
	public TextManager() {
		super();
	}
	
	public void setBasename(String name){
		_basename = name;
		_property = null;
		_log.debug("Basename for property file now is " + _basename);
	}

	public String getBasename() {
		return _basename;
	}

	public String getLocale() {
		return _locale;
	}
	
	public void setLocale(String locale){
		_locale = locale;
		_property = null;
		_log.debug("Selected language for property file now is " + _locale);
	}
	
	public String getTextByKey(String inKey){
		if(_property == null) loadPropertyFile();
		
		if(_property == null){
			_log.error("TextManager is not properly initiated!");
			return "TextManager is not properly initiated!";
		}
		String result = _property.getProperty(inKey);
		return (result != null ? result : "Could not find text by key = " + inKey); 
	}
	
	public String getTestMe(String inKey){
		return inKey;
	}

	public boolean hasKey(String inKey){
		return _property.containsKey(inKey);
	}
	
	public void loadPropertyFile(){
		String fileName = _basename + Constants._file_name_delimiter + _locale;
		_property = loadProperties(fileName);
		_log.debug(String.format("Propetry file %s has %d key(s)", fileName, _property.size()));
		for(Object key: _property.keySet()){
			_log.debug(String.format("%s = %s", key, _property.get(key)));
		}
	}
}
