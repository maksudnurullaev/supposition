package org.supposition.db.proxy;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Ads;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.Utils;

public class AdsProxy extends ADBProxyObject<Ads> {
	
	private static final long serialVersionUID = 1L;
	
	public AdsProxy(){
		super();
		
		setEClass(Ads.class);
		setDataContext(DBUtils.getInstance().getDBContext());
	}

	public AdsProxy(DataContext inDataContext) {
		super();
		setEClass(Ads.class);
		setDataContext(inDataContext);
	}	 	
	
	public String addDBONew(AdsBean inAdsBean){
		_log.debug("addDBORole ->");
		
		// Check for valid text
		if(!Utils.isValidString(inAdsBean.getText())){
			_log.debug("addDBORole -> Constants.isValidString -> roleBean.getName() -> " + inAdsBean.getText());			
			return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.empty.value");
		}
			
		// Create new DBO
		Ads ads = null;
		try {
			ads = createNew();
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// Setup user bean
		ads.setAds(inAdsBean);

		// Validate
		ValidationResult validationResult = ads.getValidationResult();

		if(validationResult.hasFailures()){
			// Delete Object before commit
			deleteObject(ads);
			return MessagesManager.getDefault("web.error.result.prefix")
						+ DBUtils.getFailuresAsString(validationResult);
		}

		commitChanges();		
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
			+ MessagesManager.getText("message.data.saved");		
	}

	@Override
	public List<String> getColumnNames() {
		return null;
	}	
}
