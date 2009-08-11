package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.User;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.Constants;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;

public class Users extends ADBProxyObject<User> {

	private static final long serialVersionUID = 1L;

	public Users(){
		super();
		setEClass(User.class);
	}
	
	@Override
	public List<String> getColumnNames() {
		String[] result = { "#", "Mail", "Created", "Updated" };
		return Arrays.asList(result);
	}

	public User getDBObjectByPk(int userPk) {
		return DataObjectUtils.objectForPK(getContext(), User.class, userPk);
	}	

	public String getFormUpdate(int userPk) {
		String result;
		User user = getDBObjectByPk(userPk);
		
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

		User user =getDBObjectByPk(inUser.getId());
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

		getContext().commitChanges();
		return MessagesManager.getText("message.data.saved");
	}

	public String updateUserPassword(UserBean inUser) {
		// Check for valid NEW passwords
		String result = DBUtils.validatePassword(inUser);
		if (result != null)
			return Constants._web_error_result_prefix + result;

		User user = getDBObjectByPk(inUser.getId());

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

		getContext().commitChanges();
		return Constants._web_ok_result_prefix
				+ MessagesManager.getText("message.new.password.saved");
	}
}
