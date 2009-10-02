package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Company;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.Utils;

public class CompanyProxy extends ADBProxyObject<Company> {
	
	private static final long serialVersionUID = 1L;
	
	public CompanyProxy(){
		super();
		
		setEClass(Company.class);
		setDataContext(DBUtils.getInstance().getDBContext());
	}

	public CompanyProxy(DataContext inDataContext) {
		super();
		setEClass(Company.class);
		setDataContext(inDataContext);
	}	 	
	

	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");
		
		String[] result = {"#","Name"};
		return Arrays.asList(result);
	}
	
	public String addDBONew(CompanyBean inBean){
		_log.debug("addDBORole ->");
		
		// Check for valid text
		if(!Utils.isValidString(inBean.getName())){
			_log.debug("addDBONew -> Constants.isValidString -> inBean.getName -> " + inBean.getName());			
			return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.empty.value");
		}
			
		// Create new DBO
		Company company = null;
		try {
			company = createNew();
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// Setup user bean
		company.setBean(inBean);

		// Validate
		ValidationResult validationResult = company.getValidationResult();

		if(validationResult.hasFailures()){
			// Delete Object before commit
			deleteObject(company);
			return MessagesManager.getDefault("web.error.result.prefix")
						+ DBUtils.getFailuresAsString(validationResult);
		}

		commitChanges();		
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
			+ MessagesManager.getText("message.data.saved");		
	}	
}
