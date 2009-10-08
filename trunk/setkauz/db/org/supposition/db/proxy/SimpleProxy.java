package org.supposition.db.proxy;

import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.access.DataContext;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;

public class SimpleProxy<E extends CayenneDataObject> extends ADBProxyObject<E> {

	@Override
	public List<String> getColumnNames() {
		return null;
	}

	public SimpleProxy(Class<E> inClass){
		super();
		setEClass(inClass);
		setDataContext(DBUtils.getInstance().getDBContext());
	}

	public SimpleProxy(DataContext inDataContext, Class<E> inClass) {
		super();
		setEClass(inClass);
		setDataContext(inDataContext);
	}	 		
	
}
