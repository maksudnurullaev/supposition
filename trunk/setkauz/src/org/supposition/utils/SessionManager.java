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
	
	public static Object getFromSession(String inKey){
		Object result = getHttpSession().getAttribute(inKey);
		if(result != null)
			return getHttpSession().getAttribute(inKey);
		return DBDefaults(inKey);
	}
	
	private static Object DBDefaults(String inKey) {
		// TODO Need to inpliment with database
		return "Need to inpliment with database";
	}

	public static void setToSession(String inKey, Object inObject){
		getHttpSession().setAttribute(inKey, inObject);
	}
	
	public static Object getSessionValue(String inKey){
		return getFromSession(inKey);
	}
	
	public static String setSessionValue(String inKey, Object obj){
		setToSession(inKey, obj);
		return "OK";
	}	
}
