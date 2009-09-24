package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.supposition.db.Company;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;

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
		
		String[] result = { "#", "Name"};
		return Arrays.asList(result);
	}
}
