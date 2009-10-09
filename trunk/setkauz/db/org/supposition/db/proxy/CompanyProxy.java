package org.supposition.db.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Ads;
import org.supposition.db.Cgroup;
import org.supposition.db.Company;
import org.supposition.db.Group;
import org.supposition.db.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;
import org.supposition.utils.Utils;

public class CompanyProxy extends ADBProxyObject<Company> {
	
	private static final long serialVersionUID = 1L;
	
	public CompanyProxy(){
		super();
		
		setEClass(Company.class);
		setDataContext(DBUtils.getInstance().getDBContext());
	}

	public CompanyProxy(DataContext inDataContext) {
		super();
		setEClass(Company.class);
		setDataContext(inDataContext);
	}	 	
	
	public String addDBONew(CompanyBean inBean){
		_log.debug("addDBORole ->" + Thread.currentThread().getContextClassLoader().getClass().getName());
		
		// Check for valid text
		if(!Utils.isValidString(inBean.getName())){
			_log.debug("addDBONew -> Constants.isValidString -> inBean.getName -> " + inBean.getName());			
			return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.empty.value");
		}
			
		// Create new DBO
		Company company = null;
		try {
			company = createNew();
			company.setBean(inBean);
		} catch (Exception e) {
			deleteObject(company);
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// Validate
		ValidationResult validationResult = company.getValidationResult();
		
		if(validationResult.hasFailures()){
			// Delete Object before commit
			deleteObject(company);
			return MessagesManager.getDefault("web.error.result.prefix")
						+ DBUtils.getFailuresAsString(validationResult);
		}

		commitChanges();		
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
			+ MessagesManager.getText("message.data.saved");		
	}
	
	public String addDBONewGroup(GroupBean inBean){
		log(inBean);
		
		CompanyProxy companyProxy = new CompanyProxy(getDataContext());
		
		Company company = companyProxy.getDBObjectByUuid(inBean.getCuuid());
		
		if(company == null){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		if(!isManager(company)){
			return MessagesManager.getText("errors.user.has.not.access");
		}		
		
		GroupProxy groupProxy = new GroupProxy(getDataContext());
		
		Group group = null;
		try {
			group = groupProxy.createNew();
		} catch (Exception e) {
			getDataContext().deleteObject(group);
			e.printStackTrace();
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		group.setBean(inBean);
		
		company.addToGroups(group);
		company.getCgroup().addToGroups(group);
		
		// Validate
		ValidationResult validationResult = group.getValidationResult();

		if(validationResult.hasFailures()){
			// Delete Object before commit
			getDataContext().deleteObject(group);
			return MessagesManager.getDefault("web.error.result.prefix")
						+ DBUtils.getFailuresAsString(validationResult);
		}
		
		commitChanges();
				
		return getDetails(inBean.getCuuid());
	}	
	
	public String addDBOGroup(GroupBean inBean){
		log(inBean);
		
		CompanyProxy companyProxy = new CompanyProxy(getDataContext());
		
		Company company = companyProxy.getDBObjectByUuid(inBean.getCuuid());
		
		if(company == null){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		if(!isManager(company)){
			return MessagesManager.getText("errors.user.has.not.access");
		}		
		
		GroupProxy groupProxy = new GroupProxy(getDataContext());
		
		Group group = groupProxy.getDBObjectByUuid(inBean.getUuid());
		
		if(group == null){
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.null.object");
		}
				
		company.addToGroups(group);
		
		try {
			commitChanges();			
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") 
			+ e.getMessage();
		}
				
		return getDetails(inBean.getCuuid());
	}	
	
	public String getGroupItemsAsHTMLTable(GroupBean inBean){
		log(inBean);
		// 0. check incoming beans
		if(inBean == null 
				|| inBean.getCuuid() == null
				|| inBean.getUuid() == null){
			_log.warn("errors.null.object");
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.null.object");
		}
		
		boolean isGetAllItems = false;
		
		// 1. get group
		GroupProxy groupProxy = new GroupProxy(getDataContext());
		
		Group group = groupProxy.getDBObjectByUuid(inBean.getUuid());
		
		if(group == null){
			if(inBean.getUuid().equals(Utils.ROOT_ID_DEF)){
				isGetAllItems = true;
			}else{
				_log.warn("errors.null.object");
				return MessagesManager.getDefault("web.error.result.prefix") 
					+ MessagesManager.getText("errors.null.object");
			}
		} 
		
		if(!isGetAllItems){
			_log.debug("get ads from group");
			return "getAdsAsHTMLTable(group.getAds())";
		}
		
		
		// 2. get company								
		_log.debug("get ads from company");
		return "getAdsAsHTMLTable(getCompanyAds(inBean.getCuuid()))";
	}	
	
	private List<?> getCompanyAds(String inUuid){
		CompanyProxy companyProxy = new CompanyProxy(getDataContext());
		
		Company company = companyProxy.getDBObjectByUuid(inUuid);
		
		if(company == null){
			_log.warn("errors.null.object");
			return null;
		}		
		
		return company.getAds();
	}
	
	private String getAdsAsHTMLTable(List<?> ads) {
		if(ads.size() == 0)
			return MessagesManager.getText("errors.data.not.found");
		return "Found " + ads.size() + "items";
	}

	public String removeDBOGroup(GroupBean inBean){
		log(inBean);
		
		CompanyProxy companyProxy = new CompanyProxy(getDataContext());
		
		Company company = companyProxy.getDBObjectByUuid(inBean.getCuuid());
		
		if(company == null){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		if(!isManager(company)){
			return MessagesManager.getText("errors.user.has.not.access");
		}		
		
		GroupProxy groupProxy = new GroupProxy(getDataContext());
		
		Group group = groupProxy.getDBObjectByUuid(inBean.getUuid());
		
		if(group == null){
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.null.object");
		}
				
		_log.debug(String.format("XXXXXXXXXXX:Group(%s) removed from company(%s)", group.getUuid(), company.getUuid()));
		company.removeFromGroups(group);
		
		try {
			commitChanges();			
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") 
			+ e.getMessage();
		}
				
		return getDetails(inBean.getCuuid());
	}		

	private void log(GroupBean inBean) {
		_log.debug("inBean.getName():" + inBean.getName());
		_log.debug("inBean.getUuid():" + inBean.getUuid());
		_log.debug("inBean.getCuuid():" + inBean.getCuuid());
	}

	private String getAdditionalGroupLinks(Company company) {
		String result = String.format(Utils.INPUT_BUTTON_TEMPLATE,
				"CompanyProxy.groupShow()",
				MessagesManager.getText("text.show"));
		
		if(isManager(company)){
			result += String.format(Utils.INPUT_BUTTON_TEMPLATE,
					"CompanyProxy.groupShowAddForm()",
					MessagesManager.getText("text.add"));
			result += String.format(Utils.INPUT_BUTTON_TEMPLATE,
					"CompanyProxy.groupDelete()",
					MessagesManager.getText("text.remove"));
		}
		
		return result; 
	}
	
	private String getAdditionalLinks(Company inCompany) {
		String result = " [" + String.format(Utils.LINK_TEMPLATE,
				inCompany.getUuid(),
				"CompanyProxy.edit(this.id)",
				MessagesManager.getText("text.edit")) + "]";
		
		result += " [" + String.format(Utils.LINK_TEMPLATE,
				inCompany.getUuid(),
				"CompanyProxy.remove(this.id)",
				MessagesManager.getText("text.remove")) + "]";
		
		return result;
	}
	
	private void getCgroupUuidAsList(List<String> inList, Cgroup inCgroup) {
		inList.add(inCgroup.getUuid());
		
		List<?> childList = inCgroup.getChilds();
		if(childList != null){
			for(Object cgroup:childList){
				getCgroupUuidAsList(inList, (Cgroup) cgroup);
			}
		}
	} 
	
	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");
		
		String[] result = {"#","Name"};
		return Arrays.asList(result);
	}
	
	public String getDetails(String uuid){
		CompanyProxy companyProxy = new CompanyProxy();
		Company company = companyProxy.getDBObjectByUuid(uuid);
		
		if(company == null){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		String format = MessagesManager.getText("main.company.details");
		
		if(format == null){
			return MessagesManager.getText("errors.data.not.found");
		}		
		
		return String.format(format, 
				company.getName(),
				company.getAdditionals(),				
				Utils.getWwwBlankLink(company.getWww()),
				getGroupAsHTMLSelect(company),
				company.getUuid(), 
				company.getUuid());
	}	

	private String getDetailsLink(Company item) {
		return String.format(Utils.LINK_TEMPLATE,
				item.getUuid(),
				"CompanyProxy.showDetails(this.id)",
				item.getName());
	}
	
	public String getGroupAddForm(String inUuid){
		CompanyProxy companyProxy = new CompanyProxy();
		
		Company company = companyProxy.getDBObjectByUuid(inUuid);
		
		if(company == null){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		if(!isManager(company)){
			return MessagesManager.getText("errors.user.has.not.access");
		}
				
		return String.format(MessagesManager.getText("main.company.formGroupAdd"),
				getGroupNotJoinedAsHTMLSelect(company));
	}	
	
	private String getGroupAsHTMLSelect(Company company) {
		String result = "<select id=\"company.group\">";
		String resultOption = "<option value=\"%s\">%s</option>";
		
		// Add Default common item
		result += String.format(resultOption, 
				Utils.ROOT_ID_DEF,
				MessagesManager.getText("text.root.group"));
		
		String resultFooter = "</select> " 
			+ getAdditionalGroupLinks(company);
		
		if(company.getCgroup() == null) return result + resultFooter;
		if(company.getCgroup().getGroups() == null) return result + resultFooter;
		
		List<?> groupsList = company.getGroups();
		for(Object object:groupsList){
			Group group = (Group) object;
			result += String.format(resultOption,
					group.getUuid(),
					group.getName());
		}	
		
		return result 
			+ resultFooter;
	}

	private String getGroupNotJoinedAsHTMLSelect(Company company) {
		if(company.getCgroup() == null) 
			return MessagesManager.getText("errors.data.not.found");
		if(company.getCgroup().getGroups() == null ||
				company.getCgroup().getGroups().size() == 0)
			return MessagesManager.getText("errors.data.not.found");
		
		String result = "";		
		List<?> groupsAllList = company.getCgroup().getGroups();
		List<?> groupsList = company.getGroups();
		boolean found = false;
		
		for(Object object:groupsAllList){
			Group group = (Group) object;
			if(!isGroupExist(group, groupsList)){
				result += String.format("<option value=\"%s\">%s</option>",
						group.getUuid(),
						group.getName());
				found = true;
			}
		}	
		
		if(!found){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		return  "<select id=\"company.notjoined.groups\">" 
			+ result 
			+ "</select> ";
	}

	private boolean isGroupExist(Group group, List<?> groupsAllList) {
		for(Object tempObject:groupsAllList){
			Group tempGroup = (Group) tempObject;
			if(tempGroup.getUuid().equals(group.getUuid()))
				return true;
		}
		return false;
	}

	private String getHTMLTable(int inPage, List<Company> inList) {
		// Define start & end items
		int startItem = (inPage - 1) * getPageSize();
		int endItem = inPage * getPageSize();
		int allItemCount = inList.size();
		
		// Formate result
		String result = "";		
		String format = MessagesManager.getText("main.company.table.tr");
		
		for (int j = startItem; j < endItem; j++) {
			if (j >= inList.size()) break;
			Company company = inList.get(j);
			result = result
					+ String.format(format,
							(++startItem),
							getDetailsLink(company) +  (isManager(company)?getAdditionalLinks(company):""),
							company.getAdditionals() + getURLAsLink(company)
							);
		}
		
		return "<div id=\"main.company.div.header\">" 
			+ getHTMLPaginator(inPage, allItemCount)
			+ "</div><div id=\"main.company.div.body\">"
			+ MessagesManager.getText("main.company.table.header")
			+ result
			+ MessagesManager.getText("main.company.table.footer")
			+ "</div>";
	}

	public String getPageAsHTMLTable(CompanyFilterBean inFilter, int inPage) {
		_log.debug("-> getPageAsHTMLTable");
		_log.debug("inFilter.getCity(): " + inFilter.getCity());		
		_log.debug("inFilter.getGuuid(): " + inFilter.getGuuid());
		
		// Check page value 
		if (inPage <= 0 // if page negative
			||	inFilter.getCity().length() > 6) // if city has not format "[N|6][#]"
			return MessagesManager.getText("errors.unmatched.data.objects");		
		
		// Set cgroup filter
		if(!inFilter.getGuuid().equals(Utils.ROOT_ID_DEF)){
			CgroupProxy cgroupProxy = new CgroupProxy();
			Cgroup cgroup = cgroupProxy.getDBObjectByUuid(inFilter.getGuuid());
			
			if(cgroup != null){
				List<String> cgroupUuidList = new ArrayList<String>();				
				getCgroupUuidAsList(cgroupUuidList, cgroup);
				addExpression(ExpressionFactory.inDbExp("guuid", cgroupUuidList));
			}
		}	
		
		// Set city filter
		if(!inFilter.getCity().equals(Utils.ROOT_ID_DEF)){
			String cityFilter = inFilter.getCity();
			if(cityFilter.indexOf("#") != -1 &&
					cityFilter.indexOf("#") == (cityFilter.length() - 1)){
				cityFilter.replace("#", "%");
				addExpression(ExpressionFactory.likeIgnoreCaseExp("city", cityFilter));
			}else{
				addExpression(ExpressionFactory.matchDbExp("city", cityFilter));
			}
		}
		
		// Set "just own" filter
		if(inFilter.isOwner()){
			addExpression(ExpressionFactory.matchDbExp("uuuid", SessionManager.getUserUuid()));
		}
		
		// Set ordering by DEFAULT
		addOrdering(new Ordering("updated", false));
		
		List<Company> objectsList = getAll();
		
		if(objectsList == null || objectsList.size() == 0){
			return MessagesManager.getText("errors.data.not.found");	
		}
		
		return getHTMLTable(inPage, objectsList);
	}

	public String getUpdateForm(String inUuid){
		CompanyProxy companyProxy = new CompanyProxy();
		
		Company company = companyProxy.getDBObjectByUuid(inUuid);
		
		if(company == null){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		if(!isManager(company)){
			return MessagesManager.getText("errors.user.has.not.access");
		}
		
		String format = MessagesManager.getText("main.company.formUpdate");
		
		return String.format(format, 
				company.getName(),
				company.getAdditionals(),
				company.getWww(),
				company.getUuid());
	}

	private String getURLAsLink(Company inItem) {
		if(inItem.getWww().isEmpty() || 
				inItem.getWww().equals("http://"))
			return "";
		
		return ", " + Utils.getWwwBlankLink(inItem.getWww());
	}

	private boolean isManager(Company inCompany) {
		if(SessionManager.isMedorator()) return true;
		return inCompany.getUser().getUuid().equals(SessionManager.getUserUuid());
	}

	public String removeDBO(String uuid){
		CompanyProxy companyProxy = new CompanyProxy(getDataContext());
		
		Company company = companyProxy.getDBObjectByUuid(uuid);
		
		if(company == null){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		if(!isManager(company)){
			return MessagesManager.getText("errors.user.has.not.access");
		}
		
		deleteObject(company);
		commitChanges();
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
			+ MessagesManager.getText("message.data.saved");		
	}

	public String updateDBO(CompanyBean inBean){
		_log.debug("updateDBO ->" + Thread.currentThread().getContextClassLoader().getClass().getName());
		
		// Check for valid text
		if(!Utils.isValidString(inBean.getName())){
			_log.debug("addDBONew -> Constants.isValidString -> inBean.getName -> " + inBean.getName());			
			return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.empty.value");
		}
			

		CompanyProxy companyProxy = new CompanyProxy(getDataContext());
		
		Company company = companyProxy.getDBObjectByUuid(inBean.getUuid());
		
		if(company == null){
			return MessagesManager.getText("errors.data.not.found");
		}
		
		if(!isManager(company)){
			return MessagesManager.getText("errors.user.has.not.access");
		}
		
		try {
			company.setBean(inBean);
		} catch (Exception e) {
			_log.warn("Changes rollbacked");
			e.printStackTrace();
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// Validate
		ValidationResult validationResult = company.getValidationResult();

		if(validationResult.hasFailures()){
			// Delete Object before commit
			_log.warn("Changes rollbacked");
			rollbackChanges();
			return MessagesManager.getDefault("web.error.result.prefix")
						+ DBUtils.getFailuresAsString(validationResult);
		}

		_log.debug("Changes commited");
		commitChanges();		
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
			+ MessagesManager.getText("message.data.saved");			
	}
	

	
}
