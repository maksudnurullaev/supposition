package org.supposition.db.proxy;

public class GroupBean extends SimpleBean {
	private String name;
	private String cuuid;
	private int page = 1;     // default
	private int density = 15; // default
		
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
	public void setPage(int page) {
		if(page <= 0)
			this.page = 1;
		else this.page = page;
	}
	public int getPage() {
		return page;
	}
	public void setDensity(int density) {
		if(density <=0)
			this.density = 15;
		else this.density = density;
	}
	public int getDensity() {
		return density;
	}
}
