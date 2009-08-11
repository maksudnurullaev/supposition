package org.supposition.utils;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.DefaultConfiguration;
import org.supposition.db.proxy.UserBean;

public final class DBUtils {
	private static DBUtils _instance = null;

	private DBUtils() {
		DefaultConfiguration conf = new DefaultConfiguration();
		conf.addClassPath("conf");
		Configuration.initializeSharedConfiguration(conf);
	}

	public static synchronized DBUtils getInstance() {
		if (_instance == null)
			_instance = new DBUtils();
		return _instance;
	}

	public DataContext getDBContext() {
		return DataContext.createDataContext();
	}

	public static String validatePassword(UserBean inUser){
		if(!isValidString(inUser.getNewpassword()) ||
				!isValidString(inUser.getNewpassword2())){
			return MessagesManager.getText("message.data.NOT.saved") + ":\n" 
					+ "\t - " + MessagesManager.getText("errors.invalid.password") + "\n"
					+ "\t - " + MessagesManager.getText("errors.passwords.is.empty");
		}

		// Check equals of NEW passwords
		if(!inUser.getNewpassword().equals(inUser.getNewpassword2())){
			return MessagesManager.getText("message.data.NOT.saved") + ":\n" 
					+ "\t - " +  MessagesManager.getText("errors.passwords.not.equals");
		}
		return null;
	}	
	
	private static boolean isValidString(String inString) {
		if(inString == null){
			return false;
		}
		if(inString.isEmpty()){
			return false;
		}
		return true;
	}		
}
