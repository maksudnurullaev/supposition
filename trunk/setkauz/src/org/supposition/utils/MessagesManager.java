package org.supposition.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.supposition.text.TextManager;

public final class MessagesManager {
	public static Log _log = LogFactory.getLog("org.supposition.utils.MessageFactory");
	private static final String _defaultBaseName = "messages";
	private static final String _defaultLocale = "ru";
	private static final boolean _isTomcatContext = Constants.isTomcatContext();

	private static TextManager _textManager = null;

	// *** TEXT MANAGER part ****
	public static String getText(String inKey) {
		checkTextManager();
		return _textManager.getTextByKey(inKey, getLocale());
	}

	private static void checkTextManager() {
		if (_textManager == null) {
			// Get TextManager bean from spring framework
			if(_isTomcatContext){
				_log.debug("Loading TextManager from WebApplicationContext");
				_textManager = (TextManager) ContextLoader
					.getCurrentWebApplicationContext().getBean(Constants._beans_text_manager);
			}else{
				_textManager = new TextManager();
				_textManager.setBasename(_defaultBaseName);
			}
		}

	}

	// *** MESSAGE SENDER part ****
	public static boolean hasMessageByKey(String inKey) {
		checkTextManager();
		return _textManager.hasKey(inKey, getLocale());
	}

	public static void changeLocale(String inLocale) {
		checkTextManager();
		// Set locale
		if(_isTomcatContext) SessionManager.setSessionLocale(inLocale);
		_textManager.setLocale(inLocale);
	}

	public static String getLocale() {
		if(_isTomcatContext){
			return SessionManager.getSessionLocale();
		}
		return _defaultLocale;
	}

	public static int getSessionIntValue(String inKey) {
		if(_isTomcatContext)
			return SessionManager.getSessionIntValue(inKey);
		else{
			if(inKey.equalsIgnoreCase(Constants._page_size_def)){
				return Constants.getIntFromStr(MessagesManager.getText("default.page.size"));
			}
		}
		return 0;
	}

}
