package org.supposition.db.proxy;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.supposition.db.Group;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;

public class GroupProxy extends ADBProxyObject<Group> {

	public GroupProxy(){
		super();
		
		setEClass(Group.class);
		setDataContext(DBUtils.getInstance().getDBContext());
	}

	public GroupProxy(DataContext inDataContext) {
		super();
		setEClass(Group.class);
		setDataContext(inDataContext);
	}		
	
	@Override
	public List<String> getColumnNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
