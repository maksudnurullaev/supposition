package org.supposition.db;

import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.SimpleValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._Default;
import org.supposition.db.proxy.Defaults;
import org.supposition.db.proxy.KeyValueBean;
import org.supposition.utils.Constants;

public class Default extends _Default {
	private static final long serialVersionUID = 1L;
	public Log _log = LogFactory.getLog(this.getClass());

	public void setBean(KeyValueBean inKeyValue) {
		setKey(inKeyValue.getKey());
		setValue(inKeyValue.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.cayenne.CayenneDataObject#validateForSave(org.apache.cayenne
	 * .validation.ValidationResult)
	 */
	@Override
	public void validateForSave(ValidationResult validationResult) {
		super.validateForSave(validationResult);
		
		_log.debug(String.format("Validate KeyValue('%s', '%s')", getKey(),
				getValue()));

		// Check key
		if (!Constants.isValidString(getKey())) {
			_log.error("Invalid key");
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.empty.key"));
			return;
		} 
		
		// Check value
		if (!Constants.isValidString(getValue())) {
			_log.error("Invalid value");
			validationResult.addFailure(new SimpleValidationFailure(this,
					"errors.empty.value"));
			return;
		}
		
		// Check key existanse
		Defaults defaults = new Defaults();
		defaults.addExpression(ExpressionFactory.matchExp("key", getKey()));
		if(this.getPersistenceState() != org.apache.cayenne.PersistenceState.NEW){
			defaults.addExpression(ExpressionFactory.matchExp("ID", getID()));
		}
		
		if (defaults.getAll().size() != 0) {
				validationResult.addFailure(new SimpleValidationFailure(
						this, "errors.dbobject.already.registered"));
		}

		_log.debug("Validation complited, "
				+ (validationResult.hasFailures() ? "" : " NO ")
				+ " errros found!");
	}

}
