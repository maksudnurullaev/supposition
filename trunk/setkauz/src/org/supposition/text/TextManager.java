package org.supposition.text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.text.abstracts.PropertyLoader;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;
import org.supposition.utils.Utils;

public class TextManager extends PropertyLoader {
	private static final String DEFAULTS_KEY = "defaults";
	private static final String DEFAULTS_FILE_NAME = "defaults.properties";

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
		if (result.indexOf("CGROUPS_AS_SELECT") != -1) {
			result = result.replaceAll("CGROUPS_AS_SELECT",  DBUtils.getGroupsAsHTMLSelect());
			_log.debug("replaceFinalTokensInText --> FOUND --> CGROUPS_AS_SELECT");
		}
		if (result.indexOf("CGROUPS_AS_HTML_ADMIN") != -1) {
			result = result.replaceAll("CGROUPS_AS_HTML_ADMIN",  DBUtils.getGroupsAsHTML(true));
			_log.debug("replaceFinalTokensInText --> FOUND --> CGROUPS_AS_HTML_ADMIN");
		}
		if (result.indexOf("CGROUPS_AS_HTML") != -1) {
			result = result.replaceAll("CGROUPS_AS_HTML",  DBUtils.getGroupsAsHTML(false));
			_log.debug("replaceFinalTokensInText --> FOUND --> CGROUPS_AS_HTML");
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
		return _propertiesMap.get(DEFAULTS_KEY);
	}

}
