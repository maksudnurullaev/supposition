package org.supposition.db.proxy;

public class CompanyBean extends SimpleProxy {
	private String name;
	private String additionals;
		
	public void setName(String name) {
		this.name = name;
	}	
	public String getName() {
		return name;
	}
	public void setAdditionals(String additionals) {
		this.additionals = additionals;
	}
	public String getAdditionals() {
		return additionals;
	}

}
