package org.supposition.db.proxy;

public class CgroupBean extends SimpleProxy {
	private String name;
	private String guuid;
		
	public void setName(String name) {
		this.name = name;
	}	
	public String getName() {
		return name;
	}
	public void setGuuid(String guuid) {
		this.guuid = guuid;
	}
	public String getGuuid() {
		return guuid;
	}
}
