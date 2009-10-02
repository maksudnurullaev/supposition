package org.supposition.db;

import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._Company;
import org.supposition.db.interfaces.IDBOClass; 
import org.supposition.db.proxy.CompanyBean;
import org.supposition.utils.DBUtils;

public class Company extends _Company  implements IDBOClass {

	private static final long serialVersionUID = 1L;
	private Log _log = LogFactory.getLog(this.getClass());
	
	@Override
	public ValidationResult getValidationResult() {
		_log.debug("getValidationResult -> ");
		ValidationResult validationResult = new ValidationResult();
		validateBeforeSave(validationResult);
		return validationResult;
	}

	private void validateBeforeSave(ValidationResult validationResult) {
		if(isNew())
			setUuid(DBUtils.getUuid());
		
	}

	@Override
	public boolean isNew() {
		return this.getPersistenceState() == org.apache.cayenne.PersistenceState.NEW;
	}

	@Override
	public void postValidationSave() {
	}

	public void setBean(CompanyBean inBean) {
		setName(inBean.getName());
		setAdditionals(inBean.getAdditionals());
		setWww(inBean.getWww());
		
		// TODO Add current logged user 
		// TODO Add current group
		// TODO Add current city
	}
	
	
}



