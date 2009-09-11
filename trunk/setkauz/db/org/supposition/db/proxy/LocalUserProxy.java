package org.supposition.db.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.User;
import org.supposition.utils.DBUtils;
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
		if (userBean.getId() != SessionManager.getUserId()) {
			_log.warn(String.format(
					"userBean.getId(%s) != SessionManager.getUserId(%s)",
					userBean.getId(), SessionManager.getUserId()));
			return MessagesManager.getText("errors.too.many.objects");
		}

		// Check for errors - user valid or not
		User user = (new UserProxy()).getDBObjectByIntPk(userBean.getId());
		if (user == null)
			return MessagesManager.getText("errors.user.not.found");

		// Return actual result
		return _userProxy.updateDBOUserByCaptcha(userBean, false);
	}

	public String enterDBOUser(UserBean userBean) {
		return _userProxy.enterDBOUser(userBean);
	}

	public String getUserCabinet() {
		// Check for errors
		if (!SessionManager.isUserLoggedIn())
			return MessagesManager.getText("errors.user.not.loggedin");

		User user = (new UserProxy()).getDBObjectByIntPk(SessionManager
				.getUserId());

		if (user == null)
			return MessagesManager.getText("errors.user.not.found");

		// Return actual result
		String format = MessagesManager.getText("main.user.cabinetForm");

		return String.format(format, user.getMail(), user.getAdditionals(),
				DBUtils.getID(user));
	}

}
