package org.supposition.db;

import java.util.List;

import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.SimpleValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._User;
import org.supposition.db.proxy.UserBean;
import org.supposition.db.proxy.UserProxy;
import org.supposition.utils.Constants;
import org.supposition.utils.CryptoManager;

public class User extends _User {
	private static final long serialVersionUID = 1L;
	private Log _log = LogFactory.getLog(this.getClass());
	private boolean is_new = false;

	private void validateBeforeSave(ValidationResult validationResult) {
		_log.debug("->validateForSave");
		super.validateForSave(validationResult);
				
		validateMail(validationResult);		
		
		if(is_new){
			validatePassword(validationResult);
			validateFor2Mail(validationResult);
		}else{
			if(isNeedToChangePassword()) validatePassword(validationResult);
		}
	}
	
	public ValidationResult getValidationResult(){
		ValidationResult validationResult = new ValidationResult();
		validateBeforeSave(validationResult);
		return validationResult;
	}
	
	
	public void postValidationSave(){
		_log.debug("->validateForSave");
		
		if(is_new) {
			updatePassword();
			setCreated(Constants.GetCurrentDateTime());
		}
		else{
			if(isNeedToChangePassword()) updatePassword();
			setUpdated(Constants.GetCurrentDateTime());
		}
	}

	private boolean isNeedToChangePassword() {
		_log.debug("->isNeedToChangePassword RETURNS " + Constants.isValidString(getPassword()));
		return Constants.isValidString(getPassword());
	}

	protected void updatePassword() {
		_log.debug("->updatePassword");
		
		setSalt(CryptoManager.encryptPassword(getPassword()));
		setPassword(""); // remove original text
		setStatus(Constants._password_salted);
	}

	private boolean validateMail(ValidationResult validationResult) {
		_log.debug("->validateMail");

		if (!org.supposition.utils.Constants.isValidEmailAddress(getMail())) {
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.invalid.mail"));
			_log.error("Invalid email" + getMail());
			return false;
		}


		return true;
	}
	
	private boolean validateFor2Mail(ValidationResult validationResult){
		_log.debug("->validateFor2Mail");
				
		// We have no reason to check if DBO already has failures
		if(validationResult.hasFailures()) return false;
		
		UserProxy users = new UserProxy();
		users.addExpression(ExpressionFactory.matchExp("Mail", getMail()));

		List<User> usersList = users.getAll();
		
		if (usersList.size() > 0) {
			if (usersList.size() > 1) {
				validationResult.addFailure(new SimpleValidationFailure(this,
						"errors.dbobject.already.registered"));
				_log.warn("Database has too many users record with same mail - "
								+ getMail());
				return false;
			} else {
				if (!usersList.get(0).getID().equals(getID())) {
					validationResult.addFailure(new SimpleValidationFailure(
							this, "errors.dbobject.already.registered"));
					return false;
				}
			}
		}		
		return true;		
	}

	private boolean validatePassword(ValidationResult validationResult) {
		_log.debug("->validatePassword");
		// Validate for empty password
		if (getPassword().isEmpty()) {
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.passwords.is.empty"));
			_log.error("Empty password");
			return false;
		}
		// Validate for password length
		else if (getPassword().length() < Constants._min_password_length) {
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.invalid.password.length"));
			_log.error("Invalid password length, should be > "
					+ Constants._min_password_length);
			return false;
		}
		return true;
	}

	protected void onNew() {
		is_new = true;		
	}

	public boolean checkPassword(String password) {
		_log.trace("password = " + password);
		return CryptoManager.checkPassword(password, getSalt());
	}

	public void setUser(UserBean inUser) {
		setMail(inUser.getMail().trim());
		setAdditionals(inUser.getAdditionals().trim());
	}


}
