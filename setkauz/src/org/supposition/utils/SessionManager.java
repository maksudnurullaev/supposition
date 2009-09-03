package org.supposition.utils;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public final class SessionManager {
	
	public static WebContext getWebContext(){
		return WebContextFactory.get();
	}
	
	public static HttpSession getHttpSession(){
		return getWebContext().getSession();
	}
	
	public static boolean isExist(String inKey){
		return getHttpSession().getAttribute(inKey) != null;
	}
	
	/**
	 * @param inKey
	 * @return null or Object
	 */
	public static Object getFromSession(String inKey){
		return getHttpSession().getAttribute(inKey);
	}
	
	public static void setToSession(String inKey, Object inObject){
		getHttpSession().setAttribute(inKey, inObject);
	}
	
	public static Object getSessionValue(String inKey){
		return getFromSession(inKey);
	}
	
	public static int getSessionIntValue(String inKey){
		return Constants.getIntFromStr(getFromSession(inKey).toString());
	}
	
	public static String setSessionValue(String inKey, Object obj){
		setToSession(inKey, obj);
		return "OK";
	}

	public static void removeFromSession(String inKey) {
		getHttpSession().removeAttribute(inKey);
	}

	public static String getSessionLocale() {
		String locale = (String) getFromSession(Constants._session_locale_def);
		if(locale == null)
			return Constants._default_locale;
		return locale;
	}

	public static void setSessionLocale(String inLocale) {
		setToSession(Constants._session_locale_def, inLocale);		
	}	
}
