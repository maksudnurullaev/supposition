package org.supposition.db.proxy;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.User;
import org.supposition.db.interfaces.IDBOClass;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;

public class LocalUserProxy {

	private static final long serialVersionUID = 1L;
	private static final UserProxy _userProxy = new UserProxy();
	public Log _log = LogFactory.getLog(this.getClass());

	public String addDBOUser(UserBean userBean) {
		return _userProxy.addDBOUser(userBean);
	}

	public String updateDBOUserPassword(UserBean userBean){
		return _userProxy.updateDBOUserPassword(userBean);
	}
	
	public String updateDBOUser(UserBean userBean) {
		// Check for errors - user logged or not
		if (!SessionManager.isUserLoggedIn())
			return MessagesManager.getText("errors.user.not.loggedin");

		// Check for errors - same user or not
		if (!userBean.getUuid().equals(SessionManager.getUserUuid())) {
			_log.warn(String.format(
					"userBean.getUuid(%s) != SessionManager.getUserUuid(%s)",
					userBean.getUuid(), SessionManager.getUserUuid()));
			return MessagesManager.getText("errors.unmatched.data.objects");
		}

		// Check for errors - user valid or not
		IDBOClass user = (new UserProxy()).getDBObjectByUuid(userBean.getUuid());
		if (user == null)
			return MessagesManager.getText("errors.user.not.found");

		// Return actual result
		return _userProxy.updateDBOUser(userBean);
	}

	public String enterDBOUser(UserBean userBean) {
		return _userProxy.enterDBOUser(userBean);
	}

	public Map<String, String> getUserCabinet(String inTypeOfInformation) {
		
		Map<String, String> result = MessagesManager.getText2(inTypeOfInformation);

		// Check for errors
		if (!SessionManager.isUserLoggedIn()){
			result.put("text",MessagesManager.getText("errors.user.not.loggedin"));
			return result;
		}

		// Test user session
		User user = (new UserProxy()).getDBObjectByUuid(SessionManager.getUserUuid());
		if (user == null){
			result.put("text", MessagesManager.getText("errors.user.not.found"));
			return result;
		}
		
		// Test incoming type of info code
		if (inTypeOfInformation == null){
			result.put("text", MessagesManager.getText("errors.unmatched.data.objects"));
			return result;
		}
				
		_log.debug("getUserCabinet -> inTypeOfInformation -> " + inTypeOfInformation);
		
		
		if(inTypeOfInformation.equals("personal.data")){
			String format = result.get("text");
			
			if(format == null || format.isEmpty()){
				result.put("text", MessagesManager.getText("errors.empty.value"));
				return result;
			}
			
			result.put("text", 
					String.format(format, 
							user.getMail(), 
							user.getAdditionals(),
							user.getUuid()));
			
			return result;
		}
		
		return result;
	}

}
