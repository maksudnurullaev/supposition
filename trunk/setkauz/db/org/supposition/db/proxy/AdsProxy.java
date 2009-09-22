package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.supposition.db.Ads;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;

public class AdsProxy extends ADBProxyObject<Ads> {
	
	private static final long serialVersionUID = 1L;
	
	public AdsProxy(){
		super();
		
		setEClass(Ads.class);
		_context = DBUtils.getInstance().getDBContext();
	}

	public AdsProxy(DataContext inDataContext) {
		super();
		setEClass(Ads.class);
		_context = inDataContext;
	}	 	
	

	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");
		
		String[] result = { "#", "Name"};
		return Arrays.asList(result);
	}
}
