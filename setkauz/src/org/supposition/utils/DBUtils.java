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
		CgroupProxy cgroups = new CgroupProxy();		
		Cgroup cgroup_root = cgroups.getRootElement();
		
		if(cgroup_root == null){
			_log.debug("errors.data.not.found");
			return MessagesManager.getText("errors.data.not.found");
		} 
		
		return String.format("<select id='%s.guuid'>%s</select>", 
				inPrefix,
				getOptionsFromCgroupList(cgroup_root, 0));
	}
	
	@SuppressWarnings("unchecked")
	private static String getOptionsFromCgroupList(Cgroup cgroup_root, int deep) {
		String result = "";
		String format = "<option value='%s'>%s</option>";
		
		result += String.format(format, cgroup_root.getUuid(), repitter(deep) + cgroup_root.getName());
		List<Cgroup> child_list = cgroup_root.getChilds(); 
		if(child_list != null && child_list.size() != 0){
			for(Cgroup cgroup:child_list){
				result += getOptionsFromCgroupList(cgroup, deep + 1);
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
		Cgroup cgroup_root = cgroups.getRootElement();
		
		if(cgroup_root == null){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		return getULsFromCgrouplist(cgroup_root, forAdmin);
	}

	@SuppressWarnings("unchecked")
	private static String getULsFromCgrouplist(Cgroup cgroup_root, boolean forAdmin) {
		String result = "<ul>";
		String format = "<li>%s</li>";
		
		result += String.format(format, cgroup_root.getName() 
					+ (forAdmin?"&nbsp;" + makeDeleteCgroupLink(cgroup_root):""));
			
		List<Cgroup> child_list = cgroup_root.getChilds();
		
		if(child_list != null && child_list.size() != 0){
			for(Cgroup cgroup:child_list){
				result += getULsFromCgrouplist(cgroup, forAdmin);
			}
		}			
		
		return result + "</ul>";
	}	
	
	private static String makeDeleteCgroupLink(Cgroup inCgroup){
		return String.format(Utils.LINK_TEMPLATE_DEFAULT, 
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
	
	// ### paginator uils
	public static int getPageCount(int inItemCount, int inPageSize) {		
		if(inPageSize >= inItemCount) return 1;
		
		int lastItems = inItemCount % inPageSize;
		int result = 0;
		
		if(lastItems == 0)
			result = inItemCount / inPageSize;
		else 
			result = (inItemCount - lastItems) / inPageSize +1;
		
		return result;		
	}
	
	public static String getHTMLPaginator(
			int inPage, 
			int itemsCount, 
			String inInputCurrentPageID,   // Company.Ads.CurrentPage
			String inInputPageDesityID,    // Company.Ads.PageDensity
			String inJSGo2PageDef,         // CompanyProxy.Ads.go2Page()
			String inJSGo2PagePreviousDef, // CompanyProxy.Ads.go2PagePrevious()
			String inJSGo2PageForwardDef,  // CompanyProxy.Ads.go2PageForward()
			int inPageSize) {
		
		int pageCount = DBUtils.getPageCount(itemsCount, inPageSize);

		_log.debug("int inPage = " + inPage); 
		_log.debug("int itemsCount = " + itemsCount); 
		_log.debug("int inPageSize = " + inPageSize);	
		_log.debug("int pageCount = " + pageCount);	
		
		if(pageCount < inPage)
			inPage = pageCount;
		
		if(inPage == 0)
			inPage = 1;
		
		// <table class="rowed grey" border="0"><thead><tr><th>Страница 
		String result = MessagesManager.getText("template.simple.paginator.header");
		if(pageCount == 1){
            // <input id="%s" type="text" size="3" value="%s" disabled="disabled" /> 
            // 1. Current Page
			result += String.format(MessagesManager.getText("template.simple.paginator.page_current.disabled"), 
					inInputCurrentPageID, 1);
		}else{
			if(inPage != 1) 
				result += 
                    // <input type="button" value=" < " onclick="%s" />
					String.format(MessagesManager.getText("template.simple.paginator.btn_back"), inJSGo2PagePreviousDef);
                    // <input id="%s" type="text" size="3" value="%s">
			result += String.format(MessagesManager.getText("template.simple.paginator.page_current"), inInputCurrentPageID, inPage);

			if(inPage != pageCount){
                    // <input type="button" value=" > " onclick="%s" />
				result += String.format(MessagesManager.getText("template.simple.paginator.btn_forward"),inJSGo2PageForwardDef);
			}
		}
		// из <strong>%s</strong>
		result += String.format(MessagesManager.getText("template.simple.paginator.total"), pageCount);
		// <th>Плотность<input id="%s" type="text" size="3" value="%s" /><input type="button" value="Показать" onclick="%s" /></th>
		result += String.format(MessagesManager.getText("template.simple.paginator.density"),
				inInputPageDesityID,
				inPageSize,
				inJSGo2PageDef);
		
		result += MessagesManager.getText("template.simple.paginator.footer");
		
		return result;
	}	
}
