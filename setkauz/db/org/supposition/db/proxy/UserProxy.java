package org.supposition.db.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Role;
import org.supposition.db.User;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.Utils;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;

public class UserProxy extends ADBProxyObject<User> {

	private static final long serialVersionUID = 1L;

	public UserProxy() {
		super();
		setEClass(User.class);
	}

	public void setSessionFilter(UserBean inBean) {
		SessionManager.setToSession(getSessionFilterDef(), inBean);
	}

	public String setSessionFilterAndGetPageAsHTMLTable(UserBean inBean) {
		setSessionFilter(inBean);
		return getPageAsHTMLTable(1);
	}

	public String removeSessionFilterAndGetPageAsHTMLTable() {
		SessionManager.removeFromSession(getSessionFilterDef());
		return getPageAsHTMLTable(1);
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
		ValidationResult validationResult = user.getValidationResult(true);

		if (validationResult.hasFailures()) {
			String failResult = MessagesManager
					.getText("message.data.NOT.saved")
					+ ":\n";
			for (ValidationFailure fail : validationResult.getFailures()) 
				failResult += "\t - "+ MessagesManager.getText(fail.getDescription()) + "\n";
			
			// Delete Object before commit
			deleteObject(user);
			return MessagesManager.getDefault("web.error.result.prefix")
					+ failResult;
		} else {
			user.postValidationSave();
		}

		commitChanges();
		
		// Register User
		SessionManager.loginUser(user);
		
		return MessagesManager.getDefault("web.ok.result.prefix")
				+ MessagesManager.getText("message.congratulations");
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
		cleanExpressions();
		addExpression(ExpressionFactory.matchExp("Mail", userBean.getMail()));

		List<User> userList = getAll();

		if (userList.size() == 0) {
			_log.warn("errors.wrong.mail.or.password");
			return MessagesManager.getDefault("web.error.result.prefix")
					+ MessagesManager.getText("errors.wrong.mail.or.password");
		} else {
			if (userList.size() > 1) {
				_log.error("errors.too.many.objects");
				return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.too.many.objects");
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
	
	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");

		String[] result = { "#", "Mail", "Created", "Updated" };
		return Arrays.asList(result);
	}

	public String getFormUpdate(int userPk) {
		_log.debug("-> getFormUpdate");

		String result;
		User user = getDBObjectByIntPk(userPk);

		if (user != null)
			result = String.format(MessagesManager
					.getText("main.admin.users.formUpdate"), user.getMail(),
					user.getAdditionals(), userPk,
					getCurrentRolesAsHTML(userPk),
					getAvailableRolesAsHTML(userPk));
		else
			result = String.format(MessagesManager
					.getText("main.admin.users.user_not_found_text"), userPk);

		return result;
	}

	public String getCurrentRolesAsHTML(int userPk) {
		User user = getDBObjectByIntPk(userPk);
		String result = "";

		List<Role> userRolesList = user.getRoles();

		if (userRolesList.size() == 0) {
			result += MessagesManager.getText("text.no.data");
		} else {
			for (Role role : userRolesList) {
				result += String.format(MessagesManager
						.getText("template.input.button"), role.getID(), role
						.getName(), "UserProxy.removeRole(this.id)");
			}
		}
		return result;
	}

	public String getAvailableRolesAsHTML(int userPk) {
		User user = getDBObjectByIntPk(userPk);
		String result = "";

		RoleProxy roleProxy = new RoleProxy();

		List<Role> allRolesList = roleProxy.getAll();
		List<Role> availableRolesList = subRoles(allRolesList, user.getRoles());

		if (availableRolesList.size() == 0) {
			result += MessagesManager.getText("text.no.data");
		} else {
			for (Role role : availableRolesList) {
				result += String.format(MessagesManager
						.getText("template.input.button"), role.getID(), role
						.getName(), "UserProxy.addRole(this.id)");
			}
		}

		return result;
	}

	private List<Role> subRoles(List<Role> mainRoles, List<Role> subRoles) {
		_log
				.debug(String.format("subRoles contains %s roles", subRoles
						.size()));

		List<Role> result = new ArrayList<Role>();

		for (Role role : mainRoles) {
			if (!containsRole(role, subRoles))
				result.add(role);
		}
		return result;
	}

	private boolean containsRole(Role inRole, List<Role> mainRoles) {
		for (Role role : mainRoles) {
			if (inRole.getID().equals(role.getID()))
				return true;
		}
		return false;
	}

	public String getPageAsHTMLTable(int inPage) {
		_log.debug("-> getPageAsHTMLTable");

		if (inPage <= 0)
			return MessagesManager.getText("errors.too.many.objects");

		String format = MessagesManager.getText("main.admin.users.table.tr");
		String result = "";

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
					+ MessagesManager.getText("errors.object.not.found");

		// Formate result
		int startItem = (inPage - 1) * getPageSize();
		int endItem = inPage * getPageSize();

		for (int j = startItem; j < endItem; j++) {
			if (j >= users.size())
				break;
			User user = users.get(j);
			result = result
					+ String.format(format, (j + 1), user.getID(), user
							.getMailWithRoles(), user.getStatus(), user
							.getAdditionals(), user.getCreated(), user
							.getUpdated(), user.getID());
		}

		return getHTMLPaginator(inPage)
				+ MessagesManager.getText("main.admin.users.table.header")
				+ result
				+ MessagesManager.getText("main.admin.users.table.footer");
	}

	/**
	 * Just for stub function without using kaptcha
	 * @param userBean
	 * @return String 
	 */
	public String updateDBOUser(UserBean userBean){
		return updateDBOUserByCaptcha(userBean, false);
	}
	
	public String updateDBOUserByCaptcha(UserBean userBean, boolean withCaptcha) {
		_log.debug("-> updateDBOUser");

		if(userBean == null)
			return MessagesManager.errorPrefix() +
				MessagesManager.getText("errors.null.object");
		
		User user = getDBObjectByIntPk(userBean.getId());
		user.setUser(userBean);

		ValidationResult validationResult = user.getValidationResult(withCaptcha);

		if (validationResult.hasFailures()) {
			String failResult = MessagesManager.errorPrefix()
					+ MessagesManager.getText("message.data.NOT.saved")	
					+ ":\n";
			for (ValidationFailure fail : validationResult.getFailures()) {
				failResult += "\t - "
						+ MessagesManager.getText(fail.getDescription()) + "\n";
			}
			// RollBack changes
			rollbackChanges();
			return failResult;
		} else {
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

		User user = getDBObjectByIntPk(userBean.getId());

		// Trying to update real password of Database Object
		_log.debug("New password:" + userBean.getNewpassword());
		ValidationResult validationResult = new ValidationResult();
		
		user.setPassword(userBean.getNewpassword());
		user.validatePassword(validationResult);
		
		if (validationResult.hasFailures()) {
			_log.warn("### Validation Failed ###");
			String failResult = MessagesManager
					.getText("message.data.NOT.saved")
					+ ":\n";
			for (ValidationFailure fail : validationResult.getFailures()) {
				_log.warn("Fails: " + fail.getDescription());
				failResult += "\t - "
						+ MessagesManager.getText(fail.getDescription());
			}
			// RollBack changes
			rollbackChanges();
			return MessagesManager.getDefault("web.error.result.prefix")
					+ failResult;
		} else {
			user.postValidationSave();
			commitChanges();
		}
		return MessagesManager.getDefault("web.ok.result.prefix")
				+ MessagesManager.getText("message.new.password.saved");
	}

	public String findItemsByFilter(UserBean inUser) {
		applyFilter(inUser);
		return String.format(
				MessagesManager.getText("message.found.N.records"), getAll()
						.size());
	}

	private void applyFilter(UserBean inUser) {
		cleanExpressions();
		addExpression(ExpressionFactory.likeIgnoreCaseExp("Mail",
				normalizeString4Filter(inUser.getMail())));
	}

	private String normalizeString4Filter(String inString) {
		int notFound = -1;
		if (inString.indexOf("%") == notFound)
			return "%" + inString + "%";
		return inString;
	}

	public String addDBORole(UserBean userBean) {
		_log.debug(String.format("AddRole(%s) for User(%s)", userBean.getId(),
				userBean.getRoleId()));

		User user = getDBObjectByIntPk(userBean.getId());

		user.addToRoles(DataObjectUtils.objectForPK(getObjectContext(),
				Role.class, userBean.getRoleId()));

		commitChanges();

		return MessagesManager.getDefault("web.ok.result.prefix")
				+ MessagesManager.getText("message.data.saved");
	}

	public String removeDBORole(UserBean userBean) {
		_log.debug(String.format("RemoveRole(%s) for User(%s)", userBean
				.getId(), userBean.getRoleId()));

		User user = getDBObjectByIntPk(userBean.getId());

		user.removeFromRoles(DataObjectUtils.objectForPK(getObjectContext(),
				Role.class, userBean.getRoleId()));

		commitChanges();

		return MessagesManager.getDefault("web.ok.result.prefix")
				+ MessagesManager.getText("message.data.saved");
	}

}
