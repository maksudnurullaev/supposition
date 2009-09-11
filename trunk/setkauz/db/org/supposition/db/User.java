package org.supposition.db;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.SimpleValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._User;
import org.supposition.db.proxy.UserBean;
import org.supposition.db.proxy.UserProxy;
import org.supposition.utils.CryptoManager;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;
import org.supposition.utils.Utils;

public class User extends _User {
	private static final long serialVersionUID = 1L;
	private Log _log = LogFactory.getLog(this.getClass());
	private boolean is_new = false;

	private String kaptcha = "";

	private void validateBeforeSave(ValidationResult validationResult) {
		_log.debug("->validateForSave");
		super.validateForSave(validationResult);

		validateMail(validationResult);

		validateFor2Mail(validationResult);

		if (is_new) {
			validatePassword(validationResult);
		} else {
			if (isNeedToChangePassword())
				validatePassword(validationResult);
		}
	}

	private void validateKaptcha(ValidationResult validationResult) {
		// Check captcha
		if(getKaptcha() == null){
			_log.error("errors.null.object - Kaptcha");			
			validationResult.addFailure(new SimpleValidationFailure(this,
				"errors.null.object"));
			return;
		}

		// For tests
		if(getKaptcha().equalsIgnoreCase(MessagesManager.getDefault("testing.string"))) return;
		
		String sessionKaptchaValue = (String) SessionManager
				.getSessionValue(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (getKaptcha() == null
				|| !getKaptcha().equalsIgnoreCase(sessionKaptchaValue)) {
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.invalid.kaptcha"));
			_log.error(String.format("Invalid kaptcha(%s) should be %s", getKaptcha(), sessionKaptchaValue));
		}

	}

	public ValidationResult getValidationResult(boolean withKaptcha) {
		ValidationResult validationResult = new ValidationResult();
		
		if(withKaptcha)
			validateKaptcha(validationResult);
		
		validateBeforeSave(validationResult);
		
		if(validationResult.hasFailures())
			_log.debug(String.format("Validation failed for User(%s)",getMail()));
		
		return validationResult;
	}

	public void postValidationSave() {
		_log.debug("->validateForSave");

		if (is_new) {
			updatePassword();
			setCreated(Utils.GetCurrentDateTime());
		} else {
			if (isNeedToChangePassword())
				updatePassword();
			setUpdated(Utils.GetCurrentDateTime());
		}
	}

	private boolean isNeedToChangePassword() {
		_log.debug("->isNeedToChangePassword RETURNS "
				+ Utils.isValidString(getPassword()));
		return Utils.isValidString(getPassword());
	}

	protected void updatePassword() {
		_log.debug("->updatePassword");

		setSalt(CryptoManager.encryptPassword(getPassword()));
		setPassword(""); // remove original text
		setStatus(MessagesManager.getDefault("password.salted"));
	}

	private boolean validateMail(ValidationResult validationResult) {
		_log.debug("->validateMail");

		if (!org.supposition.utils.Utils.isValidEmailAddress(getMail())) {
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.invalid.mail"));
			_log.debug("Invalid email" + getMail());
			return false;
		}

		return true;
	}

	private boolean validateFor2Mail(ValidationResult validationResult) {
		_log.debug("->validateFor2Mail");

		UserProxy users = new UserProxy();
		users.addExpression(ExpressionFactory.matchExp("Mail", getMail()));

		List<User> usersList = users.getAll();

		_log.debug("Founded users = " + usersList.size());
		
		if (usersList.size() > 0) {
			if (usersList.size() > 1) {
				validationResult.addFailure(new SimpleValidationFailure(this,
						"errors.dbobject.already.registered"));
				_log
						.warn("Database has too many users record with same mail - "
								+ getMail());
				return false;
			} else {
				if (!DBUtils.getID(usersList.get(0)).equals(DBUtils.getID(this))) {
					validationResult.addFailure(new SimpleValidationFailure(
							this, "errors.dbobject.already.registered"));
					return false;
				}
			}
		}
		return true;
	}

	public boolean validatePassword(ValidationResult validationResult) {
		_log.debug("->validatePassword");
		// Validate for empty password
		if (getPassword().isEmpty()) {
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.passwords.is.empty"));
			_log.debug("Empty password");
			return false;
		}
		// Validate for password length
		else if (getPassword().length() < Utils.getIntFromStr(MessagesManager.getDefault("min.password.length"))) {
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.invalid.password.length"));
			_log.error("Invalid password length, should be > "
					+ MessagesManager.getDefault("min.password.length"));
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
		setKaptcha(inUser.getKaptcha());
	}

	public boolean hasRoles() {
		return getRoles().size() > 0;
	}

	public String getRolesAsStr() {
		String result = "";

		for (Object role : getRoles()) {
			result += ((Role)role).getName() + ",";
		}

		return result.substring(0, result.length() - 1);
	}

	public List<String> getRolesAsList() {
		List<String> result = new ArrayList<String>();
		for (Object role : getRoles()) {
			result.add(((Role)role).getName());
		}
		
		return result;
	}	
	
	public String getMailWithRoles() {
		if (!hasRoles())
			return getMail();
		return getMail() + String.format("<sup>(%s)</sup>", getRolesAsStr());
	}

	public void setKaptcha(String kaptcha) {
		this.kaptcha = kaptcha;
	}

	public String getKaptcha() {
		return kaptcha;
	}

}