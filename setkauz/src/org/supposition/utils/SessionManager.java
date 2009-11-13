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

	private static final boolean _isWebContext = Utils.isWebContext();
	private static Map<String, Object> _valueHouse = new HashMap<String, Object>();
	
	public static final String ADMIN_ROLE_DEF   = "admin";
	public static final String MANAGER_ROLE_DEF = "manager";
	
	public static final String DEFAULT_NONREGISTERED_POSTFIX = "nonregistered";
	private static final String DEFAULT_REGISTERED_POSTFIX = "registered";	
	private static Log _log = LogFactory.getLog("org.supposition.utils.SessionManager");	
	
	public static Object getFromSession(String inKey) {
		if (_isWebContext)
			return getHttpSession().getAttribute(inKey);
		return _valueHouse.get(inKey);
	}

	public static HttpSession getHttpSession() {
		return getWebContext().getSession();
	}

	public static int getSessionIntValue(String inKey) {
		if (_isWebContext)
			return Utils.getIntFromStr(getFromSession(inKey).toString());
		return Utils.getIntFromStr(_valueHouse.get(inKey).toString());
	}

	public static String getSessionLocale() {
		if(_isWebContext){
			_log.debug("It's web session");
			if(SessionManager.isExist(MessagesManager.getDefault("session.locale.def"))){
				return (String) SessionManager.getFromSession(MessagesManager.getDefault("session.locale.def"));
			}
		}
		return MessagesManager.getDefault("default.locale");
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
		if(!SessionManager.isUserLoggedIn()){
			resultList.add(DEFAULT_NONREGISTERED_POSTFIX);
			_log.debug("Current user has role:" + DEFAULT_NONREGISTERED_POSTFIX);
		}else{
			resultList.add(DEFAULT_REGISTERED_POSTFIX);
			_log.debug("Current user has role:" + DEFAULT_REGISTERED_POSTFIX);
			User user = users.getDBObjectByUuid(getUserUuid());
			for(String role:user.getRolesAsList()){
				resultList.add(role);
				_log.debug("Current user has role:" + role);
			}			
		}
		return resultList;
	}

	public static String getUserUuid() {
		return (String) getFromSession(MessagesManager.getDefault("session.userid.key"));
	}

	public static WebContext getWebContext() {
		return WebContextFactory.get();
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

	public static boolean isExist(String inKey) {
		if (_isWebContext)
			return getHttpSession().getAttribute(inKey) != null;		
		return false;
	}
	
	public static boolean isMedorator(){
		return (SessionManager.hasRole(SessionManager.MANAGER_ROLE_DEF) 
				|| SessionManager.hasRole(SessionManager.ADMIN_ROLE_DEF));
	}

	public static boolean isUserLoggedIn(){
		return 	isExist(MessagesManager.getDefault("session.userid.key"));
	}
	
	public static void loginUser(User user){
			setToSession(MessagesManager.getDefault("session.userid.key"), user.getUuid());				
	}

	public static void logoffUser(){
		getHttpSession().invalidate();
	}
	
	public static void removeFromSession(String inKey) {
		if (_isWebContext)
			getHttpSession().removeAttribute(inKey);
		else
			_valueHouse.remove(inKey);
	}	

	public static void setSessionLocale(String inLocale) {
		if (_isWebContext)
			setToSession(MessagesManager.getDefault("session.locale.def"), inLocale);
	}
	
	public static void setToSession(String inKey, Object inObject) {
		if (_isWebContext)
			getHttpSession().setAttribute(inKey, inObject);
		else
			_valueHouse.put(inKey, inObject);
	}
}
