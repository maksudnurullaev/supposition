package org.supposition.db.proxy;

public class LocalUserProxy {
	
	private static final long serialVersionUID = 1L;
	private static final UserProxy _userProxy = new UserProxy();
	
	public String addDBOUser(UserBean userBean){
		return _userProxy.addDBOUser(userBean);
	}

	public String enterDBOUser(UserBean userBean){
		System.out.println("userBean.getMail() = " + userBean.getMail());
		System.out.println("userBean.getPassword() = " + userBean.getPassword());
		System.out.println("userBean.getNewpassword() = " + userBean.getNewpassword());
		System.out.println("userBean.getNewpassword2() = " + userBean.getNewpassword2());

		return _userProxy.enterDBOUser(userBean);		
	}
}
