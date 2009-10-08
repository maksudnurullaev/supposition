package org.supposition.db;

import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._Group;
import org.supposition.db.interfaces.IDBOClass;
import org.supposition.db.proxy.GroupBean;
import org.supposition.utils.DBUtils;

public class Group extends _Group implements IDBOClass {
	private Log _log = LogFactory.getLog(this.getClass());
	private static final long serialVersionUID = 1L;

	@Override
	public ValidationResult getValidationResult() {
		_log.debug("getValidationResult -> ");
		ValidationResult validationResult = new ValidationResult();
		validateBeforeSave(validationResult);
		return validationResult;
	}

	@Override
	public boolean isNew() {
		return this.getPersistenceState() == org.apache.cayenne.PersistenceState.NEW;
	}

	@Override
	public void postValidationSave() {
	}

	public void setBean(GroupBean inBean) {
		setName(inBean.getName());
		
	}

	private void validateBeforeSave(ValidationResult validationResult) {
		if(isNew())
			setUuid(DBUtils.getUuid());
		
		super.validateForSave(validationResult);
		
	}	
}



