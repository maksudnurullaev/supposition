package org.supposition.db;

import java.util.List;

import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.SimpleValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._User;
import org.supposition.db.proxy.UserBean;
import org.supposition.db.proxy.Users;
import org.supposition.utils.Constants;
import org.supposition.utils.CryptoManager;

public class User extends _User {
	private static final long serialVersionUID = 1L;
	private Log _log = LogFactory.getLog(this.getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.cayenne.CayenneDataObject#validateForSave(org.apache.cayenne
	 * .validation.ValidationResult)
	 */
	@Override
	protected void validateForSave(ValidationResult validationResult) {
		super.validateForSave(validationResult);

		_log.debug("->validateForSave");

		validateMail(validationResult);

		if (validatePassword(validationResult) && isNeedToChangePassword())
			updatePassword();
	}

	private boolean isNeedToChangePassword() {
		return (getPassword() != Constants._password_salted);
	}

	protected void updatePassword() {

		_log.debug("->updatePassword to " + getPassword());

		setSalt(CryptoManager.encryptPassword(getPassword()));
		setPassword(Constants._password_salted); // remove original text
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.cayenne.CayenneDataObject#validateForInsert(org.apache.cayenne
	 * .validation.ValidationResult)
	 */
	@Override
	public void validateForInsert(ValidationResult validationResult) {
		super.validateForInsert(validationResult);

		_log.debug("->validateForInsert");

		validateMail(validationResult);
		validatePassword(validationResult);

	}

	private boolean validateMail(ValidationResult validationResult) {

		_log.debug("->validateMail");

		// Validate for invalid mail
		if (!org.supposition.utils.Constants.isValidEmailAddress(getMail())) {
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.invalid.mail"));
			_log.error("Invalid email" + getMail());
			return false;
		}
		// Validate for existing mail
		Users users = new Users();
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
		_log.debug("->onNew");
		setCreated(Constants.GetCurrentDateTime());
	}

	protected void onUpdate() {
		_log.debug("->onUpdate");
		setUpdated(Constants.GetCurrentDateTime());
	}

	public boolean checkPassword(String password) {
		//_log.debug(String.format("CryptoManager.checkPassword(%s, %s) = ", password, getSalt()));
		//_log.debug(String.format("CryptoManager(%s, %s) = ", CryptoManager.encryptPassword(password), getSalt()));
		return CryptoManager.checkPassword(password, getSalt());
	}

	public void setUser(UserBean inUser) {
		setMail(inUser.getMail().trim());
		setAdditionals(inUser.getAdditionals().trim());
	}
}
