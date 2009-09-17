package org.supposition.db.proxy;

public class RoleBean extends SimpleProxy {
	private String name;
		
	public void setName(String name) {
		this.name = name;
	}	
	public String getName() {
		return name;
	}
}
