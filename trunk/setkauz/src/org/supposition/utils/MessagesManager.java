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

	// *** TEXT MANAGER part ****
	public static String getText(String inKey) {
		checkInitTextManager();
		return _textManager.getTextByKey(inKey, getLocale());
	}

	// *** TEXT MANAGER part  # 2****
	public static Map<String, String> getText2(String inKey) {
		checkInitTextManager();
		
		//DefaultHttpRequestBeanProxy result = new DefaultHttpRequestBeanProxy();
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("eval", null);
		result.put("text", null);
		
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
	
	public static String getDefault(String inKey) {
		checkInitTextManager();
		return _textManager.getDefaultByKey(inKey);
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

	// *** MESSAGE SENDER part ****
	public static boolean hasMessageByKey(String inKey) {
		checkInitTextManager();
		return _textManager.hasKey(inKey, getLocale());
	}
	
	public static boolean hasDefaultByKey(String inKey) {
		checkInitTextManager();
		return _textManager.hasDefaultKey(inKey);
	}

	public static void changeLocale(String inLocale) {
		checkInitTextManager();
		// Set locale
		SessionManager.setSessionLocale(inLocale);
	}

	public static String getLocale() {
		return SessionManager.getSessionLocale();
	}

	public static int getSessionIntValue(String inKey) {
		return SessionManager.getSessionIntValue(inKey);
	}

	public static String okDataSaveMessage() {
		return  okPrefix() + getText("message.data.saved");
	}

	public static String errorDataNotSaveMessage() {
		return  errorPrefix() + getText("message.data.NOT.saved");
	}
	
	public static String errorPrefix(){
		return  getDefault("web.error.result.prefix");
	}
	
	public static String okPrefix(){
		return  getDefault("web.ok.result.prefix");
	}

	public static Properties getDefaults() {
		return  _textManager.getDefaults();
	}
	


}
