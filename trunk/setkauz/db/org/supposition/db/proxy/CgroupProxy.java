package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.supposition.db.Cgroup;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;

public class CgroupProxy extends ADBProxyObject<Cgroup> {
	
	private static final long serialVersionUID = 1L;
	
	public CgroupProxy(){
		super();
		
		setEClass(Cgroup.class);
		_context = DBUtils.getInstance().getDBContext();
	}

	public CgroupProxy(DataContext inDataContext) {
		super();
		setEClass(Cgroup.class);
		_context = inDataContext;
	}	 	
	

	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");
		
		String[] result = { "#", "Name"};
		return Arrays.asList(result);
	}

}
