package org.supposition.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.supposition.text.TextManager;

public final class MessagesManager {
	public static Log _log = LogFactory.getLog("org.supposition.utils.MessageFactory");
	private static final String _beans_text_manager_id = "textManager";
	private static final String _defaultBaseName = "messages";
	private static final boolean _isTomcatContext = Utils.isTomcatContext();

	private static TextManager _textManager = null;

	// *** TEXT MANAGER part ****
	public static String getText(String inKey) {
		checkInitTextManager();
		return _textManager.getTextByKey(inKey, getLocale());
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



}
