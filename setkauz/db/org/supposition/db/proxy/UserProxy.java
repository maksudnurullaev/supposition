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

	@Override
	public int getCount() {
		return getAll().size();
	}

	public String addUser(UserBean inUser){
		_log.debug("Mail:" + inUser.getMail());
		_log.debug("Additionals:" + inUser.getAdditionals());
		_log.debug("Password:" + inUser.getNewpassword());
		_log.debug("Password2:" + inUser.getNewpassword2());
		
		// Check for valid NEW passwords
		String result = DBUtils.validatePassword(inUser);
		if(result !=null) return Constants._web_error_result_prefix + result;		
		
		User user = null;
		try {
			user = createNew();
		} catch (Exception e) {
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		user.setUser(inUser);
		user.setPassword(inUser.getNewpassword());
		
		ValidationResult validationResult = new ValidationResult(); 		
		user.validateForUpdate(validationResult);

		if(validationResult.hasFailures()){
			System.out.println("### Validation Failed ###");
			String failResult = MessagesManager.getText("message.data.NOT.saved") + ":\n";
			for(ValidationFailure fail: validationResult.getFailures()){
				System.out.println("Fails: " + fail.getDescription());
				failResult += "\t - " + MessagesManager.getText(fail.getDescription()) + "\n";;
			}			
			return Constants._web_error_result_prefix + failResult;
		}		
		
		commitChanges();		
		return Constants._web_ok_result_prefix + MessagesManager.getText("message.data.saved");		
	}

	public String enterUser(UserBean inUser){
		_log.debug("Mail:" + inUser.getMail());
		_log.debug("Password:" + inUser.getPassword());
		
		// Validate for existing user (by mail)
		addExpression(ExpressionFactory.matchExp("Mail", inUser.getMail()));
		
		List<User> userList = getAll();
		
		if(userList.size() == 0){
			_log.warn("Users not found with mail - " + inUser.getMail());
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.wrong.mail.or.password");
		} else {
			if (userList.size() > 1) {
				_log.warn("Database has too many users record with same mail - " + inUser.getMail());
				return Constants._web_error_result_prefix + MessagesManager.getText("errors.dbobject.already.registered") ;
			} else {
				if (!userList.get(0).checkPassword(inUser.getPassword())) {
					return Constants._web_error_result_prefix + MessagesManager.getText("errors.wrong.mail.or.password");
				}
			}			
		}
		
		SessionManager.setSessionValue(Constants._string_userId, userList.get(0).getID());
		return Constants._web_ok_result_prefix + MessagesManager.getText("message.data.saved");		
	}

	// ############################
	@Override
	public List<String> getColumnNames() {
		String[] result = { "#", "Mail", "Created", "Updated" };
		return Arrays.asList(result);
	}

	public String getFormUpdate(int userPk) {
		String result;
		User user = getDBObjectByIntPk(userPk);
		
		if (user != null)
			result = String.format(MessagesManager
					.getText("main.admin.users.formUpdate"), user.getMail(),
					user.getAdditionals(), user.getID());
		else
			result = String.format(MessagesManager
					.getText("main.admin.users.user_not_found_text"), userPk);

		return String.format(result, userPk);
	}

	// ######################################################################
	public String getPageAsHTMLTable(int inPage) {
		String format = MessagesManager.getText("main.admin.users.table.tr");
		String result = "";
		List<User> users = getAll();
		int i = 0;
		for (User user : users) {
			result = result
					+ String.format(format, ++i, user.getMail(), user
							.getAdditionals(), user.getCreated(), user
							.getUpdated(), user.getID());
		}
		return MessagesManager.getText("main.admin.users.table.header")
				+ result
				+ MessagesManager.getText("main.admin.users.table.footer");
	}

	public String updateUserData(UserBean inUser) {
		_log.debug("ID:" + inUser.getId());
		_log.debug("Mail:" + inUser.getMail());
		_log.debug("Additionals:" + inUser.getAdditionals());

		User user =getDBObjectByIntPk(inUser.getId());
		user.setUser(inUser);

		ValidationResult validationResult = new ValidationResult();
		user.validateForUpdate(validationResult);

		if (validationResult.hasFailures()) {
			System.out.println("### Validation Failed ###");
			String failResult = MessagesManager
					.getText("message.data.NOT.saved")
					+ ":\n";
			for (ValidationFailure fail : validationResult.getFailures()) {
				System.out.println("Fails: " + fail.getDescription());
				failResult += "\t - "
						+ MessagesManager.getText(fail.getDescription()) + "\n";
			}
			return failResult;
		}

		commitChanges();
		return MessagesManager.getText("message.data.saved");
	}

	public String updateUserPassword(UserBean inUser) {
		// Check for valid NEW passwords
		String result = DBUtils.validatePassword(inUser);
		if (result != null)
			return Constants._web_error_result_prefix + result;

		User user = getDBObjectByIntPk(inUser.getId());

		// Check for valid OLD password
		_log.debug("user.сheckPassword(inUser.getPassword()) = "
				+ user.checkPassword(inUser.getPassword()));
		if (!user.checkPassword(inUser.getPassword())) {
			return Constants._web_error_result_prefix
					+ MessagesManager.getText("message.data.NOT.saved") + ":\n"
					+ "\t - "
					+ MessagesManager.getText("errors.invalid.old.password");
		}

		// Trying to update real password of Database Object
		_log.debug("New password:" + inUser.getNewpassword());
		ValidationResult validationResult = new ValidationResult();
		user.setPassword(inUser.getNewpassword());
		user.validateForUpdate(validationResult);
		if (validationResult.hasFailures()) {
			System.out.println("### Validation Failed ###");
			String failResult = MessagesManager
					.getText("message.data.NOT.saved")
					+ ":\n";
			for (ValidationFailure fail : validationResult.getFailures()) {
				System.out.println("Fails: " + fail.getDescription());
				failResult += "\t - "
						+ MessagesManager.getText(fail.getDescription());
			}
			return Constants._web_error_result_prefix + failResult;
		}

		commitChanges();
		return Constants._web_ok_result_prefix
				+ MessagesManager.getText("message.new.password.saved");
	}	
	
}
