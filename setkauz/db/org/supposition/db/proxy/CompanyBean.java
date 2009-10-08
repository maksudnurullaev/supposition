package org.supposition.db.proxy;

public class CompanyBean extends SimpleBean {
	private String name;
	private String additionals;
	private String www;
	// Cgroup uuid;
	private String guuid;
	// Company city;
	private String city;
	
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
	public void setGuuid(String guuid) {
		this.guuid = guuid;
	}
	public String getGuuid() {
		return guuid;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return city;
	}

}
