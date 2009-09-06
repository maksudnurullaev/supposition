package org.supposition.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public final class SessionManager {

	private static final boolean _isTomcatContext = Utils.isTomcatContext();
	private static Map<String, Object> _valueHouse = new HashMap<String, Object>();

	public static WebContext getWebContext() {
		return WebContextFactory.get();
	}

	public static HttpSession getHttpSession() {
		return getWebContext().getSession();
	}

	public static boolean isExist(String inKey) {
		if (_isTomcatContext)
			return getHttpSession().getAttribute(inKey) != null;
		return false;
	}

	/**
	 * @param inKey
	 * @return null or Object
	 */
	public static Object getFromSession(String inKey) {
		if (_isTomcatContext)
			return getHttpSession().getAttribute(inKey);
		return _valueHouse.get(inKey);
	}

	public static void setToSession(String inKey, Object inObject) {
		if (_isTomcatContext)
			getHttpSession().setAttribute(inKey, inObject);
		else
			_valueHouse.put(inKey, inObject);
	}

	public static Object getSessionValue(String inKey) {
		if (_isTomcatContext)
			return getFromSession(inKey);
		return _valueHouse.get(inKey);
	}

	public static int getSessionIntValue(String inKey) {
		if (_isTomcatContext)
			return Utils.getIntFromStr(getFromSession(inKey).toString());
		return Utils.getIntFromStr(_valueHouse.get(inKey).toString());
	}

	public static void removeFromSession(String inKey) {
		if (_isTomcatContext)
			getHttpSession().removeAttribute(inKey);
		else
			_valueHouse.remove(inKey);
	}

	public static String getSessionLocale() {
		if(_isTomcatContext){
			if(SessionManager.isExist(MessagesManager.getDefault("session.locale.def"))){
				return (String) SessionManager.getFromSession(MessagesManager.getDefault("session.locale.def"));
			}
		}
		return MessagesManager.getDefault("default.locale");
	}

	public static void setSessionLocale(String inLocale) {
		if (_isTomcatContext)
			setToSession(MessagesManager.getDefault("session.locale.def"), inLocale);
	}
}
