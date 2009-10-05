package org.supposition.db.proxy;

public class CompanyFilterBean {
	// Cgroup
	private String guuid;
	// Company city;
	private String city;
	// Company owner
	private boolean owner;
	
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
	public void setOwner(boolean owner) {
		this.owner = owner;
	}
	public boolean isOwner() {
		return owner;
	}
}
