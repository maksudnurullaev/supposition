package org.supposition.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.supposition.text.TextManager;

public final class MessagesManager {
	public static Log _log = LogFactory.getLog("org.supposition.utils.WebMessageFactory");	
	private static WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	
	private static TextManager _textManager = (TextManager) wac.getBean(Constants._beans_text_manager);
	
	
	// *** TEXT MANAGER part ****
	public static  String getText(String inKey){
		return _textManager.getTextByKey(inKey);
	}
	
	// *** MESSAGE SENDER part ****
	public static boolean hasMessageByKey(String inKey){
		return _textManager.hasKey(inKey);
	}
	
	public static void changeLocale(String inLocale) {
		_textManager.setLocale(inLocale);
	}

	public static String getLocale() {
		return _textManager.getLocale();
	}	
}
