package org.supposition.text;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.User;
import org.supposition.db.proxy.UserProxy;
import org.supposition.text.abstracts.PropertyLoader;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;
import org.supposition.utils.Utils;

public class TextManager extends PropertyLoader {
	private static final String DEFAULTS_KEY = "defaults";
	private static final String DEFAULTS_FILE_NAME = "defaults.properties";
	private static final String DEFAULT_NONREGISTERED_POSTFIX = ".nonregistered";
	private static final String DEFAULT_REGISTERED_POSTFIX = ".registered";

	private static Log _log = LogFactory.getLog(PropertyLoader.class);

	private Map<String, Properties> _propertiesMap = new HashMap<String, Properties>();

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
		if (!hasDefaultKey(inKey)){
			_log.error("Could not find DEFAULT by key = " + inKey);
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

	public String getTextByKey(String inKey, String inLocale) {
		if (!hasKey(inKey, inLocale)){
			_log.error("Could not find text by key = " + inKey);
			return "Could not find text by key = " + inKey;
		}

		// Build string with start & end tags
		String result = "";
		
		// 1. HEADER TAG
		result += tryToGetText(inLocale,inKey + MessagesManager.getDefault("text.header.def"));
		
		// 2. ACTUAL TEXT
		result += _propertiesMap.get(inLocale).getProperty(inKey);


		// 3. Refistered & Non-registered part - REST
		if(SessionManager.isUserLoggedIn()){
			result += tryToGetText(inLocale,inKey + DEFAULT_REGISTERED_POSTFIX);
			UserProxy users = new UserProxy();
			User user = users.getDBObjectByUuid(SessionManager.getUserUuid());
			if(user != null){
				for(String role:user.getRolesAsList()){
					result += tryToGetText(inLocale,inKey + "." + role);
				}
			}
		}else
			result += tryToGetText(inLocale,inKey + DEFAULT_NONREGISTERED_POSTFIX);		
		
		// 4. FOOTER TAG
		result += tryToGetText(inLocale,inKey + MessagesManager.getDefault("text.footer.def"));

		return replaceFinalTokensInText(result);
	}

	private String tryToGetText(String inLocale, String inKey){
		if (_propertiesMap.get(inLocale).containsKey(inKey)) {
			return _propertiesMap.get(inLocale).getProperty(inKey);
		}		
		return "";
	}
	
	private String replaceFinalTokensInText(String result) {
		if (result.indexOf("INSERT_UNIQUE_DATETIME") != -1) {
			result = result.replaceAll("INSERT_UNIQUE_DATETIME", Utils
					.getUniqueDateTime());
		}else if (result.indexOf("INSTERT_SYSTEM_DEFAULTS") != -1) {
			result = result.replaceAll("INSTERT_SYSTEM_DEFAULTS",  SessionManager.getSystemDefaultsAsHTMLMgmTable());
		}
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
		return _propertiesMap.get(DEFAULTS_KEY);
	}

}
