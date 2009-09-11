package org.supposition.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.supposition.db.User;
import org.supposition.db.proxy.UserProxy;

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

	public static int getUserId() {
		return getSessionIntValue(MessagesManager.getDefault("session.userid.key"));
	}
	
	public static boolean isUserLoggedIn(){
		return 	isExist(MessagesManager.getDefault("session.userid.key"));
	}


	public static void logoffUser(){
		removeFromSession(MessagesManager.getDefault("session.userid.key"));
	}
	
	public static void loginUser(User user){
			setToSession(MessagesManager.getDefault("session.userid.key"), DBUtils.getID(user));				
	}

	public static String getUserMail() {
		UserProxy users = new UserProxy();
		User user = users.getDBObjectByIntPk(getUserId());
		if(user == null){
			return "NULL";
		}

		return user.getMail();
	}

	public static String getSystemDefaultsAsHTMLMgmTable() {
		Properties defaults = MessagesManager.getDefaults();
		
		// Table + Header
		String result = MessagesManager.getText("main.admin.system.defaults.table.header");
		result +=  MessagesManager.getText("main.admin.system.defaults.table.th");
		
		TreeSet<Object> sortedkeys = new TreeSet<Object>(defaults.keySet());
		
		// Table rows
		String tr_format = MessagesManager.getText("main.admin.system.defaults.table.tr");
		for(Object key:sortedkeys){
			result += String.format(tr_format, 
					key, 
					key, 
					defaults.get(key),
					key);
		}
		
		// Table footer
		result += MessagesManager.getText("main.admin.system.defaults.table.footer");		
		return result;
	}
}
