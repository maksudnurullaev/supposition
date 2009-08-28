package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.User;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.Constants;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;

public class UserProxy extends ADBProxyObject<User> {
	
	private static final long serialVersionUID = 1L;
	
	public UserProxy(){
		super();
		setEClass(User.class);
	}		

	public void setSessionFilter(UserBean inBean){
		SessionManager.setSessionValue(getSessionFilterDef(), inBean);
	} 
	
	public String setSessionFilterAndGetPageAsHTMLTable(UserBean inBean){
		setSessionFilter(inBean);
		return getPageAsHTMLTable(1);
	} 	
	
	public String removeSessionFilterAndGetPageAsHTMLTable(){
		SessionManager.removeFromSession(getSessionFilterDef());
		return getPageAsHTMLTable(1);
	}
		
	public String addDBOUser(UserBean userBean){
		_log.debug("-> addDBOUser");
		
		// Check for valid NEW passwords
		String result = DBUtils.validatePassword(userBean);
		if(result !=null) return Constants._web_error_result_prefix + result;		
		
		// Create new DBO
		User user = null;
		try {
			user = createNew();
		} catch (Exception e) {
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// Setup user bean
		user.setUser(userBean);
		user.setPassword(userBean.getNewpassword());
		
		// Validate
		ValidationResult validationResult = user.getValidationResult();

		if(validationResult.hasFailures()){
			_log.warn("### Validation Failed ###");
			String failResult = MessagesManager.getText("message.data.NOT.saved") + ":\n";
			for(ValidationFailure fail: validationResult.getFailures()){
				_log.warn("Fails: " + fail.getDescription());
				failResult += "\t - " + MessagesManager.getText(fail.getDescription()) + "\n";;
			}		
			// Delete Object before commit
			deleteObject(user);
			return Constants._web_error_result_prefix + failResult;
		}else {
			user.postValidationSave();		
		}

		commitChanges();		
		
		return Constants._web_ok_result_prefix + MessagesManager.getText("message.data.saved");		
	}

	public String enterDBOUser(UserBean userBean){
		_log.debug("-> enterDBOUser ");
		
		_log.debug("userBean.getMail() = " + userBean.getMail());
		_log.debug("userBean.getPassword() = " + userBean.getPassword());
		_log.debug("userBean.getNewpassword() = " + userBean.getNewpassword());
		_log.debug("userBean.getNewpassword2() = " + userBean.getNewpassword2());
		
		// Check for NULL object
		if(userBean == null){
			_log.error("errors.null.object");
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.null.object");
		}
		
		// Check for mail
		if(!Constants.isValidString(userBean.getMail())){
			_log.error("errors.wrong.mail");			
			_log.debug("Mail:" + userBean.getMail());
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.empty.mail");
		}
		
		if(!Constants.isValidEmailAddress(userBean.getMail())){
			_log.error("errors.invalid.mail");			
			_log.debug("Mail:" + userBean.getMail());
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.invalid.mail");
		}
		
		// Check for password
		if(!Constants.isValidString(userBean.getPassword())){
			_log.error("errors.empty.password");			
			_log.debug("Password:" + userBean.getPassword());
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.passwords.is.empty");
		}
		
		// Validate for existing user (by mail)
		cleanExpressions();
		addExpression(ExpressionFactory.matchExp("Mail", userBean.getMail()));
		
		List<User> userList = getAll();
		
		if(userList.size() == 0){
			_log.warn("errors.wrong.mail.or.password");
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.object.not.found");
		} else {
			if (userList.size() > 1) {
				_log.warn("errors.dbobject.already.registered");
				return Constants._web_error_result_prefix + MessagesManager.getText("errors.too.many.objects") ;
			} else {
				if (!userList.get(0).checkPassword(userBean.getPassword())) {
					_log.warn("errors.wrong.mail.or.password");
					return Constants._web_error_result_prefix + MessagesManager.getText("errors.wrong.mail.or.password");
				}
			}			
		}
		
		SessionManager.setSessionValue(Constants._string_userId, userList.get(0).getID());
		return Constants._web_ok_result_prefix + MessagesManager.getText("message.data.saved");		
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
					user.getAdditionals(), 
					user.getID());
		else
			result = String.format(MessagesManager
					.getText("main.admin.users.user_not_found_text"), userPk);

		return String.format(result, userPk);
	}

	public String getPageAsHTMLTable(int inPage) {
		_log.debug("-> getPageAsHTMLTable");
		
		if(inPage <= 0)
			return MessagesManager.getText("errors.too.many.objects");
		
		String format = MessagesManager.getText("main.admin.users.table.tr");
		String result = "";
		
		// Check for filter
		if(isSessionHasFilter()){
			applyFilter((UserBean) SessionManager.getFromSession(getSessionFilterDef()));
		}
		
		// Get items
		List<User> users = getAll();
		
		// Check for items
		if(users == null ||
				users.size() == 0)
			return getHTMLPaginator(inPage) + 
				MessagesManager.getText("errors.object.not.found");
		
		// Formate result		
		int startItem = (inPage - 1) * getPageSize();
		int endItem = inPage * getPageSize();
		
		for (int j = startItem; j < endItem; j++) {
			if(j >= users.size()) break;
			User user = users.get(j);
			result = result
					+ String.format(format, 
							(j + 1), 
							user.getID(), 
							user.getMail(), 
							user.getStatus(), 
							user.getAdditionals(), 
							user.getCreated(), 
							user.getUpdated(), 
							user.getID());
		}
		
		return  getHTMLPaginator(inPage)
				+ MessagesManager.getText("main.admin.users.table.header")
				+ result
				+ MessagesManager.getText("main.admin.users.table.footer");
	}

	public String updateDBOUser(UserBean userBean) {
		_log.debug("-> updateDBOUser");

		User user =getDBObjectByIntPk(userBean.getId());
		user.setUser(userBean);

		ValidationResult validationResult = user.getValidationResult();

		if (validationResult.hasFailures()) {
			_log.warn("### Validation Failed ###");
			String failResult = MessagesManager
					.getText("message.data.NOT.saved")
					+ ":\n";
			for (ValidationFailure fail : validationResult.getFailures()) {
				_log.warn("Fails: " + fail.getDescription());
				failResult += "\t - "
						+ MessagesManager.getText(fail.getDescription()) + "\n";
			}
			// RollBack changes
			rollbackChanges();
			return failResult;
		}else {
			user.postValidationSave();		
		}
		commitChanges();		
		
		return MessagesManager.getText("message.data.saved");
	}

	public String updateDBOUserPassword(UserBean userBean) {
		_log.debug("-> updateDBOUserPassword");
		
		// Check for valid NEW passwords
		String result = DBUtils.validatePassword(userBean);
		if (result != null)
			return Constants._web_error_result_prefix + result;

		User user = getDBObjectByIntPk(userBean.getId());

		// Check for valid OLD password
		_log.debug("user.сheckPassword(inUser.getPassword()) = "
				+ user.checkPassword(userBean.getPassword()));
		if (!user.checkPassword(userBean.getPassword())) {
			return Constants._web_error_result_prefix
					+ MessagesManager.getText("message.data.NOT.saved") + ":\n"
					+ "\t - "
					+ MessagesManager.getText("errors.invalid.old.password");
		}

		// Trying to update real password of Database Object
		_log.debug("New password:" + userBean.getNewpassword());
		ValidationResult validationResult = new ValidationResult();
		user.setPassword(userBean.getNewpassword());
		user.validateForUpdate(validationResult);
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
			return Constants._web_error_result_prefix + failResult;
		}else {
			user.postValidationSave();		
			commitChanges();		
		}
		return Constants._web_ok_result_prefix
				+ MessagesManager.getText("message.new.password.saved");
	}

	public String findItemsByFilter(UserBean inUser){
		applyFilter(inUser);
		return String.format(MessagesManager.getText("message.found.N.records"), 
				getAll().size());
	}

	private void applyFilter(UserBean inUser) {
		cleanExpressions();
		addExpression(ExpressionFactory.likeIgnoreCaseExp("Mail", normalizeString4Filter(inUser.getMail())));
	}
	
	private String normalizeString4Filter(String inString) {
		int notFound = -1;
		if(inString.indexOf("%") == notFound )
			return "%" + inString + "%";
		return inString;
	}
}
