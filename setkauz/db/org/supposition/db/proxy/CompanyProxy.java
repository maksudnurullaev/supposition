package org.supposition.db.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Cgroup;
import org.supposition.db.Company;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
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
	
	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");
		
		String[] result = {"#","Name"};
		return Arrays.asList(result);
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
			e.printStackTrace();
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

	public String getPageAsHTMLTable(CompanyFilterBean inFilter, int inPage) {
		_log.debug("-> getPageAsHTMLTable");
		_log.debug("inFilter.getCity(): " + inFilter.getCity());		
		_log.debug("inFilter.getGuuid(): " + inFilter.getGuuid());
		
		// Check page value 
		if (inPage <= 0 // if page negative
			||	inFilter.getCity().length() > 6) // if city has not format "[N|6][#]"
			return MessagesManager.getText("errors.too.many.objects");		
		
		// Set cgroup filter
		if(!inFilter.getGuuid().equals("root")){
			CgroupProxy cgroupProxy = new CgroupProxy();
			Cgroup cgroup = cgroupProxy.getDBObjectByUuid(inFilter.getGuuid());
			
			if(cgroup != null){
				List<String> cgroupUuidList = new ArrayList<String>();				
				getCgroupUuidAsList(cgroupUuidList, cgroup);
				addExpression(ExpressionFactory.inDbExp("guuid", cgroupUuidList));
			}
		}	
		
		// Set city filter
		if(!inFilter.getCity().equals("root")){
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
		// addOrdering(new Ordering("created", false));
		
		List<Company> objectsList = getAll();
		
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

	private String getHTMLTable(int inPage, List<Company> inList) {
		// Define start & end items
		int startItem = (inPage - 1) * getPageSize();
		int endItem = inPage * getPageSize();
		int allItemCount = inList.size();
		
		// Formate result
		String result = "";		
		String format = MessagesManager.getText("main.company.table.tr");
		
		// Define medorator role
		boolean isMedorator = (SessionManager.hasRole(SessionManager.MANAGER_ROLE_DEF) 
				|| SessionManager.hasRole(SessionManager.ADMIN_ROLE_DEF));
				// || SessionManager.getUserUuid().equals(anObject));
		
		for (int j = startItem; j < endItem; j++) {
			if (j >= inList.size()) break;
			Company item = inList.get(j);
			result = result
					+ String.format(format,
							(++startItem),
							getURLAsLink(item) + getAdditionalLinks(item, (isMedorator || item.getUser().getUuid().equals(SessionManager.getUserUuid()))),
							item.getAdditionals()
							);
		}
		
		return getHTMLPaginator(inPage, allItemCount)
			+ MessagesManager.getText("main.company.table.header")
			+ result
			+ MessagesManager.getText("main.company.table.footer");
	}

	private String getURLAsLink(Company inItem) {
		if(inItem.getWww().isEmpty() || 
				inItem.getWww().equals("http://"))
			return inItem.getName();
		
		return String.format("<a href=\"%s\" target=\"_blank\">%s</a>", inItem.getWww(), inItem.getName());
	}

	private String getAdditionalLinks(Company inItem, boolean isMedorator) {
		if(!isMedorator) return "";
		
		String result = " [" + String.format(Utils.linkTemplate,
				inItem.getUuid(),
				"CompanyProxy.edit(this.id)",
				MessagesManager.getText("text.edit")) + "]";
		
		result += " [" + String.format(Utils.linkTemplate,
				inItem.getUuid(),
				"CompanyProxy.remove(this.id)",
				MessagesManager.getText("text.remove")) + "]";
		
		return result;
	}

}
