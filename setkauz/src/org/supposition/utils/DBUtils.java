package org.supposition.utils;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.DefaultConfiguration;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.proxy.UserBean;

public final class DBUtils {
	private static DBUtils _instance = null;
	private static Log _log = LogFactory
			.getLog("org.supposition.utils.DBUtils");

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

	public static String validatePassword(UserBean inUser) {
		if (!Utils.isValidString(inUser.getNewpassword())
				|| !Utils.isValidString(inUser.getNewpassword2())) {
			return MessagesManager.getText("message.data.NOT.saved") + ":\n"
					+ "\t - "
					+ MessagesManager.getText("errors.invalid.password") + "\n"
					+ "\t - "
					+ MessagesManager.getText("errors.passwords.is.empty");
		}

		// Check equals of NEW passwords
		if (!inUser.getNewpassword().equals(inUser.getNewpassword2())) {
			return MessagesManager.getText("message.data.NOT.saved") + ":\n"
					+ "\t - "
					+ MessagesManager.getText("errors.passwords.not.equals");
		}
		return null;
	}

	public static String getState(int inPersistenceState) {
		String result = "PersistenceState ";
		switch (inPersistenceState) {
		case PersistenceState.COMMITTED:
			result += "COMMITTED";
			break;
		case PersistenceState.DELETED:
			result += "DELETED";
			break;
		case PersistenceState.HOLLOW:
			result += "HOLLOW";
			break;
		case PersistenceState.MODIFIED:
			result += "MODIFIED";
			break;
		case PersistenceState.NEW:
			result += "NEW";
			break;
		case PersistenceState.TRANSIENT:
			result += "TRANSIENT";
			break;
		default:
			result += "...unkown...";
			_log.error(result);
			break;
		}
		return result;
	}
	
	public static String getFailuresAsString(ValidationResult validationResult){
		String result = MessagesManager.getText("message.data.NOT.saved") + ":\n";
		for(Object fail:validationResult.getFailures()){
			_log.debug("Fails: " + ((ValidationFailure)fail).getDescription());
			result += ((ValidationFailure)fail).getDescription() + "\n";
		}
		return result;
	}

	public static Integer getID(CayenneDataObject cdo) {
		// stumb
		return -1;
	}	
}
