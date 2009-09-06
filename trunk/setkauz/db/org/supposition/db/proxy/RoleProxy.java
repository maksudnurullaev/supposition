package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Role;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.Utils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;

public class RoleProxy extends ADBProxyObject<Role> {
	
	private static final long serialVersionUID = 1L;
	
	public RoleProxy(){
		super();
		setEClass(Role.class);
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
			_log.debug("addDBORole -> Constants.isValidString");			
			_log.debug("addDBORole -> Constants.isValidString -> roleBean.getName() -> " + roleBean.getName());			
			return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.empty.value");
		}
			
		
		// Create new DBO
		Role role = null;
		try {
			role = createNew();
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") + MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// Setup user bean
		role.setRole(roleBean);

		// Validate
		ValidationResult validationResult = role.getValidationResult();

		if(validationResult.hasFailures()){
			_log.warn("### Validation Failed ###");
			String failResult = MessagesManager.getText("message.data.NOT.saved") + ":\n";
			for(ValidationFailure fail: validationResult.getFailures()){
				_log.warn("Fails: " + fail.getDescription());
				failResult += "\t - " + MessagesManager.getText(fail.getDescription()) + "\n";;
			}		
			// Delete Object before commit
			deleteObject(role);
			return MessagesManager.getDefault("web.error.result.prefix")
						+ failResult;
		}

		commitChanges();		
		
		return MessagesManager.getDefault("web.ok.result.prefix") + MessagesManager.getText("message.data.saved");		
	}

	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");
		
		String[] result = { "#", "Name"};
		return Arrays.asList(result);
	}

	public String getFormUpdate(int rolePk) {
		_log.debug("-> getFormUpdate");
		
		String result = "";
		Role role = getDBObjectByIntPk(rolePk);
		
		if (role != null)
			result = String.format(MessagesManager
					.getText("main.admin.roles.formUpdate"),
					role.getName(), 
					role.getID());
		else
			result = String.format(MessagesManager
					.getText("main.admin.roles.not_found_text"), rolePk);

		return String.format(result, rolePk);
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
							role.getID(), 
							role.getName(), 
							role.getUsers().size(), 
							role.getID());
		}
		
		return  getHTMLPaginator(inPage)
				+ MessagesManager.getText("main.admin.roles.table.header")
				+ result
				+ MessagesManager.getText("main.admin.roles.table.footer");
	}

	public String updateDBORole(RoleBean roleBean) {
		_log.debug("updateDBORole -> ");

		Role role =getDBObjectByIntPk(roleBean.getId());
		role.setRole(roleBean);

		ValidationResult validationResult = role.getValidationResult();

		if (validationResult.hasFailures()) {
			_log.debug("updateDBORole -> validationResult.hasFailures() -> TRUE");
			String failResult = MessagesManager.getText("message.data.NOT.saved")
					+ ":\n";
			for (ValidationFailure fail : validationResult.getFailures()) {
				_log.warn("Fails: " + fail.getDescription());
				failResult += "\t - "
						+ MessagesManager.getText(fail.getDescription()) + "\n";
			}
			// RollBack changes
			rollbackChanges();
			return failResult;
		}
		
		commitChanges();		
		
		return MessagesManager.getText("message.data.saved");
	}
}
