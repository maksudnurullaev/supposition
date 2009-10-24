package org.supposition.db;

import java.util.Date;

import org.apache.cayenne.validation.SimpleValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._Company;
import org.supposition.db.interfaces.IDBOClass;
import org.supposition.db.proxy.CgroupProxy;
import org.supposition.db.proxy.CompanyBean;
import org.supposition.db.proxy.UserProxy;
import org.supposition.exceptions.NullObject;
import org.supposition.exceptions.UserAccessException;
import org.supposition.utils.DBUtils;
import org.supposition.utils.SessionManager;

public class Company extends _Company implements IDBOClass {

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
		if (isNew()){
			// setup uuid
			setUuid(DBUtils.getUuid());
			
			// setup owner
			if(getUser() == null){
				_log.debug("errors.null.object - User");			
				validationResult.addFailure(new SimpleValidationFailure(this,
				"errors.null.object"));
			}else{
				setUuuid(getUser().getUuid());
			}
		}
		
		
		// Final
		super.validateForSave(validationResult);
	}

	@Override
	public boolean isNew() {
		return this.getPersistenceState() == org.apache.cayenne.PersistenceState.NEW;
	}

	@Override
	public void postValidationSave() {
	}

	public void setBean(CompanyBean inBean) throws UserAccessException, NullObject{
		setName(inBean.getName());
		setAdditionals(inBean.getAdditionals());
		setWww(inBean.getWww());
		setCity(inBean.getCity());
		setGuuid(inBean.getGuuid());

		if (isNew()) {
			// Add current logged user as owner
			if (SessionManager.isUserLoggedIn()) {
				UserProxy userProxy = new UserProxy(getDataContext());
				User user = userProxy.getDBObjectByUuid(SessionManager
						.getUserUuid());
				if (user == null)
					throw new NullObject();
				setUser(user);
				setUuuid(user.getUuid());
			} else
				throw new UserAccessException();
		}

		// Add to group
		if (inBean.getGuuid() != null) {
			CgroupProxy cgroups = new CgroupProxy(getDataContext());
			Cgroup cgroup = cgroups.getDBObjectByUuid(inBean.getGuuid());
			
			if (cgroup != null)
				this.setCgroup(cgroup);
			else
				throw new NullObject();
		}		
		
		// Set updated time
		setUpdated(new Date());
		
	}

}
