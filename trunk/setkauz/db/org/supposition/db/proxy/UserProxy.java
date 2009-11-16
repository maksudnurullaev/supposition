package org.supposition.db.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Role;
import org.supposition.db.User;
import org.supposition.db.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;
import org.supposition.utils.Utils;

public class UserProxy extends ADBProxyObject<User> {

	private static final long serialVersionUID = 1L;

	public UserProxy() {
		super();
		setEClass(User.class);
		setDataContext(DBUtils.getInstance().getDBContext());
	}

	public UserProxy(DataContext inDataContext) {
		super();
		setEClass(User.class);
		setDataContext(inDataContext);
	}

	public String addDBORole(UserBean userBean) {
		_log.debug(String.format("AddRole(%s) for User(%s)", userBean.getRoleuuid(),
				userBean.getUuid()));

		User user = getDBObjectByUuid(userBean.getUuid());
		
		Role role = (new RoleProxy(getDataContext())).getDBObjectByUuid(userBean.getRoleuuid());
		
		user.addToRoles(role);
		
		commitChanges();

		return MessagesManager.getDefault("web.ok.result.prefix")
				+ MessagesManager.getText("message.data.saved");
	}

	public String addDBOUser(UserBean userBean) {
		_log.debug("-> addDBOUser");

		// Check for valid NEW passwords
		String result = DBUtils.validatePassword(userBean);
		if (result != null)
			return MessagesManager.getDefault("web.error.result.prefix")
					+ result;

		// Create new DBO
		User user = null;
		try {
			user = createNew();
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix")
					+ MessagesManager
							.getText("errors.could.not.create.dbobject");
		}

		// Setup user bean
		user.setUser(userBean);
		user.setPassword(userBean.getNewpassword());

		// Validate
		ValidationResult validationResult = user.getValidationResult();

		if (validationResult.hasFailures()) {
			deleteObject(user);
			return DBUtils.getFailuresAsString(validationResult);
		} else {
			user.postValidationSave();
		}
		
		// Check for 1st user (Administrator)
		if(getCount() == 0){
			_log.debug("Set to 1st user " + user.getMail() + " 'admin' role");
			
			// Check for admin role
			_log.debug("Check existance of admin role");
			RoleProxy rolesProxy = new RoleProxy(getDataContext());
			Role role;
			
			rolesProxy.addExpression(ExpressionFactory.matchDbExp("name", "admin"));
			
			if(rolesProxy.getCount() == 0){
				_log.debug("Role 'admin' does not exist, create new one");
				// Create new "admin" role
				RoleBean roleBean = new RoleBean();
				roleBean.setName("admin");
				rolesProxy.addDBORole(roleBean);
			}else{
				_log.debug("Role 'admin' already exist, we will use old one");
			}
			role = rolesProxy.getAll().get(0);
			//Role adminRole =
			user.addToRoles(role);
			_log.debug("1st user " + user.getMail() + " now has 'admin' role");
		}
		
		commitChanges();
		// Register User
		SessionManager.loginUser(user);
		
		return MessagesManager.getDefault("web.ok.result.prefix")
				+ MessagesManager.getText("message.congratulations");
	}

	private void applyFilter(UserBean inUser) {
		clearExpressions();
		addExpression(ExpressionFactory.likeIgnoreCaseExp("mail",
				normalizeString4Filter(inUser.getMail())));
	}

	private boolean containsRole(Role inRole, List<?> subRoles) {
		for (Object role : subRoles) {
			if (inRole.getUuid().equals(((Role) role).getUuid()))
				return true;
		}
		return false;
	}

	public String enterDBOUser(UserBean userBean) {
		_log.debug("-> enterDBOUser ");

		// Check for NULL object
		if (userBean == null) {
			_log.error("errors.null.object - userBean");
			return MessagesManager.getDefault("web.error.result.prefix")
					+ MessagesManager.getText("errors.null.object");
		}

		// Check for mail
		if (!Utils.isValidString(userBean.getMail())) {
			_log.error("errors.wrong.mail - userBean");
			return MessagesManager.getDefault("web.error.result.prefix")
					+ MessagesManager.getText("errors.empty.mail");
		}

		// Check for password
		if (!Utils.isValidString(userBean.getPassword())) {
			_log.error("errors.empty.password - userBean");
			return MessagesManager.getDefault("web.error.result.prefix")
					+ MessagesManager.getText("errors.passwords.is.empty");
		}

		// Validate for existing user (by mail)
		clearExpressions();
		addExpression(ExpressionFactory.matchExp("mail", userBean.getMail()));

		List<User> userList = getAll();

		if (userList == null ||
				userList.size() == 0) {
			_log.warn("errors.wrong.mail.or.password");
			return MessagesManager.getDefault("web.error.result.prefix")
					+ MessagesManager.getText("errors.wrong.mail.or.password");
		} else {
			if (userList.size() > 1) {
				_log.error("errors.unmatched.data.objects");
				return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.unmatched.data.objects");
			} else {
				if (!userList.get(0).checkPassword(userBean.getPassword())) {
					_log.warn("errors.wrong.mail.or.password");
					return MessagesManager
							.getDefault("web.error.result.prefix")
							+ MessagesManager
									.getText("errors.wrong.mail.or.password");
				}
			}
		}

		// Register User		
		SessionManager.loginUser(userList.get(0));
		
		return MessagesManager.getDefault("web.ok.result.prefix")
				+ MessagesManager.getText("message.welcome");
	}
	
	public String findItemsByFilter(UserBean inUser) {
		applyFilter(inUser);
		return String.format(
				MessagesManager.getText("message.found.N.records"), getAll()
						.size());
	}

	public String getAvailableRolesAsHTML(String inUuid) {
		User user = getDBObjectByUuid(inUuid);
		
		if(user == null)
			return "no user found by uuid = " + inUuid;
		
		String result = "";

		RoleProxy roleProxy = new RoleProxy();

		List<Role> allRolesList = roleProxy.getAll();
		List<Role> availableRolesList = subRoles(allRolesList, user.getRoles());

		_log.debug("Available Roles size: " + availableRolesList.size() + 
				" for user: " + user.getMail());
		if(_log.isDebugEnabled()){
			for(Role role:availableRolesList){
				_log.debug("Available Roles name: " + role.getName());				
			}
		}
		
		result += generateBottons(availableRolesList, "UserProxy.addRole(this.id)");

		return result;
	}

	public String getCurrentRolesAsHTML(String inUuid) {
		User user = getDBObjectByUuid(inUuid);
		
		if(user == null)
			return "no user found by uuid = " + inUuid;
		
		String result = "";

		List<?> userRolesList = user.getRoles();

		_log.debug("Current Roles size: " + userRolesList.size() + 
				" for user: " + user.getMail());
		
		if(_log.isDebugEnabled()){
			for(Object role:userRolesList){
				_log.debug("Current Roles name: " + ((Role)role).getName());				
			}
		}		
		
		result += generateBottons(userRolesList, "UserProxy.removeRole(this.id)");
		
		return result;
	}	
	
	private String generateBottons(List<?> inRolesList, String inJScript){
		String result = "";
		if (inRolesList == null 
				|| inRolesList.size() == 0) {
			result = MessagesManager.getText("errors.data.not.found");
		} else {
			for (int i = 0; i < inRolesList.size(); i++) {
				Role role = (Role) inRolesList.get(i);
				if(i != 0) result += " | ";
				result += String.format(MessagesManager
						.getDefault("template.button.ID.ONCLICK.VALUE"), 
						((Role) role).getUuid(), 
						inJScript, 
						((Role)role).getName());
			}
		}
		
		return result;
	}
	
	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");

		String[] result = { "#", "Mail", "Created", "Updated" };
		return Arrays.asList(result);
	}

	public String getFormUpdate(String inUuid) {
		_log.debug("-> getFormUpdate");

		String result;
		User user = getDBObjectByUuid(inUuid);

		if (user != null)
			result = String.format(MessagesManager
					.getText("main.admin.users.formUpdate"), user.getMail(),
					user.getAdditionals(), inUuid,
					getCurrentRolesAsHTML(inUuid),
					getAvailableRolesAsHTML(inUuid));
		else
			result = MessagesManager.getText("errors.data.not.found");

		return result;
	}

	public String getPageAsHTMLTable(int inPage) {
		_log.debug("-> getPageAsHTMLTable");

		if (inPage <= 0)
			return MessagesManager.getText("errors.unmatched.data.objects");

		// Check for filter
		if (isSessionHasFilter()) {
			applyFilter((UserBean) SessionManager
					.getFromSession(getSessionFilterDef()));
		}

		// Get items
		List<User> users = getAll();

		// Check for items
		if (users == null || users.size() == 0)
			return getHTMLPaginator(inPage)
					+ MessagesManager.getText("errors.data.not.found");

		// Formate result
		String format = MessagesManager.getText("main.admin.users.table.tr");
		String result = "";		
		int startItem = (inPage - 1) * getPageSize();
		int endItem = inPage * getPageSize();

		for (int j = startItem; j < endItem; j++) {
			if (j >= users.size())
				break;
			User user = users.get(j);
			result = result
					+ String.format(format, 
							(j + 1), 
							user.getMailWithRoles(), 
							user.getStatus(), 
							user.getAdditionals(), 
							user.getCreated(), 
							user.getUpdated(), 
							user.getUuid());
		}

		return getHTMLPaginator(inPage)
				+ MessagesManager.getText("main.admin.users.table.header")
				+ result
				+ MessagesManager.getText("main.admin.users.table.footer");
	}

	private String normalizeString4Filter(String inString) {
		int notFound = -1;
		if (inString.indexOf("%") == notFound)
			return "%" + inString + "%";
		return inString;
	}

	public String removeDBORole(UserBean userBean) {
		_log.debug(String.format("RemoveRole(%s) for User(%s)", userBean.getRoleuuid(), userBean.getUuid()));

		User user = getDBObjectByUuid(userBean.getUuid());
		
		Role role = (new RoleProxy()).getDBObjectByUuid(userBean.getRoleuuid());

		user.removeFromRoles(role);

		commitChanges();

		return MessagesManager.getDefault("web.ok.result.prefix")
				+ MessagesManager.getText("message.data.saved");
	}

	public String removeSessionFilterAndGetPageAsHTMLTable() {
		SessionManager.removeFromSession(getSessionFilterDef());
		return getPageAsHTMLTable(1);
	}

	public void setSessionFilter(UserBean inBean) {
		SessionManager.setToSession(getSessionFilterDef(), inBean);
	}

	public String setSessionFilterAndGetPageAsHTMLTable(UserBean inBean) {
		setSessionFilter(inBean);
		return getPageAsHTMLTable(1);
	}

	private List<Role> subRoles(List<Role> mainRoles, List<?> subRoles) {
		_log.debug(String.format("subRoles contains %s roles", subRoles.size()));

		List<Role> result = new ArrayList<Role>();

		for (Role role : mainRoles) {
			if (!containsRole((Role)role, subRoles))
				result.add(role);
		}
		return result;
	}

	public String updateDBOUser(UserBean userBean){
		_log.debug("-> updateDBOUser");

		if(userBean == null){
			_log.debug("errors.null.object");
			return MessagesManager.errorPrefix() +
				MessagesManager.getText("errors.null.object");
		}else{
			_log.debug("userBean not NULL - OK");			
		}
		
		User user = getDBObjectByUuid(userBean.getUuid());
		user.setUser(userBean);
		user.check4Kaptcha(false); // due update 

		ValidationResult validationResult = user.getValidationResult();

		if (validationResult.hasFailures()) {
			_log.debug("Validation failed, and validationResult.getFailures().size() = " + validationResult.getFailures().size());			
			rollbackChanges();
			return DBUtils.getFailuresAsString(validationResult);
		} else {
			_log.debug("Validation PASSED, now process user.postValidationSave()");			
			user.postValidationSave();
		}
		
		commitChanges();

		return MessagesManager.okPrefix() + MessagesManager.getText("message.data.saved");
	}

	public String updateDBOUserPassword(UserBean userBean) {
		_log.debug("-> updateDBOUserPassword");

		// Check for valid NEW passwords
		String result = DBUtils.validatePassword(userBean);
		if (result != null)
			return MessagesManager.getDefault("web.error.result.prefix")
					+ result;

		User user = getDBObjectByUuid(userBean.getUuid());

		// Trying to update real password of Database Object
		_log.debug("New password:" + userBean.getNewpassword());
		ValidationResult validationResult = new ValidationResult();
		
		user.setPassword(userBean.getNewpassword());
		user.validatePassword(validationResult);
		
		if (validationResult.hasFailures()) {
			rollbackChanges();
			return DBUtils.getFailuresAsString(validationResult);
		} else {
			user.postValidationSave();
			commitChanges();
		}
		return MessagesManager.getDefault("web.ok.result.prefix")
				+ MessagesManager.getText("message.new.password.saved");
	}

}
