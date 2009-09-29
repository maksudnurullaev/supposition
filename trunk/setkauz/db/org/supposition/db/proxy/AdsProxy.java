package org.supposition.db.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Ads;
import org.supposition.db.Cgroup;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
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
		if(!SessionManager.hasRole(SessionManager.MODERATOR_ROLE_DEF)){
			return MessagesManager.getDefault("web.error.result.prefix")
				+ MessagesManager.getText("errors.user.has.not.access");
		}
		
		AdsProxy adsProxy = new AdsProxy(getDataContext());
		Ads ads = adsProxy.getDBObjectByUuid(inUuid);
		
		if(ads == null) return MessagesManager.getDefault("web.error.result.prefix")
		+ MessagesManager.getText("errors.object.not.found");
		
		deleteObject(ads);
		commitChanges();
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
		+ MessagesManager.getText("message.data.saved");
	}
	public String addDBONew(AdsBean inAdsBean){
		_log.debug("addDBORole ->");
		
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
		ads.setAds(inAdsBean);

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

	public String getPageAsHTMLTable(String guuid, int inPage) {
		_log.debug("-> getPageAsHTMLTable");
		
		// Check page value 
		if (inPage <= 0)
			return MessagesManager.getText("errors.too.many.objects");
				
		// For all ads
		if(guuid.equals("root")) return getFromAllAds(inPage);	
		
		CgroupProxy cgroupProxy = new CgroupProxy();
		Cgroup cgroup = cgroupProxy.getDBObjectByUuid(guuid);
		
		if(cgroup == null){
			return MessagesManager.getText("error.data.not.found");
		}
		
		List<Ads> adsList = new ArrayList<Ads>();
		getAdsAsList(adsList, cgroup);
		
		if(adsList.size() == 0){
			return MessagesManager.getText("error.data.not.found");	
		}
		
		return getAdsAsHTMLTable(inPage, adsList);
	}

	private List<Ads> getAdsAsList(List<Ads> adsList, Cgroup cgroup) {
		List<?>objects  = cgroup.getAds();
		
		for(final Object ads:objects){
			adsList.add((Ads) ads);
		}
		
		if(cgroup.getChilds() != null){
			List<?> child_cgroups = cgroup.getChilds();
			for (Object child_cgroup:child_cgroups) {
				getAdsAsList(adsList, (Cgroup) child_cgroup);
			}
		}
		
		return adsList;
	}

	private String getFromAllAds(int inPage) {
		// Get all
		List<Ads> adsList = getAll();
				
		return getAdsAsHTMLTable(inPage, adsList);
	}

	private String getAdsAsHTMLTable(int inPage, List<Ads> adsList) {
		// Check for result 
		if (adsList == null || adsList.size() == 0)
			return MessagesManager.getText("errors.object.not.found");		
		
		// Created "sorted by created property list" of ads
		List<Ads> sortedAdsList = new ArrayList<Ads>();
		for(Ads ads:adsList)sortedAdsList.add(ads);	
		
		adsList = null;
		
		Ordering ordering  = new Ordering("created", false);
		ordering.orderList(sortedAdsList);
				
		
		
		// Formate result
		String result = "";		
		String format = MessagesManager.getText("main.ads.table.tr");
		int startItem = (inPage - 1) * getPageSize();
		int endItem = inPage * getPageSize();
		
		// Define medorator role
		boolean isMedorator = SessionManager.hasRole(SessionManager.MODERATOR_ROLE_DEF);
		
		for (int j = startItem; j < endItem; j++) {
			if (j >= sortedAdsList.size())
				break;
			Ads ads = sortedAdsList.get(j);
			result = result
					+ String.format(format, 
							(j + 1), 
							ads.getText() + getAdditionalLinks(ads, isMedorator),
							Utils.GetFormatedDate("yyyy.MM.dd", ads.getDeleteAfter()),
							Utils.GetFormatedDate("yyyy.MM.dd HH:mm:ss.SSS", ads.getCreated()));
		}
		
		return getHTMLPaginator(inPage, sortedAdsList.size())
			+ MessagesManager.getText("main.ads.table.header")
			+ result
			+ MessagesManager.getText("main.ads.table.footer");
	}

	private String getAdditionalLinks(Ads ads, boolean isMedorator) {
		if(!isMedorator) return "";
		
		String linkTemplate = MessagesManager.getDefault("link.with.onlick.template");
		
		return "&nbsp;" + String.format(linkTemplate,
				ads.getUuid(),
				"AdsProxy.remove(this.id)",
				MessagesManager.getText("text.remove"));
	}
}
