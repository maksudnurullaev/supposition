package org.supposition.db.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Ads;
import org.supposition.db.Cgroup;
import org.supposition.db.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;
import org.supposition.utils.Utils;

public class AdsProxy extends ADBProxyObject<Ads> {
	
	private static final long serialVersionUID = 1L;
	
	public AdsProxy(){
		super();
		
		setEClass(Ads.class);
		setDataContext(DBUtils.getInstance().getDBContext());
	}

	public AdsProxy(DataContext inDataContext) {
		super();
		setEClass(Ads.class);
		setDataContext(inDataContext);
	}	 	
	
	public String removeDBO(String inUuid){
		if(!SessionManager.hasRole(SessionManager.MANAGER_ROLE_DEF)){
			return MessagesManager.getDefault("web.error.result.prefix")
				+ MessagesManager.getText("errors.user.has.not.access");
		}
		
		AdsProxy adsProxy = new AdsProxy(getDataContext());
		Ads ads = adsProxy.getDBObjectByUuid(inUuid);
		
		if(ads == null) return MessagesManager.getDefault("web.error.result.prefix")
		+ MessagesManager.getText("errors.data.not.found");
		
		deleteObject(ads);
		commitChanges();
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
		+ MessagesManager.getText("message.data.saved");
	}

	public String addDBONew(AdsBean inAdsBean){
		_log.debug("addDBONew ->");
		
		// Check for valid text
		if(!Utils.isValidString(inAdsBean.getText())){
			_log.debug("addDBORole -> Constants.isValidString -> roleBean.getName() -> " + inAdsBean.getText());			
			return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.empty.value");
		}
			
		// Create new DBO
		Ads ads = null;
		try {
			ads = createNew();
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// Setup user bean
		ads.setBean(inAdsBean);

		// Validate
		ValidationResult validationResult = ads.getValidationResult();

		if(validationResult.hasFailures()){
			// Delete Object before commit
			deleteObject(ads);
			return MessagesManager.getDefault("web.error.result.prefix")
						+ DBUtils.getFailuresAsString(validationResult);
		}

		commitChanges();		
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
			+ MessagesManager.getText("message.data.saved");		
	}

	@Override
	public List<String> getColumnNames() {
		return null;
	}	

	public String getPageAsHTMLTable(CompanyFilterBean inFilter, int inPage) {
		_log.debug("inFilter.getCity() = " + inFilter.getCity());		
		_log.debug("inFilter.getGuuid() = " + inFilter.getGuuid());
		
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
			if(cityFilter.indexOf("#") != -1){
				_log.debug("# symbol found!");
				cityFilter.replace("#", "%");
				addExpression(ExpressionFactory.likeIgnoreCaseExp("city", cityFilter.replace("#", "%")));
				_log.debug(String.format("Filter is (city LIKE %s)", cityFilter.replace("#", "%")));
			}else{
				_log.debug("# symbol NOT found!");
				addExpression(ExpressionFactory.matchDbExp("city", cityFilter));
				_log.debug(String.format("Filter is (city = %s)", cityFilter));
			}
		}
		
		// Set ordering by DEFAULT
		addOrdering(new Ordering("created", false));
		
		List<Ads> objectsList = getAll();
		
		if(objectsList == null || objectsList.size() == 0){
			return MessagesManager.getText("errors.data.not.found");	
		}
		
		return getHTMLTable(inPage, objectsList);
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

	private String getHTMLTable(int inPage, List<Ads> adsList) {
		// Define start & end items
		int startItem = (inPage - 1) * getPageSize();
		int endItem = inPage * getPageSize();
		int allItemCount = adsList.size();
		
		// Formate result
		String result = "";		
		String format = MessagesManager.getText("main.ads.table.tr");
		
		// Define medorator role
		boolean isMedorator = (SessionManager.hasRole(SessionManager.MANAGER_ROLE_DEF) 
				||	SessionManager.hasRole(SessionManager.ADMIN_ROLE_DEF));
		
		for (int j = startItem; j < endItem; j++) {
			if (j >= adsList.size()) break;
			Ads ads = adsList.get(j);
			result = result
					+ String.format(format, 
							(++startItem), 
							"<u>" + MessagesManager.getText(ads.getType()) + "</u> - " + ads.getText() + getAdditionalLinks(ads, isMedorator),
							Utils.GetFormatedDate("yyyy.MM.dd", ads.getDeleteAfter()),
							Utils.GetFormatedDate("yyyy.MM.dd HH:mm:ss.SSS", ads.getCreated()));
		}
		
		return getHTMLPaginator(inPage, allItemCount)
			+ MessagesManager.getText("main.ads.table.header")
			+ result
			+ MessagesManager.getText("main.ads.table.footer");
	}

	private String getAdditionalLinks(Ads ads, boolean isMedorator) {
		if(!isMedorator) return "";
		
		return "&nbsp;" + String.format(Utils.LINK_TEMPLATE,
				ads.getUuid(),
				"AdsProxy.remove(this.id)",
				MessagesManager.getText("text.remove"));
	}
}
