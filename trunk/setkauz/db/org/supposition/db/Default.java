package org.supposition.db;

import org.apache.cayenne.validation.SimpleValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._Default;
import org.supposition.db.proxy.KeyValueBean;
import org.supposition.utils.Constants;

public class Default extends _Default {
	private static final long serialVersionUID = 1L;
	public Log _log = LogFactory.getLog(this.getClass());

	public void setBean(KeyValueBean inKeyValue) {
		setKey(inKeyValue.getKey());
		setValue(inKeyValue.getValue());		
	}

	/* (non-Javadoc)
	 * @see org.apache.cayenne.CayenneDataObject#validateForSave(org.apache.cayenne.validation.ValidationResult)
	 */
	@Override
	public void validateForSave(ValidationResult validationResult) {
		super.validateForSave(validationResult);
		
		_log.debug(String.format("Validate KeyValue('%s', '%s')", getKey(), getValue()));
		
		if(!Constants.isValidString(getKey())){
			_log.error("Invalid key");
			validationResult.addFailure(new SimpleValidationFailure(this, "errors.empty.key"));
		}
		if(!Constants.isValidString(getValue())){
			_log.error("Invalid vaue");
			validationResult.addFailure(new SimpleValidationFailure(this, "errors.empty.value"));
		}
		_log.debug("Validation complited, " + (validationResult.hasFailures()?"":" NO ") + " errros found!");
	}
	
	
}
