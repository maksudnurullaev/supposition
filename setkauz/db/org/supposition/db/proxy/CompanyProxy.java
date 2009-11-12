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
	private static final String COMPANY_SHOW_DETAILS_JS = "CompanyProxy.showDetails('%s')";
	private static final String COMPANIES_SET_DENSITY_JS = "CompanyProxy.setPageDensity()";
	private static final String COMPANY_EDIT_JS = "CompanyProxy.edit('%s')";
	private static final String COMPANY_REMOVE_JS = "CompanyProxy.remove('%s')";
	
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
	
	public String addDBOAds(AdsBean inBean){
		logAdsBean(inBean);
		
		// 1. Get Company
		CompanyProxy companyProxy = new CompanyProxy(getDataContext());
		
		Company company = companyProxy.getDBObjectByUuid(inBean.getCuuid());
		
		if(company == null){
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.data.not.found");
		}
		
		if(!isManager(company)){
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.user.has.not.access");
		}			

		// 2. Group
		GroupProxy groupProxy = null;
		Group group = null;
		if(!inBean.getGuuid().equals(MessagesManager.getDefault("root.id.def"))){
			groupProxy = new GroupProxy(getDataContext());
			
			group = groupProxy.getDBObjectByUuid(inBean.getGuuid());
			
			if(group == null){
				return MessagesManager.getDefault("web.error.result.prefix") 
					+ MessagesManager.getText("errors.null.object");
			}		
		}
		
		// 3. Create new Ads
		// 3.1 Check for valid text
		if(!Utils.isValidString(inBean.getText())){
			_log.debug("addDBORole -> Constants.isValidString -> roleBean.getName() -> " + inBean.getText());			
			return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.empty.value");
		}
			
		// 3.2 Create new DBO
		Ads ads = null;
		AdsProxy adsProxy = new AdsProxy(getDataContext());
		try {
			ads = adsProxy.createNew();
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// 3.3 Setup user bean
		ads.setBean(inBean);
		
		// 3.3.1 Set Company
		ads.setCompany(company);
		ads.setCuuid(inBean.getCuuid());
		
		// 3.3.2 Set mandatory fields
		ads.setCity("company");
		ads.setType("company");

		// 3.3.3 Set Group
		if(group != null)
			ads.setGroup(group);
		else
			ads.setGuuid(inBean.getGuuid());
		
		// 3.4 Validate
		ValidationResult validationResult = ads.getValidationResult();

		if(validationResult.hasFailures()){
			// Delete Object before commit
			adsProxy.deleteObject(ads);
			return MessagesManager.getDefault("web.error.result.prefix")
						+ DBUtils.getFailuresAsString(validationResult);
		}
		
		commitChanges();		
		
		return getAdsAsHTMLTable(inBean.getCuuid(), inBean.getGuuid());			
	}
	
	public String addDBOGroup(GroupBean inBean){
		logGroupBean(inBean);
		
		CompanyProxy companyProxy = new CompanyProxy(getDataContext());
		
		Company company = companyProxy.getDBObjectByUuid(inBean.getCuuid());
		
		if(company == null){
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.data.not.found");
		}
		
		if(!isManager(company)){
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.user.has.not.access");
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
		logGroupBean(inBean);
		
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
	
	private String getAdditionalGroupLinks(Company company) {
		String result = String.format(MessagesManager.getDefault("template.button"),
				"CompanyProxy.groupShow()",
				MessagesManager.getText("text.show"));
		
		if(isManager(company)){
			result += String.format(MessagesManager.getDefault("template.button"),
					"CompanyProxy.groupShowAddForm()",
					MessagesManager.getText("text.add"));
			result += String.format(MessagesManager.getDefault("template.button"),
					"CompanyProxy.groupDelete()",
					MessagesManager.getText("text.remove"));
			// Ads records aprt
			result += " | ";
			result += String.format(MessagesManager.getDefault("template.button"),
					"CompanyProxy.showAddAdsForm()",
					MessagesManager.getText("text.add"));
		}
		
		return result; 
	}	
		
	public String removeAdsAndGetAdsAsHTMLTable(GroupBean inFilter){
		logGroupBean(inFilter);
		Company company = getDBObjectByUuid(inFilter.getCuuid());

		// check company
		if(company == null)
			return MessagesManager.getDefault("web.error.result.prefix") 
			+ MessagesManager.getText("errors.null.object");
		
		// check incoming beans
		if(!isCorrectFilter(inFilter)){
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.null.object");
		}
		
		// check right
		if(!isManager(company))
			return MessagesManager.getDefault("web.error.result.prefix")
			+ MessagesManager.getText("errors.user.has.not.access");
		
		AdsProxy adsProxy = new AdsProxy(getDataContext());
		Ads ads = adsProxy.getDBObjectByUuid(inFilter.getAuuid());
		
		if(ads == null) return MessagesManager.getDefault("web.error.result.prefix")
		+ MessagesManager.getText("errors.data.not.found");
		
		adsProxy.deleteObject(ads);
		commitChanges();
		
		return getAdsAsHTMLTable(inFilter);
		
	}
	
	public String getAdsAsHTMLTable(GroupBean inFilter){
		logGroupBean(inFilter);
		
		Company company = getDBObjectByUuid(inFilter.getCuuid());
		
		if(company == null)
			return MessagesManager.getDefault("web.error.result.prefix") 
			+ MessagesManager.getText("errors.null.object");
		
		// check incoming beans
		if(!isCorrectFilter(inFilter)){
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.null.object");
		}
		
		// Get all ads from root group
		if(isRootGroup(inFilter)){
			_log.debug("get non-grouped ads");
			return getAdsAsHTMLTableFromList(inFilter, getCompanyAdsAsList(inFilter.getCuuid()), isManager(company), false);
		}
		
		Group group = getGroup(inFilter);
		
		if(group == null){
			_log.warn("errors.null.object");
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.null.object");
		} 
		
		// Get all ads from grouped ads
		_log.debug("get grouped ads");
		
		AdsProxy adsProxy = new AdsProxy();
		adsProxy.addExpression(ExpressionFactory.matchDbExp("guuid", inFilter.getUuid()));
		adsProxy.addExpression(ExpressionFactory.matchDbExp("cuuid", inFilter.getCuuid()));
		
		return getAdsAsHTMLTableFromList(inFilter, adsProxy.getAll(), isManager(company), true);
	}
	
	private String getAdsAsHTMLTable(String cuuid, String guuid) {
		GroupBean groupBean = new GroupBean();
		groupBean.setCuuid(cuuid);
		groupBean.setUuid(guuid);
		return getAdsAsHTMLTable(groupBean);
	}

	private String getAdsAsHTMLTableFromList(GroupBean inFilter, List<?> adsList, boolean isMedorator, boolean isGrouped) {
		
		logGroupBean(inFilter);
		// check data
		if(adsList == null ||
				adsList.size() == 0){
			return MessagesManager.getText("errors.data.not.found");			
		}
		
		// Check margin of pages 
		if(inFilter.getPage() > DBUtils.getPageCount(adsList.size(), inFilter.getDensity()))
			inFilter.setPage(1);
		
		// Define start & end items
		int currentItemIndex = (inFilter.getPage() - 1) * inFilter.getDensity();
		int endItemIndex = inFilter.getPage() * inFilter.getDensity();
		
		
		// Formate result
		String result = "";
		
		String format = MessagesManager.getText("main.company.ads");
		
		_log.debug(String.format("Get ads(count = %s), where startItem(%s) and endItem(%s)", adsList.size(), currentItemIndex, endItemIndex));
				
		for (;;currentItemIndex++) {
			if (currentItemIndex == adsList.size()) break;
			if (currentItemIndex == endItemIndex) break;
			Ads ads = (Ads) adsList.get(currentItemIndex);
			result = result
					+ String.format(format, 
							(currentItemIndex+1), 
							ads.getText(),
							ads.getPrice(),
							(!isGrouped?getGroupLink(ads, isMedorator, isGrouped):""),
							(isMedorator?getManageLinks(ads):"")
							);
			// Final <hr />
			if	((currentItemIndex+1) != endItemIndex 
					&& (currentItemIndex+1) < adsList.size())
				result += "<hr />";
		}
		
		return DBUtils.getHTMLPaginator(inFilter.getPage(), adsList.size(), 
				"company.ads.page.current", 
				"company.ads.page.density", 
				"CompanyProxy.groupShow()",
				"CompanyProxy.AdsGo2PagePrevious()",
				"CompanyProxy.AdsGo2PageForward()",
				inFilter.getDensity())
			+ (isGrouped?
					String.format("<div class=\"selector\"><strong>%s</strong></div><hr />",((Ads)adsList.get(0)).getGroup().getName())
					:
					"")
			+ result;
	}		

	private String getGroupLink(Ads ads, boolean isMedorator, boolean isGrouped) {
		String result = "";
		if(ads.getGroup() != null){
			result += " | " + MessagesManager.getText("text.group") + ": " 
							+ String.format(MessagesManager.getDefault("template.link"),
					ads.getGroup().getUuid(),
					"CompanyProxy.selectGroup(this.id)",
					ads.getGroup().getName());
		}		
		return result;
	}	
	
	private String getManageLinks(Ads ads){
		return " | " + String.format(MessagesManager.getDefault("template.link"),
				ads.getUuid(),
				"CompanyProxy.removeAds(this.id)",
				MessagesManager.getText("text.remove"));
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
	
	private List<?> getCompanyAdsAsList(String inUuid){
		CompanyProxy companyProxy = new CompanyProxy(getDataContext());
		
		Company company = companyProxy.getDBObjectByUuid(inUuid);
		
		if(company == null){
			_log.warn("errors.null.object");
			return null;
		}		
		
		return company.getAds();
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
				company.getName()
					// WWW Link
					+ (Utils.isValidLink(company.getWww())? 
							" "	+ String.format(MessagesManager.getDefault("template.link._blank"), company.getWww())
							:
							""
					   )
					// "Back to list" button
					+ " " + String.format(MessagesManager.getDefault("template.button"),
							COMPANIES_SET_DENSITY_JS,
							MessagesManager.getText("text.go.to.list")
							),
				company.getAdditionals(),				
				getGroupAsHTMLSelect(company),
				company.getUuid(), 
				company.getUuid(), 
				getAllAdsOfCompany(company));
	} 
	
	private String getAllAdsOfCompany(Company company) {
		GroupBean filterBean = new GroupBean();
		filterBean.setUuid("root");
		filterBean.setCuuid(company.getUuid());
		filterBean.setPage(1);
		filterBean.setDensity(15);
		
		return getAdsAsHTMLTable(filterBean);
	}

	private String getAdditionalButtonsAndLinks(Company inCompany) {
		String result = "";
		
		// Www link
		if(Utils.isValidLink(inCompany.getWww()))
			result += " "  + String.format(MessagesManager.getDefault("template.link._blank"), inCompany.getWww());

		// Show button
		result += " " + String.format(MessagesManager.getDefault("template.button"),
				String.format(COMPANY_SHOW_DETAILS_JS, 
				inCompany.getUuid()),
				MessagesManager.getText("text.show"));		
		
		// Manager's links
		if(isManager(inCompany)){
			result += " " + String.format(MessagesManager.getDefault("template.button"),
					String.format(COMPANY_EDIT_JS,
					inCompany.getUuid()),
					MessagesManager.getText("text.edit"));
			
			result += " " + String.format(MessagesManager.getDefault("template.button"),
					String.format(COMPANY_REMOVE_JS,
					inCompany.getUuid()),
					MessagesManager.getText("text.remove"));			
		}
		
		return result;
	}
	
	private Group getGroup(GroupBean inFilter){
		GroupProxy groupProxy = new GroupProxy(getDataContext());
		return groupProxy.getDBObjectByUuid(inFilter.getUuid());
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
				MessagesManager.getDefault("root.id.def"),
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
	
	private String getHTMLTable(int inPage, List<Company> inList) {
		// Define start & end items
		int currentItemIndex = (inPage - 1) * getPageSize();
		int endItemIndex = inPage * getPageSize();
		int allItemCount = inList.size();
		
		// Formate result
		String result = "";		
		String format = MessagesManager.getText("company.title.div");
		
		for (;; currentItemIndex++) {
			if (currentItemIndex == inList.size()) break;
			if (currentItemIndex == endItemIndex) break;
			Company company = inList.get(currentItemIndex);
			result = result
					+ String.format(format,
							(currentItemIndex+1),
							company.getName(),
							getAdditionalButtonsAndLinks(company),
							company.getAdditionals());
			// Final <hr />
			if	((currentItemIndex+1) != endItemIndex 
					&& (currentItemIndex+1) < inList.size())
				result += "<hr />";			
		}
		
		return String.format(MessagesManager.getText("companies.paginator.with.list.div"),
				getHTMLPaginator(inPage, allItemCount),
				result);
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
		if(!inFilter.getGuuid().equals(MessagesManager.getDefault("root.id.def"))){
			CgroupProxy cgroupProxy = new CgroupProxy();
			Cgroup cgroup = cgroupProxy.getDBObjectByUuid(inFilter.getGuuid());
			
			if(cgroup != null){
				List<String> cgroupUuidList = new ArrayList<String>();				
				getCgroupUuidAsList(cgroupUuidList, cgroup);
				addExpression(ExpressionFactory.inDbExp("guuid", cgroupUuidList));
			}
		}	
		
		// Set city filter
		if(!inFilter.getCity().equals(MessagesManager.getDefault("root.id.def"))){
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

	private boolean isCorrectFilter(GroupBean inFilter){
		if(inFilter == null 
				|| inFilter.getCuuid() == null
				|| inFilter.getUuid() == null){
			_log.warn("errors.null.object");
			return false;
		}
		return true;
	}

	private boolean isGroupExist(Group group, List<?> groupsAllList) {
		for(Object tempObject:groupsAllList){
			Group tempGroup = (Group) tempObject;
			if(tempGroup.getUuid().equals(group.getUuid()))
				return true;
		}
		return false;
	}

	private boolean isManager(Company inCompany) {
		if(SessionManager.isMedorator()) return true;
		return inCompany.getUser().getUuid().equals(SessionManager.getUserUuid());
	}

	private boolean isRootGroup(GroupBean inFilter){
		return inFilter.getUuid().equals(MessagesManager.getDefault("root.id.def"));
	}

	private void logAdsBean(AdsBean inBean) {
		_log.debug("inBean.getText():" + inBean.getText());
		_log.debug("inBean.getGuuid():" + inBean.getGuuid());
		_log.debug("inBean.getCuuid():" + inBean.getCuuid());
	}

	private void logGroupBean(GroupBean inBean) {
		_log.debug("inBean.getName():" + inBean.getName());
		_log.debug("inBean.getUuid():" + inBean.getUuid());
		_log.debug("inBean.getCuuid():" + inBean.getCuuid());
		_log.debug("inBean.getPage():" + inBean.getPage());
		_log.debug("inBean.getDensity():" + inBean.getDensity());
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

	public String removeDBOGroup(GroupBean inBean){
		logGroupBean(inBean);
		
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
							
		if(group.getAds() != null && 
				group.getAds().size() != 0)
			return MessagesManager.getDefault("web.error.result.prefix")
				+ MessagesManager.getText("errors.nonempty.list");				
		
		_log.debug(String.format("Group(%s) removed from company(%s)", 
				group.getUuid(), 
				company.getUuid()));
		
		company.removeFromGroups(group);
		
		try {
			commitChanges();			
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") 
			+ e.getMessage();
		}
				
		return getDetails(inBean.getCuuid());
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
