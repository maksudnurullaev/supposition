package org.supposition.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public final class SessionManager {

	private static final boolean _isTomcatContext = Constants.isTomcatContext();
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
		return _valueHouse.containsKey(inKey);
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
			return Constants.getIntFromStr(getFromSession(inKey).toString());
		return Constants.getIntFromStr(_valueHouse.get(inKey).toString());
	}

	public static void removeFromSession(String inKey) {
		if (_isTomcatContext)
			getHttpSession().removeAttribute(inKey);
		else
			_valueHouse.remove(inKey);
	}

	public static String getSessionLocale() {
		String locale = (String) (_isTomcatContext?
				getFromSession(Constants._session_locale_def):
					_valueHouse.get(Constants._session_locale_def));
		if (locale == null)
			return Constants._default_locale;
		return locale;
	}

	public static void setSessionLocale(String inLocale) {
			setToSession(Constants._session_locale_def, inLocale);
	}
}
