package org.supposition.db.proxy;

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
	public List<String> getColumnNames() {
		return null;
	}

	@Override
	public int getCount() {
		return 0;
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
		Users users = new Users();
		users.addExpression(ExpressionFactory.matchExp("Mail", inUser.getMail()));
		
		List<User> userList = users.getAll();
		
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

}
