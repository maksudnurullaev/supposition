package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.supposition.db.Default;
import org.supposition.db.proxy.abstracts.ADBProxyObject;

public class Defaults extends ADBProxyObject<Default> {
	
	public Defaults(){
		super();
		setEClass(Default.class);
	}
	
	@Override
	public List<String> getColumnNames() {
		String[] result = { "Key", "Value" };
		return Arrays.asList(result);
	}	
	
	@Override
	public int getCount() {
		return getAll().size();
	}

}
