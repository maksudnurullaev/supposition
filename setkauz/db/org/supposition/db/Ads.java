package org.supposition.db;

import java.util.Date;

import org.apache.cayenne.validation.SimpleValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.auto._Ads;
import org.supposition.db.interfaces.IDBOClass;
import org.supposition.db.proxy.AdsBean;
import org.supposition.db.proxy.CgroupProxy;
import org.supposition.db.proxy.CompanyProxy;
import org.supposition.db.proxy.UserProxy;
import org.supposition.utils.DBUtils;
import org.supposition.utils.SessionManager;

public class Ads extends _Ads  implements IDBOClass{
	private static final long serialVersionUID = 1L;
	private Log _log = LogFactory.getLog(this.getClass());
	private String kaptcha = "";

	public void setAds(AdsBean inAdsBean){
		setType(inAdsBean.getType());
		setText(DBUtils.removeHTMLTags(inAdsBean.getText()));
		setPrice(DBUtils.removeHTMLTags(inAdsBean.getPrice()));
		setDeleteAfter(inAdsBean.getWeeks2keep());
		setKaptcha(inAdsBean.getKaptcha());
		
		// Check for owner
		if(SessionManager.getUserUuid() != null){
			UserProxy userProxy = new UserProxy(getDataContext());
			User current_user = userProxy.getDBObjectByUuid(SessionManager.getUserUuid());
			if(current_user != null)
				this.setUser(current_user);
		}
		
		// Check for group
		if(inAdsBean.getGuuid() != null){
			CgroupProxy cgroup = new CgroupProxy(getDataContext());
			Cgroup group = cgroup.getDBObjectByUuid(inAdsBean.getGuuid());
			if(group != null)
				this.setCgroup(group);
		}
		
		// Check for company
		if(inAdsBean.getCuuid() != null){
			CompanyProxy companyProxy = new CompanyProxy(getDataContext());
			Company company = companyProxy.getDBObjectByUuid(inAdsBean.getCuuid());
			if(company != null)
				this.setCompany(company);
		}
	}

	private void setDeleteAfter(int weeks2keep) {
		setDeleteAfter(DBUtils.dateAfterInWeeks(weeks2keep));
	}

	public ValidationResult getValidationResult() {
		_log.debug("getValidationResult -> ");
		ValidationResult validationResult = new ValidationResult();
		
		validateBeforeSave(validationResult);
		return validationResult;
	}

	private void validateBeforeSave(ValidationResult validationResult) {
		if(isNew()){
			setUuid(DBUtils.getUuid());
			setCreated(new Date());
		}
		
		super.validateForSave(validationResult);
		
		// Validate kaptcha
		validateKaptcha(validationResult);
		
	}

	private void validateKaptcha(ValidationResult validationResult) {
		// Ignore validation for logged user 
		if(SessionManager.getUserUuid() != null ) return;

		// Check captcha
		if(getKaptcha() == null){
			_log.warn("errors.null.object - Kaptcha");			
			validationResult.addFailure(new SimpleValidationFailure(this,
				"errors.null.object"));
			return;
		}
		
		DBUtils.checkKaptcha(getKaptcha(), validationResult, this);
	}


	

	@Override
	public boolean isNew() {
		return this.getPersistenceState() == org.apache.cayenne.PersistenceState.NEW;
	}

	@Override
	public void postValidationSave() {
		// TODO Auto-generated method stub
		
	}

	public void setKaptcha(String kaptcha) {
		this.kaptcha = kaptcha;
	}

	public String getKaptcha() {
		return kaptcha;
	}	
}



