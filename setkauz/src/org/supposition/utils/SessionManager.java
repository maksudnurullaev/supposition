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
}
