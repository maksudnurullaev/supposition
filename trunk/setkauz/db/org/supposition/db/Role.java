package org.supposition.db;

import java.util.List;

import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.SimpleValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._Role;
import org.supposition.db.proxy.RoleBean;
import org.supposition.db.proxy.RoleProxy;

public class Role extends _Role {
	private static final long serialVersionUID = 1L;
	private Log _log = LogFactory.getLog(this.getClass());

	public void setRole(RoleBean roleBean) {
		setName(roleBean.getName());
	}

	public ValidationResult getValidationResult(){
		_log.debug("getValidationResult -> ");
		ValidationResult validationResult = new ValidationResult();
		validateBeforeSave(validationResult);
		return validationResult;
	}

	private void validateBeforeSave(ValidationResult validationResult) {
		RoleProxy roles  = new RoleProxy();
		roles.addExpression(ExpressionFactory.matchExp("Name", getName()));

		List<Role> rolesList = roles.getAll();
		
		_log.debug("rolesList.size is " + rolesList.size());
		
		if (rolesList.size() > 0) {
			if (rolesList.size() > 1) {
				validationResult.addFailure(new SimpleValidationFailure(this,
						"errors.dbobject.already.registered"));
				_log.warn("Database has too many users record with same name - "
								+ getName());
			} else {
				if (!rolesList.get(0).getID().equals(getID())) {
					validationResult.addFailure(new SimpleValidationFailure(
							this, "errors.dbobject.already.registered"));
				}
			}
		}		
	}

}
