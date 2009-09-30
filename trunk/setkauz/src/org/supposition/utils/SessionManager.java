package org.supposition.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.supposition.db.User;
import org.supposition.db.proxy.UserProxy;

public final class SessionManager {

	private static final boolean _isTomcatContext = Utils.isTomcatContext();
	private static Map<String, Object> _valueHouse = new HashMap<String, Object>();
	
	public static String ADMIN_ROLE_DEF = "admin";
	public static String MODERATOR_ROLE_DEF = "moderator";
	
	public static final String DEFAULT_NONREGISTERED_POSTFIX = "nonregistered";
	private static final String DEFAULT_REGISTERED_POSTFIX = "registered";	
	private static Log _log = LogFactory.getLog("org.supposition.utils.SessionManager");	
	
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

	public static String getUserUuid() {
		return (String) getFromSession(MessagesManager.getDefault("session.userid.key"));
	}
	
	public static boolean isUserLoggedIn(){
		return 	isExist(MessagesManager.getDefault("session.userid.key"));
	}


	public static void logoffUser(){
		getHttpSession().invalidate();
	}
	
	public static void loginUser(User user){
			setToSession(MessagesManager.getDefault("session.userid.key"), user.getUuid());				
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
	
	public static List<String> getUserRoles() {
		List<String> resultList = new ArrayList<String>();
		UserProxy users = new UserProxy();
		User user = users.getDBObjectByUuid(getUserUuid());
		if(user == null){
			resultList.add(DEFAULT_NONREGISTERED_POSTFIX);
			_log.debug("Current user has role:" + DEFAULT_NONREGISTERED_POSTFIX);
		}else{
			resultList.add(DEFAULT_REGISTERED_POSTFIX);
			_log.debug("Current user has role:" + DEFAULT_REGISTERED_POSTFIX);
			for(String role:user.getRolesAsList()){
				resultList.add(role);
				_log.debug("Current user has role:" + role);
			}			
		}
		return resultList;
	}	

	public static boolean hasRole(String inRole){
		//Return false if user not registered
		if(!isUserLoggedIn()) return false;
		for(String role:getUserRoles()){
			if(role.equals(inRole)) return true;
			// Admin should always access to everything
			if(role.equals(ADMIN_ROLE_DEF)) return true;
		}
		// Return false if nothing found
		return false;
	}
}
