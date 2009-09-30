package org.supposition.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.DefaultConfiguration;
import org.apache.cayenne.validation.SimpleValidationFailure;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.Cgroup;
import org.supposition.db.proxy.CgroupProxy;
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
		String errorString= "";
		for(Object fail:validationResult.getFailures()){
			errorString = ((ValidationFailure)fail).getDescription();
			_log.warn("Fails: " + errorString);
			if(MessagesManager.hasMessageByKey(errorString))
				result += "\t - " + MessagesManager.getText(errorString) + "\n";
			else
				result += "\t - " + errorString + "\n";
		}
		return result;
	}

	public static String getUuid() {
		return UUID.randomUUID().toString();
	}

	public static String getGroupsAsHTMLSelect(String inPrefix) {
		String result = String.format("<select id='%s.guuid'>", inPrefix);
		String format = "<option value='%s'>%s</option>";
		result += String.format(format, "root", MessagesManager.getText("text.root.cgroup"));

		CgroupProxy cgroups = new CgroupProxy();		
		List<Cgroup> cgroup_list = cgroups.getRootElements();
		
		if(cgroup_list == null ||
				cgroup_list.size() == 0){
			_log.debug("text.no.data");
		} result += getOptionsFromCgroupList(cgroup_list, 0);
		return result + "</select>";
	}
	
	@SuppressWarnings("unchecked")
	private static String getOptionsFromCgroupList(List<Cgroup> cgroup_list, int deep) {
		String result = "";
		String format = "<option value='%s'>%s</option>";
		for(Cgroup cgroup:cgroup_list){
			result += String.format(format, cgroup.getUuid(), repitter(deep) + cgroup.getName());
			List<Cgroup> child_list = cgroup.getChilds(); 
			if(child_list != null && child_list.size() != 0){
				result += getOptionsFromCgroupList(child_list, deep + 1);
			}
		}
		
		return result;
	}

	private static String repitter(int deep) {
		if(deep <= 0) return "";
		
		String result = "";
		String repit_string = "&rarr;";
		while(deep != 0){
			result += repit_string;
			deep = deep - 1;
		}
		return result;
	}

	public static String getGroupsAsHTML(boolean forAdmin) {
		CgroupProxy cgroups = new CgroupProxy();
		List<Cgroup> cgroup_list = cgroups.getRootElements();
		
		if(cgroup_list == null ||
				cgroup_list.size() == 0){
			return MessagesManager.getText("text.no.data");
		}
		
		return getULsFromCgrouplist(cgroup_list, forAdmin);
	}

	@SuppressWarnings("unchecked")
	private static String getULsFromCgrouplist(List<Cgroup> cgroup_list, boolean forAdmin) {
		String result = "<ul>";
		String format = "<li>%s</li>";
		for(Cgroup cgroup:cgroup_list){
			result += String.format(format, cgroup.getName() 
						+ (forAdmin?"&nbsp;" + makeDeleteCgroupLink(cgroup):""));
			
			List<Cgroup> child_list = cgroup.getChilds(); 
			if(child_list != null && child_list.size() != 0){
				result += getULsFromCgrouplist(child_list, forAdmin);
			}			
		}
		return result + "</ul>";
	}	
	
	private static String makeDeleteCgroupLink(Cgroup inCgroup){
		return String.format(MessagesManager.getDefault("link.with.onlick.template"), 
				inCgroup.getUuid(),
				"CgroupProxy.remove(this.id)",
				MessagesManager.getText("text.remove"));
	}

	public static String removeHTMLTags(String inString){
		if(inString == null) return null;
		return inString.replaceAll("\\<.*?>","");
	}

	public static Date dateAfterInWeeks(int weeks){
		Calendar now = Calendar.getInstance();
		now.add(Calendar.WEEK_OF_YEAR, weeks);
		return now.getTime();		
	}
	
	public static void checkKaptcha(String kaptcha, ValidationResult validationResult, CayenneDataObject inDataObject) {
		String sessionKaptchaValue = (String) SessionManager
				.getFromSession(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (kaptcha == null
				|| !kaptcha.equalsIgnoreCase(sessionKaptchaValue)) {
			validationResult.addFailure(new SimpleValidationFailure(inDataObject,
					"errors.invalid.kaptcha"));
			_log.error(String.format("Invalid kaptcha(%s) should be %s", kaptcha, sessionKaptchaValue));
		}
	}		
}
