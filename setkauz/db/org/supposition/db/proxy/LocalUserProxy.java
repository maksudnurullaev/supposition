package org.supposition.db.proxy;

public class LocalUserProxy {
	
	private static final long serialVersionUID = 1L;
	private static final UserProxy _userProxy = new UserProxy();
	
	public String addDBOUser(UserBean userBean){
		return _userProxy.addDBOUser(userBean);
	}

	public String enterDBOUser(UserBean userBean){
		return _userProxy.enterDBOUser(userBean);		
	}
}
