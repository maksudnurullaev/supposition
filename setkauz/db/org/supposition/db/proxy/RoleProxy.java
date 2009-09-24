package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Role;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;
import org.supposition.utils.Utils;

public class RoleProxy extends ADBProxyObject<Role> {
	
	private static final long serialVersionUID = 1L;
	
	public RoleProxy(){
		super();
		setEClass(Role.class);
		setDataContext(DBUtils.getInstance().getDBContext());
	}

	public RoleProxy(DataContext inDataContext) {
		super();
		setEClass(Role.class);
		setDataContext(inDataContext);
	}	
	
	public void setSessionFilter(UserBean inBean){
		SessionManager.setToSession(getSessionFilterDef(), inBean);
	} 
	
	public String setSessionFilterAndGetPageAsHTMLTable(UserBean inBean){
		setSessionFilter(inBean);
		return getPageAsHTMLTable(1);
	} 	
	
	public String removeSessionFilterAndGetPageAsHTMLTable(){
		SessionManager.removeFromSession(getSessionFilterDef());
		return getPageAsHTMLTable(1);
	}
		
	public String addDBORole(RoleBean roleBean){
		_log.debug("addDBORole ->");
		
		// Check for valid name		
		if(!Utils.isValidString(roleBean.getName())){
			_log.debug("addDBORole -> Constants.isValidString -> roleBean.getName() -> " + roleBean.getName());			
			return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.empty.value");
		}
			
		
		// Create new DBO
		Role role = null;
		try {
			role = createNew();
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// Setup user bean
		role.setRole(roleBean);

		// Validate
		ValidationResult validationResult = role.getValidationResult();

		if(validationResult.hasFailures()){
			// Delete Object before commit
			deleteObject(role);
			return MessagesManager.getDefault("web.error.result.prefix")
						+ DBUtils.getFailuresAsString(validationResult);
		}

		commitChanges();		
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
			+ MessagesManager.getText("message.data.saved");		
	}

	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");
		
		String[] result = { "#", "Name"};
		return Arrays.asList(result);
	}

	public String getFormUpdate(String inUuid) {
		_log.debug("-> getFormUpdate");
		
		String result = "";
		Role role = getDBObjectByUuid(inUuid);
		
		if (role != null)
			result = String.format(MessagesManager
					.getText("main.admin.roles.formUpdate"),
					role.getName(), 
					role.getUuid());
		else
			result = String.format(MessagesManager
					.getText("main.admin.roles.not_found_text"), inUuid);

		return String.format(result, inUuid);
	}

	public String getPageAsHTMLTable(int inPage) {
		_log.debug("-> getPageAsHTMLTable");
		
		if(inPage <= 0)
			return MessagesManager.getText("errors.too.many.objects");
		
		String format = MessagesManager.getText("main.admin.roles.table.tr");
		
		String result = "";
		
		// Get items
		List<Role> roles = getAll();
		
		// Check for items
		if(roles == null ||
				roles.size() == 0)
			return getHTMLPaginator(inPage) + 
				MessagesManager.getText("errors.object.not.found");
		
		// Formate result		
		int startItem = (inPage - 1) * getPageSize();
		int endItem = inPage * getPageSize();
		
		for (int j = startItem; j < endItem; j++) {
			if(j >= roles.size()) break;
			Role role = roles.get(j);
			result = result
					+ String.format(format, 
							(j + 1), 
							role.getName(), 
							role.getUsers().size(), 
							role.getUuid());
		}
		
		return  getHTMLPaginator(inPage)
				+ MessagesManager.getText("main.admin.roles.table.header")
				+ result
				+ MessagesManager.getText("main.admin.roles.table.footer");
	}

	public String updateDBORole(RoleBean roleBean) {
		_log.debug("updateDBORole -> ");

		Role role =getDBObjectByUuid(roleBean.getUuid());
		role.setRole(roleBean);

		ValidationResult validationResult = role.getValidationResult();

		if (validationResult.hasFailures()) {
			_log.debug("Validation Failed");
			rollbackChanges();
			return MessagesManager.getDefault("web.error.result.prefix")
				+ DBUtils.getFailuresAsString(validationResult);
		}
		
		commitChanges();		
		
		return MessagesManager.getDefault("web.ok.result.prefix")
			+ MessagesManager.getText("message.data.saved");
	}
}
