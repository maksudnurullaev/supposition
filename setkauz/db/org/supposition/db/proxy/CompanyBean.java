package org.supposition.db.proxy;

public class CompanyBean extends SimpleProxy {
	private String name;
	private String additionals;
	private String www;
		
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
	public void setWww(String www) {
		this.www = www;
	}
	public String getWww() {
		return www;
	}

}
