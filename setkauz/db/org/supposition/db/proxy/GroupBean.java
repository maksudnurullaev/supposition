package org.supposition.db.proxy;

public class GroupBean extends SimpleBean {
	private String name;
	private String cuuid;
		
	public void setName(String name) {
		this.name = name;
	}	
	public String getName() {
		return name;
	}
	public void setCuuid(String cuuid) {
		this.cuuid = cuuid;
	}
	public String getCuuid() {
		return cuuid;
	}
}
